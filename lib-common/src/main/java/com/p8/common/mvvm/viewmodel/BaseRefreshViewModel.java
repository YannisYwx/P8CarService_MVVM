package com.p8.common.mvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.p8.common.event.SingleLiveEvent;
import com.p8.common.mvvm.model.BaseModel;

import java.util.Collections;
import java.util.List;

/**
 * @author : WX.Y
 * date : 2021/3/12 16:24
 * description :
 */
public class BaseRefreshViewModel<M extends BaseModel, T> extends BaseViewModel<M> {
    public BaseRefreshViewModel(@NonNull Application application, M model) {
        super(application, model);
    }

    private SingleLiveEvent<List<T>> mFinishRefreshEvent;
    private SingleLiveEvent<List<T>> mFinishLoadMoreEvent;
    /**
     * null:失败,size==0:成功,size!=0:执行onRefeshSucc
     */
    public SingleLiveEvent<List<T>> getFinishRefreshEvent() {
        return mFinishRefreshEvent = createLiveData(mFinishRefreshEvent);
    }
    /**
     * null:失败,size==0:成功,size!=0:执行onLoadmoreSucc
     */
    public SingleLiveEvent<List<T>> getFinishLoadMoreEvent() {
        return mFinishLoadMoreEvent = createLiveData(mFinishLoadMoreEvent);
    }

    /**
     * 当界面下拉刷新时,默认直接结束刷新
     */
    public void onViewRefresh() {
        getFinishRefreshEvent().postValue(Collections.emptyList());
    }

    /**
     * 当界面下拉更多时,默认没有更多数据
     */
    public void onViewLoadMore() {
        getFinishLoadMoreEvent().postValue(Collections.emptyList());
    }
}

