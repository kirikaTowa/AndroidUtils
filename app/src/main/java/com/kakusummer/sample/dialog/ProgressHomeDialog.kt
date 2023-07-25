package com.kakusummer.sample.dialog

import android.content.Context
import com.assistant.bases.BaseDialog
import com.assistant.viewcustom.progress.ProgressDigitalSeekBar
import com.kakusummer.androidutils.R
import com.kakusummer.androidutils.databinding.DialogProgressHomeBinding
import com.kakusummer.androidutils.databinding.DialogStatementBinding


class ProgressHomeDialog
    (
    context: Context?,
    var callbackWidth: ((Float) -> Unit)?,
    var callbackDepth: ((Float) -> Unit)?
) :
    BaseDialog<DialogProgressHomeBinding>(context!!) {


    override val dialogStyleId: Int
        get() = defaultStyle
    override val isCanceledTouch: Boolean
        get() = true
    override val isCanceledReturn: Boolean
        get() = true
    override val layoutId: Int
        get() = R.layout.dialog_progress_home
    override val TAG: String
        get() = "TAG_TipUserPoliceDialog"

    //View的事件
    override fun initViewEvent() {
        binding.apply {
            pgsWidth.also {
                it.setProgress((2 - AppSpUtils.getWeightScaling()) * 2)
                it.setOnSeekChangeListener(object : ProgressDigitalSeekBar.OnSeekChangeListener {
                    override fun onProgressChanged(
                        view: ProgressDigitalSeekBar?,
                        progress: Float,
                        fromUser: Boolean
                    ) {
                        if (fromUser) {
                            callbackWidth?.invoke(progress)
                        }
                    }

                    override fun onStartTrackingTouch(view: ProgressDigitalSeekBar?) {
                    }

                    override fun onStopTrackingTouch(view: ProgressDigitalSeekBar?) {
                        AppSpUtils.saveWeightScaling(MainActivity.weightScaling)
                    }

                })
            }

            pgsDepth.also {
                it.setProgress(AppSpUtils.getDeepScaling())
                it.setOnSeekChangeListener(object : ProgressDigitalSeekBar.OnSeekChangeListener {
                    override fun onProgressChanged(
                        view: ProgressDigitalSeekBar?,
                        progress: Float,
                        fromUser: Boolean
                    ) {
                        if (fromUser) {
                            callbackDepth?.invoke(progress)
                        }
                    }

                    override fun onStartTrackingTouch(view: ProgressDigitalSeekBar?) {
                    }

                    override fun onStopTrackingTouch(view: ProgressDigitalSeekBar?) {
                        AppSpUtils.saveDeepScaling(MainActivity.scaleScaling)
                    }

                })
            }
        }

    }
}