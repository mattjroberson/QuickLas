package com.example.quicklasdemo.rv_items

import android.view.View
import kotlinx.android.synthetic.main.item_label.view.*

open class RvItem(val title: String){
    open fun attach(itemView : View){
        itemView.apply {
            label_title.text = title
            setOnClickListener { _ ->
                //Do Something here?
            }
        }
    }
}