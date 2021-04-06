package com.p8.main.viewholder

import android.animation.ObjectAnimator
import android.view.View
import android.widget.ImageView
import com.p8.main.R
import com.p8.main.bean.GuideBean
import com.zhpan.bannerview.holder.ViewHolder

/**
 *  @author : WX.Y
 *  date : 2021/3/16 15:29
 *  description :
 */
class GuideViewHolder : ViewHolder<GuideBean> {
    override fun getLayoutId() = R.layout.main_item_guide
    override fun onBind(itemView: View?, data: GuideBean?, position: Int, size: Int) {
        val img: ImageView? = itemView?.findViewById(R.id.iv_banner)
        data?.imgRes?.let { img?.setImageResource(it) }
        img?.let {
            val alphaAnimator = ObjectAnimator.ofFloat(img, "alpha", .0f, 1f)
            alphaAnimator.duration = 1500
            alphaAnimator.start()

        }
    }

    private lateinit var mClickListener: (View, Int) -> Unit

    fun setOnPagerClickListener(listener: (View, Int) -> Unit) {
        this.mClickListener = listener
    }
}