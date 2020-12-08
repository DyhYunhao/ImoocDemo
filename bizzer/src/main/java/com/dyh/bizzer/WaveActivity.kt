package com.dyh.bizzer

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dyh.bizzer.iview.WaveView


class WaveActivity : AppCompatActivity() {

    private lateinit var mBtnWave: Button
    private lateinit var waveView: WaveView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wave)

        initView()

        mBtnWave.setOnClickListener {
            waveView.setOnStart(object: WaveView.onStartListener1 {
                override fun onIsStart(isStart: Boolean) {
                    if (isStart) {
                        mBtnWave.text = "停止动画";
                        Log.e("isStart","$isStart ")
                    } else {
                        mBtnWave.text = "开始动画";
                        Log.e("isStart","$isStart ")
                    }
                }
            })
            waveView.isStart()
        }

    }

    private fun initView() {
        mBtnWave = findViewById(R.id.btn_start_wave)
        waveView = findViewById(R.id.waveView)
    }
}