package com.p8.main.mvvm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.p8.common.App
import com.p8.main.mvvm.model.LoginModel
import com.p8.main.mvvm.viewmodel.LoginViewModel

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
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> return LoginViewModel(
                LoginModel()
            ) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}