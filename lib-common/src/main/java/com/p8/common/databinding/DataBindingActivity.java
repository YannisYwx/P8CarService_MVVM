package com.p8.common.databinding;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.CollectionUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.p8.common.R;
import com.p8.common.event.FragmentEvent;
import com.p8.common.base.IBaseView;
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

/**
 * @author : WX.Y
 * date : 2021/3/24 17:22
 * description :
 */
public abstract class DataBindingActivity<DB extends ViewDataBinding, VM extends BaseViewModel<?>> extends AppCompatActivity implements IBaseView, Consumer<Disposable> {
    protected static String TAG = DataBindingActivity.class.getSimpleName();
    /**
     * 公用Handler
     */
    protected Handler mHandler = new Handler(Looper.getMainLooper());
    /**
     * Disposable容器
     */
    protected CompositeDisposable mDisposables = new CompositeDisposable();
    protected ARouter mRouter = ARouter.getInstance();
    /**
     * 状态页管理
     */
    protected LoadService<?> mLoadService;
    protected DB mBinding;
    protected VM mViewModel;
    protected boolean hasInit;
    /**
     * 默认标题栏
     */
    protected CommonTitleBar mCommonTitleBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = "Ywx_" + getClass().getSimpleName();
        Log.d(TAG, "onCreate() savedInstanceState = [" + savedInstanceState + "]");
        setContentView(R.layout.common_layout_root);
        BarUtils.setStatusBarLightMode(this, isLightMode());
        ImmersionBar.with(this).init();
        initDataBefore();
        initDataBindingViewModel();
        EventBus.getDefault().register(this);
        mRouter.inject(this);
        initCommonView();
        initView();
        bindListener();
        initData();
        hasInit = true;
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
     * 注册自定义的view观察者
     */
    public abstract void initViewObservable();

    /**
     * 注册ViewModel与View的契约UI回调事件
     */
    protected void initBaseViewObservable() {
        mViewModel.getInitLoadViewEvent().observe(this, (Observer<Void>) show -> showInitView());
        mViewModel.getLoadingViewEvent().observe(this, (Observer<String>) this::showLoadingView);
        mViewModel.getEmptyViewEvent().observe(this, (Observer<Void>) show -> showEmptyView());
        mViewModel.getNetErrViewEvent().observe(this, (Observer<Void>) show -> showErrorView());
        mViewModel.getFinishSelfEvent().observe(this, (Observer<Integer>) i -> finish());
        mViewModel.getClearStatusEvent().observe(this, (Observer<Void>) v -> clearStatus());
    }

    /**
     * 初始化databinding 和 viewmodel
     */
    private void initDataBindingViewModel() {
        DataBindingConfig dataBindingConfig = getDataBindingConfig();

        ViewStub viewStubContent = findViewById(R.id.vsContent);
        viewStubContent.setLayoutResource(dataBindingConfig.getLayout());

        mBinding = DataBindingUtil.bind(viewStubContent.inflate());
        mViewModel = createViewModel();
        //TODO tip: DataBinding 严格模式：
        // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
        // 通过这样的方式，来彻底解决 视图调用的一致性问题，
        // 如此，视图调用的安全性将和基于函数式编程思想的 Jetpack Compose 持平。

        // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910

        mBinding.setLifecycleOwner(this);
        //管理databinding和viewmodel
        mBinding.setVariable(dataBindingConfig.getVmVariableId(), dataBindingConfig.getStateViewModel());
        SparseArray<?> bindingParams = dataBindingConfig.getBindingParams();
        for (int i = 0, length = bindingParams.size(); i < length; i++) {
            mBinding.setVariable(bindingParams.keyAt(i), bindingParams.valueAt(i));
        }
    }

    /**
     * 设置绑定配置
     *
     * @return DataBindingConfig
     */
    protected abstract DataBindingConfig getDataBindingConfig();

    @Override
    public void accept(Disposable disposable) throws Exception {
        mDisposables.add(disposable);
    }

    /**
     * 初始化基本布局
     */
    @Override
    public void initCommonView() {
        if (enableCommonBar()) {
            ViewStub viewStubBar = findViewById(R.id.vsBar);
            viewStubBar.setLayoutResource(R.layout.common_layout_titlebar);
            mCommonTitleBar = viewStubBar.inflate().findViewById(R.id.ctb);
            initCommonTitleBar(mCommonTitleBar);
        }
        LoadSir.Builder builder = new LoadSir.Builder()
                .addCallback(getEmptyStatus())
                .addCallback(getErrorStatus())
                .addCallback(getLoadingStatus())
                .setDefaultCallback(getLoadingStatus().getClass());
        if (!CollectionUtils.isEmpty(getExtraStatus())) {
            for (Callback callback : getExtraStatus()) {
                builder.addCallback(callback);
            }
        }
        ViewGroup.MarginLayoutParams layoutParams = null;
        if (enableCommonBar()) {
            layoutParams = new FrameLayout.LayoutParams((ViewGroup.MarginLayoutParams) mBinding.getRoot().getLayoutParams());
            boolean b = StatusBarUtils.supportTransparentStatusBar();
            int barHeight = b ? BarUtils.getStatusBarHeight() : 0;
            layoutParams.topMargin = getResources().getDimensionPixelOffset(R.dimen.simpleBarHeight) + barHeight;
        }
        mLoadService = builder.build().register(mBinding.getRoot(), layoutParams, (Callback.OnReloadListener) DataBindingActivity.this::onReload);
        mLoadService.showSuccess();
    }

