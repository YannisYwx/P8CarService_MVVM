package com.p8.home.ui

import android.content.res.Resources
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.AdaptScreenUtils
import com.p8.common.base.BaseSupportActivity
import com.p8.common.bean.LoginInfo
import com.p8.common.router.RouterActivityPath
import com.p8.common.router.RouterKeyCode
import com.p8.home.R
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator
import me.yokeyword.fragmentation.anim.FragmentAnimator

/**
 *  @author : WX.Y
 *  date : 2021/3/23 9:49
 *  description :
 */
@Route(path = RouterActivityPath.Home.PAGER_HOME)
class HomeActivity : BaseSupportActivity() {

    @Autowired(name = RouterKeyCode.Home.USER_INFO)
    lateinit var loginInfo:LoginInfo


    override fun initData() {
        println(loginInfo.toString())
        if (findFragment(HomeFragment::class.java) == null) {
            loadRootFragment(
                R.id.fl_container,
                HomeFragment.newInstance()
            )
        }
    }

    override fun onBindLayout() = R.layout.home_container

    override fun onCreateFragmentAnimator(): FragmentAnimator? {
        // 设置横向(和安卓4.x动画相同)
        return DefaultHorizontalAnimator()
    }

    override fun getResources(): Resources? {
        return AdaptScreenUtils.adaptWidth(super.getResources(), 1080)
    }

    override fun enableCommonBar() = false

}