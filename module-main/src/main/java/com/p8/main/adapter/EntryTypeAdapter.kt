package com.p8.main.adapter

import android.graphics.Color
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.jakewharton.rxbinding3.view.clicks
import com.p8.main.R
import com.p8.main.bean.UserType
import com.p8.common.observer.ViewClickObserver
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import java.util.*
import java.util.concurrent.TimeUnit

/**
 *  @author : WX.Y
 *  date : 2021/3/17 18:11
 *  description :
 */
class EntryTypeAdapter(
    var c: Consumer<in Disposable>,
    data: List<UserType> = getDefaultUserTypes()
) :
    BaseQuickAdapter<UserType, BaseViewHolder>(R.layout.main_item_enter_type, data) {

    override fun convert(helper: BaseViewHolder, item: UserType?) {
        helper.setText(R.id.tv_name, item?.type)
        helper.setText(R.id.tv_label, item?.type?.substring(0, 1))
        helper.itemView.setBackgroundColor(item!!.color)
        helper.itemView.clicks().doOnSubscribe(c)?.throttleFirst(1, TimeUnit.SECONDS)
            ?.subscribe(object : ViewClickObserver() {
                override fun onClick() {
                    mListener?.invoke(helper.itemView, helper.layoutPosition)
                }
            })
    }

    companion object {
        fun getDefaultUserTypes(): List<UserType> {
            val userTypes: MutableList<UserType> =
                ArrayList()
            userTypes.add(UserType(0, "大主", 0, Color.parseColor("#3B3B5B")))
            userTypes.add(UserType(1, "地主", 0, Color.parseColor("#4898F6")))
            userTypes.add(UserType(2, "场主", 0, Color.parseColor("#1EA388")))
            userTypes.add(UserType(3, "小主", 0, Color.parseColor("#F77539")))
            userTypes.add(UserType(4, "台主", 0, Color.parseColor("#EBC82C")))
            userTypes.add(UserType(5, "中主", 0, Color.parseColor("#3177AC")))
            userTypes.add(UserType(6, "自主", 0, Color.parseColor("#C0D9F5")))
            userTypes.add(UserType(7, "施主", 0, Color.parseColor("#14CFA0")))
            userTypes.add(UserType(8, "其他", 0, Color.parseColor("#706F89")))
            return userTypes
        }
    }

    fun setOnEntryTypeClickListener(listener: ((View, Int) -> Unit)) {
        this.mListener = listener
    }

    private var mListener: ((View, Int) -> Unit)? = null

}