package com.kakusummer.sample.dialog.rulerview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.Scroller
import com.kakusummer.androidutils.R
import kotlin.math.abs
import kotlin.math.roundToInt


/**
 * Created by Android Studio.
 * author: yhs
 * Date: 2023/5/11
 * Time: 11:33
 * 备注：
 */


open class RulerView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    View(context, attrs, defStyleAttr) {
    private var mMinVelocity = 0
    private var mScroller //Scroller是一个专门用于处理滚动效果的工具类   用mScroller记录/计算View滚动的位置，再重写View的computeScroll()，完成实际的滚动
            : Scroller? = null
    private var mVelocityTracker //主要用跟踪触摸屏事件（flinging事件和其他gestures手势事件）的速率。
            : VelocityTracker? = null
    private var mWidth = 0
    private var mHeight = 0
    private var mSelectorValue = 50.0f // 未选择时 默认的值 滑动后表示当前中间指针正在指着的值
    private var mMaxValue = 200f // 最大数值
    private var mMinValue = 100.0f //最小的数值
    private var mPerValue = 1f //最小单位  如 1:表示 每2条刻度差为1.   0.1:表示 每2条刻度差为0.1

    // 在demo中 身高mPerValue为1  体重mPerValue 为0.1
    private var mLineSpaceWidth = 5f //  尺子刻度2条线之间的距离
    private var mLineWidth = 4f //  尺子刻度的宽度
    private var mLineMaxHeight = 420f //  尺子刻度分为3中不同的高度。 mLineMaxHeight表示最长的那根(也就是 10的倍数时的高度)
    private var mLineMidHeight = 30f //  mLineMidHeight  表示中间的高度(也就是 5  15 25 等时的高度)
    private var mLineMinHeight = 17f //  mLineMinHeight  表示最短的那个高度(也就是 1 2 3 4 等时的高度)
    private var mTextMarginTop = 10f //o
    private var mTextSize = 30f //尺子刻度下方数字 textsize
    private var mAlphaEnable = false // 尺子 最左边 最后边是否需要透明 (透明效果更好点)
    private var mTextHeight //尺子刻度下方数字  的高度
            = 0f
    private var mTextPaint // 尺子刻度下方数字( 也就是每隔10个出现的数值) paint
            : Paint? = null
    private var mLinePaint //  尺子刻度  paint
            : Paint? = null
    private var mTotalLine //共有多少条 刻度
            = 0
    private var mMaxOffset //所有刻度 共有多长
            = 0
    private var mOffset // 默认状态下，mSelectorValue所在的位置  位于尺子总刻度的位置
            = 0f
    private var mLastX = 0
    private var mMove = 0
    private var mListener // 滑动后数值回调
            : OnValueChangeListener? = null
    private var mLineColor: Int = Color.GRAY //刻度的颜色
    private var mTextColor: Int = Color.BLACK //文字的颜色

    constructor(context: Context) : this(context, null) {}
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {}

    init {
        init(context, attrs)
    }

