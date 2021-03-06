package com.p8.common.databinding.ui;

import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.AdaptScreenUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.CollectionUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.callback.SuccessCallback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.orhanobut.logger.Logger;
import com.p8.common.App;
import com.p8.common.R;
import com.p8.common.databinding.DataBindingConfig;
import com.p8.common.event.FragmentEvent;
import com.p8.common.mvvm.viewmodel.BaseViewModel;
import com.wuhenzhizao.titlebar.statusbar.StatusBarUtils;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.ParameterizedType;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * @author : WX.Y
 * date : 2021/3/24 17:50
 * description :
 */
public abstract class DataBindingFragment<DB extends ViewDataBinding, VM extends BaseViewModel<?>> extends SupportFragment implements IBaseView, Consumer<Disposable> {
    protected String TAG = DataBindingFragment.class.getSimpleName();
    protected Application mApplication;
    /**
     * ??????Handler
     */
    protected Handler mHandler = new Handler(Looper.getMainLooper());
    /**
     * Disposable??????
     */
    protected CompositeDisposable mDisposables = new CompositeDisposable();

    protected ARouter mRouter = ARouter.getInstance();
    /**
     * ?????????
     */
    protected View mView;
    /**
     * ???????????????
     */
    protected LoadService<?> mBaseLoadService;
    /**
     * ???????????????
     */
    protected CommonTitleBar mCommonTitleBar;
    /**
     * ???????????????????????????
     */
    private boolean isFirst = true;

    protected boolean hasInit;

    protected DB mBinding;
    protected VM mViewModel;

    /**
     * ?????????????????????
     */
    protected boolean isReuse;

    protected ViewStub vsBar;
    protected ViewStub vsContent;

