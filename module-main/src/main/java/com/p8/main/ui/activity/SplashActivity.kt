package com.p8.main.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.orhanobut.logger.Logger
import com.p8.common.AppConstant
import com.p8.common.storage.MmkvHelper
import com.p8.main.R
import java.lang.ref.WeakReference

/**
 *  @author : WX.Y
 *  date : 2021/3/16 19:16
 *  description :
 */
class SplashActivity : AppCompatActivity() {
    private var handler: MyHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_splash)
        if ((intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) > 0) {
            finish()
            return
        }
        handler = MyHandler(this)
        val isGuide = MmkvHelper.decodeBoolean(AppConstant.Entry.IS_FIRST_IN)
        val isLogin = MmkvHelper.decodeBoolean(AppConstant.Entry.IS_FIRST_IN)

        Logger.e("isGuide = $isGuide isLogin = $isLogin")
        if (isGuide) {
            if (isLogin) {
                handler?.sendEmptyMessageDelayed(1001, 3500)
            } else {
                handler?.sendEmptyMessageDelayed(1002, 3500)
            }
        } else {
            handler?.sendEmptyMessageDelayed(1003, 3500)
        }
    }

    override fun onDestroy() {
        handler?.removeCallbacksAndMessages(null)
        handler = null
        super.onDestroy()
        val viewGroup: ViewGroup = window.decorView as ViewGroup
        viewGroup.removeAllViews()
        System.gc()
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    internal class MyHandler(activity: SplashActivity) : Handler() {
        var mActivityReference: WeakReference<SplashActivity> = WeakReference(activity)
        override fun handleMessage(msg: Message) {
            val activity: Activity? = mActivityReference.get()
            if (activity != null) {
                when (msg.what) {
                    1001 -> {
                        val intent = Intent(activity, GuideActivity::class.java)
                        activity.startActivity(intent)
                        activity.finish()
                    }
                    1002 -> {
                        val intent = Intent(activity, GuideActivity::class.java)
                        activity.startActivity(intent)
                        activity.finish()
                    }
                    1003 -> {
                        val intent = Intent(activity, GuideActivity::class.java)
                        activity.startActivity(intent)
                        activity.finish()
                    }
                    else -> {
                    }
                }
            }
        }

    }
}