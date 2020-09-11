package com.example.handlerdemo1

import android.annotation.SuppressLint
import android.nfc.cardemulation.HostNfcFService
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView

/**
 * Handler主线程与子线程通讯
 */
class FirstActivity : AppCompatActivity() {

    private val TAG = "tag"

    private lateinit var textView: TextView
    private lateinit var thread: HandlerThread
    private lateinit var threadHandler: Handler

    private var mainHandler = @SuppressLint("HandlerLeak")
    object: Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            var message: Message = Message()
            threadHandler.sendMessageDelayed(message, 1000)
            Log.d(TAG, "----------main handler---------------")
            textView.text = "Main handler"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        textView = findViewById(R.id.textView)
        thread = HandlerThread("Handler Thread")
        thread.start()

        threadHandler = object: Handler(thread.looper){
            @SuppressLint("SetTextI18n")
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                var message: Message = Message()
                mainHandler.sendMessageDelayed(message, 1000)
                Log.d(TAG, "------------thread handler--------------")
                runOnUiThread(Runnable {
                    textView.text = "thread handler"
                })
            }
        }
    }

    fun startHandler(view: View?) {
        mainHandler.sendEmptyMessage(1)
    }

    fun stopHandler(view: View?) {
        threadHandler.removeCallbacksAndMessages(null)
        mainHandler.removeMessages(1)
    }
}