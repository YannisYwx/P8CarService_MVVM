package com.p8.common.mvvm.view;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.p8.common.mvvm.viewmodel.BaseRefreshViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

/**
 * @author : WX.Y
 * date : 2021/3/12 16:26
 * description :
 */
public abstract class BaseRefreshActivity<DB extends ViewDataBinding, VM extends BaseRefreshViewModel, T>
        extends BaseMvvmActivity<DB, VM>
        implements OnRefreshLoadMoreListener {

    private WrapRefresh mWrapRefresh;

    @CallSuper
    @Override
    public void bindListener() {
        super.bindListener();
        mWrapRefresh = onBindWrapRefresh();
        mWrapRefresh.refreshLayout.setOnRefreshLoadMoreListener(this);
    }

    /**
     * 绑定SmartRefresh
     *
     * @return
     */
    @NonNull
    protected abstract WrapRefresh onBindWrapRefresh();

    @Override
    protected void initBaseViewObservable() {
        super.initBaseViewObservable();
        mViewModel.getFinishRefreshEvent().observe(this, (Observer<List<T>>) list -> {
            if (list == null) {
                mWrapRefresh.refreshLayout.finishRefresh(false);
                return;
            }
            if (list.size() == 0) {
                mWrapRefresh.refreshLayout.finishRefresh(true);
                return;
            }
            mWrapRefresh.refreshLayout.finishRefresh(true);
            onRefreshSucc(list);
        });
        mViewModel.getFinishLoadMoreEvent().observe(this, (Observer<List<T>>) list -> {
            if (list == null) {
                mWrapRefresh.refreshLayout.finishLoadMore(false);
                return;
            }
            if (list.size() == 0) {
                mWrapRefresh.refreshLayout.finishLoadMoreWithNoMoreData();
                return;
            }
            mWrapRefresh.refreshLayout.finishLoadMore(true);
            onLoadMoreSucc(list);
        });
    }

    protected void onRefreshSucc(List<T> list) {
        if (mWrapRefresh.quickAdapter != null) {
            mWrapRefresh.quickAdapter.setNewData(list);
        }
    }

    protected void onLoadMoreSucc(List<T> list) {
        if (mWrapRefresh.quickAdapter != null) {
            mWrapRefresh.quickAdapter.addData(list);
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mViewModel.onViewLoadMore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mViewModel.onViewRefresh();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mWrapRefresh) {
            mWrapRefresh.refreshLayout.setOnRefreshLoadMoreListener(null);
        }
    }

    protected class WrapRefresh {
        SmartRefreshLayout refreshLayout;
        BaseQuickAdapter<T, BaseViewHolder> quickAdapter;

        public WrapRefresh(@NonNull SmartRefreshLayout refreshLayout, BaseQuickAdapter<T, BaseViewHolder> quickAdapter) {
            this.refreshLayout = refreshLayout;
            this.quickAdapter = quickAdapter;
        }
    }
}

