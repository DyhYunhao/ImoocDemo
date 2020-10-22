package com.example.book1.cp1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.book1.R

/**
 * singleTask启动模式
 */
class Demo1Activity : AppCompatActivity() {

    private val TAG = "Demo1Activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo1)

        val mBtnSingleTask = findViewById<Button>(R.id.btn_single_task)
        mBtnSingleTask.setOnClickListener {
            val intent = Intent()
            intent.setClass(this, Demo1Activity::class.java)
            intent.putExtra("time", System.currentTimeMillis())
            startActivity(intent)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d(TAG, "onNewIntent: time = " + intent?.getLongExtra("time", 0))
    }
}