    fun init(context: Context, attrs: AttributeSet?) {
        Log.d("zkk---", "init")
        mScroller = Scroller(context)
        mLineSpaceWidth = myfloat(25.0f).toFloat()
        mLineWidth = myfloat(2.0f).toFloat()
        mLineMaxHeight = myfloat(100.0f).toFloat()
        mLineMidHeight = myfloat(60.0f).toFloat()
        mLineMinHeight = myfloat(40.0f).toFloat()
        mTextHeight = myfloat(40.0f).toFloat()
        val typedArray:TypedArray = context.obtainStyledAttributes(attrs, R.styleable.RulerView)

        mAlphaEnable = typedArray.getBoolean(R.styleable.RulerView_ruler_alphaEnable, mAlphaEnable)
        mLineSpaceWidth =
            typedArray.getDimension(R.styleable.RulerView_ruler_lineSpaceWidth, mLineSpaceWidth)
        mLineWidth = typedArray.getDimension(R.styleable.RulerView_ruler_lineWidth, mLineWidth)
        mLineMaxHeight =
            typedArray.getDimension(R.styleable.RulerView_ruler_lineMaxHeight, mLineMaxHeight)
        mLineMidHeight =
            typedArray.getDimension(R.styleable.RulerView_ruler_lineMidHeight, mLineMidHeight)
        mLineMinHeight =
            typedArray.getDimension(R.styleable.RulerView_ruler_lineMinHeight, mLineMinHeight)
        mLineColor = typedArray.getColor(R.styleable.RulerView_ruler_lineColor, mLineColor)
        mTextSize = typedArray.getDimension(R.styleable.RulerView_ruler_textSize, mTextSize)
        mTextColor = typedArray.getColor(R.styleable.RulerView_ruler_textColor, mTextColor)
        mTextMarginTop =
            typedArray.getDimension(R.styleable.RulerView_ruler_textMarginTop, mTextMarginTop)
        mSelectorValue = typedArray.getFloat(R.styleable.RulerView_ruler_selectorValue, 0.0f)
        mMinValue = typedArray.getFloat(R.styleable.RulerView_ruler_minValue, 0.0f)
        mMaxValue = typedArray.getFloat(R.styleable.RulerView_ruler_maxValue, 100.0f)
        mPerValue = typedArray.getFloat(R.styleable.RulerView_ruler_perValue, 0.1f)
        mMinVelocity = ViewConfiguration.get(getContext()).scaledMinimumFlingVelocity
        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint!!.textSize = mTextSize
        mTextPaint!!.color = mTextColor
        mTextHeight = getFontHeight(mTextPaint)
        mLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mLinePaint!!.strokeWidth = mLineWidth
        mLinePaint!!.color = mLineColor
        typedArray.recycle()

         setValue(140f, 120f, 200f, 0.1f);
    }

    private fun getFontHeight(paint: Paint?): Float {
        val fm: Paint.FontMetrics = paint!!.fontMetrics
        return fm.descent - fm.ascent
    }

    /**
     *
     * @param selectorValue 未选择时 默认的值 滑动后表示当前中间指针正在指着的值
     * @param minValue   最大数值
     * @param maxValue   最小的数值
     * @param per   最小单位  如 1:表示 每2条刻度差为1.   0.1:表示 每2条刻度差为0.1 在demo中 身高mPerValue为1  体重mPerValue 为0.1
     */
    fun setValue(selectorValue: Float, minValue: Float, maxValue: Float, per: Float) {
        mSelectorValue = selectorValue
        mMaxValue = maxValue
        mMinValue = minValue
        mPerValue = (per * 10.0f).toInt().toFloat()
        mTotalLine = ((mMaxValue * 10 - mMinValue * 10) / mPerValue).toInt() + 1
        mMaxOffset = (-(mTotalLine - 1) * mLineSpaceWidth).toInt()
        mOffset = (mMinValue - mSelectorValue) / mPerValue * mLineSpaceWidth * 10
        Log.d(
            "zkk===", "mOffset--           " + mOffset + "         =====mMaxOffset    " + mMaxOffset
                    + "  mTotalLine  " + mTotalLine
        )
        invalidate()
        visibility = VISIBLE
    }