    /**
     * 初始化通用标题栏
     */
    protected void initCommonTitleBar(CommonTitleBar mCommonTitleBar) {
        // 中间
        if (onBindBarCenterStyle() == CommonBarStyle.CENTER_TITLE) {
            String[] strings = onBindBarTitleText();
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
        } else if (onBindBarCenterStyle() == CommonBarStyle.CENTER_CUSTOM && onBindBarCenterCustome() != null) {
            ViewGroup group = mCommonTitleBar.getCenterCustomView().findViewById(R.id.fl_custome);
            group.setVisibility(View.VISIBLE);
            group.addView(onBindBarCenterCustome(), new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        //左边
        if (onBindBarLeftStyle() == CommonBarStyle.LEFT_BACK) {

            ImageView backView = mCommonTitleBar.getLeftCustomView().findViewById(R.id.iv_back);
            if (onBindBarBackIcon() != null) {
                backView.setImageResource(onBindBarBackIcon());
            }
            backView.setVisibility(View.VISIBLE);
            backView.setOnClickListener(v -> onSimpleBackClick());
        } else if (onBindBarLeftStyle() == CommonBarStyle.LEFT_BACK_TEXT) {
            View backIcon = mCommonTitleBar.getLeftCustomView().findViewById(R.id.iv_back);
            backIcon.setVisibility(View.VISIBLE);
            backIcon.setOnClickListener(v -> onSimpleBackClick());
            View backTv = mCommonTitleBar.getLeftCustomView().findViewById(R.id.tv_back);
            backTv.setVisibility(View.VISIBLE);
            backTv.setOnClickListener(v -> onSimpleBackClick());
        } else if (onBindBarLeftStyle() == CommonBarStyle.LEFT_ICON && onBindBarLeftIcon() != null) {
            ImageView icon = mCommonTitleBar.getLeftCustomView().findViewById(R.id.iv_left);
            icon.setVisibility(View.VISIBLE);
            icon.setImageResource(onBindBarLeftIcon());
            icon.setOnClickListener(this::onLeftIconClick);
        }
        //右边
        switch (onBindBarRightStyle()) {
            case RIGHT_TEXT:
                String[] strings = onBindBarRightText();
                if (strings == null || strings.length == 0) {
                    break;
                }
                if (null != strings[0] && strings[0].trim().length() > 0) {
                    TextView tv1 = mCommonTitleBar.getRightCustomView().findViewById(R.id.tv1_right);
                    tv1.setVisibility(View.VISIBLE);
                    tv1.setText(strings[0]);
                    tv1.setOnClickListener(this::onRight1Click);
                }
                if (strings.length > 1 && null != strings[1] && strings[1].trim().length() > 0) {
                    TextView tv2 = mCommonTitleBar.getRightCustomView().findViewById(R.id.tv2_right);
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
                    ImageView iv1 = mCommonTitleBar.getRightCustomView().findViewById(R.id.iv1_right);
                    iv1.setVisibility(View.VISIBLE);
                    iv1.setImageResource(ints[0]);
                    iv1.setOnClickListener(this::onRight1Click);
                }
                if (ints.length > 1 && null != ints[1]) {
                    ImageView iv2 = mCommonTitleBar.getRightCustomView().findViewById(R.id.iv2_right);
                    iv2.setVisibility(View.VISIBLE);
                    iv2.setImageResource(ints[1]);
                    iv2.setOnClickListener(this::onRight2Click);
                }
                break;
            case RIGHT_CUSTOM:
                if (onBindBarRightCustome() != null) {
                    ViewGroup group = mCommonTitleBar.getRightCustomView().findViewById(R.id.fl_custome);
                    group.setVisibility(View.VISIBLE);
                    group.addView(onBindBarRightCustome(), new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                }
                break;
            default:
                break;
        }
    }


    /**
     * 设置标题栏标题文字
     *
     * @param strings
     */
    protected void setTitle(String[] strings) {
        if (!enableCommonBar()) {
            throw new IllegalStateException("导航栏中不可用,请设置enableSimplebar为true");
        } else if (onBindBarCenterStyle() != CommonBarStyle.CENTER_TITLE) {
            throw new IllegalStateException("导航栏中间布局不为标题类型,请设置onBindBarCenterStyle(CommonBarStyle.CENTER_TITLE)");
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
     * 设置标题栏文字颜色
     *
     * @param color
     */
    protected void setBarTextColor(@ColorInt int color) {
        if (!enableCommonBar()) {
            throw new IllegalStateException("导航栏中不可用,请设置enableSimplebar为true");
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
     * 设置标题栏返回按钮图片
     *
     * @param res
     */
    protected void setBarBackIconRes(@DrawableRes int res) {
        if (!enableCommonBar()) {
            throw new IllegalStateException("导航栏中不可用,请设置enableSimplebar为true");
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

    private final Runnable mLoadStatusRun = new Runnable() {
        @Override
        public void run() {
            mLoadService.showCallback(getLoadingStatus().getClass());
        }
    };

    /**
     * 清除所有状态页
     */
    public void clearStatus() {
        mHandler.removeCallbacks(mLoadStatusRun);
        mLoadService.showSuccess();
    }

    /**
     * 点击状态页默认执行事件
     */
    protected void onReload(View v) {
        showInitView();
        initData();
    }

    protected void post(Runnable runnable) {
        mHandler.post(runnable);
    }

    protected void postDelayed(Runnable runnable, long delayMillis) {
        mHandler.postDelayed(runnable, delayMillis);
    }

    /**
     * 显示loading状态页
     *
     * @param tip 为null时不带提示文本
     */
    public void showLoadingView(String tip) {
        mHandler.removeCallbacks(mLoadStatusRun);
        mLoadService.showSuccess();
        mLoadService.setCallBack(getLoadingStatus().getClass(), (context, view) -> {
            TextView tvTip = view.findViewById(R.id.tv_tip);
            if (tvTip == null) {
                throw new IllegalStateException(getLoadingStatus().getClass() + "必须带有显示提示文本的TextView,且id为R.id.tv_tip");
            }
            if (tip == null) {
                tvTip.setVisibility(View.GONE);
            } else {
                tvTip.setVisibility(View.VISIBLE);
                tvTip.setText(tip);
            }
        });
        //延时300毫秒显示,避免闪屏
        postDelayed(mLoadStatusRun, 300);
    }


    /**
     * 显示初始化状态页
     */
    public void showInitView() {
        clearStatus();
        mLoadService.showCallback(getInitStatus().getClass());
    }


    /**
     * 显示出错状态页
     */
    public void showErrorView() {
        clearStatus();
        mLoadService.showCallback(getErrorStatus().getClass());
    }

    /**
     * 显示空状态页
     */
    public void showEmptyView() {
        clearStatus();
        mLoadService.showCallback(getEmptyStatus().getClass());
    }

    /**
     * 是否开启通用标题栏,默认true
     *
     * @return
     */
    protected boolean enableCommonBar() {
        return true;
    }

    /**
     * 是否可滑动返回,默认true
     *
     * @return
     */
    protected boolean enableSwipeBack() {
        return true;
    }

    /**
     * 初始化右边标题栏类型
     *
     * @return
     */
    protected CommonBarStyle onBindBarRightStyle() {
        return CommonBarStyle.RIGHT_ICON;
    }

    /**
     * 初始化左边标题栏类型
     *
     * @return
     */
    protected CommonBarStyle onBindBarLeftStyle() {
        return CommonBarStyle.LEFT_BACK;
    }

    /**
     * 初始化中间标题栏类型
     *
     * @return
     */
    protected CommonBarStyle onBindBarCenterStyle() {
        return CommonBarStyle.CENTER_TITLE;
    }

    /**
     * 初始化标题栏右边文本
     *
     * @return
     */
    protected String[] onBindBarRightText() {
        return null;
    }

    /**
     * 初始化标题文本
     *
     * @return
     */
    protected String[] onBindBarTitleText() {
        return null;
    }

    /**
     * 初始化标题栏右边图标
     *
     * @return
     */
    protected @DrawableRes
    Integer[] onBindBarRightIcon() {
        return null;
    }

    /**
     * 初始化标题栏左边附加图标
     *
     * @return
     */
    protected @DrawableRes
    Integer onBindBarLeftIcon() {
        return null;
    }

    /**
     * 初始化标题栏左边返回按钮图标
     *
     * @return
     */
    protected @DrawableRes
    Integer onBindBarBackIcon() {
        return null;
    }

    /**
     * 初始化标题栏右侧自定义布局
     *
     * @return
     */
    protected View onBindBarRightCustome() {
        return null;
    }

    /**
     * 初始化标题栏中间自定义布局
     *
     * @return
     */
    protected View onBindBarCenterCustome() {
        return null;
    }

    /**
     * 点击标题栏靠右第一个事件
     *
     * @return
     */
    protected void onRight1Click(View v) {

    }

    /**
     * 点击标题栏靠右第二个事件
     *
     * @return
     */
    protected void onRight2Click(View v) {

    }

    /**
     * 点击标题栏靠左附加事件
     *
     * @return
     */
    protected void onLeftIconClick(View v) {

    }

    /**
     * 点击标题栏返回按钮事件
     */
    public void onSimpleBackClick() {
        finish();
    }

    /**
     * 设置标题栏背景颜色
     *
     * @return
     */
    protected void setSimpleBarBg(@ColorInt int color) {
        mCommonTitleBar.setBackgroundColor(color);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposables.clear();
        mBinding.unbind();
        mBinding = null;
        mHandler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);
    }
}

