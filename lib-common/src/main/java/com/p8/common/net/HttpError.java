package com.p8.common.net;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import retrofit2.HttpException;

/**
 * @author : WX.Y
 * date : 2020/9/17 16:01
 * description :
 */
public final class HttpError extends RuntimeException {
    private static final long serialVersionUID = -134024482758434333L;
    /**
     * 展示在前端的错误描述信息
     */
    @NonNull
    public String msg;

    /**
     * <p>
     * 请求失败保存失败信息,for example:
     * <li>1.original json:  原始的json</li>
     * <li>2.Throwable: 抛出的异常信息{@link HttpException}</li>
     * <li>3.自定义的一些对象</li>
     * </p>
     */
    @Nullable
    public final transient Object body;

    public HttpError(String msg) {
        this(msg, null);
    }

    public HttpError(String msg, @Nullable Object body) {
        super(msg);
        if (body instanceof Throwable) {
            initCause((Throwable) body);
        }
        this.msg = msg != null ? msg : "null";
        this.body = body;
    }

    /**
     * {@link okhttp3.Request#tag(Class)}
     * 转换body为想要的类型，如果类型不匹配，则返回null
     *
     * @param clazz 类型对象class
     * @param <T>   泛型用于指定类型
     */
    @Nullable
    public <T> T castBody(@NonNull Class<? extends T> clazz) {
        if (clazz.isInstance(body)) {
            return clazz.cast(body);
        }
        return null;
    }

    /**
     * 保证和msg一致
     */
    @Override
    public String getMessage() {
        return msg;
    }

    @Override
    public String toString() {
        return "HttpError {msg="
                + msg
                + ", body="
                + body
                + '}';
    }

}