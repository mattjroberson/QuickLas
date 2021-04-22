package com.example.quicklasdemo.rv_items

import android.view.View
import kotlinx.android.synthetic.main.item_track_entry.view.*

class RvEntryItem(
        trackName: String,
        val actionHandler: (item: RvEntryItem, type: ActionType) -> Unit) : RvItem(trackName) {

    companion object {
        enum class ActionType { EDIT, DELETE, LABEL_ACTION }
    }

    override fun attach(itemView: View) {
        itemView.apply {

            track_name.apply {
                text = title
                setOnClickListener {
                    actionHandler(this@RvEntryItem, ActionType.LABEL_ACTION)
                }
            }

            edit_track_button.setOnClickListener {
                actionHandler(this@RvEntryItem, ActionType.EDIT)
            }
            delete_track_button.setOnClickListener {
                actionHandler(this@RvEntryItem, ActionType.DELETE)
            }
        }
    }
}