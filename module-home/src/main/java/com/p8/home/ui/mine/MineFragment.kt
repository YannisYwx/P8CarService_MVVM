package com.p8.home.ui.mine

import com.blankj.utilcode.util.CacheDoubleUtils
import com.p8.common.Constants
import com.p8.common.bean.LoginInfo
import com.p8.common.databinding.DataBindingConfig
import com.p8.common.databinding.ui.DataBindingFragment
import com.p8.common.event.EventCode
import com.p8.common.event.FragmentEvent
import com.p8.common.router.RouterActivityPath
import com.p8.common.router.RouterKeyCode
import com.p8.home.BR
import com.p8.home.R
import com.p8.home.databinding.HomeFragmentMineBinding
import com.p8.home.mvvm.ViewModelFactory
import com.p8.home.mvvm.viewmodel.MineViewModel
import me.yokeyword.fragmentation.SupportFragment

/**
 *  @author : WX.Y
 *  date : 2021/3/29 10:38
 *  description : 我的
 */
class MineFragment : DataBindingFragment<HomeFragmentMineBinding, MineViewModel>() {

    companion object {
        @JvmStatic
        fun start(fragment: SupportFragment) {
            fragment.start(MineFragment())
        }
    }

    override fun getDataBindingConfig() =
        DataBindingConfig(R.layout.home_fragment_mine, BR.vm, mViewModel)

    override fun initView() {
    }

    override fun onBindViewModelFactory() = ViewModelFactory.getInstance()

    override fun enableCommonBar() = true

    override fun setTitle(strings: Array<out String>?) {
        super.setTitle(strings)
    }

    override fun onBindBarTitleText(): Array<String> {
        return arrayOf("我的")
    }

    override fun initViewObservable() {
    }

    override fun initBaseViewObservable() {
        super.initBaseViewObservable()
    }

    override fun initData() {
        super.initData()
    }

    override fun startToFragment(aClass: Class<out SupportFragment>) {
        super.startToFragment(aClass)
        if (aClass.isAssignableFrom(ModifyPasswordFragment::class.java)) {
            ModifyPasswordFragment.start(this)
        }
    }

    override fun onEvent(event: FragmentEvent?) {
        super.onEvent(event)
        event?.let {
            if (it.code == EventCode.App.RESET_PASSWORD) {
                _mActivity.finish()
                mRouter.build(RouterActivityPath.Main.PAGER_ENTRY).navigation()
            }
        }
    }

}