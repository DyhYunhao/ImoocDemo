package com.example.animationdemo

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_surface_view.*
import java.lang.Math.sin
import java.util.*


class SurfaceViewActivity : AppCompatActivity() {

    private lateinit var holder: SurfaceHolder
    private lateinit var paint: Paint
    private val HEIGHT = 320f
    private val WIDTH = 640f
    private val X_OFFSET = 40f
    var centerY = HEIGHT / 2
    private var cx = X_OFFSET
    var timer: Timer = Timer()
    var task: TimerTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_surface_view)

        var surfaceView: SurfaceView = findViewById(R.id.sfv_show)
        holder = surfaceView.holder
        paint = Paint()
        paint.strokeWidth = 3f

        holder.addCallback(object: SurfaceHolder.Callback{
            override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
                drawBack(holder)
            }

            override fun surfaceDestroyed(p0: SurfaceHolder) {
                timer.cancel()
            }

            override fun surfaceCreated(p0: SurfaceHolder) {
            }
        })

        btn_sin.setOnClickListener {
            doDraw(0)
        }

        btn_cos.setOnClickListener {
            doDraw(1)
        }
    }

    private fun drawBack(holder: SurfaceHolder?) {
        var canvas: Canvas = holder!!.lockCanvas()
        canvas.drawColor(Color.GRAY)

        var p: Paint = Paint()
        p.setColor(Color.BLACK)
        p.strokeWidth = 3f

        canvas.drawLine(X_OFFSET, centerY, WIDTH, centerY, p)
        canvas.drawLine(X_OFFSET, 40f, X_OFFSET, HEIGHT, p)

        holder.unlockCanvasAndPost(canvas)
        holder.lockCanvas(Rect(0, 0, 0,0))
        holder.unlockCanvasAndPost(canvas)
    }

    private fun doDraw(i: Int) {
        drawBack(holder)
        cx = X_OFFSET
        if (task != null) {
            task!!.cancel()
        }
        task = object : TimerTask() {
            override fun run() {
                val radian = (cx - X_OFFSET) * 2 * Math.PI / 150
                val cy: Float =
                    if (i == 0) centerY - (100 * Math.sin(radian)).toInt() else (centerY - (100 * Math.cos(radian))).toFloat()
                paint.color = if (i == 0) Color.GREEN else Color.RED
                val canvas =
                    holder.lockCanvas(Rect(cx.toInt(), (cy - 2).toInt(), (cx + 2).toInt(),
                        (cy + 2).toInt()
                    ))
                canvas.drawPoint(cx, cy.toFloat(), paint)
                cx++
                if (cx > WIDTH) {
                    task!!.cancel()
                    task = null
                }
                holder.unlockCanvasAndPost(canvas)
            }
        }
        timer.schedule(task, 0, 30)
    }


}