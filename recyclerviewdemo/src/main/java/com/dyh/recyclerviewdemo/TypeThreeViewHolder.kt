package com.dyh.recyclerviewdemo

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/*********************************************
 * @author daiyh
 * 创建日期：2020-11-18
 * 描述：
 *********************************************
 */
class TypeThreeViewHolder(itemView: View) : TypeAbstractViewHolder(itemView) {
    var avatar: ImageView = itemView.findViewById(R.id.iv_demo1_avatar)
    var name: TextView = itemView.findViewById(R.id.tv_demo1_name)
    var content: TextView = itemView.findViewById(R.id.tv_demo1_content)
    var contentImage: ImageView = itemView.findViewById(R.id.iv_demo1_content_img)

    override fun bindHolder(model: DataModel) {
        avatar.setBackgroundResource(model.avatarColor)
        name.setText(model.name)
        content.text = model.content
        contentImage.setBackgroundResource(model.contentColor)
    }
}