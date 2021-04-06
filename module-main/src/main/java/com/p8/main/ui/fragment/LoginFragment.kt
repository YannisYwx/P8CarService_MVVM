package com.p8.main.ui.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.CacheDoubleUtils
import com.blankj.utilcode.util.ToastUtils
import com.p8.common.Constants
import com.p8.common.Constants.USER_TYPE
import com.p8.common.bean.LoginInfo
import com.p8.common.databinding.DataBindingConfig
import com.p8.common.databinding.ui.DataBindingFragment
import com.p8.common.router.RouterActivityPath
import com.p8.common.router.RouterKeyCode
import com.p8.main.BR
import com.p8.main.R
import com.p8.main.databinding.MainFragmentLoginRegisterBinding
import com.p8.main.mvvm.ViewModelFactory
import com.p8.main.mvvm.viewmodel.LoginViewModel

/**
 *  @author : WX.Y
 *  date : 2021/3/17 18:33
 *  description :
 */
class LoginFragment : DataBindingFragment<MainFragmentLoginRegisterBinding, LoginViewModel>() {

    companion object {
        fun newInstance(@Constants.UserType type: Int): LoginFragment {
            val args = Bundle()
            args.putInt(USER_TYPE, type)
            val fragment = LoginFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun loadView() {
        super.loadView()
        clearStatus()
    }

    override fun isLightBarMode() = true

    override fun onBindViewModelFactory() = ViewModelFactory.getInstance()

    override fun initView() {
        arguments?.let {
            mViewModel?.initAccount(it.getInt(USER_TYPE))
        }
    }

    override fun initViewObservable() {
    }

    override fun bindListener() {
    }

    override fun onBindLayout(): Int {
        TODO("Not yet implemented")
    }


    override fun initBaseViewObservable() {
        super.initBaseViewObservable()
        mViewModel.mLoginEvent.observe(this, Observer {
            ToastUtils.showShort("登录成功")
            mRouter.build(RouterActivityPath.Home.PAGER_HOME).withParcelable(
                RouterKeyCode.Home.USER_INFO,
                CacheDoubleUtils.getInstance()
                    .getParcelable(Constants.Key.LOGIN_INFO, LoginInfo.CREATOR)
            ).navigation()
        })
    }


    override fun getDataBindingConfig() =
        DataBindingConfig(R.layout.main_fragment_login_register, BR.viewModel, mViewModel)

}