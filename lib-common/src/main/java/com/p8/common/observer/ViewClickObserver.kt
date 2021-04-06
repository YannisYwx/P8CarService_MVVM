package com.p8.common.observer

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 *  @author : WX.Y
 *  date : 2021/3/22 16:37
 *  description :
 */
abstract class ViewClickObserver: Observer<Unit> {

    override fun onComplete() {
    }

    override fun onSubscribe(d: Disposable) {
    }

    override fun onNext(t: Unit) {
        onClick()
    }

    override fun onError(e: Throwable) {
    }

    abstract fun onClick()
}