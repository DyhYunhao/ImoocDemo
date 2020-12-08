package com.dyh.bizzer.iview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.dyh.bizzer.R

/**
 * describe: 三阶贝塞尔曲线
 * create by daiyh on 2020-12-8
 */
class Bezier3 : View {

    private var mCustomSize: Int = 0
    private var mCustomColor: Int = 0
    private lateinit var mCustomPaint: Paint
    private lateinit var mCustomCirclePaint: Paint
    private var mSize: Int = 0
    private var height: Int? = 0
    private var weight: Int = 0
    private var startX: Int = 0
    private var startY: Int = 0
    private var endX: Int = 0
    private var endY: Int = 0
    private var eventLeftX: Int = 0
    private var eventLeftY: Int = 0
    private var eventRightX: Int = 0
    private var eventRightY: Int = 0
    private lateinit var mCustomLinePaint: Paint
    private var isClean: Boolean = false
    private var isMoveLeft: Boolean = true

    constructor(context: Context?, attrs: AttributeSet) : super(context, attrs) {
        initAttr(context, attrs)
        initPaint()
    }

    private fun initPaint() {
        mCustomPaint = Paint()
        mCustomPaint.color = mCustomColor
        mCustomPaint.isAntiAlias = true
        mCustomPaint.strokeWidth = 20f
        mCustomPaint.style = Paint.Style.STROKE

        mCustomCirclePaint = Paint()
        mCustomCirclePaint.color = Color.parseColor("#ffffff")
        mCustomCirclePaint.isAntiAlias = true
        mCustomCirclePaint.strokeWidth = 17f
        mCustomCirclePaint.style = Paint.Style.STROKE

        mCustomLinePaint = Paint()
        mCustomLinePaint.color = Color.parseColor("#ffffff")
        mCustomLinePaint.isAntiAlias = true
        mCustomLinePaint.strokeWidth = 13f
    }

    @SuppressLint("CustomViewStyleable")
    private fun initAttr(context: Context?, attrs: AttributeSet) {
        val typedArray = context!!.obtainStyledAttributes(attrs, R.styleable.BaseBezier3)
        mCustomColor = typedArray.getColor(R.styleable.BaseBezier3_custom_color3, mCustomColor)
        mCustomSize = typedArray.getDimension(
            R.styleable.BaseBezier3_custom_size3,
            mCustomSize.toFloat()
        ).toInt()
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        weight = MeasureSpec.getSize(widthMeasureSpec)
        height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(weight, height!!)
        startX = weight / 2 - 500
        startY = height!! / 2
        endX = weight / 2 + 500
        endY = height!! / 2
        eventLeftX = weight / 2 / 2
        eventLeftY = height!! / 2 - 500
        eventRightX = weight / 4 * 3
        eventRightY = height!! / 2 - 500
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                if (isMoveLeft) {
                    eventLeftX = event.x.toInt()
                    eventLeftY = event.y.toInt()
                } else {
                    eventRightX = event.x.toInt()
                    eventRightY = event.y.toInt()
                }
                postInvalidate()
            }
        }
        return true
    }

    //移动左控制点
    fun moveLeft() {
        isMoveLeft = true
    }

    //移动右控制点
    fun moveRight() {
        isMoveLeft = false
    }

    fun cleanSub() {
        if (onIsCleanSubListener != null) {
            onIsCleanSubListener!!.onCleanSub(isClean)
        }
        isClean = false
        mCustomLinePaint.alpha = 255
        postInvalidate()
    }

    fun Sub() {
        if (onIsCleanSubListener != null) {
            onIsCleanSubListener!!.onCleanSub(isClean)
        }
        isClean = true
        mCustomLinePaint.alpha = 0
        postInvalidate()
    }

    /**
     * 是否清除辅助线
     */
    fun isCleanSub() {
        if (isClean) {
            cleanSub()
        } else {
            Sub()
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        var path = Path()
        path.moveTo(startX.toFloat(), startY.toFloat())
        path.cubicTo(
            eventLeftX.toFloat(),
            eventLeftY.toFloat(),
            eventRightX.toFloat(),
            eventRightY.toFloat(),
            endX.toFloat(),
            endY.toFloat()
        )
        canvas!!.drawPath(path, mCustomPaint)


        canvas.drawCircle(startX.toFloat(), startY.toFloat(), 25f, mCustomCirclePaint) // 左固定点圆
        canvas.drawCircle(endX.toFloat(), endY.toFloat(), 25f, mCustomCirclePaint) //右固定点圆

        canvas.drawCircle(eventLeftX.toFloat(), eventLeftY.toFloat(), 25f, mCustomCirclePaint) //控制点圆
        canvas.drawCircle(eventRightX.toFloat(), eventRightY.toFloat(), 25f, mCustomCirclePaint) //控制点圆

        canvas.drawLine(startX.toFloat(), startY.toFloat(), eventLeftX.toFloat(), eventLeftY.toFloat(), mCustomLinePaint)//路径连线
        canvas.drawLine(eventRightX.toFloat(), eventRightY.toFloat(), endX.toFloat(), endY.toFloat(), mCustomLinePaint)//路径连线
        canvas.drawLine(eventLeftX.toFloat(), eventLeftY.toFloat(), eventRightX.toFloat(), eventRightY.toFloat(), mCustomLinePaint)
        path.reset()

    }

    /**
     * 暴露接口
     */
    var onIsCleanSubListener: onIsCleanSubListener3? = null

    interface onIsCleanSubListener3 {
        fun onCleanSub(isClean: Boolean?)
    }

    fun setOnIsCleanSubListener3(onIsCleanSubListener: onIsCleanSubListener3?) {
        this.onIsCleanSubListener = onIsCleanSubListener
    }
}