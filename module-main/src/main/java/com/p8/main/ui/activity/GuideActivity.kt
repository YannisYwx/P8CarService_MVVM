package com.p8.main.ui.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.ToastUtils
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.p8.main.R
import com.p8.main.bean.GuideBean
import com.p8.main.viewholder.GuideViewHolder
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.bannerview.adapter.OnPageChangeListenerAdapter
import com.zhpan.bannerview.constants.TransformerStyle
import com.zhpan.bannerview.holder.HolderCreator
import com.zhpan.bannerview.utils.BannerUtils
import com.zhpan.indicator.enums.IndicatorSlideMode

import kotlinx.android.synthetic.main.main_activity_guide.*
import java.util.*

/**
 *  @author : WX.Y
 *  date : 2021/3/16 16:19
 *  description :
 */
class GuideActivity : AppCompatActivity(), HolderCreator<GuideViewHolder> {

    private lateinit var mViewPager: BannerViewPager<GuideBean, GuideViewHolder>

    private val des = arrayOf("在这里\n你可以听到周围人的心声", "在这里\nTA会在下一秒遇见你", "在这里\n不再错过可以改变你一生的人")

    private val transforms = intArrayOf(
        TransformerStyle.NONE,
        TransformerStyle.ACCORDION,
        TransformerStyle.STACK,
        TransformerStyle.DEPTH,
        TransformerStyle.ROTATE,
        TransformerStyle.SCALE_IN
    )

    private lateinit var guideBeans: List<GuideBean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_guide)
        ImmersionBar.with(this)
            .titleBar(topView)
            .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
            .init()
        setupViewPager()
        updateUI(0)
    }

    private fun setupViewPager() {
        guideBeans = List<GuideBean>(3) {
            val drawable = resources.getIdentifier("guide$it", "drawable", packageName)
            GuideBean(drawable)
        }
        mViewPager = findViewById(R.id.viewpager)
        mViewPager.setAutoPlay(false)
            .setCanLoop(false)
            .setPageTransformerStyle(transforms[Random().nextInt(6)])
            .setPageTransformerStyle(transforms[0])
            .setScrollDuration(ANIMATION_DURATION)
            .setIndicatorMargin(0, 0, 0, resources.getDimension(R.dimen.main_dp_100).toInt())
            .setIndicatorGap(resources.getDimension(R.dimen.main_dp_10).toInt())
            .setIndicatorColor(
                ContextCompat.getColor(this,
                    R.color.common_colorWhite
                ),
                ContextCompat.getColor(this,
                    R.color.main_white_alpha_75
                )
            )
            .setIndicatorSlideMode(IndicatorSlideMode.SMOOTH)
            .setIndicatorRadius(
                resources.getDimension(R.dimen.main_dp_3).toInt(),
                resources.getDimension(R.dimen.main_dp_4_5).toInt()
            )
            .setOnPageChangeListener(object : OnPageChangeListenerAdapter() {
                override fun onPageSelected(position: Int) {
                    BannerUtils.log("position:$position")
                    updateUI(position)
                }
            })
            .setHolderCreator(this)
            .create(guideBeans)
    }

    private fun updateUI(position: Int) {
        tvDescribe.text = des[position]
        val translationAnim = ObjectAnimator.ofFloat(tvDescribe, "translationX", -120f, 0f)
        translationAnim.duration = ANIMATION_DURATION.toLong()
        translationAnim.interpolator = DecelerateInterpolator()
        val alphaAnimator1 = ObjectAnimator.ofFloat(tvDescribe, "alpha", 0f, 1f)
        alphaAnimator1.duration = ANIMATION_DURATION.toLong()
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(translationAnim, alphaAnimator1)
        animatorSet.start()

        if (position == mViewPager.list.size - 1 && btnStart?.visibility == View.GONE) {
            btnStart?.visibility = View.VISIBLE
            ObjectAnimator
                .ofFloat(btnStart, "alpha", 0f, 1f)
                .setDuration(ANIMATION_DURATION.toLong()).start()
        } else {
            btnStart?.visibility = View.GONE
        }
    }

    override fun createViewHolder(): GuideViewHolder {
        val guideViewHolder = GuideViewHolder()
        guideViewHolder.setOnPagerClickListener { _, i -> run { ToastUtils.showShort("Click position $i") } }
        return guideViewHolder
    }

    companion object {
        private const val ANIMATION_DURATION = 1300
    }

    fun onClick(v:View){
        EntryActivity.start(this)
        finish()
    }

}