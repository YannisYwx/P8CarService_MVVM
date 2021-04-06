package com.p8.home.mvvm.viewmodel

import android.text.TextUtils
import androidx.databinding.ObservableField
import com.blankj.utilcode.util.CacheDoubleUtils
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.ToastUtils
import com.p8.common.Constants
import com.p8.common.bean.LoginInfo
import com.p8.common.event.ActivityEvent
import com.p8.common.event.EventCode
import com.p8.common.event.FragmentEvent
import com.p8.common.mvvm.viewmodel.BaseViewModel
import com.p8.common.net.HttpResponse
import com.p8.common.net.observer.RxAdapter
import com.p8.common.observer.P8HttpSubscriber
import com.p8.common.util.DataCleanManager
import com.p8.home.mvvm.model.MineModel
import com.p8.home.ui.mine.ModifyPasswordFragment
import org.greenrobot.eventbus.EventBus

/**
 *  @author : WX.Y
 *  date : 2021/3/29 11:49
 *  description :
 */
class MineViewModel(model: MineModel) : BaseViewModel<MineModel>(mModel = model) {

    val eventHandler: EventHandler = EventHandler()

    var appCache: String = DataCleanManager.getTotalCacheSize(getApplication())

    /**
     * 旧密码
     */
    var oldPassword: ObservableField<String> = ObservableField("")

    /**
     * 新面貌
     */
    var newPassword: ObservableField<String> = ObservableField("")

    /**
     * 确认密码
     */
    var verifyPassword: ObservableField<String> = ObservableField("")

    private val PASSWORD_REGEX = "^[a-zA-Z0-9]{6,16}$"

    inner class EventHandler {

        fun onModifyPassword() {
            ToastUtils.showShort("修改密码")
            startToFMEvent.postValue(ModifyPasswordFragment::class.java)
        }

        fun onAppUpdate() {
            ToastUtils.showShort("App更新")
        }

        fun onCacheClear() {
            ToastUtils.showShort("清除缓存")
        }
    }

    fun doModifyPassword() {
        val oldPassword = oldPassword.get()
        val newPassword = newPassword.get()
        val verifyPassword = verifyPassword.get()
        if (TextUtils.isEmpty(oldPassword)) {
            ToastUtils.showShort("请输入旧的密码")
            return
        }
        if (TextUtils.isEmpty(newPassword)) {
            ToastUtils.showShort("请输入新的密码")
            return
        }
        if (TextUtils.isEmpty(verifyPassword)) {
            ToastUtils.showShort("请再次输入新的密码")
            return
        }
        val thePassword = CacheDoubleUtils.getInstance()
            .getParcelable(Constants.Key.LOGIN_INFO, LoginInfo.CREATOR).password
        if (oldPassword.equals(thePassword)) {
            ToastUtils.showShort("旧密码不正确")
            return
        }
        if (!RegexUtils.isMatch(PASSWORD_REGEX, newPassword)) {
            ToastUtils.showShort("密码必须由6-16位字母、数字组成")
            return
        }
        if (newPassword != verifyPassword) {
            ToastUtils.showShort("新密码不一致")
            return
        }

        mModel.netManager.resetPassword("/app_agency/change_pwd.html", newPassword, oldPassword)
            .compose(
                RxAdapter.schedulersTransformer()
            )
            .doOnSubscribe(this).subscribe(object :
                P8HttpSubscriber<HttpResponse<String>>() {
                override fun onSuccess(data: HttpResponse<String>) {
                    ToastUtils.showShort("修改成功")
                    reLogin()
                }
            })
    }

    private fun reLogin() {
        CacheDoubleUtils.getInstance().clear()
        EventBus.getDefault().post(FragmentEvent(EventCode.App.RESET_PASSWORD))
    }

}