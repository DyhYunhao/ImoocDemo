package com.dyh.recyclerviewdemo

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/*********************************************
 * @author daiyh
 * 创建日期：2020-11-18
 * 描述：
 *********************************************
 */

abstract class TypeAbstractViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bindHolder(model: DataModel)

}