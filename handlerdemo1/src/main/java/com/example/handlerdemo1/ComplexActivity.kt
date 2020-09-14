package com.example.handlerdemo1

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Handler.Callback
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.lang.ref.WeakReference
import kotlin.math.log

/**
 * 子线程中用Handler
 */
class ComplexActivity : AppCompatActivity() {

    private lateinit var main_image_view: ImageView
    private var bitmapObject: Bitmap? = null
    private val mHandler: MyHandler = MyHandler(this)

    companion object {
        private var count = 0
    }

    class MyHandler : Handler {
        private var activityWeakReference: WeakReference<ComplexActivity>? = null

        constructor(activity: ComplexActivity) {
            activityWeakReference = WeakReference(activity)
        }

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            var activity: ComplexActivity? = activityWeakReference?.get()
            if (activity != null) {
                when(msg.what) {
                    0 -> activity.main_image_view.setImageResource(R.mipmap.ic_launcher)
                    100 -> {
                        count ++
                        Log.d("TAG", "count = $count")
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complex)

        main_image_view = findViewById(R.id.main_image_view)
        bitmapObject = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        main_image_view.setImageBitmap(bitmapObject)

        var isUiThread = Looper.getMainLooper().thread == Thread.currentThread()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            isUiThread = Looper.getMainLooper().isCurrentThread
        }
        Log.d("TAG", "isUiThread = $isUiThread")
        var message: Message = mHandler2.obtainMessage()
        message.what = 1
        message.obj = 100
        message.target = mHandler2
        message.sendToTarget()

        var message2: Message = mHandler2.obtainMessage()
        message2.what = 0
        message2.obj = 200
        message2.sendToTarget()

        

    }

    var mHandler2: Handler =
        @SuppressLint("HandlerLeak")
        object : Handler(Callback { msg -> //TODO 拦截消息，包装修改，再返回给上面
            val what = msg.what
            if (what == 1) {
                msg.obj = "win"
                false
            } else {
                true
            }
        }) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                //TODO 处理消息
                Log.e(
                    "TAG", "mHandler2---->currentThread:  " + Thread.currentThread()
                )
                if (msg.what == 1) Log.e("TAG", "msg = " + msg.obj)
            }
        }

    class LooperThread1: Thread() {
        lateinit var mHandler: Handler

        override fun run() {
            super.run()
            Looper.prepare()
            mHandler = @SuppressLint("HandlerLeak")
            object: Handler(){
                override fun handleMessage(msg: Message) {
                    super.handleMessage(msg)
                    Log.d("TAG", "LooperThread1---->what = $msg.what ,currentThread: " + Thread.currentThread())
                }
            }
            mHandler.sendEmptyMessage(1)
            Looper.loop()
            mHandler.sendEmptyMessage(2)
            mHandler.sendEmptyMessage(3)
            mHandler.sendEmptyMessage(4)
            Looper.loop()
        }
    }

    class LooperThread2: Thread() {
        lateinit var mHandler: Handler

        override fun run() {
            super.run()
            mHandler = object: Handler(Looper.getMainLooper()) {
                override fun handleMessage(msg: Message) {
                    super.handleMessage(msg)
                    Log.d("TAG", "LooperThread2 ok")
                }
            }
            mHandler.sendEmptyMessage(1)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null);

        if (bitmapObject != null && !bitmapObject!!.isRecycled()) {
            bitmapObject!!.recycle();
            main_image_view.setImageBitmap(null);
            System.gc();
            Log.e("TAG", "GC");
        }
    }
}