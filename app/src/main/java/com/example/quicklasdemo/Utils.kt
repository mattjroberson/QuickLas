package com.example.quicklasdemo

import android.content.Context
import android.widget.Toast

class Utils {
    companion object{
        fun printMessage(context: Context?, text: String){
            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        }
    }
}