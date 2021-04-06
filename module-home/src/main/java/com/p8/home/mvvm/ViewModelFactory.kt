package com.p8.home.mvvm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.p8.common.App
import com.p8.home.mvvm.model.HomeModel
import com.p8.home.mvvm.model.MineModel
import com.p8.home.mvvm.viewmodel.HomeViewModel
import com.p8.home.mvvm.viewmodel.MineViewModel

/**
 *  @author : WX.Y
 *  date : 2021/3/19 16:58
 *  description : ViewModel提供工厂
 */
class ViewModelFactory private constructor(var app: Application) : NewInstanceFactory() {

    companion object {
        private var sInstance: ViewModelFactory? = null
        fun getInstance(app: Application = App.getInstance()): ViewModelFactory =
            sInstance ?: synchronized(this) {
                sInstance ?: ViewModelFactory(app).also { sInstance = it }
            }
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> return HomeViewModel(
                HomeModel()
            ) as T
            modelClass.isAssignableFrom(MineViewModel::class.java) -> return MineViewModel(
                MineModel()
            ) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}