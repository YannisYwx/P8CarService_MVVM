package com.p8.common.dialog

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.lxj.xpopup.core.BottomPopupView
import com.p8.common.R

/**
 *  @author : WX.Y
 *  date : 2021/3/26 17:19
 *  description :
 */
class TakePhotoPopup(context: Context) : BottomPopupView(context), View.OnClickListener {
    lateinit var btnCancel: Button
    lateinit var tvGallery: TextView
    lateinit var tvCamera: TextView

    private var listener: DialogUtils.OnTakePhotoDialogChoiceListener? = null

    override fun getImplLayoutId(): Int {
        return R.layout.common_dialog_select_photo
    }

    override fun onCreate() {
        super.onCreate()
        btnCancel = findViewById(R.id.btn_cancel)
        tvGallery = findViewById(R.id.tv_select_gallery)
        tvCamera = findViewById(R.id.tv_select_camera)

        btnCancel.setOnClickListener(this)
        tvCamera.setOnClickListener(this)
        tvGallery.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            btnCancel -> {
                dismiss()
            }

            tvCamera -> {
                listener?.onSelectCamera()
            }

            tvGallery -> {
                listener?.onSelectGallery()
            }
        }
    }

    fun setOnTakePhotoDialogChoiceListener(listener: DialogUtils.OnTakePhotoDialogChoiceListener): TakePhotoPopup {
        this.listener = listener
        return this
    }


}