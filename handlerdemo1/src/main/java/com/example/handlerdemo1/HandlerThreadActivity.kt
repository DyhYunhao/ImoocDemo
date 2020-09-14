package com.example.handlerdemo1

import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_first.*
import java.lang.ref.WeakReference

class HandlerThreadActivity : AppCompatActivity() {
    private lateinit var handler: MyHandler
    private lateinit var thread: HandlerThread

    class MyHandler: Handler {
        lateinit var weakReference: WeakReference<HandlerThreadActivity>
        constructor(activity: HandlerThreadActivity) {
            this.weakReference = WeakReference(activity)
        }
        constructor(looper: Looper, activity: HandlerThreadActivity) {

            this.weakReference = WeakReference(activity)
        }

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            var handlerThreadActivity: HandlerThreadActivity? = weakReference.get()
            if (handlerThreadActivity != null) {
                Log.d("TAG", "message： " + msg.what + "  thread： " + Thread.currentThread().getName());
                handlerThreadActivity.textView.setText("handleMessage");
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_handler_thread)
        example()
    }

    fun example() {
        thread = object: HandlerThread("Handler Thread") {
            override fun onLooperPrepared() {
                super.onLooperPrepared()
                textView.setText("onLooperPrepared")
            }
        }
        thread.start()
        handler = MyHandler(thread.looper, HandlerThreadActivity())
        handler.sendEmptyMessage(1)
    }

    override fun onDestroy() {
        super.onDestroy()
        thread.quit()
        handler.removeCallbacksAndMessages(null)
    }
}