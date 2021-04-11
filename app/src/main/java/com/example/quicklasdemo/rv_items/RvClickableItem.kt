package com.example.quicklasdemo.rv_items

import android.view.View
import kotlinx.android.synthetic.main.item_clickable.view.*

class RvClickableItem (
    title: String,
    val actionHandler: () -> Unit) : RvItem(title) {

    override fun attach(itemView: View) {
        itemView.label_title.text = title

        itemView.apply {
            setOnClickListener() { actionHandler() }
        }
    }
}