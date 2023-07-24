package com.assistant.viewcustom.progress

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.floor

class ProgressDigitalSeek @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {
    //进度条背景路径
    private var mPathProgressBg: Path? = null

    //进度条前景路径
    private var mPathProgressFg: Path? = null

    //绘制进度条的画笔
    private var mPaintProgress: Paint? = null

    //绘制显示进度的圆角矩形的画笔
    private var mPaintRoundRect: Paint? = null

    //绘制显示进度文字的画笔
    private var mPaintProgressText: Paint? = null

    //进度条高度
    private val mProgressHeight = 5f
    private var mPathMeasure: PathMeasure? = null

    //进度条背景
    private val mColorProgressBg = Color.GRAY

    //进度条前景
    private val mColorProgressFg = Color.BLUE

    //拖拽的圆角矩形的背景颜色
    private val mColorSeekGg = Color.WHITE

    //进度条进度
    private var mProgress = 0.5f

    //进度条文字大小
    private val mTextSize = 30f

    //用于获取画笔绘制文字的参数
    private var mFontMetricsInt: FontMetricsInt? = null

    //绘制文字的颜色
    private val mColorProgressText = Color.BLUE

    //显示进度的文字与显示进度的圆角矩形垂直方向的边距
    private val mProgressStrMarginV = 5f

    //显示进度的文字与显示进度的圆角矩形水平方向的边距
    private val mProgressStrMarginH = 10f

    //圆角矩形的圆角半径
    private val mRoundRectRadius = 10f

    //显示进度的圆角矩形（用于判断手指触摸的点是否在它的内部）
    private var mProgressRoundRectF: RectF? = null

    //绘制的文字的最大值（用于确定显示进度的矩形的宽高）
    private val mProgressMaxText = "100%"

    init {
        //声明进度条画笔  背景和前景两条线都靠这个实现
        mPaintProgress = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            isAntiAlias = true
            strokeCap = Paint.Cap.ROUND //设置线头为圆角
            style = Paint.Style.STROKE //设置绘制样式为线条
            strokeJoin = Paint.Join.ROUND //设置拐角为圆角
            strokeWidth = mProgressHeight
        }

