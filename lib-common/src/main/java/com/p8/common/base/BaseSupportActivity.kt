package com.p8.common.base

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.AdaptScreenUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.CollectionUtils
import com.blankj.utilcode.util.ToastUtils
import com.gyf.immersionbar.ImmersionBar
import com.kingja.loadsir.callback.Callback.OnReloadListener
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.kingja.loadsir.core.Transport
import com.p8.common.R
import com.p8.common.base.IBaseView.Companion.SHOW_SPACE
import com.p8.common.event.FragmentEvent
import com.p8.common.mvvm.view.BaseActivity
import com.wuhenzhizao.titlebar.statusbar.StatusBarUtils
import com.wuhenzhizao.titlebar.widget.CommonTitleBar
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.common_layout_root.*
import me.yokeyword.fragmentation.SupportActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *  @author : WX.Y
 *  date : 2021/3/17 10:03
 *  description : BaseSupportActivity 单activity框架
 */
abstract class BaseSupportActivity : SupportActivity(), IBaseView, View.OnClickListener,
    Consumer<Disposable> {

    /**
     * 默认标题栏
     */
    protected lateinit var mCommonTitleBar: CommonTitleBar

    protected var TAG =
        BaseActivity::class.java.simpleName

    /**
     * 公用Handler
     */
    protected var mHandler = Handler(Looper.getMainLooper())

    /**
     * Disposable容器
     */
    protected var mDisposables = CompositeDisposable()

    /**
     * 路由
     */
    protected var mRouter = ARouter.getInstance()

    /**
     * 状态页管理
     */
    protected lateinit var mLoadService: LoadService<*>
    protected var hasInit = false


    protected lateinit var mRootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TAG = "Ywx_" + javaClass.simpleName
        setContentView(R.layout.common_layout_root)
        BarUtils.setStatusBarLightMode(this, isLightMode())
        ImmersionBar.with(this).init()
        vsContent.layoutResource = onBindLayout()
        mRootView = vsContent.inflate()
        mRouter.inject(this)
        EventBus.getDefault().register(this)
        if (!isAllowScreenRotate()) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        initDataBefore()
        initCommonView()
        initView()
        bindListener()
        initData()
        hasInit = true
    }

    override fun initDataBefore() {
    }

    override fun onClick(v: View?) {
    }

    open fun setStatusBarLightMode(isLightMode: Boolean) {
        BarUtils.setStatusBarLightMode(this, isLightMode)
    }

    /**
     * 注册点击事件
     */
    fun registerOnClickListener(@IdRes viewIds: Array<Int>) {
        viewIds.forEach { findViewById<View>(it).setOnClickListener(this) }
    }

    /**
     * 初始化基本布局
     */
    override fun initCommonView() {
        if (enableCommonBar()) {
            vsBar.layoutResource = R.layout.common_layout_titlebar
            mCommonTitleBar = vsBar.inflate().findViewById(R.id.ctb)
            initCommonTitleBar(mCommonTitleBar)
        }
        val builder = LoadSir.Builder()
            .addCallback(getEmptyStatus())
            .addCallback(getErrorStatus())
            .addCallback(getLoadingStatus())
            .setDefaultCallback(getLoadingStatus().javaClass)
        if (!CollectionUtils.isEmpty(getExtraStatus())) {
            getExtraStatus()?.let {
                for (callback in it) {
                    builder.addCallback(callback!!)
                }
            }
        }
        var layoutParams: MarginLayoutParams? = null
        if (enableCommonBar()) {
            layoutParams = FrameLayout.LayoutParams(
                (mRootView.layoutParams as MarginLayoutParams)
            )
            val b = StatusBarUtils.supportTransparentStatusBar()
            val barHeight = if (b) BarUtils.getStatusBarHeight() else 0
            layoutParams.topMargin =
                resources!!.getDimensionPixelOffset(R.dimen.simpleBarHeight) + barHeight
        }
        mLoadService = builder.build().register(mRootView, layoutParams,
            OnReloadListener { v: View? -> this@BaseSupportActivity.onReload(v) }
        )
        mLoadService.showSuccess()
    }

    @Throws(Exception::class)
    override fun accept(disposable: Disposable?) {
        disposable?.let { mDisposables.add(it) }
    }

    /**
     * 初始化通用标题栏
     */
    protected open fun initCommonTitleBar(mCommonTitleBar: CommonTitleBar) {
        // 中间
        if (onBindBarCenterStyle() == IBaseView.CommonBarStyle.CENTER_TITLE) {
            val strings: Array<String?>? = onBindBarTitleText()
            if (strings != null && strings.isNotEmpty()) {
                if (null != strings[0] && strings[0]!!.trim { it <= ' ' }.isNotEmpty()) {
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
        } else if (onBindBarCenterStyle() == IBaseView.CommonBarStyle.CENTER_CUSTOM && onBindBarCenterCustom() != null) {
            val group =
                mCommonTitleBar.centerCustomView.findViewById<ViewGroup>(R.id.fl_custome)
            group.visibility = View.VISIBLE
            group.addView(
                onBindBarCenterCustom(), FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }
        //左边
        if (onBindBarLeftStyle() == IBaseView.CommonBarStyle.LEFT_BACK) {
            val backView = mCommonTitleBar.leftCustomView
                .findViewById<ImageView>(R.id.iv_back)
            onBindBarBackIcon()?.let {
                backView.setImageResource(it)
            }
            backView.visibility = View.VISIBLE
            backView.setOnClickListener { onBackClick() }
        } else if (onBindBarLeftStyle() == IBaseView.CommonBarStyle.LEFT_BACK_TEXT) {
            val backIcon =
                mCommonTitleBar.leftCustomView.findViewById<View>(R.id.iv_back)
            backIcon.visibility = View.VISIBLE
            backIcon.setOnClickListener { onBackClick() }
            val backTv =
                mCommonTitleBar.leftCustomView.findViewById<View>(R.id.tv_back)
            backTv.visibility = View.VISIBLE
            backTv.setOnClickListener { onBackClick() }
        } else if (onBindBarLeftStyle() == IBaseView.CommonBarStyle.LEFT_ICON && onBindBarLeftIcon() != null) {
            val icon = mCommonTitleBar.leftCustomView
                .findViewById<ImageView>(R.id.iv_left)
            icon.visibility = View.VISIBLE
            icon.setImageResource(onBindBarLeftIcon()!!)
            icon.setOnClickListener { v: View? ->
                onLeftIconClick(v)
            }

        }
        when (onBindBarRightStyle()) {
            IBaseView.CommonBarStyle.RIGHT_TEXT -> {
                val strings: Array<String?>? = onBindBarRightText()
                if (strings == null || strings.isEmpty()) {
                    return
                }

                if (null != strings[0] && strings[0]!!.trim { it <= ' ' }.isNotEmpty()) {
                    val tv1 =
                        mCommonTitleBar.rightCustomView.findViewById<TextView>(R.id.tv1_right)
                    tv1.visibility = View.VISIBLE
                    tv1.text = strings[0]
                    tv1.setOnClickListener { v: View? ->
                        this.onRight1Click(v)
                    }
                }
                if (strings.size > 1 && null != strings[1] && strings[1]!!
                        .trim { it <= ' ' }.isNotEmpty()
                ) {
                    val tv2 =
                        mCommonTitleBar.rightCustomView.findViewById<TextView>(R.id.tv2_right)
                    tv2.visibility = View.VISIBLE
                    tv2.text = strings[1]
                    tv2.setOnClickListener { v: View? ->
                        this.onRight2Click(
                            v
                        )
                    }
                }
            }
            IBaseView.CommonBarStyle.RIGHT_ICON -> {
                val ints: Array<Int?>? = onBindBarRightIcon()
                if (ints == null || ints.isEmpty()) {
                    return
                }
                if (null != ints[0]) {
                    val iv1 = mCommonTitleBar.rightCustomView
                        .findViewById<ImageView>(R.id.iv1_right)
                    iv1.visibility = View.VISIBLE
                    iv1.setImageResource(ints[0]!!)
                    iv1.setOnClickListener { v: View? ->
                        this.onRight1Click(
                            v
                        )
                    }
                }
                if (ints.size > 1 && null != ints[1]) {
                    val iv2 = mCommonTitleBar.rightCustomView
                        .findViewById<ImageView>(R.id.iv2_right)
                    iv2.visibility = View.VISIBLE
                    iv2.setImageResource(ints[1]!!)
                    iv2.setOnClickListener { v: View? ->
                        this.onRight2Click(
                            v
                        )
                    }
                }
            }
            IBaseView.CommonBarStyle.RIGHT_CUSTOM -> if (onBindBarRightCustom() != null) {
                val group =
                    mCommonTitleBar.rightCustomView.findViewById<ViewGroup>(R.id.fl_custome)
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
    protected open fun setBarTextColor(@ColorInt color: Int) {
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
    protected open fun setBarBackIconRes(@DrawableRes res: Int) {
        check(enableCommonBar()) { "导航栏中不可用,请设置enableSimplebar为true" }
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

    private val mLoadStatusRun =
        Runnable { mLoadService.showCallback(getLoadingStatus().javaClass) }

    /**
     * 清除所有状态页
     */
    open fun clearStatus() {
        mHandler.removeCallbacks(mLoadStatusRun)
        mLoadService.showSuccess()
    }

    /**
     * 点击状态页默认执行事件
     */
    protected open fun onReload(v: View?) {
        showInitView()
        initData()
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

    /**
     * 显示loading状态页
     *
     * @param tip 为null时不带提示文本
     */
    open fun showLoadingView(tip: String?) {
        mHandler.removeCallbacks(mLoadStatusRun)
        mLoadService.showSuccess()
        mLoadService.setCallBack(
            getLoadingStatus().javaClass,
            Transport { _: Context?, view: View ->
                val tvTip = view.findViewById<TextView>(R.id.tv_tip)
                    ?: throw IllegalStateException(getLoadingStatus().javaClass.toString() + "必须带有显示提示文本的TextView,且id为R.id.tv_tip")
                if (tip == null) {
                    tvTip.visibility = View.GONE
                } else {
                    tvTip.visibility = View.VISIBLE
                    tvTip.text = tip
                }
            }
        )
        //延时300毫秒显示,避免闪屏
        postDelayed(mLoadStatusRun, 300)
    }


    /**
     * 显示初始化状态页
     */
    open fun showInitView() {
        clearStatus()
        mLoadService.showCallback(getInitStatus().javaClass)
    }


    /**
     * 显示出错状态页
     */
    open fun showErrorView() {
        clearStatus()
        mLoadService.showCallback(getErrorStatus().javaClass)
    }

    /**
     * 显示空状态页
     */
    open fun showEmptyView() {
        clearStatus()
        mLoadService.showCallback(getEmptyStatus().javaClass)
    }

    /**
     * 是否开启通用标题栏,默认true
     *
     * @return
     */
    protected open fun enableCommonBar(): Boolean {
        return true
    }

    /**
     * 是否可滑动返回,默认true
     *
     * @return
     */
    protected open fun enableSwipeBack(): Boolean {
        return true
    }

    /**
     * 初始化右边标题栏类型
     *
     * @return
     */
    protected open fun onBindBarRightStyle(): IBaseView.CommonBarStyle? {
        return IBaseView.CommonBarStyle.RIGHT_ICON
    }

    /**
     * 初始化左边标题栏类型
     *
     * @return
     */
    protected open fun onBindBarLeftStyle(): IBaseView.CommonBarStyle? {
        return IBaseView.CommonBarStyle.LEFT_BACK
    }

    /**
     * 初始化中间标题栏类型
     *
     * @return
     */
    protected open fun onBindBarCenterStyle(): IBaseView.CommonBarStyle {
        return IBaseView.CommonBarStyle.CENTER_TITLE
    }

    /**
     * 初始化标题栏右边文本
     *
     * @return
     */
    protected open fun onBindBarRightText(): Array<String?>? {
        return null
    }

    /**
     * 初始化标题文本
     *
     * @return
     */
    protected open fun onBindBarTitleText(): Array<String?>? {
        return null
    }

    /**
     * 初始化标题栏右边图标
     *
     * @return
     */
    @DrawableRes
    protected open fun onBindBarRightIcon(): Array<Int?>? {
        return null
    }

    /**
     * 初始化标题栏左边附加图标
     *
     * @return
     */
    @DrawableRes
    protected open fun onBindBarLeftIcon(): Int? {
        return null
    }

    /**
     * 初始化标题栏左边返回按钮图标
     *
     * @return
     */
    @DrawableRes
    protected open fun onBindBarBackIcon(): Int? {
        return null
    }

    /**
     * 初始化标题栏右侧自定义布局
     *
     * @return
     */
    protected open fun onBindBarRightCustom(): View? {
        return null
    }

    /**
     * 初始化标题栏中间自定义布局
     *
     * @return
     */
    protected open fun onBindBarCenterCustom(): View? {
        return null
    }

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
    protected open fun setCommonTitleBarBg(@ColorInt color: Int) {
        mCommonTitleBar.setBackgroundColor(color)
    }

    public override fun onDestroy() {
        super.onDestroy()
        ToastUtils.cancel()
        mHandler.removeCallbacksAndMessages(null)
        EventBus.getDefault().unregister(this)
        mDisposables.clear()
        //  ThirdHelper.refWatcher.watch(this);
    }

    /**
     * [简化Toast]
     *
     * @param msg
     */
    protected open fun showToast(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    /**
     * 屏幕适配 默认宽为1080pt
     */
    override fun getResources(): Resources? {
        return AdaptScreenUtils.adaptWidth(super.getResources(), 1080)
    }

    /**
     * 是否允许屏幕旋转
     *
     * @return true : 允许屏幕旋转，false : 不允许
     */
    open fun isAllowScreenRotate(): Boolean {
        return false
    }

    /**
     * [页面跳转]
     *
     * @param clz
     */
    open fun startActivity(clz: Class<*>?) {
        startActivity(Intent(this, clz))
    }


    /**
     * [携带数据的页面跳转]
     *
     * @param clz
     * @param bundle
     */
    open fun startActivity(clz: Class<*>, bundle: Bundle?) {
        val intent = Intent()
        intent.setClass(this, clz)
        bundle?.let { intent.putExtras(bundle) }
        startActivity(intent)
    }

    /**
     * [含有Bundle通过Class打开编辑界面]
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    open fun startActivityForResult(
        cls: Class<*>, bundle: Bundle?,
        requestCode: Int
    ) {
        val intent = Intent()
        intent.setClass(this, cls)
        bundle?.let { intent.putExtras(bundle) }
        startActivityForResult(intent, requestCode)
    }

    /**
     * 隐藏软键盘
     */
    fun hideSoftInput() {
        val view = this.window.decorView
        val imm = view.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 显示软键盘,调用该方法后,会在onPause时自动隐藏软键盘
     */
    fun showSoftInput(view: View) {
        val imm = view.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        view.postDelayed({
            imm.showSoftInput(
                view,
                InputMethodManager.SHOW_FORCED
            )
        }, SHOW_SPACE)
    }
}