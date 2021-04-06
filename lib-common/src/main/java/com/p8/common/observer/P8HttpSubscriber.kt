package com.p8.common.observer

import android.content.Context
import com.blankj.utilcode.util.ToastUtils
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.impl.LoadingPopupView
import com.orhanobut.logger.Logger
import com.p8.common.Constants
import com.p8.common.event.ActivityEvent
import com.p8.common.event.EventCode
import com.p8.common.net.HttpError
import com.p8.common.net.HttpResponse
import com.p8.common.net.observer.ObservableSubscriber
import org.greenrobot.eventbus.EventBus

/**
 *  @author : WX.Y
 *  date : 2021/3/22 11:03
 *  description :
 */
abstract class P8HttpSubscriber<T>(var context: Context? = null) : ObservableSubscriber<T>() {
    var msg: String = "网络请求中..."
    private var loadingPopup: LoadingPopupView? = null

    override fun onStart() {
        if (isShowProgressDialog() && context != null) {
            loadingPopup = XPopup.Builder(context).dismissOnBackPressed(false)
                .asLoading(msg)
                .show() as LoadingPopupView
        }
        super.onStart()

    }

    override fun onEnd() {
        super.onEnd()
        if (isShowProgressDialog()) {
            loadingPopup?.let {
                it.postDelayed(Runnable {
                    it.dismiss()
                }, 500)
            }
        }

    }

    override fun onFail(httpError: HttpError?) {
        Logger.e(httpError.toString())
        httpError?.let {
            ToastUtils.showShort(it.msg)
            val body: Any? = it.body
            if (body is Throwable) {
                body.message?.let { it1 ->
                    Logger.e(it1)
                }
            }
        }
    }

    override fun onNext(t: T) {
        if (t is HttpResponse<*>) {
            val tr = t as HttpResponse<*>
            val code = (t as HttpResponse<*>).code
            Logger.d(tr.toString())
            when (code) {
                Constants.P8Code.SUCCESS -> onSuccess(t)
                Constants.P8Code.FAILED -> onFail(HttpError(tr.msg))
                Constants.P8Code.TOKEN_ERROR -> {
                    EventBus.getDefault().post(ActivityEvent(EventCode.App.TOKEN_INVALID))
                }
                else -> {

                }
            }
        }
    }

    open fun isShowProgressDialog() = false

    /**
     * 请求成功
     */
    abstract fun onSuccess(data: T)
}