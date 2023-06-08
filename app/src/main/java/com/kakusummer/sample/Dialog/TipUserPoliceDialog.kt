package com.kakusummer.sample.Dialog

import android.content.Context
import com.assistant.bases.BaseDialog
import com.kakusummer.androidutils.R
import com.kakusummer.androidutils.databinding.DialogStatementBinding


class TipUserPoliceDialog
    (context: Context?, var callback: ((Boolean) -> Unit)?) :
    BaseDialog<DialogStatementBinding>(context!!) {


    override val dialogStyleId: Int
        get() = defaultStyle
    override val isCanceledTouch: Boolean
        get() = false
    override val isCanceledReturn: Boolean
        get() = false
    override val layoutId: Int
        get() = R.layout.dialog_statement

    //View的事件
    override fun initViewEvent() {
        //设置对话框那个叉叉的方法，点击关闭对话框
        binding.apply {
            tvYes.setOnClickListener {
                callback?.invoke(true)
            }

            tvNo.setOnClickListener {
                callback?.invoke(false)
            }


//            val stringContext = context.resources.getString(R.string.str_statement_content)
//          /*  val colorSpan =
//                ForegroundColorSpan(ContextCompat.getColor(context, R.color.color_effect_180))*/
//
//            val stringProtocolOne = context.resources.getString(R.string.str_protocol_one)
//            val positionOne = stringContext.indexOf(stringProtocolOne)
//            val spannableBuilder = SpannableStringBuilder(stringContext)
//         /*   // 单独设置字体颜色
//
//            spannableBuilder.setSpan(
//                colorSpan,
//                positionOne,
//                positionOne+stringProtocolOne.length,
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//            )*/
//            // 单独设置点击事件
//            val clickableSpanOne: ClickableSpan = object : ClickableSpan() {
//
//                override fun onClick(widget: View) {
//                    //(context as Activity).overridePendingTransition(R.anim.anim_in_up, R.anim.anim_origin)
//                }
//
//                override fun updateDrawState(ds: TextPaint) {
//                    /**set textColor**/
//                    ds.color = ds.linkColor
//                    /**Remove the underline**/
//                    ds.isUnderlineText = false
//                }
//            }
//
//            spannableBuilder.setSpan(clickableSpanOne,    positionOne,
//                positionOne+stringProtocolOne.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//
//            val stringProtocolTwo = context.resources.getString(R.string.str_protocol_two)
//            val positionTwo = stringContext.indexOf(stringProtocolTwo)
//            // 单独设置点击事件
//            val clickableSpanTwo: ClickableSpan = object : ClickableSpan() {
//
//                override fun onClick(widget: View) {
//                    //(context as Activity).overridePendingTransition(R.anim.anim_in_up, R.anim.anim_origin)
//                }
//
//                override fun updateDrawState(ds: TextPaint) {
//                    /**set textColor**/
//                    ds.color = ds.linkColor
//                    /**Remove the underline**/
//                    ds.isUnderlineText = false
//                }
//            }
//
//            spannableBuilder.setSpan(clickableSpanOne,    positionOne,
//                positionOne+stringProtocolOne.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//
//            spannableBuilder.setSpan(clickableSpanTwo,    positionTwo,
//                positionTwo+stringProtocolTwo.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            tvContent.movementMethod = LinkMovementMethod.getInstance()
//            tvContent.highlightColor = Color.TRANSPARENT
//            tvContent.text = spannableBuilder;
        }
    }
}