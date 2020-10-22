package com.example.book1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.book1.cp1.Demo1Activity
import com.example.book1.cp2.Demo2Activity

/**
 * 安卓开发艺术探索示例代码
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mBtnCp1: Button = findViewById(R.id.btn_cp1)
        mBtnCp1.setOnClickListener {
            startActivity(Intent(this, Demo1Activity::class.java))
        }

        val mBtnCp2: Button = findViewById(R.id.btn_cp2)
        mBtnCp2.setOnClickListener {
            startActivity(Intent(this, Demo2Activity::class.java))
        }

        val mBtnBinderPool: Button = findViewById(R.id.btn_binder_pool)
        mBtnBinderPool.setOnClickListener {
            startActivity(Intent(this, BinderPoolActivity::class.java))
        }
    }


}