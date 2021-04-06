package com.p8.home.mvvm.model

import com.blankj.utilcode.util.CacheDoubleUtils
import com.p8.common.Constants
import com.p8.common.bean.LoginInfo
import com.p8.common.mvvm.model.BaseModel
import com.p8.common.net.observer.RxAdapter

/**
 *  @author : WX.Y
 *  date : 2021/3/23 11:15
 *  description :
 */
class HomeModel : BaseModel() {

    private lateinit var mLoginInfo: LoginInfo

    fun requestAgencyInfo() =
        netManager.agencyInfo.compose(RxAdapter.schedulersTransformer())

    fun getLoginInfo(): LoginInfo {
        return CacheDoubleUtils.getInstance()
            .getParcelable(Constants.Key.LOGIN_INFO, LoginInfo.CREATOR)
    }



}