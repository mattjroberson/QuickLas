package com.example.quicklasdemo.rv_items

import android.view.View
import com.example.quicklasdemo.data.Track
import kotlinx.android.synthetic.main.item_track_entry.view.*

class RvTrackEntryItem(
        trackData: Track,
        val actionHandler: (item: RvTrackEntryItem, type: ActionType) -> Unit) : RvItem(trackData.trackName) {

    companion object {
        enum class ActionType { EDIT, DELETE }
    }

    override fun attach(itemView: View) {
        itemView.apply {

            track_name.apply {
                text = title
                setOnClickListener {
                    actionHandler(this@RvTrackEntryItem, ActionType.EDIT)
                }
            }

            edit_track_button.setOnClickListener {
                actionHandler(this@RvTrackEntryItem, ActionType.EDIT)
            }
            delete_track_button.setOnClickListener {
                actionHandler(this@RvTrackEntryItem, ActionType.DELETE)
            }
        }
    }
}