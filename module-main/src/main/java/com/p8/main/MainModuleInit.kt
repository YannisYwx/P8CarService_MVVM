package com.p8.main

import com.blankj.utilcode.util.Utils
import com.kingja.loadsir.core.LoadSir
import com.orhanobut.logger.Logger
import com.p8.common.App
import com.p8.common.IModuleInit
import com.p8.common.adapter.ScreenAutoAdapter
import com.p8.common.mvvm.view.status.EmptyStatus
import com.p8.common.mvvm.view.status.ErrorStatus
import com.p8.common.mvvm.view.status.InitStatus
import com.p8.common.mvvm.view.status.LoadingStatus
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.cache.converter.GsonDiskConverter
import com.zhouyou.http.cache.model.CacheMode

/**
 *  @author : WX.Y
 *  date : 2021/3/23 10:17
 *  description :
 */
class MainModuleInit : IModuleInit {
    private val HOST = "http://service.p8.world"

    override fun onInitAhead(application: App?): Boolean {
        ScreenAutoAdapter.setup(application!!)
        EasyHttp.init(application)
        if (application.isDebug) {
            EasyHttp.getInstance().debug("easyhttp", true)
        }
        EasyHttp.getInstance()
            .setBaseUrl(HOST)
            .setReadTimeOut(15 * 1000.toLong())
            .setWriteTimeOut(15 * 1000.toLong())
            .setConnectTimeout(15 * 1000.toLong())
            .setRetryCount(3)
            .setCacheDiskConverter(GsonDiskConverter())
            .setCacheMode(CacheMode.FIRSTREMOTE)
        LoadSir.beginBuilder()
            .addCallback(ErrorStatus())
            .addCallback(LoadingStatus())
            .addCallback(EmptyStatus())
            .addCallback(InitStatus())
            .setDefaultCallback(InitStatus::class.java)
            .commit()
        Utils.init(application)
        Logger.i("main组件初始化完成 -- onInitAhead")
        return false
    }

    override fun onInitLow(application: App?) = false
}