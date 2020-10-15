package com.example.databinderdemo1

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.databinderdemo1.bean.UserBean
import com.example.databinderdemo1.databinding.Demo6Binding

/**
 * 事件绑定也是一种变量绑定，只不过设置的变量是回调接口而已 事件绑定可用于以下多种回调事件
 */
class DemoActivity6 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: Demo6Binding = DataBindingUtil.setContentView(this, R.layout.activity_demo6)
        val user = UserBean("code", "12131")
        binding.userInfo = user
        binding.userPresenter = UserPresenter(this, user, binding)
    }

    class UserPresenter(private val context: Context, private val user: UserBean, private val binding: Demo6Binding) {
        fun onUserNameClick(user: UserBean) {
            Toast.makeText(context, "用户名： " + user.name, Toast.LENGTH_SHORT).show()
        }

        fun afterTextChanged(s: Editable) {
            user.name = s.toString()
            binding.userInfo = user
        }

        fun afterUserPasswordChanged(s: Editable) {
            user.password = s.toString()
            binding.userInfo = user
        }
    }
}