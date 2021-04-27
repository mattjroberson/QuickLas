package com.example.quicklasdemo

import android.widget.TextView

//Container Class for Chart Labels
class Label (val label_min : TextView, val label_name: TextView, val label_max: TextView){
    fun setData(min: Float, max: Float, name: String, color: Int){
        label_min.also {
            it.text = min.toString()
            it.setTextColor(color)
        }
        label_max.also{
            it.text = max.toString()
            it.setTextColor(color)
        }
        label_name.also{
            it.text = name
            it.setTextColor(color)
        }
    }
}