package com.p8.main.mvvm.model

import com.p8.common.App
import com.p8.common.bean.LoginInfo
import com.p8.common.mvvm.model.BaseModel
import com.p8.common.net.HttpResponse
import com.p8.common.net.RxAdapter
import io.reactivex.Observable

/**
 *  @author : WX.Y
 *  date : 2021/3/19 15:58
 *  description :
 */
class LoginModel : BaseModel(App.getInstance()) {

    fun login(userName: String, password: String): Observable<HttpResponse<LoginInfo>> =
        netManager.doLoginByLargeMaster(userName, password)
//            .compose(RxAdapter.exceptionTransformer())
            .compose(RxAdapter.schedulersTransformer())

}