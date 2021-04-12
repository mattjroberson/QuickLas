package com.example.quicklasdemo.rv_items

import android.view.View
import com.example.quicklasdemo.data.Curve
import kotlinx.android.synthetic.main.item_curve_entry.view.*

class RvCurveEntryItem(
        val curve : Curve,
        val actionHandler : (item : RvCurveEntryItem) -> Unit) : RvItem(curve.curveName){

    val isSelected = curve.isSelected;

    override fun attach(itemView : View){
        itemView.apply{
            curve_name.text = title
            curve_selected.isSelected = isSelected

            curve_selected.setOnClickListener() {
                isSelected = it.isSelected
            }

            edit_curve_button.setOnClickListener {
                //actionHandler(this@RvCurveEntryItem, ActionType.EDIT)
            }
        }
    }
}