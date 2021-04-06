package com.p8.common.net;

import com.p8.common.App;
import com.p8.common.net.interceptor.CacheControlInterceptor;
import com.p8.common.net.interceptor.ForceNetworkControlInterceptor;
import com.p8.common.net.interceptor.LoggerInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * @author : WX.Y
 * date : 2020/9/4 16:40
 * description :
 */
public class OkHttpFactory {
    private final static long DEFAULT_CONNECT_TIMEOUT = 10;
    private final static long DEFAULT_WRITE_TIMEOUT = 30;
    private final static long DEFAULT_READ_TIMEOUT = 30;

    /**
     * 默认OkHttpClient
     *
     * @return OkHttpClient
     */
    public static OkHttpClient getDefaultOkHttpClient() {
        return getOkHttpClient(new CacheControlInterceptor());
    }

    /**
     * 不缓存的OkHttpClient
     *
     * @return OkHttpClient
     */
    public static OkHttpClient getOkHttpClient() {
        return getOkHttpClient(new ForceNetworkControlInterceptor());
    }

    private static OkHttpClient getOkHttpClient(Interceptor cacheControl) {
        //定制OkHttp
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        //设置超时时间
        httpClientBuilder.connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS);
        //设置缓存
        File httpCacheDirectory = new File(App.getInstance().getCacheDir(), "OkHttpCache");
        httpClientBuilder.cache(new Cache(httpCacheDirectory, 100 * 1024 * 1024));
        //设置拦截器
        httpClientBuilder.addInterceptor(new LoggerInterceptor());
        httpClientBuilder.addInterceptor(cacheControl);
        httpClientBuilder.addNetworkInterceptor(cacheControl);
        return httpClientBuilder.build();
    }
}

