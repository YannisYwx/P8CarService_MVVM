package com.p8.common.databinding

import android.util.SparseArray
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModel

/**
 *  @author : WX.Y
 *  date : 2021/3/24 11:30
 *  description :
 * TODO tip:
 * 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
 * 通过这样的方式，来彻底解决 视图调用的一致性问题，
 * 如此，视图实例的安全性将和基于函数式编程思想的 Jetpack Compose 持平。
 * 而 DataBindingConfig 就是在这样的背景下，用于为 base 页面中的 DataBinding 提供绑定项。
 */
class DataBindingConfig(
    @LayoutRes var layout: Int,
    var vmVariableId: Int,
    var stateViewModel: ViewModel
) {
    private val bindingParams = SparseArray<Any>()

    fun addBindingParam(
        variableId: Int,
        `object`: Any
    ): DataBindingConfig {
        if (bindingParams[variableId] == null) {
            bindingParams.put(variableId, `object`)
        }
        return this
    }

    fun getBindingParams() = bindingParams
}