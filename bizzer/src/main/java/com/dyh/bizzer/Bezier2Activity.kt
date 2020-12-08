package com.dyh.bizzer

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dyh.bizzer.iview.Bezier2

class Bezier2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bezier2)

        val bezier2: Bezier2 = findViewById(R.id.bezier2)
        val subLine: Button = findViewById(R.id.btn_bezier2_line)

        subLine.setOnClickListener {
            bezier2.setOnIsCleanSubListener(object : Bezier2.onIsCleanSubListener2 {
                override fun onCleanSub(isClean: Boolean?) {
                    if (isClean!!) {
                        subLine.text = "去除辅助线"
                    } else {
                        subLine.text = "添加辅助线"
                    }
                }
            })
            bezier2.isCleanSub()
        }
    }
}