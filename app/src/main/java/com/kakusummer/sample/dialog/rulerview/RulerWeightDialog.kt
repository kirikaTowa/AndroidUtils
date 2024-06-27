
package com.kakusummer.sample.dialog.rulerview

import android.content.Context
import com.assistant.bases.BaseDialog
import com.kakusummer.androidutils.R
import com.kakusummer.androidutils.databinding.DialogRulerWeightBinding

/**
 * Created by Android Studio.
 * author: yhs
 * Date: 2023/5/11
 * Time: 14:37
 * 备注：
 */
class RulerWeightDialog(context: Context, var mType:Int, var mStartData :Float, var callback: ((Float) -> Unit)?) : BaseDialog<DialogRulerWeightBinding>(context) {
    override val layoutId: Int
        get() = R.layout.dialog_ruler_weight
    override val TAG: String
        get() = "TAG_DialogRulerWeight"
    override val dialogStyleId: Int
        get() = com.assistant.bases.R.style.common_dialog_style
    override val isCanceledTouch: Boolean
        get() = false
    override val isCanceledReturn: Boolean
        get() = false

    override fun initViewEvent() {
        super.initViewEvent()
        if (mType == 1) {
            //步伐
            binding.titleLabel.text = "设定目标"
            binding.unitLabel.text = "步"
            binding.rulerView.setValue(mStartData, 10f, 999999f, 1f)
            binding.contentLabel.text = "${mStartData.toInt()}"
        } else if (mType == 3) {
            //三围
            binding.titleLabel.text = "三围"
            binding.unitLabel.text = "cm"
            binding.rulerView.setValue(mStartData, 20f, 200f, 1f)
            binding.contentLabel.text = "$mStartData"
        }

        binding.rulerView.setOnValueChangeListener(object :
            RulerView.OnValueChangeListener {
            override fun onValueChange(value: Float) {
                if (mType == 1) {
                    binding.contentLabel.text = "${value.toInt()}"
                } else if (mType == 3) {
                    binding.contentLabel.text = "${value.toInt()}"
                }

                mStartData = value
            }
        })


        binding.cancelBtn.setOnClickListener {
            dismiss()
        }

        binding.sureBtn.setOnClickListener {
            if (mType == 1) {
                 callback?.invoke(mStartData)
            } else if (mType == 3) {
                callback?.invoke(mStartData)
            }

            dismiss()
        }

    }


//    /**
//     * 设置弹框宽度全屏
//     */
//    override fun onStart() {
//        super.onStart()
//        val win = dialog!!.window
//        // 一定要设置Background，如果不设置，window属性设置无效
//        win!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        val dm = DisplayMetrics()
//        requireActivity().windowManager.defaultDisplay.getMetrics(dm)
//        val params = win.attributes
//        params.gravity = Gravity.BOTTOM
//        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
//        params.width = ViewGroup.LayoutParams.MATCH_PARENT
//        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
//        win.attributes = params
//
////        isCancelable = true
//    }

    interface AlertListener {
        fun sureClick(value: Float) {}
        fun sureClick(value: Int) {}
    }


}