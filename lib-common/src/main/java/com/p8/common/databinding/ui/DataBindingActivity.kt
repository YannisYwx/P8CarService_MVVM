package com.p8.common.databinding.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewStub
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.CollectionUtils
import com.kingja.loadsir.callback.Callback.OnReloadListener
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.p8.common.R
import com.p8.common.databinding.DataBindingConfig
import com.p8.common.event.FragmentEvent
import com.p8.common.mvvm.view.status.EmptyStatus
import com.p8.common.mvvm.view.status.ErrorStatus
import com.p8.common.mvvm.view.status.InitStatus
import com.p8.common.mvvm.view.status.LoadingStatus
import com.p8.common.mvvm.viewmodel.BaseViewModel
import com.wuhenzhizao.titlebar.statusbar.StatusBarUtils
import com.wuhenzhizao.titlebar.widget.CommonTitleBar
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.reflect.ParameterizedType

/**
 * @author : WX.Y
 * date : 2021/3/24 17:22
 * description :
 */
abstract class DataBindingActivity<DB : ViewDataBinding?, VM : BaseViewModel<*>?> :
    AppCompatActivity(), IBaseView,
    Consumer<Disposable?> {
    /**
     * 公用Handler
     */
    protected var mHandler = Handler(Looper.getMainLooper())

    /**
     * Disposable容器
     */
    protected var mDisposables = CompositeDisposable()
    protected var mRouter = ARouter.getInstance()

    /**
     * 状态页管理
     */
    protected lateinit var mLoadService: LoadService<*>
    protected var mBinding: DB? = null
    protected var mViewModel: VM? = null
    protected var hasInit = false

    /**
     * 默认标题栏
     */
    protected lateinit var mCommonTitleBar: CommonTitleBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TAG = "Ywx_${javaClass.simpleName}"
        Log.d(TAG, "onCreate() savedInstanceState = [$savedInstanceState]")
        setContentView(R.layout.common_layout_root)
        initDataBefore()
        initDataBindingViewModel()
        EventBus.getDefault().register(this)
        mRouter.inject(this)
        initCommonView()
        initView()
        bindListener()
        initData()
        hasInit = true
    }

    override fun initData() {
        initBaseViewObservable()
        initViewObservable()
    }

    private fun createViewModel(): VM {
        val tClass =
            (javaClass.genericSuperclass as ParameterizedType?)!!.actualTypeArguments[1] as Class<VM>
        return ViewModelProviders.of(this, onBindViewModelFactory())[tClass]
    }

    abstract fun onBindViewModelFactory(): ViewModelProvider.Factory?

    /**
     * 注册自定义的view观察者
     */
    abstract fun initViewObservable()

    /**
     * 注册ViewModel与View的契约UI回调事件
     */
    private fun initBaseViewObservable() {
        mViewModel?.getInitLoadViewEvent()?.observe(this,
            Observer { showInitView() }
        )
        mViewModel?.getLoadingViewEvent()?.observe(this,
            Observer { tip: String? ->
                showLoadingView(
                    tip
                )
            }
        )
        mViewModel?.getEmptyViewEvent()?.observe(this,
            Observer { showEmptyView() }
        )
        mViewModel?.getNetErrViewEvent()?.observe(this,
            Observer { showErrorView() }
        )
        mViewModel?.getFinishSelfEvent()?.observe(this,
            Observer { finish() }
        )
        mViewModel?.getClearStatusEvent()?.observe(this,
            Observer { clearStatus() }
        )
    }

    /**
     * 初始化databinding 和 viewmodel,绑定xml
     */
    private fun initDataBindingViewModel() {
        mViewModel = createViewModel()
        val dataBindingConfig = dataBindingConfig
        val viewStubContent = findViewById<ViewStub>(R.id.vsContent)
        viewStubContent.layoutResource = dataBindingConfig.layout
        mBinding = DataBindingUtil.bind<DB>(viewStubContent.inflate())
        //TODO tip: DataBinding 严格模式：
        // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
        // 通过这样的方式，来彻底解决 视图调用的一致性问题，
        // 如此，视图调用的安全性将和基于函数式编程思想的 Jetpack Compose 持平。

        // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910
        mBinding?.lifecycleOwner = this
        //管理databinding和viewmodel
        mBinding?.setVariable(dataBindingConfig.vmVariableId, dataBindingConfig.stateViewModel)
        val bindingParams: SparseArray<*> = dataBindingConfig.getBindingParams()
        bindingParams.forEach { key, any ->
            mBinding?.setVariable(key, any)
        }
    }

    override fun onBindLayout() = 0

    /**
     * 设置绑定配置
     *
     * @return DataBindingConfig
     */
    protected abstract val dataBindingConfig: DataBindingConfig

    @Throws(Exception::class)
    override fun accept(disposable: Disposable?) {
        mDisposables.add(disposable!!)
    }

    fun Array<String?>.isNotEmptyAt(index: Int): Boolean =
        if (this.size <= index) {
            false
        } else {
            this[index] != null && this[index]!!.trim { it <= ' ' }.isNotEmpty()
        }

    /**
     * 初始化基本布局
     */
    override fun initCommonView() {
        if (enableCommonBar()) {
            val viewStubBar = findViewById<ViewStub>(R.id.vsBar)
            viewStubBar.layoutResource = R.layout.common_layout_titlebar
            mCommonTitleBar = viewStubBar.inflate().findViewById(R.id.ctb)
            initCommonTitleBar(mCommonTitleBar)
        }
        val builder = LoadSir.Builder()
            .addCallback(emptyStatus)
            .addCallback(errorStatus)
            .addCallback(loadingStatus)
            .setDefaultCallback(LoadingStatus::class.java)
        if (!CollectionUtils.isEmpty(extraStatus)) {
            extraStatus?.let {
                for (callback in it) {
                    builder.addCallback(callback!!)
                }
            }
        }
        var layoutParams: MarginLayoutParams? = null
        if (enableCommonBar()) {
            layoutParams = FrameLayout.LayoutParams(
                (mBinding!!.root.layoutParams as MarginLayoutParams)
            )
            val b = StatusBarUtils.supportTransparentStatusBar()
            val barHeight = if (b) BarUtils.getStatusBarHeight() else 0
            layoutParams.topMargin =
                resources.getDimensionPixelOffset(R.dimen.simpleBarHeight) + barHeight
        }
        mLoadService = builder.build().register(
            mBinding?.root, layoutParams,
            OnReloadListener { v: View? -> onReload(v) }
        )
        mLoadService.showSuccess()
    }

    /**
     * 初始化通用标题栏
     */
    protected open fun initCommonTitleBar(titleBar: CommonTitleBar) {
        // 中间
        if (onBindBarCenterStyle() == IBaseView.CommonBarStyle.CENTER_TITLE) {
            onBindBarTitleText()?.let {
                if (it.isNotEmpty()) {
                    if (it.isNotEmptyAt(0)) {
                        val title =
                            titleBar.centerCustomView.findViewById<TextView>(R.id.tv_title)
                        title.visibility = View.VISIBLE
                        title.text = it[0]
                    }
                    if (it.isNotEmptyAt(1)) {
                        val subtitle =
                            titleBar.centerCustomView.findViewById<TextView>(R.id.tv_subtitle)
                        subtitle.visibility = View.VISIBLE
                        subtitle.text = it[1]
                    }
                }
            }
        } else if (onBindBarCenterStyle() == IBaseView.CommonBarStyle.CENTER_CUSTOM && onBindBarCenterCustom() != null) {
            val group =
                titleBar.centerCustomView.findViewById<ViewGroup>(R.id.fl_custome)
            group.visibility = View.VISIBLE
            group.addView(
                onBindBarCenterCustom(), FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }
        //左边
        if (onBindBarLeftStyle() == IBaseView.CommonBarStyle.LEFT_BACK) {
            val backView =
                titleBar.leftCustomView.findViewById<ImageView>(R.id.iv_back)
            if (onBindBarBackIcon() != null) {
                backView.setImageResource(onBindBarBackIcon()!!)
            }
            backView.visibility = View.VISIBLE
            //            backView.setOnClickListener(v -> onSimpleBackClick());
        } else if (onBindBarLeftStyle() == IBaseView.CommonBarStyle.LEFT_BACK_TEXT) {
            val backIcon =
                titleBar.leftCustomView.findViewById<View>(R.id.iv_back)
            backIcon.visibility = View.VISIBLE
            //            backIcon.setOnClickListener(v -> onSimpleBackClick());
            val backTv =
                titleBar.leftCustomView.findViewById<View>(R.id.tv_back)
            backTv.visibility = View.VISIBLE
            //            backTv.setOnClickListener(v -> onSimpleBackClick());
        } else if (onBindBarLeftStyle() == IBaseView.CommonBarStyle.LEFT_ICON && onBindBarLeftIcon() != null) {
            val icon =
                titleBar.leftCustomView.findViewById<ImageView>(R.id.iv_left)
            icon.visibility = View.VISIBLE
            icon.setImageResource(onBindBarLeftIcon()!!)
            icon.setOnClickListener { v: View? ->
                onLeftIconClick(v)
            }
        }
        when (onBindBarRightStyle()) {
            IBaseView.CommonBarStyle.RIGHT_TEXT -> {
                val strings = onBindBarRightText()
                onBindBarRightText()?.let {
                    if (it.isNotEmpty()) {
                        if (it.isNotEmptyAt(0)) {
                            val tv1 =
                                titleBar.rightCustomView.findViewById<TextView>(R.id.tv1_right)
                            tv1.visibility = View.VISIBLE
                            tv1.text = it[0]
                            tv1.setOnClickListener { v: View? ->
                                onRight1Click(v)
                            }
                        }
                        if (it.isNotEmptyAt(1)) {
                            val tv2 =
                                titleBar.rightCustomView.findViewById<TextView>(R.id.tv2_right)
                            tv2.visibility = View.VISIBLE
                            tv2.text = it[1]
                            tv2.setOnClickListener { v: View? ->
                                onRight2Click(v)
                            }
                        }
                    }
                }
            }
            IBaseView.CommonBarStyle.RIGHT_ICON -> {
                val ints = onBindBarRightIcon()
                if (ints == null || ints.isEmpty()) {
                    return
                }
                if (null != ints[0]) {
                    val iv1 = titleBar.rightCustomView
                        .findViewById<ImageView>(R.id.iv1_right)
                    iv1.visibility = View.VISIBLE
                    iv1.setImageResource(ints[0]!!)
                    iv1.setOnClickListener { v: View? ->
                        onRight1Click(v)
                    }
                }
                if (ints.size > 1 && null != ints[1]) {
                    val iv2 = titleBar.rightCustomView
                        .findViewById<ImageView>(R.id.iv2_right)
                    iv2.visibility = View.VISIBLE
                    iv2.setImageResource(ints[1]!!)
                    iv2.setOnClickListener { v: View? ->
                        onRight2Click(v)
                    }
                }
            }
            IBaseView.CommonBarStyle.RIGHT_CUSTOM -> if (onBindBarRightCustom() != null) {
                val group =
                    titleBar.rightCustomView.findViewById<ViewGroup>(R.id.fl_custome)
                group.visibility = View.VISIBLE
                group.addView(
                    onBindBarRightCustom(), FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
            }
            else -> {
            }
        }
    }


    /**
     * 设置标题栏标题文字
     *
     * @param strings
     */
    protected open fun setTitle(strings: Array<String?>) {
        check(enableCommonBar()) { "导航栏中不可用,请设置enableCommonBar为true" }
        check(onBindBarCenterStyle() == IBaseView.CommonBarStyle.CENTER_TITLE) { "导航栏中间布局不为标题类型,请设置onBindBarCenterStyle(CommonBarStyle.CENTER_TITLE)" }
        if (strings.isNotEmpty()) {
            if (strings.isNotEmpty() && null != strings[0] && strings[0]!!
                    .trim { it <= ' ' }.isNotEmpty()
            ) {
                val title =
                    mCommonTitleBar.centerCustomView.findViewById<TextView>(R.id.tv_title)
                title.visibility = View.VISIBLE
                title.text = strings[0]
            }
            if (strings.size > 1 && null != strings[1] && strings[1]!!
                    .trim { it <= ' ' }.isNotEmpty()
            ) {
                val subtitle = mCommonTitleBar.centerCustomView
                    .findViewById<TextView>(R.id.tv_subtitle)
                subtitle.visibility = View.VISIBLE
                subtitle.text = strings[1]
            }
        }
    }

    /**
     * 设置标题栏文字颜色
     *
     * @param color
     */
    protected fun setBarTextColor(@ColorInt color: Int) {
        check(enableCommonBar()) { "导航栏中不可用,请设置enableSimplebar为true" }
        val tvBack =
            mCommonTitleBar.leftCustomView.findViewById<TextView>(R.id.tv_back)
        tvBack.setTextColor(color)
        val tvTitle =
            mCommonTitleBar.centerCustomView.findViewById<TextView>(R.id.tv_title)
        tvTitle.setTextColor(color)
        val tvSubtitle =
            mCommonTitleBar.centerCustomView.findViewById<TextView>(R.id.tv_subtitle)
        tvSubtitle.setTextColor(color)
        val tv1 =
            mCommonTitleBar.rightCustomView.findViewById<TextView>(R.id.tv1_right)
        tv1.setTextColor(color)
        val tv2 =
            mCommonTitleBar.rightCustomView.findViewById<TextView>(R.id.tv2_right)
        tv2.setTextColor(color)
    }

    /**
     * 设置标题栏返回按钮图片
     *
     * @param res
     */
    protected fun setBarBackIconRes(@DrawableRes res: Int) {
        check(enableCommonBar()) { "导航栏中不可用,请设置enableSimplebar为true" }
        val ivBack = mCommonTitleBar.leftCustomView
            .findViewById<ImageView>(R.id.iv_back)
        ivBack.setImageResource(res)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: FragmentEvent?) {
    }

    @Subscribe(
        threadMode = ThreadMode.MAIN,
        sticky = true
    )
    fun onEventSticky(event: FragmentEvent?) {
    }

    private val mLoadStatusRun = Runnable { mLoadService.showCallback(LoadingStatus::class.java) }

    /**
     * 清除所有状态页
     */
    fun clearStatus() {
        mHandler.removeCallbacks(mLoadStatusRun)
        mLoadService.showSuccess()
    }

    /**
     * 点击状态页默认执行事件
     */
    protected fun onReload(v: View?) {
        showInitView()
        initData()
    }

    protected fun post(runnable: Runnable) {
        mHandler.post(runnable)
    }

    protected fun postDelayed(
        runnable: Runnable,
        delayMillis: Long
    ) {
        mHandler.postDelayed(runnable, delayMillis)
    }

    /**
     * 显示loading状态页
     *
     * @param tip 为null时不带提示文本
     */
    fun showLoadingView(tip: String?) {
        mHandler.removeCallbacks(mLoadStatusRun)
        mLoadService.showSuccess()
        mLoadService.setCallBack(
            LoadingStatus::class.java
        ) { _: Context?, view: View ->
            val tvTip = view.findViewById<TextView>(R.id.tv_tip)
                ?: throw IllegalStateException(LoadingStatus::class.java.toString() + "必须带有显示提示文本的TextView,且id为R.id.tv_tip")
            if (tip == null) {
                tvTip.visibility = View.GONE
            } else {
                tvTip.visibility = View.VISIBLE
                tvTip.text = tip
            }
        }
        //延时300毫秒显示,避免闪屏
        postDelayed(mLoadStatusRun, 300)
    }

    /**
     * 显示初始化状态页
     */
    fun showInitView() {
        clearStatus()
        mLoadService.showCallback(InitStatus::class.java)
    }

    /**
     * 显示出错状态页
     */
    fun showErrorView() {
        clearStatus()
        mLoadService.showCallback(ErrorStatus::class.java)
    }

    /**
     * 显示空状态页
     */
    fun showEmptyView() {
        clearStatus()
        mLoadService.showCallback(EmptyStatus::class.java)
    }

    /**
     * 是否开启通用标题栏,默认true
     *
     * @return
     */
    protected fun enableCommonBar(): Boolean {
        return true
    }

    /**
     * 是否可滑动返回,默认true
     *
     * @return
     */
    protected fun enableSwipeBack(): Boolean {
        return true
    }

    /**
     * 初始化右边标题栏类型
     *
     * @return
     */
    protected fun onBindBarRightStyle(): IBaseView.CommonBarStyle {
        return IBaseView.CommonBarStyle.RIGHT_ICON
    }

    /**
     * 初始化左边标题栏类型
     *
     * @return
     */
    protected fun onBindBarLeftStyle(): IBaseView.CommonBarStyle {
        return IBaseView.CommonBarStyle.LEFT_BACK
    }

    /**
     * 初始化中间标题栏类型
     *
     * @return
     */
    protected fun onBindBarCenterStyle(): IBaseView.CommonBarStyle {
        return IBaseView.CommonBarStyle.CENTER_TITLE
    }

    /**
     * 初始化标题栏右边文本
     *
     * @return
     */
    protected fun onBindBarRightText(): Array<String?>? {
        return null
    }

    /**
     * 初始化标题文本
     *
     * @return
     */
    protected fun onBindBarTitleText(): Array<String?>? {
        return null
    }

    /**
     * 初始化标题栏右边图标
     *
     * @return
     */
    @DrawableRes
    protected fun onBindBarRightIcon(): Array<Int?>? {
        return null
    }

    /**
     * 初始化标题栏左边附加图标
     *
     * @return
     */
    @DrawableRes
    protected fun onBindBarLeftIcon(): Int? {
        return null
    }

    /**
     * 初始化标题栏左边返回按钮图标
     *
     * @return
     */
    @DrawableRes
    protected fun onBindBarBackIcon(): Int? {
        return null
    }

    /**
     * 初始化标题栏右侧自定义布局
     *
     * @return
     */
    protected fun onBindBarRightCustom(): View? {
        return null
    }

    /**
     * 初始化标题栏中间自定义布局
     *
     * @return
     */
    protected fun onBindBarCenterCustom(): View? {
        return null
    }

    /**
     * 点击标题栏靠右第一个事件
     *
     * @return
     */
    protected fun onRight1Click(v: View?) {}

    /**
     * 点击标题栏靠右第二个事件
     *
     * @return
     */
    protected fun onRight2Click(v: View?) {}

    /**
     * 点击标题栏靠左附加事件
     *
     * @return
     */
    protected fun onLeftIconClick(v: View?) {}

    /**
     * 点击标题栏返回按钮事件
     */
    open fun onBackClick() {
        finish()
    }

    /**
     * 设置标题栏背景颜色
     *
     * @return
     */
    protected fun setSimpleBarBg(@ColorInt color: Int) {
        mCommonTitleBar.setBackgroundColor(color)
    }

    public override fun onDestroy() {
        super.onDestroy()
        mDisposables.clear()
        mBinding!!.unbind()
        mBinding = null
        mHandler.removeCallbacksAndMessages(null)
        EventBus.getDefault().unregister(this)
    }

    companion object {
        protected var TAG =
            DataBindingActivity::class.java.simpleName
    }
}