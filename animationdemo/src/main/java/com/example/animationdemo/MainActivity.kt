package com.example.animationdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_toFrame_1.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, FrameOne::class.java))
        })

        btn_toTween.setOnClickListener {
            startActivity(Intent(this, TweenedActivity::class.java))
        }

        btn_surfaceView.setOnClickListener {
            startActivity(Intent(this, SurfaceViewActivity::class.java))
        }
    }
}
