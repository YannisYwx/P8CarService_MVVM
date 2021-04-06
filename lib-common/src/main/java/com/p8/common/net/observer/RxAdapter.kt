package com.p8.common.net.observer

import android.util.Log
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.p8.common.net.HttpResponse
import com.p8.common.net.exception.CustomException
import com.p8.common.net.exception.ExceptionConverter
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

/**
 *  @author : WX.Y
 *  date : 2021/3/23 15:26
 *  description :
 */
class RxAdapter {

    companion object {
        /**
         * 线程调度器
         */
        fun <T> schedulersTransformer(): ObservableTransformer<T, T>? {
            return ObservableTransformer { upstream: Observable<T> ->
                upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
        }

        /**
         * 异常处理方式
         * <br></br>StreamHandler(将内部异常选择性抛出,可设置需要重试的异常)->
         * <br></br>ExceptionRetry(所有异常都会经过此处,可拦截需要重试的内部异常,如Token超时等)->
         * <br></br>ExceptionHandler(统一处理未被拦截内部异常和所有外部异常)
         */
        fun <T> exceptionTransformer(): ObservableTransformer<T, T>? {
            return ObservableTransformer { observable: Observable<T> ->
                observable
                    .flatMap(StreamHandler())
                    .retryWhen(ExceptionRetry()) //拦截需要处理的异常
                    .onErrorResumeNext(ExceptionHandler())
            }
        }
    }

    /**
     * 将内部异常选择性抛出,可设置需要重试的异常
     */
    private class StreamHandler<T> :
        Function<T, ObservableSource<T>> {
        @Throws(Exception::class)
        override fun apply(o: T): Observable<T> {
            return handle(o)
        }

        private fun handle(o: T): Observable<T> {
            if (o is HttpResponse<*>) {
                val respDTO = o as HttpResponse<*>
                //选择性抛出内部异常
                if (respDTO.code != ExceptionConverter.P8HttpCode.SUCCESS) {
                    val throwable: Exception =
                        CustomException(respDTO.code, respDTO.msg)
                    return Observable.error(throwable)
                }
            }
            return Observable.just(o)
        }
    }

    /**
     * 统一处理未被拦截内部异常和所有外部异常
     */
    private class ExceptionHandler<T> :
        Function<Throwable, Observable<T>> {
        override fun apply(t: Throwable): Observable<T> {
            return handle(t)
        }

        private fun handle(t: Throwable): Observable<T> {
            //转换外部异常
            ToastUtils.showShort(t.message)
            return if (t !is CustomException) {
                Observable.error(ExceptionConverter.convert(t))
            } else {
                Observable.error(t)
            }
        }
    }


    /**
     * 所有异常都会经过此处,可拦截需要重试的内部异常,如Token超时等
     */
    class ExceptionRetry :
        Function<Observable<Throwable?>, Observable<*>> {

        @Throws(java.lang.Exception::class)
        override fun apply(observable: Observable<Throwable?>): Observable<*> {
            return observable.compose { upstream: Observable<Throwable?> ->
                upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
                .flatMap(Function<Throwable?, Observable<*>> { throwable: Throwable? ->
                    LogUtils.d(
                        Log.getStackTraceString(
                            throwable
                        )
                    )

                    Observable.error<Any?>(throwable)
                    /*
                  //拦截内部异常
                  if (throwable instanceof InterceptableException) {
                      InterceptableException ex = (InterceptableException) throwable;
                      switch (ex.code) {
                          case InterceptableException.TOKEN_OUTTIME:
                              return reLogin();
                          default:
                              return Observable.error(throwable);
                      }
                  } else {
                      //外部异常直接放过
                      return Observable.error(throwable);
                  }*/
                })
        }
    }

}