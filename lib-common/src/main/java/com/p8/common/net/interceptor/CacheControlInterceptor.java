package com.p8.common.net.interceptor;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.NetworkUtils;
import com.p8.common.App;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author : Yannis.Ywx
 * createTime : 2017/9/18  16:05
 * description : 网络缓存拦截器
 * 没有网络的情况下就从缓存中取
 * 有网络的情况则从网络获取
 */
public class CacheControlInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetworkUtils.isConnected()) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }

        Response response = chain.proceed(request);
        if (NetworkUtils.isConnected()) {
            //默认缓存两个小时
            int maxAge = 60 * 60 * 2;
            String cacheControl = request.cacheControl().toString();
            if (TextUtils.isEmpty(cacheControl)) {
                cacheControl = "public, max-age=" + maxAge;
            }
            response = response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", cacheControl)
                    .build();

        } else {
            int maxStale = 60 * 60 * 24 * 30;
            response = response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
        return response;
    }
}
