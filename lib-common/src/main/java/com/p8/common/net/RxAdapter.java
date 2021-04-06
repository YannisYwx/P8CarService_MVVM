package com.p8.common.net;

import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.p8.common.net.exception.CustomException;
import com.p8.common.net.exception.ExceptionConverter;
import com.p8.common.net.exception.InterceptableException;
import com.p8.common.net.exception.ExceptionRetry;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author : WX.Y
 * date : 2021/3/12 10:09
 * description :
 */
public class RxAdapter {

    /**
     * 线程调度器
     */
    public static <T> ObservableTransformer<T, T> schedulersTransformer() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 异常处理方式
     * <br/>StreamHandler(将内部异常选择性抛出,可设置需要重试的异常)->
     * <br/>ExceptionRetry(所有异常都会经过此处,可拦截需要重试的内部异常,如Token超时等)->
     * <br/>ExceptionHandler(统一处理未被拦截内部异常和所有外部异常)
     */
    public static<T> ObservableTransformer<T,T> exceptionTransformer() {
        return observable -> observable
                .flatMap(new StreamHandler<>())
                .retryWhen(new ExceptionRetry())//拦截需要处理的异常
                .onErrorResumeNext(new ExceptionHandler<>());
    }

    /**
     * 将内部异常选择性抛出,可设置需要重试的异常
     */
    private static class StreamHandler<T> implements Function<T, ObservableSource<T>> {

        @Override
        public Observable<T> apply(T o) throws Exception {
            return handle(o);
        }

        private Observable<T> handle(T o) {
            if (o instanceof HttpResponse) {
                HttpResponse respDTO = (HttpResponse) o;
                //选择性抛出内部异常

                if(respDTO.getCode() != ExceptionConverter.P8HttpCode.SUCCESS) {
                    Exception throwable = new CustomException(respDTO.getCode(), respDTO.getMsg());
                    return Observable.error(throwable);
                }
            }
            return  Observable.just(o);
        }
    }

    private static final String TAG = "RxAdapter";
    /**
     * 统一处理未被拦截内部异常和所有外部异常
     */
    private static class ExceptionHandler<T> implements Function<Throwable, Observable<T>> {
        @Override
        public Observable<T> apply(Throwable t) {
            return handle(t);
        }

        private Observable<T> handle(Throwable t) {
            //转换外部异常
            if (!(t instanceof CustomException)) {
                t = ExceptionConverter.convert(t);
            }
            ToastUtils.showShort(t.getMessage());
            Log.e(TAG, "handle: ",t);
            return Observable.error(t);
        }
    }
}

