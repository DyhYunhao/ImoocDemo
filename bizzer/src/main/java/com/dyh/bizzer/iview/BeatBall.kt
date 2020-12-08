package com.dyh.bizzer.iview

import android.R.attr
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.addListener


/**
 * describe: 弹弹球
 * create by daiyh on 2020-12-8
 */
class BeatBall : View {

    private lateinit var path: Path
    private lateinit var mBallPaint: Paint
    private lateinit var mLinePaint: Paint
    private lateinit var mLineCirclePaint: Paint
    private var width: Int? = 0
    private var height: Int? = 0
    private var mballControlly = 0f //球运动的控制点y坐标
    private var mballMiny = 300 //球运动的最低点
    private var mballMaxy = -1300 //球运动的最高点
    private var mLineControllx = 0f //绳的贝塞尔曲线控制点x坐标
    private var mLineControlly = 0f //绳的贝塞尔曲线控制点y坐标
    private var downAnimator: ValueAnimator? = null
    private var upAnimator: ValueAnimator? = null


    constructor(context: Context?, attrs: AttributeSet) : super(context, attrs) {
        initPaint()
        start()
    }

    private fun start() {
        //加速插值器
        downAnimator = ValueAnimator.ofFloat(mballMaxy.toFloat(), mballMiny.toFloat())
        //球加速下降 当球下降接触到绳时 绳也下降
        downAnimator!!.duration = 1500
        downAnimator!!.interpolator = AccelerateDecelerateInterpolator()
        downAnimator!!.addUpdateListener {
            mballControlly = it.animatedValue as Float
            if (height!! / 2 + mballControlly >= height!! / 2) {
                mLineControlly = (it.animatedValue as Float * 2.5).toFloat()
            }
            postInvalidate()
        }
        downAnimator!!.start()
        downAnimator!!.addListener {
            startUpAnimator()
        }
        //球和绳减速上升 当绳超过反向mballControlly 时球绳分离
        //减速插值器
        upAnimator = ValueAnimator.ofFloat(mballMiny.toFloat(), mballMaxy.toFloat())
        upAnimator!!.duration = (1500 * 0.8).toLong()
        upAnimator!!.interpolator = DecelerateInterpolator()
        upAnimator!!.addUpdateListener {
            val animatedValue = it.animatedValue as Float
            mballControlly = animatedValue
            if (mballControlly + height!! / 2 >= height!! / 2 + mballMaxy * 0.4 && mballControlly < 150) { //小球先上升后绳在上升
                mLineControlly = animatedValue
            } else if (mballControlly + height!! / 2 < height!! / 2 + mballMaxy / 3) {
                mLineControlly = -animatedValue / 2;
            }
            if (animatedValue <= -898) { //执行完毕 绳回到中心点
                mLineControlly = 0f
            }
            postInvalidate()
        }
        upAnimator!!.addUpdateListener {
            startDownAnimator()
        }
    }

    private fun initPaint() {
        path = Path()
        mBallPaint = Paint()
        mBallPaint.color = Color.YELLOW
        mBallPaint.strokeWidth = 15f
        mBallPaint.isAntiAlias = true
        mBallPaint.isDither = true
        mLinePaint = Paint()
        mLinePaint.color = Color.WHITE
        mLinePaint.strokeWidth = 20f
        mLinePaint.isAntiAlias = true
        mLinePaint.style = Paint.Style.STROKE
        mLineCirclePaint = Paint()
        mLineCirclePaint.color = Color.YELLOW
        mLineCirclePaint.strokeWidth = 15f
        mLineCirclePaint.isAntiAlias = true
        mLineCirclePaint.isDither = true
        mLineCirclePaint.style = Paint.Style.STROKE
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        width = MeasureSpec.getSize(widthMeasureSpec)
        height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width!!, height!!)
        mLineControllx = (width!! / 2).toFloat()
        mballControlly = mballMaxy.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas!!.drawCircle(30f, (height!! / 2 + 500).toFloat(), 25f, mLineCirclePaint) // 绳左球
        canvas.drawCircle(
            (width!! - 30).toFloat(),
            (height!! / 2 + 500).toFloat(),
            25f,
            mLineCirclePaint
        ) // 绳右球
        canvas.drawCircle(
            (width!! / 2).toFloat(),
            height!! / 2 + 500 + mballControlly,
            60f,
            mBallPaint
        ) //下落的球
        /*贝塞尔曲线绘制绳*/
        path.reset()
        path.moveTo(23f, (height!! / 2 + 100).toFloat())
        path.quadTo(
            mLineControllx,
            height!! / 2 + 100 + mLineControlly,
            (width!! - 23).toFloat(),
            (height!! / 2 + 100).toFloat()
        )
        canvas.drawPath(path, mLinePaint)
    }

    /**
     * 开始反弹
     */
    private fun startUpAnimator() {
        if (upAnimator != null && upAnimator!!.values != null && upAnimator!!.values.isNotEmpty() && !upAnimator!!.isRunning) {
            upAnimator!!.start()
        }
    }

    /**
     * 开始下降
     */
    private fun startDownAnimator() {
        if (downAnimator != null && downAnimator!!.values != null && downAnimator!!.values.isNotEmpty() && !downAnimator!!.isRunning) {
            downAnimator!!.start()
        }
    }
}