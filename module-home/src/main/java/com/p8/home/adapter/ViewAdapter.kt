package com.p8.home.adapter

import androidx.databinding.BindingAdapter
import com.p8.common.util.DataCleanManager.CACHE_CLEAR
import com.p8.common.widget.CommonItemView
import com.p8.home.R

/**
 *  @author : WX.Y
 *  date : 2021/3/29 15:33
 *  description :
 */
class ViewAdapter {

    companion object {

        @JvmStatic
        @BindingAdapter(value = ["rightText"], requireAll = false)
        fun setCommonRightText(commonTextView: CommonItemView, rightText: String?) {
            commonTextView.setTextRight(
                rightText,
                if (rightText == CACHE_CLEAR) commonTextView.resources.getColor(R.color.line_gray) else commonTextView.resources.getColor(
                    R.color.orangeRed
                )
            )
        }
    }
}