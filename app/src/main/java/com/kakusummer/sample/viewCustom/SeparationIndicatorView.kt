package com.kakusummer.sample.viewCustom

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.kakusummer.androidutils.R
import com.kakusummer.sample.CommonUtils.dp
import com.kakusummer.sample.CommonUtils.parseColor

/**
 * 自定义血压血糖指示器
 */
class SeparationIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        const val PRESSURE = 0
        const val BLOOD_SUGAR = 1
    }

    private val mSpacePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPathPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPath = Path()
    private val pathEffect = CornerPathEffect(10f)

    private var mPressureType = PRESSURE //类型
    private var mPressureColors = intArrayOf("#00BDFD".parseColor(), "#1F66FE".parseColor(), "#FFC600".parseColor(), "#FA9B46".parseColor(), "#FF733D".parseColor(), "#FF2D3C".parseColor())
    private var mPressureHeight = 24f
    private var mPressureRadius = 10f
    private var mSpaceWidth = 2f //线的宽度
    private var mSpaceColor = Color.WHITE
    private var mLevel = 0 //当前级别
    private val mTriangleHeight = 10.dp //三角形高度
    private val mTriangleWidth = 20.dp //三角形宽度
    private val mTriangleMarginBottom = 5.dp //三角形距离指示器

    init {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PressureView)
        setAttrs(attrs, typedArray)
        initPaint()
    }

    private fun setAttrs(attrs: AttributeSet?, typedArray: TypedArray) {
        attrs?.let {
            val count = typedArray.indexCount
            for (index in 0 until count) {
                when (val attr = typedArray.getIndex(index)) {
                    R.styleable.PressureView_pressureType -> {
                        mPressureType = typedArray.getInt(attr, PRESSURE)
                    }
                    R.styleable.PressureView_pressureColor -> {
                        val colorArrayResId = typedArray.getResourceId(attr, 0)
                        if (colorArrayResId != 0) {
                            mPressureColors = resources.getIntArray(colorArrayResId)
                        }
                    }
                    R.styleable.PressureView_pressureHeight -> {
                        mPressureHeight = typedArray.getDimension(attr, 20f)
                    }
                    R.styleable.PressureView_pressureRadius -> {
                        mPressureRadius = typedArray.getDimension(attr, 10f)
                    }
                    R.styleable.PressureView_pressureSpaceWidth -> {
                        mSpaceWidth = typedArray.getDimension(attr, 2f)
                    }
                    R.styleable.PressureView_pressureSpaceColor -> {
                        mSpaceColor = typedArray.getColor(attr, Color.WHITE)
                    }
                }
            }
        }
        typedArray.recycle()
    }

    private fun initPaint() {
        mSpacePaint.color = mSpaceColor
        mSpacePaint.style = Paint.Style.FILL
        mSpacePaint.strokeWidth = mSpaceWidth

        mPathPaint.style = Paint.Style.FILL_AND_STROKE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawRect(canvas)
        drawTriangle(canvas)
    }

    private fun drawTriangle(canvas: Canvas) {
        val oneWidth = (width - mSpaceWidth * (mPressureColors.size - 1)) / mPressureColors.size
        val triangleBottomX = mLevel * oneWidth + mSpaceWidth * mLevel + oneWidth / 2 //三角形指示器坐标
        mPath.reset()
        mPathPaint.pathEffect = pathEffect
        mPath.moveTo(triangleBottomX, mTriangleHeight)
        mPath.lineTo(triangleBottomX - mTriangleWidth / 2, 0f)
        mPath.lineTo(triangleBottomX + mTriangleWidth / 2, 0f)
        mPath.close()
        mPathPaint.color = mPressureColors[mLevel]
        canvas.drawPath(mPath, mPathPaint)
    }

    private fun drawRect(canvas: Canvas) {
        val oneWidth = (width - mSpaceWidth * (mPressureColors.size - 1)) / mPressureColors.size
        mPressureColors.forEachIndexed { index, color ->
            mPath.reset()
            val leftX = index * oneWidth + mSpaceWidth * index
            val lineX = leftX - mSpaceWidth / 2
            val topY = mTriangleHeight + mTriangleMarginBottom  //进度条y轴坐标
            when (index) {
                0 -> {
                    mPath.addRoundRect(0f, topY, oneWidth, topY + mPressureHeight, floatArrayOf(
                        mPressureRadius, mPressureRadius, 0f, 0f, 0f, 0f,  mPressureRadius, mPressureRadius
                    ), Path.Direction.CW)
                }
                mPressureColors.size - 1 -> {
                    mPath.addRoundRect(leftX, topY, leftX + oneWidth, topY + mPressureHeight, floatArrayOf(
                        0f, 0f,  mPressureRadius, mPressureRadius, mPressureRadius, mPressureRadius ,0f, 0f
                    ), Path.Direction.CW)
                    canvas.drawLine(lineX, topY, lineX, topY + mPressureHeight, mSpacePaint)
                }
                else -> {
                    mPath.addRect(leftX, topY, leftX + oneWidth, topY + mPressureHeight, Path.Direction.CW)
                    canvas.drawLine(lineX, topY, lineX, topY + mPressureHeight, mSpacePaint)
                }
            }
            mPathPaint.color = color
            mPathPaint.pathEffect = null
            canvas.drawPath(mPath, mPathPaint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val measureHeight = (mTriangleHeight + mTriangleMarginBottom + mPressureHeight).toInt()
        val height = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> measureHeight
            else -> measureHeight
        }
        setMeasuredDimension(widthMeasureSpec, height)
    }

    fun setData(shrink: Int = 0, stretch: Int = 0, bloodSugar: Double = 0.0) {
        if (mPressureType == PRESSURE) {
            mLevel = pressureStateLevel(shrink, stretch)
        } else if (mPressureType == BLOOD_SUGAR) {
            mLevel = bloodSugarLevel(bloodSugar)
        }
        invalidate()
    }

    /**
     * shrink: 收缩压
     * stretch: 舒张压
     */
    private fun pressureStateLevel(shrink: Int, stretch: Int): Int {
        return when {
            shrink < 90 && stretch < 60 -> 0
            shrink < 120 && stretch < 80 -> 1
            shrink < 130 && stretch < 90 -> 2
            shrink < 140 && stretch < 100 -> 3
            shrink <= 180 && stretch < 110 -> 4
            else -> 5
        }
    }

    private fun bloodSugarLevel(bloodSugar: Double): Int {
        return when {
            bloodSugar < 4 -> 0
            bloodSugar <= 6.1 -> 1
            else -> 2
        }
    }


}