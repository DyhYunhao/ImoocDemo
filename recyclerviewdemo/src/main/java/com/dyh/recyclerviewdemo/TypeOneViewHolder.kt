package com.dyh.recyclerviewdemo

import android.view.View
import android.widget.ImageView
import android.widget.TextView

/*********************************************
 * @author daiyh
 * 创建日期：2020-11-18
 * 描述：
 *********************************************
 */
class TypeOneViewHolder(itemView: View) : TypeAbstractViewHolder(itemView) {
    var avatar: ImageView = itemView.findViewById(R.id.iv_demo1_avatar)
    var name: TextView = itemView.findViewById(R.id.tv_demo1_name)

    override fun bindHolder(model: DataModel) {
        avatar.setBackgroundResource(model.avatarColor)
        name.setText(model.name)
    }
}