    fun setOnValueChangeListener(listener: OnValueChangeListener?) {
        mListener = listener
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0) {
            mWidth = w
            mHeight = h
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var left: Float
        var height: Float
        var value: String
        var alpha = 0
        var scale: Float
        val srcPointX = mWidth / 2
        for (i in 0 until mTotalLine) {
            left = srcPointX + mOffset + i * mLineSpaceWidth
            if (left < 0 || left > mWidth) {
                continue  //  先画默认值在正中间，左右各一半的view。  多余部分暂时不画(也就是从默认值在中间，画旁边左右的刻度线)
            }
            height = if (i % 10 == 0) {
                mLineMaxHeight
            } else if (i % 5 == 0) {
                mLineMidHeight
            } else {
                mLineMinHeight
            }
            if (mAlphaEnable) {
                scale = 1 - abs(left - srcPointX) / srcPointX
                alpha = (255 * scale * scale).toInt()
                mLinePaint!!.alpha = alpha
            }
            canvas.drawLine(left, 0f, left, height, mLinePaint!!)
            if (i % 10 == 0) {
                value = (mMinValue + i * mPerValue / 10).toInt().toString()
                if (mAlphaEnable) {
                    mTextPaint!!.alpha = alpha
                }
                canvas.drawText(
                    value, left - mTextPaint!!.measureText(value) / 2,
                    height + mTextMarginTop + mTextHeight, mTextPaint!!
                ) // 在为整数时,画 数值

                Log.d("canvas.drawText","$value   $mTextMarginTop")
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d("zkk---", "onTouchEvent-")
        val action = event.action
        val xPosition = event.x.toInt()
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker!!.addMovement(event)
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mScroller!!.forceFinished(true)
                mLastX = xPosition
                mMove = 0
            }
            MotionEvent.ACTION_MOVE -> {
                mMove = mLastX - xPosition
                changeMoveAndValue()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                countMoveEnd()
                countVelocityTracker()
                return false
            }
            else -> {}
        }
        mLastX = xPosition
        return true
    }

    private fun countVelocityTracker() {
        Log.d("zkk---", "countVelocityTracker-")
        mVelocityTracker!!.computeCurrentVelocity(1000) //初始化速率的单位
        val xVelocity = mVelocityTracker!!.xVelocity //当前的速度
        if (abs(xVelocity) > mMinVelocity) {
            mScroller!!.fling(0, 0, xVelocity.toInt(), 0, Int.MIN_VALUE, Int.MAX_VALUE, 0, 0)
        }
    }

    /**
     * 滑动结束后，若是指针在2条刻度之间时，改变mOffset 让指针正好在刻度上。
     */
    private fun countMoveEnd() {
        mOffset -= mMove.toFloat()
        if (mOffset <= mMaxOffset) {
            mOffset = mMaxOffset.toFloat()
        } else if (mOffset >= 0) {
            mOffset = 0f
        }
        mLastX = 0
        mMove = 0
        mSelectorValue =
            mMinValue + (abs(mOffset) * 1.0f / mLineSpaceWidth).roundToInt() * mPerValue / 10.0f
        mOffset = (mMinValue - mSelectorValue) * 10.0f / mPerValue * mLineSpaceWidth
        notifyValueChange()
        postInvalidate()
    }

    /**
     * 滑动后的操作
     */
    private fun changeMoveAndValue() {
        mOffset -= mMove.toFloat()
        if (mOffset <= mMaxOffset) {
            mOffset = mMaxOffset.toFloat()
            mMove = 0
            mScroller!!.forceFinished(true)
        } else if (mOffset >= 0) {
            mOffset = 0f
            mMove = 0
            mScroller!!.forceFinished(true)
        }
        mSelectorValue =
            mMinValue + Math.round(Math.abs(mOffset) * 1.0f / mLineSpaceWidth) * mPerValue / 10.0f
        notifyValueChange()
        postInvalidate()
    }

    private fun notifyValueChange() {
        if (null != mListener) {
            mListener!!.onValueChange(mSelectorValue)
        }
    }

    /**
     * 滑动后的回调
     */
    interface OnValueChangeListener {
        fun onValueChange(value: Float)
    }

    override fun computeScroll() {
        Log.d("zkk---", "computeScroll-")
        super.computeScroll()
        if (mScroller!!.computeScrollOffset()) {     //mScroller.computeScrollOffset()返回 true表示滑动还没有结束
            if (mScroller!!.currX == mScroller!!.finalX) {
                countMoveEnd()
            } else {
                val xPosition = mScroller!!.currX
                mMove = mLastX - xPosition
                changeMoveAndValue()
                mLastX = xPosition
            }
        }
    }

    companion object {
        fun myfloat(paramFloat: Float): Int {
            return (0.5f + paramFloat * 1.0f).toInt()
        }
    }
}