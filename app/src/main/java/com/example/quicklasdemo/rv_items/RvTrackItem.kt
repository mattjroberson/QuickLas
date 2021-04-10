package com.example.quicklasdemo.rv_items

import android.view.View
import kotlinx.android.synthetic.main.item_track.view.*

class RvTrackItem(title: String, val actionHandler : (item : RvTrackItem, type : ActionType) -> Unit) : RvItem(title){
    companion object{
        enum class ActionType {
            EDIT, DELETE
        }
    }

    override fun attach(itemView : View){
        itemView.apply{
            track_name.text = title
            edit_track_button.setOnClickListener {
                actionHandler(this@RvTrackItem, ActionType.EDIT)
            }
            delete_track_button.setOnClickListener {
                actionHandler(this@RvTrackItem, ActionType.DELETE)
            }
        }
    }
}