package com.example.quicklasdemo.rv_items

import android.view.View
import android.widget.Toast

abstract class RvItem(val title: String) {
    abstract fun attach(itemView: View)
}