package com.p8.common.net.exception;

import android.util.Log;

import com.blankj.utilcode.util.LogUtils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author : WX.Y
 * date : 2021/3/12 10:15
 * description :
 */
public class ExceptionRetry implements Function<Observable<Throwable>, Observable<?>> {
    @Override
    public Observable<?> apply(Observable<Throwable> observable) throws Exception {

        return observable.compose(upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()))
                .flatMap((Function<Throwable, Observable<?>>) throwable -> {
                    LogUtils.d(Log.getStackTraceString(throwable));
                    return Observable.error(throwable);
                });
    }

}