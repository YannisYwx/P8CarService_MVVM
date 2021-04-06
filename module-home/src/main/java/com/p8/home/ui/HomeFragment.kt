package com.p8.home.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.p8.common.bean.LoginInfo
import com.p8.common.binding.command.BindingAction
import com.p8.common.binding.command.BindingCommand
import com.p8.common.databinding.DataBindingConfig
import com.p8.common.databinding.ui.DataBindingFragment
import com.p8.common.dialog.DialogUtils
import com.p8.common.dialog.DialogUtils.OnTakePhotoDialogChoiceListener
import com.p8.common.router.RouterKeyCode
import com.p8.common.util.AliOssManager
import com.p8.common.util.GlideUtils
import com.p8.common.util.PictureSelectorUtils
import com.p8.home.BR
import com.p8.home.R
import com.p8.home.adapter.HomeAdapter
import com.p8.home.databinding.HomeFragmentHomeBinding
import com.p8.home.databinding.HomeItemUserCardBinding
import com.p8.home.mvvm.viewmodel.HomeViewModel
import com.p8.home.mvvm.ViewModelFactory
import com.p8.home.ui.mine.MineFragment
import kotlinx.android.synthetic.main.home_fragment_home.*

/**
 *  @author : WX.Y
 *  date : 2021/3/23 11:05
 *  description :
 */
class HomeFragment : DataBindingFragment<HomeFragmentHomeBinding, HomeViewModel>() {

    private lateinit var mHomeItemUserCard: HomeItemUserCardBinding
    private lateinit var mHeaderView: View
    private lateinit var manager: LinearLayoutManager
    private lateinit var mAdapter: HomeAdapter

    /**
     * 头像点击
     */
    private var headerClickCommand = BindingCommand<Unit>(BindingAction {
        DialogUtils.showTakePhotoDialog(_mActivity, object : OnTakePhotoDialogChoiceListener {
            override fun onSelectCamera() {
                //拍照
                PictureSelectorUtils.photograph(this@HomeFragment)

            }

            override fun onSelectGallery() {
                //选择相册
                PictureSelectorUtils.selectSinglePictureFromGallery(this@HomeFragment)
            }

        })
    })

    /**
     * 点击返回
     */
    private var backClickCommand = BindingCommand<Unit>(BindingAction {
        _mActivity.finish()
    })

    companion object {
        fun newInstance(loginInfo: LoginInfo? = null): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putParcelable(RouterKeyCode.Home.USER_INFO, loginInfo)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        BarUtils.setStatusBarColor(_mActivity, Color.TRANSPARENT)
    }

    override fun onBindViewModelFactory(): ViewModelProvider.Factory {
        return ViewModelFactory.getInstance()
    }

    override fun initView() {
        mHeaderView =
            layoutInflater.inflate(R.layout.home_item_user_card, rvHome.parent as ViewGroup, false)
        mAdapter.setHeaderView(mHeaderView)
        mHomeItemUserCard = DataBindingUtil.bind(mHeaderView)!!
    }

    override fun initViewObservable() {
        mViewModel.mAgencyEvent.observe(this, Observer {
            mHomeItemUserCard.setVariable(BR.user, it)
            mHomeItemUserCard.executePendingBindings()
        })
    }

    override fun initData() {
        super.initData()
        mHomeItemUserCard.setVariable(BR.headerClick, headerClickCommand)
        mHomeItemUserCard.setVariable(BR.backClick, backClickCommand)
        mViewModel.getAgencyInfo()
    }


    override fun initDataBefore() {
        manager = LinearLayoutManager(_mActivity)
        manager.orientation = RecyclerView.VERTICAL
        mAdapter = HomeAdapter()
    }

    override fun onBindLayout() = R.layout.home_fragment_home

    override fun loadView() {
        super.loadView()
        clearStatus()
    }

    override fun bindListener() {
        super.bindListener()
        //菜单点击
        mAdapter.onItemClickListener =
            BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
                if (position > 0) {
                    ToastUtils.showShort("点击了 $position")
                    when (position) {
                        1 -> {
                            MineFragment.start(this)
                        }
                        2 -> {

                        }
                    }
                }
            }
        //模块点击
        mAdapter.onItemChildClickListener =
            BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.tv_logistics -> {
                        show("物流")
                    }
                    R.id.tv_financing -> {
                        show("财务")
                    }
                    R.id.tv_order -> {
                        show("工单")
                    }
                    R.id.tv_inspection -> {
                        show("地主")
                    }
                }
            }
    }

    override fun getDataBindingConfig() =
        DataBindingConfig(
            R.layout.home_fragment_home,
            BR.vm,
            mViewModel
        ).addBindingParam(BR.adapter, mAdapter).addBindingParam(
            BR.layoutManager, manager
        )

    private fun show(msg: String) {
        ToastUtils.showShort(msg)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                val selectList = PictureSelector.obtainMultipleResult(data)
                val localMedia = selectList[0]
                if (localMedia != null) {
                    GlideUtils.setImageViewForUrl(
                        _mActivity,
                        mHomeItemUserCard.civUserHeader,
                        localMedia.path
                    )
                    AliOssManager.getInstance().uploadFiles(_mActivity, selectList)
                }
            }
        }
    }
}