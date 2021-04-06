package com.p8.common.net.interceptor;

import android.text.TextUtils;
import android.widget.TextView;

import com.blankj.utilcode.util.CacheDoubleUtils;
import com.p8.common.Constants;
import com.p8.common.storage.MmkvHelper;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.p8.common.Constants.KEY_TOKEN;

/**
 * author : WX.Y
 * date : 2020/9/23 16:57
 * description :
 */
public class TokenInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException{
        Request request = chain.request();
//        String token = AppPreferencesHelper.getSPUtils().getString(AppPreferencesHelper.KEY_TOKEN);
//        String token = MmkvHelper.INSTANCE.decodeString(KEY_TOKEN);
        String token = CacheDoubleUtils.getInstance().getString(Constants.KEY_TOKEN);
        if(TextUtils.isEmpty(token)) {
            token = "NULL";
        }
        HttpUrl httpUrl = request.url()
                .newBuilder()
                /* add parameter */
                .removeAllQueryParameters("token")
                //移除是防止重复添加参数
                //某种情况下可能会报错，造成后台获取的参数有问题
                .addQueryParameter("token", token)
                .build();
        Request build = request.newBuilder()
                /* add  header */
                .addHeader("contentType", "text/json")
                .url(httpUrl)
                .build();
        return chain.proceed(build);
    }
}

