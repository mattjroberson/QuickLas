package com.example.quicklasdemo.labels

import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.chart_header.view.*

class LabelContainer(private val container: ConstraintLayout) {
    private val label1: Label = Label(
        container.curve_label_1_min, container.curve_label_1, container.curve_label_1_max
    )
    private val label2: Label = Label(
        container.curve_label_2_min, container.curve_label_2, container.curve_label_2_max
    )
    private val label3: Label = Label(
        container.curve_label_3_min, container.curve_label_3, container.curve_label_3_max
    )

    fun getLabel(index: Int): Label? {
        if(index == 0) return label1
        if(index == 1) return label2
        if(index == 2) return label3

        return null
    }

    fun setTrackName(name: String){
        container.track_name.text = name
    }

}