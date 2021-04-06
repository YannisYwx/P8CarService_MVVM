package com.p8.main.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.AdaptScreenUtils

/**
 *  @author : WX.Y
 *  date : 2021/3/17 18:24
 *  description :
 */
class GridPaddingDecoration : ItemDecoration() {
    private fun getSpanCount(parent: RecyclerView): Int {
        // 列数
        var spanCount = -1
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            spanCount = layoutManager.spanCount
        } else if (layoutManager is StaggeredGridLayoutManager) {
            spanCount = layoutManager.spanCount
        }
        return spanCount
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val spanCount = getSpanCount(parent)
        var itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition != 0) {
            itemPosition -= 1
            when {
                itemPosition % spanCount == 0 -> {
                    outRect.left = AdaptScreenUtils.pt2Px(8f)
                    outRect.right = AdaptScreenUtils.pt2Px(4f)
                }
                (itemPosition + 1) % spanCount == 0 -> {
                    outRect.right = AdaptScreenUtils.pt2Px(8f)
                    outRect.left = AdaptScreenUtils.pt2Px(4f)
                }
                else -> {
                    outRect.right = AdaptScreenUtils.pt2Px(4f)
                    outRect.left = AdaptScreenUtils.pt2Px(4f)
                }
            }
            outRect.top = if (itemPosition < spanCount) 8 else 4
            outRect.bottom = 4
        }
    }
}

