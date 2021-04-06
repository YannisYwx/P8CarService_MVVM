package com.p8.common.net.observer;

import com.blankj.utilcode.util.NetworkUtils;
import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;
import com.orhanobut.logger.Logger;
import com.p8.common.net.HttpError;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.observers.ResourceObserver;
import retrofit2.HttpException;

/**
 * @author : WX.Y
 * date : 2020/9/17 15:50
 * description :
 */
public abstract class ObservableSubscriber<T> extends ResourceObserver<T> {

    @Override
    protected void onStart() {
        super.onStart();
        NetworkUtils.NetworkType networkType = NetworkUtils.getNetworkType();
        switch (networkType) {
            case NETWORK_NO:
            case NETWORK_UNKNOWN:
                // 一定要主动调用下面这一句,取消本次Subscriber订阅
                if (!isDisposed()) {
                    dispose();
                }
                onFail(new HttpError("网络不通畅,请检查后再试"));
                onEnd();
                break;
            default:
                break;
        }
    }

    @Override
    public void onError(Throwable t) {
        Logger.e(t.toString());
        HttpError httpError;
        if (t instanceof HttpError) {
            httpError = (HttpError) t;
        } else if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            final String msg;
            switch (httpException.code()) {
                case 400:
                    msg = "参数错误";
                    break;
                case 401:
                    msg = "身份未授权";
                    break;
                case 403:
                    msg = "禁止访问";
                    break;
                case 404:
                    msg = "地址未找到";
                    break;
                default:
                    msg = "服务异常";
            }
            httpError = new HttpError(msg, httpException);
        } else if (t instanceof UnknownHostException) {
            httpError = new HttpError("网络异常", t);
        } else if (t instanceof ConnectException) {
            httpError = new HttpError("网络异常", t);
        } else if (t instanceof SocketException) {
            httpError = new HttpError("服务异常", t);
        } else if (t instanceof SocketTimeoutException) {
            httpError = new HttpError("响应超时", t);
        } else if (t instanceof JSONException || t instanceof JsonParseException || t instanceof MalformedJsonException) {
            httpError = new HttpError("数据解析异常", t);
        } else {
            httpError = new HttpError("请求失败", t);
        }
        onFail(httpError);
        onEnd();
    }

    @Override
    public void onComplete() {
        onEnd();
    }

    /**
     * 网络请求结束的回调（不管成功还是失败都会回调，这里一般可以去做progress dismiss的操作）
     * 这和onComplete不同，onComplete只会在onNext走完之后回调,该方法不需要可以不调用
     */
    protected void onEnd() {
    }

    /**
     * 网络请求失败回调
     * @param httpError 异常原因
     */
    protected abstract void onFail(HttpError httpError);

}

