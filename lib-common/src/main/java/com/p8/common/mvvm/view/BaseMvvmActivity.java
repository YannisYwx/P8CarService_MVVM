package com.p8.common.mvvm.view;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.p8.common.mvvm.model.BaseModel;
import com.p8.common.mvvm.viewmodel.BaseViewModel;

import java.lang.reflect.ParameterizedType;

/**
 * @author : WX.Y
 * date : 2021/3/11 18:02
 * description :
 */
public abstract class BaseMvvmActivity<DB extends ViewDataBinding, VM extends BaseViewModel> extends BaseActivity<DB> {
    protected VM mViewModel;

    @Override
    public void initData() {
        initViewModel();
        initBaseViewObservable();
        initViewObservable();
    }

    @Override
    public void initView() {
        showInitView();
    }

    private void initViewModel() {
        mViewModel = createViewModel();
    }

    public VM createViewModel() {
        Class<VM> tClass = (Class<VM>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        return ViewModelProviders.of(this, onBindViewModelFactory()).get(tClass);
    }

    public abstract ViewModelProvider.Factory onBindViewModelFactory();

    public abstract void initViewObservable();

    /**
     * 注册ViewModel与View的契约UI回调事件
     */
    protected void initBaseViewObservable() {
        mViewModel.getInitLoadViewEvent().observe(this, (Observer<Void>) show -> showInitView());
        mViewModel.getLoadingViewEvent().observe(this, (Observer<String>) this::showLoadingView);
        mViewModel.getEmptyViewEvent().observe(this, (Observer<Void>) show -> showEmptyView());
        mViewModel.getNetErrViewEvent().observe(this, (Observer<Void>) show -> showErrorView());
        mViewModel.getFinishSelfEvent().observe(this, (Observer<Void>) v -> finish());
        mViewModel.getClearStatusEvent().observe(this, (Observer<Void>) v -> clearStatus());
    }

}