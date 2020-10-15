package com.example.databinderdemo1.bean

import android.telecom.Call
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.databinding.ObservableFloat

/*********************************************
 * @author daiyh
 * 创建日期：2020-10-13
 * 描述：继承于 Observable 类相对来说限制有点高，且也需要进行 notify 操作，
 * 因此为了简单起见可以选择使用 ObservableField。ObservableField 可以理解为官方对 BaseObservable 中字段的注解和刷新等操作的封装，
 * 官方原生提供了对基本数据类型的封装，例如 ObservableBoolean、ObservableByte、ObservableChar、ObservableShort、ObservableInt、
 * ObservableLong、ObservableFloat、ObservableDouble 以及 ObservableParcelable ，也可通过 ObservableField 泛型来申明其他类型
 *********************************************
 */
class ObservableGoodsBean(name: String, details: String, price: Float) {
    var name: ObservableField<String> = ObservableField(name)
    var details: ObservableField<String> = ObservableField(details)
    var price: ObservableFloat = ObservableFloat(price)
}