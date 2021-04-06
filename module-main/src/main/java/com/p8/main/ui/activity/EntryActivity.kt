package com.p8.main.ui.activity

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.AdaptScreenUtils
import com.gyf.immersionbar.ImmersionBar
import com.p8.common.base.BaseSupportActivity
import com.p8.common.router.RouterActivityPath
import com.p8.main.R
import com.p8.main.ui.fragment.EntryPagerFragment
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator
import me.yokeyword.fragmentation.anim.FragmentAnimator

/**
 *  @author : WX.Y
 *  date : 2021/3/17 14:08
 *  description :
 */
@Route(path = RouterActivityPath.Main.PAGER_ENTRY)
class EntryActivity : BaseSupportActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, EntryActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            context.startActivity(intent)
        }
    }

    override fun isLightMode(): Boolean {
        return false
    }

    override fun initView() {
        if (findFragment(EntryPagerFragment::class.java) == null) {
            loadRootFragment(R.id.fl_container,
                EntryPagerFragment.newInstance()
            )
        }
        ImmersionBar.with(this)
            .init()
    }

    override fun enableCommonBar() = false

    override fun initData() {

    }

    override fun onBindLayout() = R.layout.main_container

    override fun onCreateFragmentAnimator(): FragmentAnimator? {
        // 设置横向(和安卓4.x动画相同)
        return DefaultHorizontalAnimator()
    }

    override fun getResources(): Resources? {
        return AdaptScreenUtils.adaptWidth(super.getResources(), 1080)
    }

}