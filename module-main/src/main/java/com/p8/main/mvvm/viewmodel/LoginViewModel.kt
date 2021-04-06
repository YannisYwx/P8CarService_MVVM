package com.p8.main.mvvm.viewmodel

import android.content.Context
import androidx.databinding.ObservableField
import com.blankj.utilcode.util.CacheDoubleUtils
import com.blankj.utilcode.util.ToastUtils
import com.p8.common.Constants
import com.p8.common.bean.LoginInfo
import com.p8.common.binding.command.BindingAction
import com.p8.common.binding.command.BindingCommand
import com.p8.common.binding.command.BindingConsumer
import com.p8.common.event.SingleLiveEvent
import com.p8.common.mvvm.viewmodel.BaseViewModel
import com.p8.common.net.HttpResponse
import com.p8.common.observer.P8HttpSubscriber
import com.p8.main.mvvm.model.LoginModel

/**
 *  @author : WX.Y
 *  date : 2021/3/19 15:57
 *  description :
 */
class LoginViewModel(model: LoginModel) : BaseViewModel<LoginModel>(mModel = model) {
    //用户名的绑定
    var userName: ObservableField<String> = ObservableField("")

    //密码的绑定
    var password: ObservableField<String> = ObservableField("")

    var mLoginEvent : SingleLiveEvent<HttpResponse<LoginInfo>> = SingleLiveEvent<HttpResponse<LoginInfo>>()


    fun initAccount(@Constants.UserType type:Int){
        userName.set(if (type == Constants.UserType.LAND) "15919835035" else "wzh")
        password.set(if (type == Constants.UserType.LAND) "456789" else "123456")
    }

    //用户名输入框焦点改变的回调事件
    var onFocusChangeCommand: BindingCommand<Boolean> =
        BindingCommand<Boolean>(object : BindingConsumer<Boolean> {
            override fun call(isFocus: Boolean) {
                if (isFocus) {

                } else {

                }
            }
        })


    //登录按钮的点击事件
    var loginOnClickCommand: BindingCommand<Unit> = BindingCommand(BindingAction {
        val iUserName = userName.get()
        val iPassword = password.get()
        if (iUserName.isNullOrEmpty()) {
            ToastUtils.showShort("请输入账号")
            return@BindingAction
        }
        if (iPassword.isNullOrEmpty()) {
            ToastUtils.showShort("请输入密码")
            return@BindingAction
        }

        doLogin(iUserName, iPassword, null)
    })

    fun doLogin(userName: String, password: String, context: Context?) {

        mModel.login(userName, password).doOnSubscribe(this).subscribe(object :
            P8HttpSubscriber<HttpResponse<LoginInfo>>(context) {
            override fun onSuccess(data: HttpResponse<LoginInfo>) {
                CacheDoubleUtils.getInstance().put(Constants.Key.LOGIN_INFO, data.data)
                CacheDoubleUtils.getInstance().put(Constants.KEY_TOKEN, data.data.token)
                println("login>>>>>>>>>==============================${data.data.token}")
                mLoginEvent.postValue(data)
                println("login>>>>>>>>>==============================${mLoginEvent}")
            }

            override fun isShowProgressDialog(): Boolean {
                return true
            }
        })

    }


}