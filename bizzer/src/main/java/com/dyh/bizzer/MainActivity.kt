package com.dyh.bizzer

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var mBtnWaveView: Button
    private lateinit var mBtnBezier2: Button
    private lateinit var mBtnBezier3: Button
    private lateinit var mBtnBeatBall: Button

    override fun onCreate(savedInstanceState: Bundle?) {

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //版本判断
//            val window: Window = window
//            // Translucent status bar
//            window.setFlags(
//                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//            ) //设置statusbar应用所占的屏幕扩大到全屏，但是最顶上会有背景透明的状态栏，它的文字可能会盖着你的应用的标题栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

        clickEvent()
    }

    private fun clickEvent() {
        mBtnWaveView.setOnClickListener {
            startActivity(Intent(this, WaveActivity::class.java))
        }

        mBtnBezier2.setOnClickListener {
            startActivity(Intent(this, Bezier2Activity::class.java))
        }

        mBtnBezier3.setOnClickListener {
            startActivity(Intent(this, Bezier3Activity::class.java))
        }

        mBtnBeatBall.setOnClickListener {
            startActivity(Intent(this, BeatBallActivity::class.java))
        }
    }

    private fun initView() {
        mBtnWaveView = findViewById(R.id.btn_wave_view)
        mBtnBezier2 = findViewById(R.id.btn_base_bezier2)
        mBtnBezier3 = findViewById(R.id.btn_base_bezier3)
        mBtnBeatBall = findViewById(R.id.btn_point_beat)
    }


}