        //声明右端矩形画笔
        mPaintRoundRect = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = mColorSeekGg
        }

        //声明右端矩文字画笔
        mPaintProgressText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            isAntiAlias = true
            strokeWidth = 1f
            style = Paint.Style.FILL
            color = mColorProgressText
            textSize = mTextSize //设置字体大小
            textAlign = Paint.Align.CENTER //将文字水平居中
        }

        //获取文字画笔的字体属性
        mFontMetricsInt = mPaintProgressText!!.fontMetricsInt

        //声明前景和背景线
        mPathProgressBg = Path()
        mPathProgressFg = Path()
        mPathMeasure = PathMeasure()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            measureSizeWidth(widthMeasureSpec),
            measureSizeHeight(heightMeasureSpec)
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //进度条绘制在控件中央,宽度为控件宽度(mProgressHeight/2是为了显示出左右两边的圆角)
        mPathProgressBg?.also {
            it.moveTo(mProgressHeight / 2, h / 2f)
            it.lineTo(w - mProgressHeight / 2, h / 2f)
        }
        //将进度条路径设置给PathMeasure
        mPathMeasure!!.setPath(mPathProgressBg, false)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制进度条
        drawProgress(canvas)
        //绘制进度显示的圆角矩形
        drawShowProgressRoundRect(canvas)
        //绘制进度
        drawProgressText(canvas)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        //如果打开了手势监听
        if (true) {
            this.parent.requestDisallowInterceptTouchEvent(true)

            when (event.action) {
                //ACTION_DOWN和ACTION_MOVE抽一个共同调用的方法，根据x算  可以做到
                MotionEvent.ACTION_DOWN -> {
                    updateOnTouch(event)
                }

                MotionEvent.ACTION_MOVE -> {
                    //计算横向移动的距离
                    updateOnTouch(event)
                }

                MotionEvent.ACTION_UP -> {
                    this.parent.requestDisallowInterceptTouchEvent(false)

                }

                MotionEvent.ACTION_CANCEL -> {

                    this.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            return true
        }
        return false
    }

    //计算宽度
    private fun measureSizeWidth(size: Int): Int {
        val mode = MeasureSpec.getMode(size)
        val s = MeasureSpec.getSize(size)
        return if (mode == MeasureSpec.EXACTLY) {
            s
        } else {
            s.coerceAtMost(200)
        }
    }

    //计算高度
    private fun measureSizeHeight(size: Int): Int {
        val mode = MeasureSpec.getMode(size)
        val s = MeasureSpec.getSize(size)
        return if (mode == MeasureSpec.EXACTLY) {
            s
        } else {
            //自适应模式，返回所需的最小高度
            (mTextSize + mProgressStrMarginV * 2).toInt()
        }
    }

    //供外界调用
    fun setProgress(progress: Float) {
        updateProgress(progress, false)
    }

    //内部使用
    private fun updateOnTouch(event: MotionEvent) {
        //计算滑动后的进度
        val progress = event.x / mPathMeasure!!.length
        //重绘
        updateProgress(progress, true)
    }

    //设置进度及刷新
    private fun updateProgress(progress: Float, fromUser: Boolean) {
        mProgress = progress
        //重刷后
        invalidate()
    }

    private fun drawShowProgressRoundRect(canvas: Canvas) {
        var stop = mPathMeasure!!.length * mProgress //计算进度条的进度
        //根据要绘制的文字的最大长宽来计算要绘制的圆角矩形的长宽
        val rect = Rect()
        mPaintProgressText!!.getTextBounds(mProgressMaxText, 0, mProgressMaxText.length, rect)
        //要绘制矩形的宽、高
        val rectWidth = rect.width() + mProgressStrMarginH * 2
        val rectHeight = rect.height() + mProgressStrMarginV * 2
        //计算边界值（为了不让矩形在左右两边超出边界）
        if (stop < rectWidth / 2f) {
            stop = rectWidth / 2f
        } else if (stop > width - (rectWidth / 2f)) {
            stop = width - rectWidth / 2f
        }
        //定义绘制的矩形
        val left = stop - rectWidth / 2f
        val right = stop + rectWidth / 2f
        val top = height / 2f - rectHeight / 2f
        val bottom = height / 2f + rectHeight / 2f
        mProgressRoundRectF = RectF(left, top, right, bottom)
        //绘制为圆角矩形
        canvas.drawRoundRect(
            mProgressRoundRectF!!, mRoundRectRadius, mRoundRectRadius,
            mPaintRoundRect!!
        )
    }

    private fun drawProgressText(canvas: Canvas) {
        val progressText = floor((100 * mProgress).toDouble()).toInt().toString() + "%"
        //让文字垂直居中的偏移
        val offsetY =
            (mFontMetricsInt!!.bottom - mFontMetricsInt!!.ascent) / 2 - mFontMetricsInt!!.bottom
        //将文字绘制在矩形的中央
        canvas.drawText(
            progressText,
            mProgressRoundRectF!!.centerX(),
            mProgressRoundRectF!!.centerY() + offsetY,
            mPaintProgressText!!
        )
    }

    private fun drawProgress(canvas: Canvas) {
        mPathProgressFg!!.reset()
        mPaintProgress?.also {
            //计算进度条的进度
            val stop = mPathMeasure!!.length * mProgress

            //绘制背景色 俩叠一块可能过度绘制 目前问题不大
            //mPathMeasure!!.getSegment(stop, 1F, mPathProgressBg, true)
            it.color = mColorProgressBg
            canvas.drawPath(mPathProgressBg!!, it)

            //绘制进度条前景色
            // 得到与进度对应的路径 mPathMeasure.getSegment(startD, stopD, mDstPath, true); : 根据传入的起始值和终止值（相当于要截取路径的部分），将路径赋值给mDstPath
            mPathMeasure!!.getSegment(0F, stop, mPathProgressFg, true)
            it.color = mColorProgressFg
            canvas.drawPath(mPathProgressFg!!, it)
        }
    }
}