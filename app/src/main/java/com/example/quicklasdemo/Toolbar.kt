package com.example.quicklasdemo

import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar

class Toolbar (
    view: View,
    private val titleText: String,
    private val subtitleText: String,
    toolbarId: Int,
    private val menuId: Int,
    private val clickHandler: (MenuItem) -> Boolean) {

    private var toolbar: Toolbar? = view.findViewById<Toolbar>(toolbarId)?.apply {
        inflateMenu(menuId)
        title = titleText
        subtitle = subtitleText

        setOnMenuItemClickListener{
                item -> clickHandler(item)
        }
    }

    fun setTitle(newTitle: String){
        toolbar?.title = newTitle
    }
}
