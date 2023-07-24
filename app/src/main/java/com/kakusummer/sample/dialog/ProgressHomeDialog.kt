package com.kakusummer.sample.dialog

import android.content.Context
import com.assistant.bases.BaseDialog
import com.kakusummer.androidutils.R
import com.kakusummer.androidutils.databinding.DialogProgressHomeBinding
import com.kakusummer.androidutils.databinding.DialogStatementBinding


class ProgressHomeDialog
    (context: Context?, var callback: ((Boolean) -> Unit)?) :
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
        //设置对话框那个叉叉的方法，点击关闭对话框
        binding.apply {


        }
    }
}