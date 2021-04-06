package com.p8.home.mvvm.viewmodel

import com.p8.common.bean.Agency
import com.p8.common.binding.command.BindingAction
import com.p8.common.binding.command.BindingCommand
import com.p8.common.event.SingleLiveEvent
import com.p8.common.mvvm.viewmodel.BaseViewModel
import com.p8.common.net.HttpResponse
import com.p8.common.observer.P8HttpSubscriber
import com.p8.home.mvvm.model.HomeModel

/**
 *  @author : WX.Y
 *  date : 2021/3/23 16:44
 *  description :
 */
class HomeViewModel(model: HomeModel) : BaseViewModel<HomeModel>(mModel = model) {
    val mAgencyEvent = SingleLiveEvent<Agency>()
    fun getAgencyInfo() {
        mModel.requestAgencyInfo().doOnSubscribe(this)
            .subscribe(object : P8HttpSubscriber<HttpResponse<Agency>>() {
                override fun onSuccess(data: HttpResponse<Agency>) {
                    println(data.data)
                    mAgencyEvent.postValue(data.data)
                }
            })
    }

    fun getCurrentUserInfo() = mModel.getLoginInfo()

    var headerClickCommand = BindingCommand<Unit>(BindingAction {

    })

}