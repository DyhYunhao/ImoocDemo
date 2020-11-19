package com.dyh.recyclerviewdemo

/*********************************************
 * @author daiyh
 * 创建日期：2020-11-17
 * 描述：
 *********************************************
 */
class DataModel {

    companion object {
        @JvmField
        val TYPE_ONE = 1
        @JvmField
        val TYPE_TWO = 2
        @JvmField
        val TYPE_THREE = 3
    }

    var type: Int = 0
    var avatarColor: Int = 0
    var name: String = ""
    var content: String = ""
    var contentColor: Int = 0
}