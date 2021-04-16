package com.example.quicklasdemo.rv_items

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.item_text_field.view.*

class RvTextFieldItem(
        title: String,
        val actionHandler: (String) -> Boolean) : RvItem(title) {

    private lateinit var textValue: String

    override fun attach(itemView: View) {
        itemView.editable_text.apply{
            textValue = title
            setText(textValue)

            setOnEditorActionListener{ _, actionId, _ ->
                if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE){
                    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(windowToken, 0)

                    val cleanText = text.toString().trim()
                    val validInput = actionHandler(cleanText)

                    if(validInput) textValue = cleanText
                    else setText(textValue)

                    true
                } else {
                    false
                }
            }
        }
    }
}