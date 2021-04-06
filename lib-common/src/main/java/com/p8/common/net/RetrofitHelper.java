package com.p8.common.net;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.CacheDoubleUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.google.gson.Gson;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.p8.common.BuildConfig;
import com.p8.common.Constants;
import com.p8.common.bean.Agency;
import com.p8.common.bean.Inspection;
import com.p8.common.bean.Landlords;
import com.p8.common.bean.LoginInfo;
import com.p8.common.bean.Machines;
import com.p8.common.bean.Orders;
import com.p8.common.net.api.P8Api;
import com.p8.common.net.interceptor.LoggerInterceptor;
import com.p8.common.net.interceptor.TokenInterceptor;
import com.p8.common.storage.MmkvHelper;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.p8.common.Constants.KEY_TOKEN;

/**
 * @author : WX.Y
 * date : 2020/9/8 16:09
 * description :
 */
public class RetrofitHelper implements HttpHelper {

    P8Api mApi;
    OkHttpClient mClient;
    Retrofit mRetrofit;

    private RetrofitHelper() {
        mClient = createOkHttpClient();
        mRetrofit = createRetrofit(new Retrofit.Builder(), mClient);
        mApi = mRetrofit.create(P8Api.class);
    }

    private static final class RetrofitHelperHolder {
        private static RetrofitHelper INSTANCE = new RetrofitHelper();
    }

    public static RetrofitHelper getInstance(){
        return RetrofitHelperHolder.INSTANCE;
    }

    private Retrofit createRetrofit(Retrofit.Builder builder, OkHttpClient client) {
        return builder
                .baseUrl(P8Api.HOST)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        File cacheFile = new File(Constants.PATH_CACHE);
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        //设置缓存
        builder.addNetworkInterceptor(new TokenInterceptor());
        builder.interceptors().addAll(provideInterceptors());
        builder.cache(cache);
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        return builder.build();
    }

    private List<Interceptor> provideInterceptors() {
        List<Interceptor> interceptors = new ArrayList<>();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            interceptors.add(loggingInterceptor);
        }

        Logger.addLogAdapter(new AndroidLogAdapter(PrettyFormatStrategy
                .newBuilder()
                .tag("OKHTTP_LOG")
                .methodCount(1).showThreadInfo(false).build()) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });

        LoggerInterceptor logInterceptor = new LoggerInterceptor(Logger::d);
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        String token = MmkvHelper.INSTANCE.decodeString(KEY_TOKEN);
        String token = CacheDoubleUtils.getInstance().getString(Constants.KEY_TOKEN);
        if(TextUtils.isEmpty(token)) {
            token = "NULL";
        }
        //提交数据
        String finalToken = token;
        Interceptor authInterceptor = chain -> {
            Request request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", "Authorization")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .addHeader("token", finalToken)
                    .build();
            HttpUrl httpUrl = request.url()
                    .newBuilder()
                    /* add parameter */
                    .removeAllQueryParameters("token")
                    //移除是防止重复添加参数
                    //某种情况下可能会报错，造成后台获取的参数有问题
                    .addQueryParameter("token", finalToken)
                    .build();
            return chain.proceed(request.newBuilder().url(httpUrl)
                    .build());
        };

        //缓存数据
        Interceptor cacheInterceptor = chain -> {
            Request request = chain.request();
            //这里就是说判读我们的网络条件，要是有网络的话我么就直接获取网络上面的数据，要是没有网络的话我么就去缓存里面取数据
            if (!NetworkUtils.isConnected()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            if (NetworkUtils.isConnected()) {
                String cacheControl = originalResponse.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                //这里的设置的是我们的没有网络的缓存时间
                int maxTime = 4 * 24 * 60 * 60;
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxTime)
                        .removeHeader("Pragma")
                        .build();
            }
        };

        interceptors.add(logInterceptor);
        interceptors.add(authInterceptor);
        interceptors.add(cacheInterceptor);
        return interceptors;
    }


    private static RequestBody createJsonRequestBody(@NonNull Object obj) {
        Logger.d("");
        return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(obj));
    }

    private static String createJsonStr(@NonNull Object obj) {
        String json = null;
        try {
            json = URLEncoder.encode(new Gson().toJson(obj), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return json;
    }


    @Override
    public Observable<HttpResponse<LoginInfo>> doLoginByLandlord(String username, String password) {
        return mApi.doLoginByLandlord(username, password);
    }

    @Override
    public Observable<HttpResponse<LoginInfo>> doLoginByLargeMaster(String loginName, String password) {
        return mApi.doLoginByLargeMaster(loginName, password);
    }

    @Override
    public Observable<HttpResponse<Agency>> getAgencyInfo() {
        return mApi.getAgencyInfo();
    }

    @Override
    public Observable<HttpResponse<Inspection>> getInspectInfo() {
        return mApi.getInspectInfo();
    }

    @Override
    public Observable<HttpResponse<Landlords>> getLandlords(int currentPage, int pageSize) {
        return mApi.getLandlords(currentPage, pageSize);
    }

    @Override
    public Observable<HttpResponse<String>> addLandlord(String phone, String realName) {
        return mApi.addLandlord(phone, realName);
    }

    @Override
    public Observable<HttpResponse<Orders>> getOrders(int currentPage, int pageSize) {
        return mApi.getOrders(currentPage, pageSize);
    }

//    @Override
//    public Observable<HttpResponse<VCode>> getVCode(String phoneNum) {
//        return mApi.getCode(phoneNum);
//    }

    @Override
    public Observable<HttpResponse<String>> resetPassword(String url, String newPassword, String oldPassword) {
        return mApi.resetPassword(url, newPassword, oldPassword);
    }

    @Override
    public Observable<HttpResponse<Machines>> getMachines(String address, String parkingStatus, int currentPage) {
        return mApi.getMachines(address, parkingStatus, currentPage);
    }

    @Override
    public Observable<HttpResponse<Object>> bindDevice(String address, String parkingNumber, String lat, String lng) {
        return mApi.bindDevice(address, parkingNumber, lat, lng);
    }

}

