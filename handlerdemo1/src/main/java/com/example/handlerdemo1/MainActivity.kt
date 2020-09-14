package com.example.handlerdemo1

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun Complex(view: View?) {
        startActivity(ComplexActivity::class.java)
    }

    fun First(view: View?) {
        startActivity(FirstActivity::class.java)
    }

    fun HandlerThread(view: View?) {
        startActivity(HandlerThreadActivity::class.java)
    }

    fun UpdateUI(view: View?) {
        startActivity(UpdateUIActivity::class.java)
    }

    fun startActivity(cls: Class<*>?) {
        startActivity(Intent(this, cls))
    }
}
