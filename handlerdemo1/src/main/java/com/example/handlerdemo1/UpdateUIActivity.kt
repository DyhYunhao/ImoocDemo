package com.example.handlerdemo1

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class UpdateUIActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
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