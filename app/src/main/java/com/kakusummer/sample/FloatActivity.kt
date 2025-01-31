package com.kakusummer.sample

import android.animation.ValueAnimator
import android.content.Context
import android.util.Log
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import com.assistant.bases.BaseActivity
import com.kakusummer.androidutils.R
import com.kakusummer.androidutils.databinding.ActivityFloatBinding

class FloatActivity : BaseActivity<ActivityFloatBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_float
    override val TAG: String
        get() = "TAG_FloatActivity"

    private var isOpened = false
    private val menuViews = ArrayList<View>()

    override fun initDate() {
        super.initDate()
        binding.apply {
            menuViews.add(ivOne)
            menuViews.add(ivTwo)
            menuViews.add(ivThree)
        }

    }

    override fun initListener() {
        super.initListener()
        binding.ivControl.setOnLongClickListener {
            Log.d("yeTest", "LongClick: ")
            true
        }
        binding.ivControl.setOnClickListener {
            switchMenu(isOpened)
            isOpened = !isOpened
        }
    }

    private fun switchMenu(isOpen: Boolean) {
        val startRadius = dpToPixel(if (isOpen) 120 else 0)
        val endRadius = dpToPixel(if (isOpen) 0 else 120)

        //定义类一个int的变化范围
        ValueAnimator.ofInt(startRadius, endRadius).apply {
            duration = 300
            interpolator = OvershootInterpolator(6F)
            //对该 ValueAnimator的变化监听，通过回调的valueAnimator.animatedValue取值
            addUpdateListener { valueAnimator ->
                menuViews.forEach { view ->
                    val lp = view.layoutParams as ConstraintLayout.LayoutParams
                    lp.circleRadius = valueAnimator.animatedValue as Int
                    view.layoutParams = lp
                }
            }
        }.start()
    }


    fun Context.dpToPixel(dp: Int): Int {
        val displayMetrics = this.resources.displayMetrics
        return if (dp < 0) dp else Math.round(dp * displayMetrics.density)
    }
}