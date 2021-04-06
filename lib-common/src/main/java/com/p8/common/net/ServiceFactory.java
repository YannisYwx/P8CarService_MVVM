package com.p8.common.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author : WX.Y
 * date : 2020/9/4 16:59
 * description :
 */
public class ServiceFactory {

    private OkHttpClient mOkHttpClient;
    private Gson mGson;
    //服务器请求baseUrl
    private static String sBaseUrl = null;

    private static class ServiceFactoryHolder {
        private static ServiceFactory INSTANCE = new ServiceFactory();
    }

    public static ServiceFactory getNoCacheInstance() {
        ServiceFactory factory = ServiceFactoryHolder.INSTANCE;
        factory.mOkHttpClient = OkHttpFactory.getOkHttpClient();
        return factory;
    }

    private ServiceFactory() {
        mGson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();
        mOkHttpClient = OkHttpFactory.getDefaultOkHttpClient();
    }

    public static ServiceFactory getInstance() {
        return ServiceFactoryHolder.INSTANCE;
    }

    public <T> T createService(Class<T> serviceClass) {
        String baseUrl = "";
        try {
            Field field1 = serviceClass.getField("BASE_URL");
            baseUrl = (String) field1.get(serviceClass);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.getMessage();
            e.printStackTrace();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create(mGson))//添加gson转换器
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//添加rxjava转换器
                .build();
        return retrofit.create(serviceClass);
    }

}

