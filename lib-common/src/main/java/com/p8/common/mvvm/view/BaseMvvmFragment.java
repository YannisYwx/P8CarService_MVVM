package com.p8.common.mvvm.view;

import androidx.annotation.CallSuper;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.p8.common.mvvm.viewmodel.BaseViewModel;

import java.lang.reflect.ParameterizedType;

/**
 * @author : WX.Y
 * date : 2021/3/11 18:01
 * description :
 */
public abstract class BaseMvvmFragment<DB extends ViewDataBinding,VM extends BaseViewModel> extends BaseFragment<DB> {
    protected VM mViewModel;

    @Override
    public void initParam() {
        initViewModel();
        initBaseViewObservable();
        initViewObservable();
    }

    @CallSuper
    @Override
    protected void loadView() {
        super.loadView();
        //默认显示初始化视图
        showInitView();
        mBinding.setVariable(initVariableId(),mViewModel);
    }

    protected void initViewModel() {
        mViewModel = createViewModel();
    }

    protected VM createViewModel() {
        Class<VM> tClass = (Class<VM>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        return ViewModelProviders.of(this, onBindViewModelFactory()).get(tClass);
    }

    protected abstract ViewModelProvider.Factory onBindViewModelFactory();

    protected abstract void initViewObservable();

    protected void initBaseViewObservable() {
        mViewModel.getInitLoadViewEvent().observe(this, (Observer<Void>) show -> showInitView());
        mViewModel.getLoadingViewEvent().observe(this, (Observer<String>) this::showLoadingView);
        mViewModel.getEmptyViewEvent().observe(this, (Observer<Void>) show -> showEmptyView());
        mViewModel.getNetErrViewEvent().observe(this, (Observer<Void>) show -> showErrorView());
        mViewModel.getFinishSelfEvent().observe(this, (Observer<Void>) v -> {
//            pop();
        });
        mViewModel.getClearStatusEvent().observe(this, (Observer<Void>) v -> clearStatus());
    }
}

