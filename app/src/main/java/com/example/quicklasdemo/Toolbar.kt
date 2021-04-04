package com.example.quicklasdemo

import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar

class Toolbar (
    private val view: View,
    private val titleText: String,
    private val subtitleText: String,
    private val toolbarId: Int,
    private val menuId: Int,
    private val clickHandler: (MenuItem) -> Boolean) {

    init{
        view?.findViewById<Toolbar>(toolbarId)?.run {
            inflateMenu(menuId)
            title = titleText
            subtitle = subtitleText

            setOnMenuItemClickListener(){
                    item -> clickHandler(item)
            }
        }
    }
}
