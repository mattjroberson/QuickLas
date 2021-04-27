package com.example.quicklasdemo.labels

import android.graphics.Color
import android.widget.TextView

//Container Class for Chart Labels
class Label (val label_min : TextView, val label_name: TextView, val label_max: TextView){
    fun setData(min: Float, max: Float, name: String, color: String){
        label_min.also {
            it.text = "%.1f".format(min)
            it.setTextColor(getColor(color))
        }
        label_max.also{
            it.text = "%.1f".format(max)
            it.setTextColor(getColor(color))
        }
        label_name.also{
            it.text = name
            it.setTextColor(getColor(color))
        }
    }

    private fun getColor(color: String): Int {
        when(color){
            "FF0000" -> return Color.RED
            "00FF00" -> return Color.GREEN
            "0000FF" -> return Color.BLUE
        }
        return Color.WHITE
    }
}