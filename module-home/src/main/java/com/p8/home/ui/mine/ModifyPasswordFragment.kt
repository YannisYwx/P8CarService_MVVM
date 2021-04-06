package com.p8.home.ui.mine

import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.p8.common.databinding.DataBindingConfig
import com.p8.common.databinding.ui.DataBindingFragment
import com.p8.common.databinding.ui.IBaseView
import com.p8.home.BR
import com.p8.home.R
import com.p8.home.databinding.HomeFragmentModifyPasswordBinding
import com.p8.home.mvvm.ViewModelFactory
import com.p8.home.mvvm.viewmodel.MineViewModel
import me.yokeyword.fragmentation.SupportFragment

/**
 *  @author : WX.Y
 *  date : 2021/3/30 17:59
 *  description :
 */
class ModifyPasswordFragment :
    DataBindingFragment<HomeFragmentModifyPasswordBinding, MineViewModel>() {

    companion object {
        @JvmStatic
        fun start(fragment: SupportFragment) {
            fragment.start(ModifyPasswordFragment())
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(
            R.layout.home_fragment_modify_password, BR.vm, mViewModel
        )
    }

    override fun enableCommonBar() = true

    override fun setTitle(strings: Array<out String>?) {
        super.setTitle(strings)
    }

    override fun onBindBarTitleText(): Array<String> {
        return arrayOf("修改密码")
    }

    override fun onBindBarRightText(): Array<String> {
        return arrayOf("完成")
    }

    override fun onBindBarRightStyle() = IBaseView.CommonBarStyle.RIGHT_TEXT

    override fun initView() {
    }

    override fun onBindViewModelFactory() = ViewModelFactory.getInstance()

    override fun initViewObservable() {
    }

    override fun onRight1Click(v: View?) {
        super.onRight1Click(v)
        mViewModel.doModifyPassword()
    }
}