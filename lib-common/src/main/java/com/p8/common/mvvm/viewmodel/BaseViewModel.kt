package com.p8.common.mvvm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.alibaba.android.arouter.launcher.ARouter
import com.p8.common.App
import com.p8.common.event.SingleLiveEvent
import com.p8.common.mvvm.model.BaseModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import me.yokeyword.fragmentation.SupportFragment

/**
 * @author : WX.Y
 * date : 2021/3/11 18:04
 * description :
 */
open class BaseViewModel<M : BaseModel?>(
    application: Application = App.getInstance(),
    protected var mModel: M
) :
    AndroidViewModel(application), Consumer<Disposable?> {

    companion object {
        /**
         * fragment退出
         */
        const val FRAGMENT = 0x01

        /**
         * activity退出
         */
        const val ACTIVITY = 0x02
    }

    /**
     * Disposable容器 管理RxJava，主要针对RxJava异步操作造成的内存泄漏
     */
    private val mDisposables: CompositeDisposable = CompositeDisposable()
    protected var mRouter: ARouter = ARouter.getInstance()
    private var initLoadViewEvent: SingleLiveEvent<Void> = SingleLiveEvent<Void>()

    /**
     * 常规loading,null:隐藏,"":不带提示,"提示":带提示文本
     */
    private val loadingViewEvent: SingleLiveEvent<String> = SingleLiveEvent<String>()

    /**
     * 数据为空
     */
    private val emptyViewEvent: SingleLiveEvent<Void> = SingleLiveEvent<Void>()

    /**
     * 结束宿主视图
     */
    private val finishSelfEvent: SingleLiveEvent<Int> = SingleLiveEvent<Int>()

    /**
     * 清空所有状态
     */
    private val clearStatusEvent: SingleLiveEvent<Void> = SingleLiveEvent<Void>()

    /**
     * 网络异常状态
     */
    private val netErrViewEvent: SingleLiveEvent<Void> = SingleLiveEvent<Void>()

    /**
     * 跳转Fragment
     */
    val startToFMEvent: SingleLiveEvent<Class<out SupportFragment>> = SingleLiveEvent()

    fun getInitLoadViewEvent(): SingleLiveEvent<Void> {
        return initLoadViewEvent
    }

    fun getLoadingViewEvent(): SingleLiveEvent<String> {
        return loadingViewEvent
    }

    fun getEmptyViewEvent(): SingleLiveEvent<Void> {
        return emptyViewEvent
    }

    fun getFinishSelfEvent(): SingleLiveEvent<Int> {
        return finishSelfEvent
    }

    fun getClearStatusEvent(): SingleLiveEvent<Void> {
        return clearStatusEvent
    }

    fun getNetErrViewEvent(): SingleLiveEvent<Void> {
        return netErrViewEvent
    }

    @Throws(Exception::class)
    override fun accept(disposable: Disposable?) {
        mDisposables.add((disposable)!!)
    }


    protected fun <T> createLiveData(liveData: SingleLiveEvent<T>?): SingleLiveEvent<T> {
        var lD: SingleLiveEvent<T>? = liveData
        if (lD == null) {
            lD = SingleLiveEvent()
        }
        return lD
    }

    override fun onCleared() {
        super.onCleared()
        mDisposables.clear()
    }

}