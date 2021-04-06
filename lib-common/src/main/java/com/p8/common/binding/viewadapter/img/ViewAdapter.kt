package com.p8.common.binding.viewadapter.img

import android.text.TextUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 *  @author : WX.Y
 *  date : 2021/3/29 11:31
 *  description :
 */
class ViewAdapter {
    companion object {

        @JvmStatic
        @BindingAdapter(value = ["url", "placeholderRes"], requireAll = false)
        fun setImageUrl(img: ImageView, url: String, placeholderRes: Int) {
            if (!TextUtils.isEmpty(url)) {
                //使用Glide框架加载图片
                Glide.with(img.context)
                    .load(url)
                    .apply(RequestOptions().placeholder(placeholderRes))
                    .into(img)
            }
        }

        @JvmStatic
        @BindingAdapter(value = ["imgRes"], requireAll = false)
        fun setRes(imageView: ImageView, res: Int) {
            imageView.setImageResource(res)
        }
    }
}