package com.example.databinderdemo1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.databinderdemo1.adapter.UserAdapter
import com.example.databinderdemo1.bean.UserBean
import com.example.databinderdemo1.utils.DynamicChangeCallback
import java.util.*

class DemoActivity8 : AppCompatActivity() {

    private val userObservableList = ObservableArrayList<UserBean>().apply {
        for (i in 0..19) {
            val user = UserBean("user_$i", (Random().nextInt() * 4).toString())
            add(user)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo8)

        val rvList = findViewById<RecyclerView>(R.id.rvList)
        rvList.layoutManager = LinearLayoutManager(this)
        val userAdapter = UserAdapter(userObservableList)
        userAdapter.notifyDataSetChanged()
        userObservableList.addOnListChangedCallback(DynamicChangeCallback<UserBean>(userAdapter))
        rvList.adapter = userAdapter
    }

    fun addItem(view: View) {
        if (userObservableList.size >= 3) {
            val user = UserBean("user_" + 100, (Random().nextInt() * 4).toString())
            userObservableList.add(1, user)
        }
    }

    fun addItemList(view: View) {
        if (userObservableList.size >= 3) {
            val userList: MutableList<UserBean> = ArrayList<UserBean>()
            for (i in 0..2) {
                val user = UserBean("user_" + 100, (Random().nextInt() * 4).toString())
                userList.add(user)
            }
            userObservableList.addAll(1, userList)
        }
    }

    fun removeItem(view: View) {
        if (userObservableList.size >= 3) {
            userObservableList.removeAt(1)
        }
    }

    fun updateItem(view: View) {
        if (userObservableList.size >= 3) {
            val user: UserBean = userObservableList[1]
            user.name = "user_" + Random().nextInt()
            userObservableList[1] = user
        }
    }
}