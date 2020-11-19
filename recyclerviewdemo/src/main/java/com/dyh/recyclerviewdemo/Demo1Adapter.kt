package com.dyh.recyclerviewdemo

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/*********************************************
 * @author daiyh
 * 创建日期：2020-11-17
 * 描述：
 *********************************************
 */
class Demo1Adapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var mList: ArrayList<DataModel> = ArrayList()

    fun addList(list: List<DataModel>) {
        mList.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {
            DataModel.TYPE_ONE ->
                return TypeOneViewHolder(mLayoutInflater.inflate(R.layout.item_type_one, parent, false))
            DataModel.TYPE_TWO ->
                return TypeTwoViewHolder(mLayoutInflater.inflate(R.layout.item_type_two, parent, false))
            DataModel.TYPE_THREE ->
                return TypeThreeViewHolder(mLayoutInflater.inflate(R.layout.item_type_three, parent, false))

        }
        return TypeOneViewHolder(mLayoutInflater.inflate(R.layout.item_type_one, parent, false))
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TypeAbstractViewHolder).bindHolder(mList.get(position))
    }

    override fun getItemViewType(position: Int): Int {
        return mList.get(position).type
    }
}