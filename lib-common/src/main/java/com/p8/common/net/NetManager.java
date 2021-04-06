package com.p8.common.net;

import com.p8.common.bean.LoginInfo;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import io.reactivex.disposables.Disposable;

/**
 * @author : WX.Y
 * date : 2021/3/11 19:27
 * description :
 */
public class NetManager {


    public Disposable doLogin(String loginName, String password){
       return  EasyHttp.post("/app_agency/login.html").params("loginName",loginName).params("password",password).execute(new SimpleCallBack<HttpResponse<LoginInfo>>() {
            @Override
            public void onError(ApiException e) {

            }

            @Override
            public void onSuccess(HttpResponse<LoginInfo> loginInfoHttpResponse) {

            }
        });
    }


//    public Observable<HttpResponse<LoginInfo>> doLogin1(String loginName, String password){
//        return EasyHttp.post("/app_agency/login.html").params("loginName",loginName).params("password",password).execute()
//    }
}

