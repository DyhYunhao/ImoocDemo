package com.example.handlerdemo1

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private var handler: Handler = @SuppressLint("HandlerLeak")
    object: Handler(){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
        }
    }
}
