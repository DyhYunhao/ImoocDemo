package com.example.handlerdemo1

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_update.*

/**
 *  非UI线程更新UI的4种方式
 */
class UpdateUIActivity : AppCompatActivity() {

    private lateinit var thread: Thread

    private var handler: Handler = @SuppressLint("HandlerLeak")
    object: Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            textView.text = "update ui"
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        textView.setText("update ui" + Thread.currentThread().getId())
        thread = Thread.currentThread()
        Thread(Runnable {
            UpdateUI1()
        }).start()
    }


    private fun UpdateUI1() {
        handler.sendEmptyMessage(1)
    }

    private fun UpdateUI2() {
        handler.post { textView.text = "update ui" }
    }

    private fun UpdateUI3() {
        runOnUiThread { textView.text = "update ui" }
    }

    private fun UpdateUI4() {
        textView.post { textView.text = "update ui" }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

}