package com.assistant.bases

import android.app.Dialog
import android.content.Context
import android.view.Display
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding


abstract class BaseDialog<VB : ViewDataBinding>(var context: Context) {
    val defaultStyle:Int= R.style.common_dialog_style
    private val display: Display
    private var dialog : Dialog? = null
    abstract val layoutId: Int
    abstract val TAG: String

    protected abstract val dialogStyleId: Int
    protected abstract val isCanceledTouch: Boolean
    protected abstract val isCanceledReturn: Boolean
    protected lateinit var binding: VB

    init {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),layoutId, null, false)
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        display = windowManager.defaultDisplay
        dialog = if (dialogStyleId == 0) {
            Dialog(context, defaultStyle)
        } else {
            Dialog(context, dialogStyleId)
        }

        dialog?.apply {
            setContentView(binding.root)
            setCanceledOnTouchOutside(isCanceledTouch)
            setCancelable(isCanceledReturn)
            //隐藏系统输入盘
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        }
        initViewEvent()
    }
    protected open fun initViewEvent() {}

    fun show() {
        dialog!!.show()
    }

    fun dismiss() {
        dialog!!.dismiss()
    }

    //设置尺寸
    fun setDialogSize(width:Int,height:Int){
        //DeviceUtil.dip2px(context, 488f), DeviceUtil.dip2px(context, 800f)
        dialog?.apply {
            window?.setLayout(width,height)
            window?.decorView?.setPadding(0, 0, 0, 0)
        }
    }

    //设置背景色
    fun setDialogBackground(res: Int){
        dialog?.window?.decorView?.setBackgroundResource(res)
    }

    val isShowing: Boolean
        get() = dialog!!.isShowing
}