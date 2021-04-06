package com.p8.common.net.exception;

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;

import retrofit2.HttpException;

/**
 * @author : WX.Y
 * date : 2021/3/12 10:21
 * description :
 */
public class ExceptionConverter {
    public static Exception convert(Throwable e) {
        String msg;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            switch (String.valueOf(httpException.code())) {
                case NetError.UNAUTHORIZED:
                    msg="操作未授权";
                    break;
                case NetError.FORBIDDEN:
                    msg= "请求被拒绝";
                    break;
                case NetError.NOT_FOUND:
                    msg = "资源不存在";
                    break;
                case NetError.REQUEST_TIMEOUT:
                    msg = "服务器执行超时";
                    break;
                case NetError.INTERNAL_SERVER_ERROR:
                    msg = "服务器内部错误";
                    break;
                case NetError.SERVICE_UNAVAILABLE:
                    msg = "服务器不可用";
                    break;
                default:
                    msg = "网络错误";
                    break;
            }
            msg+=":"+httpException.code();
        } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof
                ParseException || e instanceof MalformedJsonException) {
            msg = "解析错误";
        } else if (e instanceof ConnectException) {
            msg = "连接失败";
        } else if (e instanceof javax.net.ssl.SSLException) {
            msg = "证书验证失败";
        } else if (e instanceof ConnectTimeoutException) {
            msg = "连接超时";
        } else if (e instanceof java.net.SocketTimeoutException) {
            msg = "连接超时";
        } else if (e instanceof java.net.UnknownHostException) {
            msg = "主机地址未知";
        }else if (e instanceof ClassCastException) {
            msg = "类型转换异常";
        }  else {
            msg = e.getMessage();
        }
        return new Exception(msg,e);
    }

    public interface NetError {
        String UNAUTHORIZED = "401";
        String FORBIDDEN = "403";
        String NOT_FOUND = "404";
        String REQUEST_TIMEOUT = "408";
        String INTERNAL_SERVER_ERROR = "500";
        String SERVICE_UNAVAILABLE = "503";
    }

    public interface AppError {
        String SUCCESS = "0000";
        String TOKEN_TIMEOUT = "0004";
        String ACCOUNT_ERROR = "0003";
    }

    public interface P8HttpCode {
        int SUCCESS = 1;
        int FAILED = 0;
        int TOKEN_ERROR = 408;
    }

}

