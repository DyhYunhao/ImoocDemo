package com.dyh.bizzer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import com.dyh.bizzer.iview.Bezier3

class Bezier3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bezier3)

        var bezier3: Bezier3 = findViewById(R.id.bezier3)
        var mBtnLeft: Button = findViewById(R.id.btn_left_bezier3)
        var mBtnRight: Button = findViewById(R.id.btn_right_bezier3)
        var mBtnSub: Button = findViewById(R.id.btn_no_sub)

        mBtnLeft.setOnClickListener {
            bezier3.moveLeft()
        }

        mBtnRight.setOnClickListener {
            bezier3.moveRight()
        }

        mBtnSub.setOnClickListener {
            bezier3.setOnIsCleanSubListener3(object: Bezier3.onIsCleanSubListener3{
                override fun onCleanSub(isClean: Boolean?) {
                    if (isClean!!) {
                        mBtnSub.text = "去除辅助线"
                    } else {
                        mBtnSub.text = "添加辅助线"
                    }
                }
            })
            bezier3.isCleanSub()
        }
    }
}