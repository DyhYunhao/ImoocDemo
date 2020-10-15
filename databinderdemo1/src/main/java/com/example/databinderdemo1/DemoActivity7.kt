package com.example.databinderdemo1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.databinderdemo1.bean.UserBean
import com.example.databinderdemo1.databinding.Demo7Binding
import com.example.databinderdemo1.databinding.ViewStubBinding

/**
 * 懒加载
 */
class DemoActivity7 : AppCompatActivity() {

    private val binding: Demo7Binding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_demo7) as Demo7Binding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.handler = Handler()
        var user = UserBean("code", "212312")
        binding.userInfo = user
        binding.viewStub.setOnInflateListener { _, inflated -> {
            //如果在 xml 中没有使用 bind:userInfo="@{userInf}" 对 viewStub 进行数据绑定
            //那么可以在此处进行手动绑定
            val viewStubBinding: ViewStubBinding? = DataBindingUtil.bind(inflated)
            viewStubBinding?.let {
                viewStubBinding.userInfo = user
            }
        }}
    }

    inner class Handler {
        fun onClick(v: View) {
            if (!binding.viewStub.isInflated) {
                binding.viewStub.viewStub?.inflate()
            }
        }
    }
}