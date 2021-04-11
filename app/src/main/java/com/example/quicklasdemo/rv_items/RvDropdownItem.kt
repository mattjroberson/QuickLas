package com.example.quicklasdemo.rv_items

import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.main.item_dropdown.view.*

class RvDropdownItem (
    title: String,
    val actionHandler: (String) -> Unit) : RvItem(title) {

    override fun attach(itemView: View) {
        itemView.dropdown_title.text = title

        itemView.dropdown.apply {

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    actionHandler(parent?.getItemAtPosition(position).toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        }
    }
}