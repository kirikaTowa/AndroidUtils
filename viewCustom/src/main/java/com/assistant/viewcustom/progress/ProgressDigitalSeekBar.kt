package com.assistant.viewcustom.progress

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.assistant.viewcustom.R
import kotlin.math.floor

@SuppressLint("Recycle")
class ProgressDigitalSeekBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {
    /**
     * 进度条背景路径
     * 进度条前景路径
     * 进度条背景
     * 进度条前景
     * 进度条高度
     * 目标进度条绘测路线
     * 目标进度
     */
    private var pathProgressBg: Path? = null
    private var pathProgressFg: Path? = null
    private var colorProgressBg = Color.GRAY
    private var colorProgressFg = Color.BLUE
    private var heightProgress = 5f
    private var measurePath: PathMeasure? = null
    private var progressDigital = 0.5f


    /**
     * 绘制进度条的画笔
     * 绘制显示进度的圆角矩形的画笔
     * 绘制显示进度文字的画笔
     */
    private var paintProgress: Paint? = null
    private var paintRoundRect: Paint? = null
    private var paintText: Paint? = null


    /**
     * 拖拽的圆角矩形的背景颜色
     * 圆角矩形的圆角半径
     * 显示进度的圆角矩形
     * 进度条文字大小
     * 获取画笔绘制文字的参数
     * 绘制文字的颜色
     * 进度的文字与圆角矩形垂直方向的边距
     * 进度的文字与圆角矩形水平方向的边距
     * 绘制的文字的最大值（用于确定显示进度的矩形的宽高）
     * 是否支持拖动
     */
    private var colorRectBg = Color.WHITE
    private var radiusRect = 10f
    private var rectFRect: RectF? = null
    private var sizeText = 30f
    private var fontMetricsInt: FontMetricsInt? = null
    private var colorText = Color.BLUE
    private var marginTextV = 8f
    private var marginTextH = 8f
    private val rateMaxText = "100%"
    private var canSeek = true


    init {
        //读取属性
        var typedArray: TypedArray? = null
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressDigitalSeekBar)
        }

        typedArray?.apply {
            colorProgressBg =
                getColor(R.styleable.ProgressDigitalSeekBar_sk_colorProgressBg, colorProgressBg)
            colorProgressFg =
                getColor(R.styleable.ProgressDigitalSeekBar_sk_colorProgressFg, colorProgressFg)
            heightProgress =
                getDimension(R.styleable.ProgressDigitalSeekBar_sk_heightProgress, heightProgress)
            colorRectBg = getColor(R.styleable.ProgressDigitalSeekBar_sk_colorRectBg, colorRectBg)
            radiusRect = getDimension(R.styleable.ProgressDigitalSeekBar_sk_radiusRect, radiusRect)
            sizeText = getDimension(R.styleable.ProgressDigitalSeekBar_sk_sizeText, sizeText)
            colorText = getColor(R.styleable.ProgressDigitalSeekBar_sk_colorText, colorText)
            marginTextV =
                getDimension(R.styleable.ProgressDigitalSeekBar_sk_marginTextV, marginTextV)
            canSeek = getBoolean(R.styleable.ProgressDigitalSeekBar_sk_canSeek, canSeek)
        }


        //声明进度条画笔  背景和前景两条线都靠这个实现
        paintProgress = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            isAntiAlias = true
            strokeCap = Paint.Cap.ROUND //设置线头为圆角
            style = Paint.Style.STROKE //设置绘制样式为线条
            strokeJoin = Paint.Join.ROUND //设置拐角为圆角
            strokeWidth = heightProgress
        }

        //声明右端矩形画笔
        paintRoundRect = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = colorRectBg
        }

        //声明右端矩文字画笔
        paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            isAntiAlias = true
            strokeWidth = 1f
            style = Paint.Style.FILL
            color = colorText
            textSize = sizeText //设置字体大小
            textAlign = Paint.Align.CENTER //将文字水平居中
        }

        //获取文字画笔的字体属性
        fontMetricsInt = paintText!!.fontMetricsInt

        //声明前景和背景线
        pathProgressBg = Path()
        pathProgressFg = Path()
        measurePath = PathMeasure()
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
        pathProgressBg?.also {
            it.moveTo(heightProgress / 2, h / 2f)
            it.lineTo(w - heightProgress / 2, h / 2f)
        }
        //将进度条路径设置给PathMeasure
        measurePath!!.setPath(pathProgressBg, false)
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
        if (canSeek) {
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
            (sizeText + marginTextV * 2).toInt()
        }
    }

    //供外界调用
    fun setProgress(progress: Float) {
        updateProgress(progress, false)
    }

    //内部使用
    private fun updateOnTouch(event: MotionEvent) {
        //计算滑动后的进度
        val positionX: Float = if (event.x > measurePath!!.length) {
            1F
        } else if (event.x < 0) {
            0F
        } else {
            event.x / measurePath!!.length
        }

        updateProgress(positionX, true)
    }

    //设置进度及刷新
    private fun updateProgress(progress: Float, fromUser: Boolean) {
        this.progressDigital = progress
        //重刷后
        invalidate()
    }

    private fun drawShowProgressRoundRect(canvas: Canvas) {
        var stop = measurePath!!.length * progressDigital //计算进度条的进度
        //根据要绘制的文字的最大长宽来计算要绘制的圆角矩形的长宽
        val rect = Rect()
        paintText!!.getTextBounds(rateMaxText, 0, rateMaxText.length, rect)
        //要绘制矩形的宽、高
        val rectWidth = rect.width() + marginTextH * 2
        val rectHeight = rect.height() + marginTextV * 2
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
        rectFRect = RectF(left, top, right, bottom)
        //绘制为圆角矩形
        canvas.drawRoundRect(
            rectFRect!!, radiusRect, radiusRect,
            paintRoundRect!!
        )
    }

    private fun drawProgressText(canvas: Canvas) {
        //向下取整
        val progressText = floor((100 * progressDigital).toDouble()).toInt().toString() + "%"
        //让文字垂直居中的偏移
        val offsetY =
            (fontMetricsInt!!.bottom - fontMetricsInt!!.ascent) / 2 - fontMetricsInt!!.bottom
        //将文字绘制在矩形的中央
        canvas.drawText(
            progressText,
            rectFRect!!.centerX(),
            rectFRect!!.centerY() + offsetY,
            paintText!!
        )
    }

    private fun drawProgress(canvas: Canvas) {
        pathProgressFg!!.reset()
        paintProgress?.also {
            //计算进度条的进度
            val stop = measurePath!!.length * progressDigital

            //绘制背景色 俩叠一块可能过度绘制 目前问题不大
            //mPathMeasure!!.getSegment(stop, 1F, mPathProgressBg, true)
            it.color = colorProgressBg
            canvas.drawPath(pathProgressBg!!, it)

            //绘制进度条前景色
            // 得到与进度对应的路径 mPathMeasure.getSegment(startD, stopD, mDstPath, true); : 根据传入的起始值和终止值（相当于要截取路径的部分），将路径赋值给mDstPath
            measurePath!!.getSegment(0F, stop, pathProgressFg, true)
            it.color = colorProgressFg
            canvas.drawPath(pathProgressFg!!, it)
        }
    }
}