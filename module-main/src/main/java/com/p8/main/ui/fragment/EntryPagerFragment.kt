package com.p8.main.ui.fragment

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.p8.common.Constants
import com.p8.common.base.BaseSupportFragment
import com.p8.main.R
import com.p8.main.adapter.EntryTypeAdapter
import com.p8.main.util.GridPaddingDecoration
import kotlinx.android.synthetic.main.main_fragment_enter_pager.*


/**
 *  @author : WX.Y
 *  date : 2021/3/17 14:59
 *  description : 入口页面
 */
class EntryPagerFragment : BaseSupportFragment() {
    lateinit var adapter: EntryTypeAdapter

    companion object {
        fun newInstance(): EntryPagerFragment {
            return EntryPagerFragment()
        }
    }

    override fun enableCommonBar() = false

    override fun onSupportVisible() {
        super.onSupportVisible()
        BarUtils.setStatusBarLightMode(_mActivity, false)
    }

    override fun initData() {
        adapter = EntryTypeAdapter(this)
        val view: View = layoutInflater.inflate(
            R.layout.main_item_enter_header,
            rvEnter.parent as ViewGroup,
            false
        )
        adapter.addHeaderView(view)
        rvEnter.layoutManager = GridLayoutManager(context, 3)
        rvEnter.adapter = adapter
        rvEnter.addItemDecoration(GridPaddingDecoration())
    }


    override fun bindListener() {
        super.bindListener()
        adapter.setOnEntryTypeClickListener { _, position ->
            ToastUtils.showShort("click item$position")
            println("click item$position")
            when (position) {
                0 -> start(
                    LoginFragment.newInstance(
                        Constants.UserType.LARGE
                    )
                )
                1 -> start(
                    LoginFragment.newInstance(
                        Constants.UserType.LAND
                    )
                )
                2 -> {
                }
                3 -> {
                }
                4 -> {
                }
                5 -> {
                }
                6 -> {
                }
                7 -> {
                }
                8 -> {
                }
                else -> {
                }
            }

        }

    }

    override fun onBindLayout() = R.layout.main_fragment_enter_pager
}