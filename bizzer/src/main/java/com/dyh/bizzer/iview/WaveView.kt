package com.dyh.bizzer.iview

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import com.dyh.bizzer.R

/**
 * describe:自定义View水波纹
 * create by daiyh on 2020-12-7
 */
class WaveView : View {

    private var mWaveViewColor: Int = 0
    private var mPaint: Paint? = null
    private var height: Int? = 0
    private var weight: Int? = 0
    private var offset: Float? = 0f
    private var animator: ValueAnimator? = null
    private var isStart: Boolean = false

    constructor(context: Context?, attrs: AttributeSet): super(context, attrs) {
        initAttr(context, attrs)
        initPaint()
    }

    /**
     * 获取自定义属性
     */
    @SuppressLint("Recycle")
    fun initAttr(context: Context?, attrs: AttributeSet) {
        val typeArray: TypedArray = context!!.obtainStyledAttributes(attrs, R.styleable.WaveView)
        mWaveViewColor = typeArray.getColor(R.styleable.WaveView_waveview_color, mWaveViewColor)
        typeArray.recycle()
    }

    /**
     * 初始化画笔
     */
    fun initPaint() {
        mPaint = Paint()
        mPaint?.color = mWaveViewColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        height = MeasureSpec.getSize(heightMeasureSpec)
        weight = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(weight!!, height!!)
    }

    /**
     * 开启动画
     */
    fun start() {
        isStart = true
        if (onStartListener != null) {
            Log.d("isStart", "start: ")
            onStartListener.onIsStart(true)
        }
        animator = ValueAnimator.ofFloat(0F, weight!!.toFloat())
        animator!!.interpolator = LinearInterpolator()
        animator!!.addUpdateListener {
            val animatorValue = animator!!.animatedValue as Float
            offset = animatorValue
            postInvalidate()
        }
        animator!!.duration = 1500
        // 重复执行不限次数
        animator!!.repeatCount = ValueAnimator.INFINITE
        animator!!.start()
    }

    /**
     * 关闭动画
     */
    fun stop() {
        isStart = false
        if (onStartListener != null) {
            onStartListener.onIsStart(false)
        }
        animator!!.cancel()
    }

    fun isStart() {
        if (isStart) {
            stop()
        } else {
            start()
        }
    }

    /**
     * 根据给定的i，判断是波峰还是波谷
     */
    fun getWaveHeight(count: Int): Int {
        if (count % 2 == 0) {
            return height!! / 2 - 170
        }
        return height!! / 2 + 170
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val path = Path()
        val itemWidth: Int = weight!! / 2
        path.moveTo((-3 * itemWidth).toFloat(), (height!! / 2).toFloat())
        path.moveTo((-3 * itemWidth).toFloat(), (height!! / 2).toFloat())
        for (i in -3..1) {
            val controlX = i * itemWidth
            path.quadTo(
                itemWidth / 2 + controlX + offset!!, getWaveHeight(i).toFloat(),
                itemWidth + controlX + offset!!, (height!! / 2).toFloat()
            )
        }
        path.lineTo(weight!!.toFloat(), height!!.toFloat())
        path.lineTo(0f, height!!.toFloat())
        path.close() //封闭空间
        canvas!!.drawPath(path, mPaint!!)
    }

    interface onStartListener1 {
        fun onIsStart(isStart: Boolean)
    }

    private lateinit var onStartListener: onStartListener1

    fun setOnStart(onStartListener: onStartListener1) {
        this.onStartListener = onStartListener
    }
}