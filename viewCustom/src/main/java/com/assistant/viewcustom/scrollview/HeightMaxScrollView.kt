package com.assistant.viewcustom.scrollview

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView
import com.assistant.viewcustom.R

class HeightMaxScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ScrollView(context, attrs) {
    var mMaxHeight = 0 //提供设置最大高度的属性

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.HeightMaxScrollView)
        mMaxHeight =
            typedArray.getLayoutDimension(R.styleable.HeightMaxScrollView_maxHeight, mMaxHeight)
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightSpec = heightMeasureSpec
        if (mMaxHeight > 0) {//如果最大属性存在则将高度设置为最大
            heightSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST)
        }
        super.onMeasure(widthMeasureSpec, heightSpec)
    }
}

