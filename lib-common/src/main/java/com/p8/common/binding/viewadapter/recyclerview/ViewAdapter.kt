package com.p8.common.binding.viewadapter.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 *  @author : WX.Y
 *  date : 2021/3/25 17:40
 *  description :
 */
class ViewAdapter {
    companion object {

        @BindingAdapter(value = ["adapter"], requireAll = false)
        @JvmStatic
        fun setAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>?) {
            println("=============================BindingAdapter ${adapter == null}")
            recyclerView.adapter = adapter
        }

        @BindingAdapter(value = ["layoutManager"], requireAll = false)
        @JvmStatic
        fun setLayoutManager(recyclerView: RecyclerView, layoutManager: LinearLayoutManager?) {
            println("=============================BindingAdapter ${layoutManager == null}")
            recyclerView.layoutManager = layoutManager
        }

        @BindingAdapter(value = ["notifyCurrentListChanged"], requireAll = false)
        @JvmStatic
        fun notifyCurrentListChanged(recyclerView: RecyclerView, notifyCurrentListChanged: Boolean) {
            if (notifyCurrentListChanged) {
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }



    }
}