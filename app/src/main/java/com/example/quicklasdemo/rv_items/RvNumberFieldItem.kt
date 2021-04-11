package com.example.quicklasdemo.rv_items

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.item_number_field.view.*

class RvNumberFieldItem(
        title: String,
        private val value: Int,
        val actionHandler: (Int) -> Unit) : RvItem(title) {

    override fun attach(itemView: View) {
        itemView.edit_number_title.text = title

        itemView.edit_text_number.apply{
            setText(value.toString());

            setOnEditorActionListener{ _, actionId, _ ->
                if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE){
                    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(windowToken, 0)

                    actionHandler(Integer.parseInt(text.toString()))
                    true
                } else {
                    false
                }
            }
        }
    }
}