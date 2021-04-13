package com.example.quicklasdemo.rv_items

import android.view.View
import kotlinx.android.synthetic.main.item_boolean.view.*

class RvBooleanItem (
    title: String,
    private var value: Boolean,
    val actionHandler: (Boolean) -> Unit) : RvItem(title) {

    override fun attach(itemView: View) {
        itemView.bool_title.text = title

        itemView.item_switch.apply {
            isChecked = value

            setOnCheckedChangeListener() { _, _ ->
                actionHandler(isChecked)
            }
        }
    }
}