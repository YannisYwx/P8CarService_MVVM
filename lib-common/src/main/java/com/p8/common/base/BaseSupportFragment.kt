package com.p8.common.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.BarUtils
import com.kingja.loadsir.callback.Callback.OnReloadListener
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.p8.common.App
import com.p8.common.R
import com.p8.common.base.IBaseView.CommonBarStyle
import com.p8.common.event.FragmentEvent
import com.p8.common.mvvm.view.BaseFragment
import com.wuhenzhizao.titlebar.statusbar.StatusBarUtils
import com.wuhenzhizao.titlebar.widget.CommonTitleBar
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import me.yokeyword.fragmentation.SupportFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *  @author : WX.Y
 *  date : 2021/3/17 14:59
 *  description :
 */
abstract class BaseSupportFragment : SupportFragment(), IBaseView,
    Consumer<Disposable> {
    protected var TAG = BaseFragment::class.java.simpleName
    protected var mApplication: Application? = null

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
     * 根部局
     */
    protected var mRootView: View? = null

    /**
     * 状态页管理
     */
    protected var mBaseLoadService: LoadService<*>? = null

    /**
     * 默认标题栏
     */
    protected lateinit var mCommonTitleBar: CommonTitleBar

    /**
     * 记录是否第一次进入
     */
    private var isFirst = true

    protected var hasInit = false

    protected var mActivity: Activity? = null

    /**
     * 是否为对象复用
     */
    protected var isReuse = false

    /**
     * 是否是再次可见
     */
    protected open fun onReVisible() {}

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        TAG = "Ywx_" + javaClass.simpleName
        Log.d(TAG, "onAttach: ")
        mActivity = activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate() savedInstanceState = [$savedInstanceState]")
        mApplication = App.getInstance()
        mRouter.inject(this)
        EventBus.getDefault().register(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: ")
        isReuse = mRootView != null
        if (isReuse) {
            val parent = mRootView!!.parent as ViewGroup
            parent.removeView(mRootView)
        } else {
            mRootView = inflater.inflate(R.layout.common_layout_root, container, false)
        }
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: ")
        super.onViewCreated(view, savedInstanceState)
        if (!isReuse) {
            initCommonView()
            //不采用懒加载
            if (!enableLazy()) {
                initDataBefore()
                loadView()
                initView()
                bindListener()
                initData()
                hasInit = true
            }
        }
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        BarUtils.setStatusBarLightMode(_mActivity, isLightBarMode())
    }

    /**
     * 状态栏字体是否是白色
     * @return true 白色  false 黑色
     */
    open fun isLightBarMode(): Boolean {
        return false
    }

    override fun onStart() {
        Log.d(TAG, "onStart: ")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "onResume: ")
        super.onResume()
        if (!isReuse && enableLazy() && isFirst) {
            initDataBefore()
            loadView()
            initView()
            initData()
            bindListener()
            hasInit = true
        }
        if (!isFirst) {
            onReVisible()
        }
        isFirst = false
    }

    override fun initDataBefore() {

    }

    override fun onPause() {
        Log.d(TAG, "onPause: ")
        super.onPause()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        Log.d(TAG, "onHiddenChanged() hidden = [$hidden]")
        super.onHiddenChanged(hidden)
    }

    override fun onStop() {
        Log.d(TAG, "onStop: ")
        super.onStop()
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView: ")
        super.onDestroyView()
    }

    @Throws(Exception::class)
    override fun accept(disposable: Disposable?) {
        println("=======> accept -->$disposable")
        disposable?.let { mDisposables.add(it) }
    }

    /**
     * 初始化基本布局
     */
    override fun initCommonView() {
        if (enableCommonBar()) {
            val viewStubBar = mRootView!!.findViewById<ViewStub>(R.id.vsBar)
            viewStubBar.layoutResource = R.layout.common_layout_titlebar
            mCommonTitleBar = viewStubBar.inflate().findViewById(R.id.ctb)
            initCommonTitleBar(mCommonTitleBar)
        }
    }

    /**
     * 填充布局(布局懒加载)
     */
    protected open fun loadView() {
        val mViewStubContent = mRootView!!.findViewById<ViewStub>(R.id.vsContent)
        mViewStubContent.layoutResource = onBindLayout()
        val contentView = mViewStubContent.inflate()
        val builder = LoadSir.Builder()
            .addCallback(getInitStatus())
            .addCallback(getEmptyStatus())
            .addCallback(getErrorStatus())
            .addCallback(getLoadingStatus())
            .setDefaultCallback(SuccessCallback::class.java)
        getExtraStatus()?.forEach {
            it?.let {
                builder.addCallback(it)
            }
        }
        var layoutParams: FrameLayout.LayoutParams? = null
        if (enableCommonBar()) {
            layoutParams =
                FrameLayout.LayoutParams((contentView.layoutParams as FrameLayout.LayoutParams))
            val b = StatusBarUtils.supportTransparentStatusBar()
            val barHeight = if (b) BarUtils.getStatusBarHeight() else 0
            layoutParams.topMargin =
                resources.getDimensionPixelOffset(R.dimen.simpleBarHeight) + barHeight
        }
        mBaseLoadService = builder.build().register(contentView, layoutParams,
            OnReloadListener { v: View? -> this.onReload(v) }
        )
    }

    fun Array<String?>?.notNullAndEmpty(): Boolean {
        return this != null && this.isNotEmpty()
    }

    fun Array<String?>.isNotEmptyAt(index: Int): Boolean =
        if (this.size <= index) {
            false
        } else {
            this[index] != null && this[index]!!.trim { it <= ' ' }.isNotEmpty()
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
        if (onBindBarLeftStyle() == CommonBarStyle.LEFT_BACK) {
            val backView =
                titleBar.leftCustomView.findViewById<ImageView>(R.id.iv_back)
            if (onBindBarBackIcon() != null) {
                backView.setImageResource(onBindBarBackIcon()!!)
            }
            backView.visibility = View.VISIBLE
            //            backView.setOnClickListener(v -> onSimpleBackClick());
        } else if (onBindBarLeftStyle() == CommonBarStyle.LEFT_BACK_TEXT) {
            val backIcon =
                titleBar.leftCustomView.findViewById<View>(R.id.iv_back)
            backIcon.visibility = View.VISIBLE
            //            backIcon.setOnClickListener(v -> onSimpleBackClick());
            val backTv =
                titleBar.leftCustomView.findViewById<View>(R.id.tv_back)
            backTv.visibility = View.VISIBLE
            //            backTv.setOnClickListener(v -> onSimpleBackClick());
        } else if (onBindBarLeftStyle() == CommonBarStyle.LEFT_ICON && onBindBarLeftIcon() != null) {
            val icon =
                titleBar.leftCustomView.findViewById<ImageView>(R.id.iv_left)
            icon.visibility = View.VISIBLE
            icon.setImageResource(onBindBarLeftIcon()!!)
            icon.setOnClickListener { v: View? ->
                onLeftIconClick(v)
            }
        }
        when (onBindBarRightStyle()) {
            CommonBarStyle.RIGHT_TEXT -> {
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
            CommonBarStyle.RIGHT_ICON -> {
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
            CommonBarStyle.RIGHT_CUSTOM -> if (onBindBarRightCustom() != null) {
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
     * 是否可滑动返回,默认true
     *
     * @return
     */
    protected open fun enableSwipeBack() = true

    /**
     * 是否开启通用标题栏,默认true
     *
     * @return
     */
    open fun enableCommonBar() = false

    /**
     * 初始化右边标题栏类型
     *
     * @return
     */
    protected open fun onBindBarRightStyle() = CommonBarStyle.RIGHT_ICON

    /**
     * 初始化左边标题栏类型
     *
     * @return
     */
    protected open fun onBindBarLeftStyle() = CommonBarStyle.LEFT_BACK

    /**
     * 初始化中间标题栏类型
     *
     * @return
     */
    protected open fun onBindBarCenterStyle() = CommonBarStyle.CENTER_TITLE

    /**
     * 初始化标题栏右边文本
     *
     * @return
     */
    protected open fun onBindBarRightText(): Array<String?>? = null

    /**
     * 初始化标题文本
     *
     * @return
     */
    protected open fun onBindBarTitleText(): Array<String?>? = null

    /**
     * 初始化标题栏右边图标
     *
     * @return
     */
    @DrawableRes
    protected open fun onBindBarRightIcon(): Array<Int?>? = null

    /**
     * 初始化标题栏左边附加图标
     *
     * @return
     */
    @DrawableRes
    protected open fun onBindBarLeftIcon(): Int? = null

    /**
     * 初始化标题栏左边返回按钮图标
     *
     * @return
     */
    @DrawableRes
    protected open fun onBindBarBackIcon(): Int? = null

    /**
     * 初始化标题栏右侧自定义布局
     *
     * @return
     */
    protected open fun onBindBarRightCustom(): View? = null

    /**
     * 初始化标题栏中间自定义布局
     *
     * @return
     */
    protected open fun onBindBarCenterCustom(): View? = null

    /**
     * 点击标题栏靠右第一个事件
     *
     * @return
     */
    protected open fun onRight1Click(v: View?) {}

    /**
     * 点击标题栏靠右第二个事件
     *
     * @return
     */
    protected open fun onRight2Click(v: View?) {}

    /**
     * 点击标题栏靠左附加事件
     *
     * @return
     */
    protected open fun onLeftIconClick(v: View?) {}

    /**
     * 设置标题栏背景颜色
     *
     * @return
     */
    protected open fun setSimpleBarBg(@ColorInt color: Int) {
        mCommonTitleBar.setBackgroundColor(color)
    }

    /**
     * 是否开启懒加载,默认true
     *
     * @return
     */
    protected open fun enableLazy() = true

    /**
     * 设置标题栏标题文字
     *
     * @param strings
     */
    protected open fun setTitle(strings: Array<String?>?) {
        check(enableCommonBar()) { "导航栏中不可用,请设置enableCommonBar为true" }
        check(onBindBarCenterStyle() == CommonBarStyle.CENTER_TITLE) { "导航栏中间布局不为标题类型,请设置onBindBarCenterStyle(CommonBarStyle.CENTER_TITLE)" }
        strings?.let {
            if (it.isNotEmpty()) {
                if (it.isNotEmptyAt(0)) {
                    val title =
                        mCommonTitleBar.centerCustomView.findViewById<TextView>(R.id.tv_title)
                    title.visibility = View.VISIBLE
                    title.text = it[0]
                }

                if (it.isNotEmptyAt(1)) {
                    val subtitle = mCommonTitleBar!!.centerCustomView
                        .findViewById<TextView>(R.id.tv_subtitle)
                    subtitle.visibility = View.VISIBLE
                    subtitle.text = it[1]
                }
            }
        }
    }

    /**
     * 设置标题栏文字颜色
     *
     * @param color
     */
    protected open fun setBarTextColor(@ColorInt color: Int) {
        check(enableCommonBar()) { "导航栏中不可用,请设置enableCommonBar为true" }
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
    protected open fun setBarBackIconRes(@DrawableRes res: Int) {
        check(enableCommonBar()) { "导航栏中不可用,请设置enableCommonBar为true" }
        val ivBack = mCommonTitleBar.leftCustomView
            .findViewById<ImageView>(R.id.iv_back)
        ivBack.setImageResource(res)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onEvent(event: FragmentEvent?) {
    }

    @Subscribe(
        threadMode = ThreadMode.MAIN,
        sticky = true
    )
    open fun onEventSticky(event: FragmentEvent?) {
    }


    /**
     * 显示初始化状态页
     */
    open fun showInitView() {
        clearStatus()
        mBaseLoadService!!.showCallback(getInitStatus().javaClass)
    }


    /**
     * 显示出错状态页
     */
    open fun showErrorView() {
        clearStatus()
        mBaseLoadService!!.showCallback(getErrorStatus().javaClass)
    }

    /**
     * 显示空状态页
     */
    open fun showEmptyView() {
        clearStatus()
        mBaseLoadService!!.showCallback(getEmptyStatus().javaClass)
    }

    /**
     * 显示loading状态页
     *
     * @param tip 为null时不带提示文本
     */
    open fun showLoadingView(tip: String?) {
        val parentFragment = parentFragment
        if (parentFragment != null && (parentFragment as BaseSupportFragment).enableCommonBar()) {
            parentFragment.showLoadingView(tip)
        } else {
            clearStatus()
            mBaseLoadService!!.setCallBack(
                getLoadingStatus().javaClass
            ) { _: Context?, view1: View ->
                val tvTip = view1.findViewById<TextView>(R.id.tv_tip)
                    ?: throw IllegalStateException(getLoadingStatus().javaClass.toString() + "必须带有显示提示文本的TextView,且id为R.id.tv_tip")
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
    }

    override fun post(runnable: Runnable) {
        mHandler.post(runnable)
    }

    protected open fun postDelayed(
        runnable: Runnable,
        delayMillis: Long = 300
    ) {
        mHandler.postDelayed(runnable, delayMillis)
    }

    private val mLoadStatusRun =
        Runnable { mBaseLoadService!!.showCallback(getLoadingStatus().javaClass) }

    /**
     * 清除所有状态页
     */
    open fun clearStatus() {
        val parentFragment = parentFragment
        if (parentFragment != null && (parentFragment as BaseSupportFragment).enableCommonBar()) {
            parentFragment.clearStatus()
        }
        mHandler.removeCallbacks(mLoadStatusRun)
        mBaseLoadService!!.showSuccess()
    }

    /**
     * 点击状态页默认执行事件
     */
    protected open fun onReload(v: View?) {
        showInitView()
        initData()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
        EventBus.getDefault().unregister(this)
        mDisposables.clear()
//        ThirdHelper.refWatcher.watch(this);
    }
}