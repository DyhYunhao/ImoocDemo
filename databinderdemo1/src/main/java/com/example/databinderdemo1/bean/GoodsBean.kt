package com.example.databinderdemo1.bean

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

/*********************************************
 * @author daiyh
 * 创建日期：2020-10-12
 * 描述：
 *********************************************
 */
class GoodsBean: BaseObservable() {
    @Bindable
    var name = ""
        set(value) {
            field = value
            notifyPropertyChanged(com.example.databinderdemo1.BR.name)
        }

    @Bindable
    var details = ""
        set(value) {
            field = value
            notifyChange()
        }

    var price = 0F
}