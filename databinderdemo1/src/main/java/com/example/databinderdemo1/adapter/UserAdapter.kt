package com.example.databinderdemo1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.databinderdemo1.R
import com.example.databinderdemo1.bean.UserBean
import com.example.databinderdemo1.databinding.ItemBinding

/*********************************************
 * @author daiyh
 * 创建日期：2020-10-15
 * 描述：
 *********************************************
 */
class UserAdapter(private val userList: List<UserBean>):
    RecyclerView.Adapter<UserAdapter.UserAdapterHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapterHolder {
        val binding: ItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_recycle_8,
            parent,
            false
        )
        return UserAdapterHolder(binding)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserAdapterHolder, position: Int) {
        holder.getBinding().user = userList[position]
    }

    inner class UserAdapterHolder(private val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun getBinding(): ItemBinding {
            return binding
        }
    }

}