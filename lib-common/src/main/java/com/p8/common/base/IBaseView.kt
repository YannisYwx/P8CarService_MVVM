package com.p8.common.base

import android.app.Activity
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.kingja.loadsir.callback.Callback
import com.p8.common.mvvm.view.status.EmptyStatus
import com.p8.common.mvvm.view.status.ErrorStatus
import com.p8.common.mvvm.view.status.InitStatus
import com.p8.common.mvvm.view.status.LoadingStatus

/**
 *  @author : WX.Y
 *  date : 2021/3/17 10:13
 *  description :
 */
interface IBaseView {

    companion object {
        var SHOW_SPACE = 200L
    }

    fun initView() {}

    /**
     * 最先的初始化，如传值之类
     */
    fun initDataBefore() {

    }

    /**
     * 初始化通用视图
     */
    fun initCommonView()

    /**
     * 初始化数据
     */
    fun initData()

    /**
     * 绑定视图布局
     *
     * @return layout res
     */
    @LayoutRes
    fun onBindLayout(): Int

    /**
     * 绑定监听事件
     */
    fun bindListener() {}

    fun getInitStatus(): Callback {
        return InitStatus()
    }

    /**
     * 加载中视图
     *
     * @return view
     */
    fun getLoadingStatus(): Callback {
        return LoadingStatus()
    }

    /**
     * 加载出错视图
     *
     * @return view
     */
    fun getErrorStatus(): Callback {
        return ErrorStatus()
    }

    /**
     * 控数据视图
     *
     * @return view
     */
    fun getEmptyStatus(): Callback {
        return EmptyStatus()
    }

    fun beginTransaction(): FragmentTransaction? {
        var fm: FragmentManager? = null
        if (this is Fragment) {
            fm = (this as Fragment).childFragmentManager
        } else if (this is Activity) {
            fm = (this as FragmentActivity).supportFragmentManager
        }
        return fm?.beginTransaction()
    }

    /**
     * 提供额外状态布局
     *
     * @return
     */
    fun getExtraStatus(): List<Callback?>? {
        return null
    }

    fun isLightMode(): Boolean {
        return true
    }

    enum class CommonBarStyle {
        /**
         * 返回图标(默认)
         */
        LEFT_BACK,

        /**
         * 返回图标+文字
         */
        LEFT_BACK_TEXT,

        /**
         * 附加图标
         */
        LEFT_ICON,

        /**
         * 标题(默认)
         */
        CENTER_TITLE,

        /**
         * 自定义布局
         */
        CENTER_CUSTOM,

        /**
         * 文字
         */
        RIGHT_TEXT,

        /**
         * 图标(默认)
         */
        RIGHT_ICON,

        /**
         * 自定义布局
         */
        RIGHT_CUSTOM
    }
}