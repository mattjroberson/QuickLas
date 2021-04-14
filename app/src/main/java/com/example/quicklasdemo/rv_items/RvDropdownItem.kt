package com.example.quicklasdemo.rv_items

import android.R
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.item_dropdown.view.*


class RvDropdownItem(
        title: String,
        private val arrayID: Int,
        private val firstSelection: Int = 0,
        val actionHandler: (String) -> Unit) : RvItem(title) {

    override fun attach(itemView: View) {
        itemView.dropdown_title.text = title

        itemView.dropdown.apply {
            val adapter = ArrayAdapter.createFromResource(itemView.context, arrayID, R.layout.simple_spinner_item)
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            setAdapter(adapter)
            setSelection(firstSelection)

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