    /**
     * ?????????????????????
     */
    @Deprecated
    protected void onReVisible() {
        Logger.i(TAG + " is onReVisible");
        Logger.e("????????????=====" + TAG);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = "Ywx_" + getClass().getSimpleName();
        Log.d(TAG, "onCreate() savedInstanceState = [" + savedInstanceState + "]");
        mApplication = App.getInstance();
        mRouter.inject(this);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        isReuse = mView != null;
        if (isReuse) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        } else {
            mView = inflater.inflate(R.layout.common_layout_root, container, false);
        }
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
        if (!isReuse) {
            initCommonView();
            //??????????????????
            if (!enableLazy()) {
                initDataBefore();
                loadView();
                initView();
                bindListener();
                initData();
                hasInit = true;
            }
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        Logger.e("|onLazyInitView|-----??????????????????>" + TAG);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        Logger.e("|onSupportVisible|-----??????>" + TAG);
        if (mCommonTitleBar != null) {
            int ch = mCommonTitleBar.getHeight();
            int cw = mCommonTitleBar.getWidth();
            System.out.println("????????????============ch = " + ch + "====cw = " + cw);
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        Logger.e("|onSupportInvisible|-----?????????>" + TAG);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
    }

    /**
     * ??????????????????????????????
     *
     * @return true ??????  false ??????
     */
    public boolean isLightBarMode() {
        return false;
    }

    @Override
    public int onBindLayout() {
        return 0;
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        if (!isReuse && enableLazy() && isFirst) {
            Logger.e("?????????  ?????????" + TAG);
            initDataBefore();
            loadView();
            initView();
            bindListener();
            initData();
            hasInit = true;
        }
        if (!isFirst) {
            onReVisible();
        }
        isFirst = false;
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.d(TAG, "onHiddenChanged() hidden = [" + hidden + "]");
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");
        super.onDestroyView();
    }

    /**
     * ?????????????????????
     */
    @Override
    public void initCommonView() {
        if (enableCommonBar()) {
            vsBar = mView.findViewById(R.id.vsBar);
            vsBar.setLayoutResource(R.layout.common_layout_titlebar);
            mCommonTitleBar = vsBar.inflate().findViewById(R.id.ctb);
            initCommonTitleBar(mCommonTitleBar);
        }
    }

    /**
     * ??????????????????
     *
     * @return DataBindingConfig
     */
    protected abstract DataBindingConfig getDataBindingConfig();

    /**
     * ?????????databinding ??? viewmodel
     */
    private void initDataBindingViewModel() {
        mViewModel = createViewModel();
        DataBindingConfig dataBindingConfig = getDataBindingConfig();
        vsContent = mView.findViewById(R.id.vsContent);
        vsContent.setLayoutResource(dataBindingConfig.getLayout());
        mBinding = DataBindingUtil.bind(vsContent.inflate());
        //TODO tip: DataBinding ???????????????
        // ??? DataBinding ??????????????? base ???????????????????????????????????????
        // ??????????????????????????????????????? ?????????????????????????????????
        // ????????????????????????????????????????????????????????????????????? Jetpack Compose ?????????

        // ?????????????????????????????????????????? https://xiaozhuanlan.com/topic/9816742350 ??? https://xiaozhuanlan.com/topic/2356748910
        assert mBinding != null;
        mBinding.setLifecycleOwner(this);
        //??????databinding???viewmodel
        mBinding.setVariable(dataBindingConfig.getVmVariableId(), dataBindingConfig.getStateViewModel());
        SparseArray<?> bindingParams = dataBindingConfig.getBindingParams();
        for (int i = 0, length = bindingParams.size(); i < length; i++) {
            mBinding.setVariable(bindingParams.keyAt(i), bindingParams.valueAt(i));
        }
    }

    @Override
    public void initData() {
        initBaseViewObservable();
        initViewObservable();
    }

    public VM createViewModel() {
        Class<VM> tClass = (Class<VM>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        return ViewModelProviders.of(this, onBindViewModelFactory()).get(tClass);
    }

    public abstract ViewModelProvider.Factory onBindViewModelFactory();

    /**
     * ??????????????????view?????????
     */
    public abstract void initViewObservable();

    /**
     * ??????ViewModel???View?????????UI????????????
     */
    protected void initBaseViewObservable() {
        mViewModel.getInitLoadViewEvent().observe(this, (Observer<Void>) show -> showInitView());
        mViewModel.getLoadingViewEvent().observe(this, (Observer<String>) this::showLoadingView);
        mViewModel.getEmptyViewEvent().observe(this, (Observer<Void>) show -> showEmptyView());
        mViewModel.getNetErrViewEvent().observe(this, (Observer<Void>) show -> showErrorView());
        mViewModel.getFinishSelfEvent().observe(this, integer -> {
            if (integer == BaseViewModel.FRAGMENT) {
                pop();
            } else if (integer == BaseViewModel.ACTIVITY) {
                _mActivity.finish();
            }
        });
        mViewModel.getClearStatusEvent().observe(this, (Observer<Void>) v -> clearStatus());
        mViewModel.getStartToFMEvent().observe(this, this::startToFragment);
    }

    /**
     * ??????fragment
     *
     * @param aClass
     */
    public void startToFragment(Class<? extends SupportFragment> aClass) {

    }

    /**
     * ????????????(???????????????)
     */
    protected void loadView() {
        BarUtils.setStatusBarLightMode(_mActivity, isLightBarMode());
        ImmersionBar.with(this).init();
        initDataBindingViewModel();
        LoadSir.Builder builder = new LoadSir.Builder()
                .addCallback(getInitStatus())
                .addCallback(getEmptyStatus())
                .addCallback(getErrorStatus())
                .addCallback(getLoadingStatus())
                .setDefaultCallback(SuccessCallback.class);
        if (!CollectionUtils.isEmpty(getExtraStatus())) {
            for (Callback callback : getExtraStatus()) {
                builder.addCallback(callback);
            }
        }
        FrameLayout.LayoutParams rootViewLayoutParams = null;
        if (enableCommonBar()) {
            initStatusBar();
            rootViewLayoutParams = new FrameLayout.LayoutParams((FrameLayout.LayoutParams) mBinding.getRoot().getLayoutParams());
            boolean b = StatusBarUtils.supportTransparentStatusBar();
            int barHeight = b ? BarUtils.getStatusBarHeight() : 0;
            rootViewLayoutParams.topMargin = AdaptScreenUtils.pt2Px(144) + barHeight;
        }
        mBaseLoadService = builder.build().register(mBinding.getRoot(), rootViewLayoutParams, (Callback.OnReloadListener) DataBindingFragment.this::onReload);
//        showInitView(); //?????????????????????
    }


    private void initStatusBar() {
        if (!isRootPager()) {
            return;
        }
        FrameLayout.LayoutParams titleBarLayoutParams = new FrameLayout.LayoutParams((FrameLayout.LayoutParams) mCommonTitleBar.getLayoutParams());
        boolean b = StatusBarUtils.supportTransparentStatusBar();
        titleBarLayoutParams.topMargin = b ? BarUtils.getStatusBarHeight() : 0;
        mCommonTitleBar.setLayoutParams(titleBarLayoutParams);
        BarUtils.setStatusBarColor(_mActivity, mCommonTitleBar.getResources().getColor(R.color.colorPrimary));
    }


    /**
     * ????????????????????????????????????
     *
     * @return ??????????????????DataBindingFragment??????SupportFragment,???????????????activity+???fragment??????
     */
    public boolean isRootPager() {
        return true;
    }


    @Override
    public void accept(Disposable disposable) throws Exception {
        mDisposables.add(disposable);
    }

    /**
     * ????????????????????????
     */
    protected void initCommonTitleBar(CommonTitleBar titleBar) {
        // ??????
        if (onBindBarCenterStyle() == CommonBarStyle.CENTER_TITLE) {
            String[] strings = onBindBarTitleText();
            if (strings != null && strings.length > 0) {
                if (null != strings[0] && strings[0].trim().length() > 0) {
                    TextView title = titleBar.getCenterCustomView().findViewById(R.id.tv_title);
                    title.setVisibility(View.VISIBLE);
                    title.setText(strings[0]);
                }
                if (strings.length > 1 && null != strings[1] && strings[1].trim().length() > 0) {
                    TextView subtitle = titleBar.getCenterCustomView().findViewById(R.id.tv_subtitle);
                    subtitle.setVisibility(View.VISIBLE);
                    subtitle.setText(strings[1]);
                }
            }
        } else if (onBindBarCenterStyle() == CommonBarStyle.CENTER_CUSTOM && onBindBarCenterCustom() != null) {
            ViewGroup group = titleBar.getCenterCustomView().findViewById(R.id.fl_custome);
            group.setVisibility(View.VISIBLE);
            group.addView(onBindBarCenterCustom(), new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        //??????
        if (onBindBarLeftStyle() == CommonBarStyle.LEFT_BACK) {

            ImageView backView = titleBar.getLeftCustomView().findViewById(R.id.iv_back);
            if (onBindBarBackIcon() != null) {
                backView.setImageResource(onBindBarBackIcon());
            }
            backView.setVisibility(View.VISIBLE);
            backView.setOnClickListener(v -> pop());
        } else if (onBindBarLeftStyle() == CommonBarStyle.LEFT_BACK_TEXT) {
            View backIcon = titleBar.getLeftCustomView().findViewById(R.id.iv_back);
            backIcon.setVisibility(View.VISIBLE);
//            backIcon.setOnClickListener(v -> onSimpleBackClick());
            View backTv = titleBar.getLeftCustomView().findViewById(R.id.tv_back);
            backTv.setVisibility(View.VISIBLE);
//            backTv.setOnClickListener(v -> onSimpleBackClick());
        } else if (onBindBarLeftStyle() == CommonBarStyle.LEFT_ICON && onBindBarLeftIcon() != null) {
            ImageView icon = titleBar.getLeftCustomView().findViewById(R.id.iv_left);
            icon.setVisibility(View.VISIBLE);
            icon.setImageResource(onBindBarLeftIcon());
            icon.setOnClickListener(this::onLeftIconClick);
        }
        //??????
        switch (onBindBarRightStyle()) {
            case RIGHT_TEXT:
                String[] strings = onBindBarRightText();
                if (strings == null || strings.length == 0) {
                    break;
                }
                if (null != strings[0] && strings[0].trim().length() > 0) {
                    TextView tv1 = titleBar.getRightCustomView().findViewById(R.id.tv1_right);
                    tv1.setVisibility(View.VISIBLE);
                    tv1.setText(strings[0]);
                    tv1.setOnClickListener(this::onRight1Click);
                }
                if (strings.length > 1 && null != strings[1] && strings[1].trim().length() > 0) {
                    TextView tv2 = titleBar.getRightCustomView().findViewById(R.id.tv2_right);
                    tv2.setVisibility(View.VISIBLE);
                    tv2.setText(strings[1]);
                    tv2.setOnClickListener(this::onRight2Click);
                }
                break;
            case RIGHT_ICON:
                Integer[] ints = onBindBarRightIcon();
                if (ints == null || ints.length == 0) {
                    break;
                }
                if (null != ints[0]) {
                    ImageView iv1 = titleBar.getRightCustomView().findViewById(R.id.iv1_right);
                    iv1.setVisibility(View.VISIBLE);
                    iv1.setImageResource(ints[0]);
                    iv1.setOnClickListener(this::onRight1Click);
                }
                if (ints.length > 1 && null != ints[1]) {
                    ImageView iv2 = titleBar.getRightCustomView().findViewById(R.id.iv2_right);
                    iv2.setVisibility(View.VISIBLE);
                    iv2.setImageResource(ints[1]);
                    iv2.setOnClickListener(this::onRight2Click);
                }
                break;
            case RIGHT_CUSTOM:
                if (onBindBarRightCustom() != null) {
                    ViewGroup group = titleBar.getRightCustomView().findViewById(R.id.fl_custome);
                    group.setVisibility(View.VISIBLE);
                    group.addView(onBindBarRightCustom(), new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                }
                break;
            default:
                break;
        }

    }


    /**
     * ?????????????????????,??????true
     *
     * @return
     */
    protected boolean enableSwipeBack() {
        return true;
    }

    /**
     * ???????????????????????????,??????true
     *
     * @return
     */
    protected boolean enableCommonBar() {
        return false;
    }

    /**
     * ??????????????????????????????
     *
     * @return
     */
    protected CommonBarStyle onBindBarRightStyle() {
        return CommonBarStyle.RIGHT_ICON;
    }

    /**
     * ??????????????????????????????
     *
     * @return
     */
    protected CommonBarStyle onBindBarLeftStyle() {
        return CommonBarStyle.LEFT_BACK;
    }

    /**
     * ??????????????????????????????
     *
     * @return
     */
    protected CommonBarStyle onBindBarCenterStyle() {
        return CommonBarStyle.CENTER_TITLE;
    }

    /**
     * ??????????????????????????????
     *
     * @return
     */
    protected String[] onBindBarRightText() {
        return null;
    }

    /**
     * ?????????????????????
     *
     * @return
     */
    protected String[] onBindBarTitleText() {
        return null;
    }

    /**
     * ??????????????????????????????
     *
     * @return
     */
    protected @DrawableRes
    Integer[] onBindBarRightIcon() {
        return null;
    }

    /**
     * ????????????????????????????????????
     *
     * @return
     */
    protected @DrawableRes
    Integer onBindBarLeftIcon() {
        return null;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @return
     */
    protected @DrawableRes
    Integer onBindBarBackIcon() {
        return null;
    }

    /**
     * ???????????????????????????????????????
     *
     * @return
     */
    protected View onBindBarRightCustom() {
        return null;
    }

    /**
     * ???????????????????????????????????????
     *
     * @return
     */
    protected View onBindBarCenterCustom() {
        return null;
    }

    /**
     * ????????????????????????????????????
     *
     * @return
     */
    protected void onRight1Click(View v) {

    }

    /**
     * ????????????????????????????????????
     *
     * @return
     */
    protected void onRight2Click(View v) {

    }

    /**
     * ?????????????????????????????????
     *
     * @return
     */
    protected void onLeftIconClick(View v) {

    }

    /**
     * ???????????????????????????
     *
     * @return
     */
    protected void setSimpleBarBg(@ColorInt int color) {
        mCommonTitleBar.setBackgroundColor(color);
    }

    /**
     * ?????????????????????,??????true
     *
     * @return
     */
    protected boolean enableLazy() {
        return true;
    }

    /**
     * ???????????????????????????
     *
     * @param strings
     */
    protected void setTitle(String[] strings) {
        if (!enableCommonBar()) {
            throw new IllegalStateException("?????????????????????,?????????enableCommonBar???true");
        } else if (onBindBarCenterStyle() != CommonBarStyle.CENTER_TITLE) {
            throw new IllegalStateException("???????????????????????????????????????,?????????onBindBarCenterStyle(CommonBarStyle.CENTER_TITLE)");
        } else {
            if (strings != null && strings.length > 0) {
                if (null != strings[0] && strings[0].trim().length() > 0) {
                    TextView title = mCommonTitleBar.getCenterCustomView().findViewById(R.id.tv_title);
                    title.setVisibility(View.VISIBLE);
                    title.setText(strings[0]);
                }
                if (strings.length > 1 && null != strings[1] && strings[1].trim().length() > 0) {
                    TextView subtitle = mCommonTitleBar.getCenterCustomView().findViewById(R.id.tv_subtitle);
                    subtitle.setVisibility(View.VISIBLE);
                    subtitle.setText(strings[1]);
                }
            }
        }
    }

    /**
     * ???????????????????????????
     *
     * @param color
     */
    protected void setBarTextColor(@ColorInt int color) {
        if (!enableCommonBar()) {
            throw new IllegalStateException("?????????????????????,?????????enableCommonBar???true");
        } else {
            TextView tvBack = mCommonTitleBar.getLeftCustomView().findViewById(R.id.tv_back);
            tvBack.setTextColor(color);
            TextView tvTitle = mCommonTitleBar.getCenterCustomView().findViewById(R.id.tv_title);
            tvTitle.setTextColor(color);
            TextView tvSubtitle = mCommonTitleBar.getCenterCustomView().findViewById(R.id.tv_subtitle);
            tvSubtitle.setTextColor(color);
            TextView tv1 = mCommonTitleBar.getRightCustomView().findViewById(R.id.tv1_right);
            tv1.setTextColor(color);
            TextView tv2 = mCommonTitleBar.getRightCustomView().findViewById(R.id.tv2_right);
            tv2.setTextColor(color);
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @param res
     */
    protected void setBarBackIconRes(@DrawableRes int res) {
        if (!enableCommonBar()) {
            throw new IllegalStateException("?????????????????????,?????????enableCommonBar???true");
        } else {
            ImageView ivBack = mCommonTitleBar.getLeftCustomView().findViewById(R.id.iv_back);
            ivBack.setImageResource(res);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FragmentEvent event) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventSticky(FragmentEvent event) {
    }


    /**
     * ????????????????????????
     */
    public void showInitView() {
        clearStatus();
        mBaseLoadService.showCallback(getInitStatus().getClass());
    }


    /**
     * ?????????????????????
     */
    public void showErrorView() {
        clearStatus();
        mBaseLoadService.showCallback(getErrorStatus().getClass());
    }

    /**
     * ??????????????????
     */
    public void showEmptyView() {
        clearStatus();
        mBaseLoadService.showCallback(getEmptyStatus().getClass());
    }

    /**
     * ??????loading?????????
     *
     * @param tip ???null?????????????????????
     */
    public void showLoadingView(String tip) {
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && ((DataBindingFragment) parentFragment).enableCommonBar()) {
            ((DataBindingFragment) parentFragment).showLoadingView(tip);
        } else {
            clearStatus();
            mBaseLoadService.setCallBack(getLoadingStatus().getClass(), (context, view1) -> {
                TextView tvTip = view1.findViewById(R.id.tv_tip);
                if (tvTip == null) {
                    throw new IllegalStateException(getLoadingStatus().getClass() + "?????????????????????????????????TextView,???id???R.id.tv_tip");
                }
                if (tip == null) {
                    tvTip.setVisibility(View.GONE);
                } else {
                    tvTip.setVisibility(View.VISIBLE);
                    tvTip.setText(tip);
                }
            });
            //??????300????????????,????????????
            postDelayed(mLoadStatusRun, 300);
        }
    }

    protected void postDelayed(Runnable runnable, long delayMillis) {
        mHandler.postDelayed(runnable, delayMillis);
    }

    private final Runnable mLoadStatusRun = new Runnable() {
        @Override
        public void run() {
            mBaseLoadService.showCallback(getLoadingStatus().getClass());
        }
    };

    /**
     * ?????????????????????
     */
    public void clearStatus() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && ((DataBindingFragment) parentFragment).enableCommonBar()) {
            ((DataBindingFragment) parentFragment).clearStatus();
        }
        mHandler.removeCallbacks(mLoadStatusRun);
        mBaseLoadService.showSuccess();
    }

    /**
     * ?????????????????????????????????
     */
    protected void onReload(View v) {
        showInitView();
        initData();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);
        mDisposables.clear();
        mBinding.unbind();
        mBinding = null;
    }

}
