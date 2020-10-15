package com.example.databinderdemo1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.databinderdemo1.bean.UserBean
import com.example.databinderdemo1.databinding.Demo1Binding

class DemoActivity1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_demo1)

        val binding: Demo1Binding = DataBindingUtil.setContentView(this, R.layout.activity_demo1)
        var user = UserBean("ddd", "123")
        binding.userInfo = user
    }
}