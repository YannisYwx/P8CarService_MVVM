package com.p8.home.adapter

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.p8.home.AppDataUtil
import com.p8.home.BR
import com.p8.home.R
import com.p8.home.bean.HomeMultipleItem

/**
 *  @author : WX.Y
 *  date : 2021/3/23 17:46
 *  description :
 */
class HomeAdapter :
    BaseMultiItemQuickAdapter<HomeMultipleItem, HomeAdapter.HomeItemViewHolder>(AppDataUtil.getUserMenus()) {


    init {
        addItemType(HomeMultipleItem.MODULE, R.layout.home_item_user_module)
        addItemType(HomeMultipleItem.MENU, R.layout.home_item_user_menu)
    }


    override fun convert(helper: HomeItemViewHolder, item: HomeMultipleItem?) {
        item?.let {
            if(helper.itemViewType == HomeMultipleItem.MENU) {
                val isShowBottomLine =
                    helper.adapterPosition < data.size - 1 && data[helper.adapterPosition].userMenu!!.hasTopLine
                it.userMenu?.hasBottomLine =
                    !(helper.layoutPosition == data.size || isShowBottomLine)
            } else if(helper.itemViewType == HomeMultipleItem.MODULE) {
                helper.addOnClickListener(
                    R.id.tv_logistics,
                    R.id.tv_financing,
                    R.id.tv_order,
                    R.id.tv_inspection
                )
            }
        }
        val binding = helper.getBinding()
        binding.setVariable(BR.hmi, item)
        binding.executePendingBindings()
    }

    override fun getItemView(layoutResId: Int, parent: ViewGroup?): View? {
        val binding: ViewDataBinding =
            DataBindingUtil.inflate(mLayoutInflater, layoutResId, parent, false)
                ?: return super.getItemView(layoutResId, parent)
        val view = binding.root
        view.setTag(R.id.BaseQuickAdapter_databinding_support, binding)
        return view
    }

    class HomeItemViewHolder(view: View) : BaseViewHolder(view) {

        fun getBinding() =
            itemView.getTag(R.id.BaseQuickAdapter_databinding_support) as ViewDataBinding
    }

}