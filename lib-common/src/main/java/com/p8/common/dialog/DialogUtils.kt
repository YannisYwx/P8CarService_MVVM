package com.p8.common.dialog

import android.content.Context
import com.lxj.xpopup.XPopup

/**
 *  @author : WX.Y
 *  date : 2021/3/26 17:11
 *  description :
 */
class DialogUtils {
    companion object {

        @JvmStatic
        fun showTakePhotoDialog(context: Context, listener: OnTakePhotoDialogChoiceListener) {
            XPopup.Builder(context)
                .hasBlurBg(false)
                .hasShadowBg(true)
                .moveUpToKeyboard(true)
                .asCustom(TakePhotoPopup(context).setOnTakePhotoDialogChoiceListener(listener))
                .show()
        }
    }

    interface OnTakePhotoDialogChoiceListener {
        fun onSelectCamera()
        fun onSelectGallery()
    }
}