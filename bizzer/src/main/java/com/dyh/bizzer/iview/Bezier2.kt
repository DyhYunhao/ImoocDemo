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
 * describe:基础的二阶贝塞尔曲线
 * create by daiyh on 2020-12-7
 */
class Bezier2: View{

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
    private var eventX: Int = 0
    private var eventY: Int = 0
    private lateinit var mCustomLinePaint: Paint
    private var isClean: Boolean = false

    constructor(context: Context?, attrs: AttributeSet): super(context, attrs) {
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
        val typedArray = context!!.obtainStyledAttributes(attrs, R.styleable.BaseBezier2)
        mCustomColor = typedArray.getColor(R.styleable.BaseBezier2_custom_color2, mCustomColor)
        mCustomSize = typedArray.getDimension(
            R.styleable.BaseBezier2_custom_size2,
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
        eventX = weight / 2
        eventY = height!! / 2 - 500
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        var path = Path()
        path.reset()
        path.moveTo(startX.toFloat(), startY.toFloat())
        path.quadTo(eventX.toFloat(), eventY.toFloat(), endX.toFloat(), endY.toFloat())
        canvas!!.drawPath(path, mCustomPaint)

        canvas.drawCircle(startX.toFloat(), startY.toFloat(), 25f, mCustomCirclePaint) // 左固定点圆
        canvas.drawCircle(endX.toFloat(), endY.toFloat(), 25f, mCustomCirclePaint) //右固定点圆
        canvas.drawCircle(eventX.toFloat(), eventY.toFloat(), 25f, mCustomCirclePaint) //控制点圆

        canvas.drawLine(startX.toFloat(), startY.toFloat(), eventX.toFloat(), eventY.toFloat(), mCustomLinePaint)//路径连线
        canvas.drawLine(eventX.toFloat(), eventY.toFloat(), endX.toFloat(), endY.toFloat(), mCustomLinePaint)//路径连线
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event!!.action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                eventX = event.x.toInt()
                eventY = event.y.toInt()
                postInvalidate()
            }
        }
        return true
    }

    fun Sub() {
        if (onIsCleanSubListener != null) {
            onIsCleanSubListener!!.onCleanSub(isClean)
        }
        isClean = true
        mCustomLinePaint.alpha = 0
        postInvalidate()
    }

    fun cleanSub() {
        if (onIsCleanSubListener != null) {
            onIsCleanSubListener!!.onCleanSub(isClean)
        }
        isClean = false
        mCustomLinePaint.alpha = 255
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

    /**
     * 暴露接口
     */
    private var onIsCleanSubListener: onIsCleanSubListener2? = null

    interface onIsCleanSubListener2 {
        fun onCleanSub(isClean: Boolean?)
    }

    fun setOnIsCleanSubListener(onIsCleanSubListener: onIsCleanSubListener2?) {
        this.onIsCleanSubListener = onIsCleanSubListener
    }

}