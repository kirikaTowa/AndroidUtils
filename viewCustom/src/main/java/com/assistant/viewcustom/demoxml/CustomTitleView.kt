package com.assistant.viewcustom.demoxml

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.assistant.viewcustom.R

class CustomTitleView constructor(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet, 0) {

    private var titleLabel: AppCompatTextView
    private var btnLabel: AppCompatTextView

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_custom_title_view, this)

        btnLabel = view.findViewById(R.id.tv_to_record_button)
        val textView = view.findViewById<AppCompatTextView>(R.id.tv_to_record)
        titleLabel = textView
//
        val obtain = context.obtainStyledAttributes(attributeSet, R.styleable.CustomTitleView)
        textView.text = obtain.getString(R.styleable.CustomTitleView_cus_title_text)


        obtain.recycle()

        initClick()
        setRecordText()
    }

    private fun initClick() {

    }

    private fun setRecordText(text: String = "你好") {
        titleLabel.text = text
        if (text == "") {
            this.visibility = View.GONE
        } else {
            this.visibility = View.VISIBLE
        }
    }

    fun setTextColor(color: Int) {
        titleLabel.setTextColor(color)
    }
}