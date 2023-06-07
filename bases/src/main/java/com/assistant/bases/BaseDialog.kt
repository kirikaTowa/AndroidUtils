package com.assistant.bases

import android.app.Dialog
import android.content.Context
import android.view.Display
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding


abstract class BaseDialog<VB : ViewDataBinding>(
    var context: Context
) {
    val DIALOG_COMMON_STYLE:Int= R.style.common_dialog_style
    private val display: Display
    private var dialog : Dialog? = null
    abstract val layoutId: Int


    protected abstract val dialogStyleId: Int
    protected abstract val stateCanceled: Boolean
    protected lateinit var binding: VB


    init {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),layoutId, null, false)
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        display = windowManager.defaultDisplay
        dialog = if (dialogStyleId == 0) {
            Dialog(context, DIALOG_COMMON_STYLE)
        } else {
            Dialog(context, dialogStyleId)
        }
        // 调整dialog背景大小
        binding.root.layoutParams = FrameLayout.LayoutParams(
            (display.width * 0.8).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        dialog?.setContentView(binding.root)
        //隐藏系统输入盘
        dialog?.setCanceledOnTouchOutside(stateCanceled)
        dialog?.window!!
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        initViewEvent()
    }
    protected open fun initViewEvent() {}

    fun show() {
        dialog!!.show()
    }

    fun dismiss() {
        dialog!!.dismiss()
    }

    val isShowing: Boolean
        get() = dialog!!.isShowing
}