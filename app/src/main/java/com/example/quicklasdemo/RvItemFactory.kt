package com.example.quicklasdemo

import android.view.View
import com.example.quicklasdemo.rv_items.RvBooleanItem
import com.example.quicklasdemo.rv_items.RvItem
import com.example.quicklasdemo.rv_items.RvTrackEntryItem
import com.example.quicklasdemo.rv_items.RvTextFieldItem

class RvItemFactory {
    companion object {
        fun getItemViewType(item : RvItem): Int {
            when(item) {
                is RvTrackEntryItem -> return 1;
                is RvTextFieldItem -> return 2;
                is RvBooleanItem -> return 3;
                is RvItem -> return 0;
            }

            return -1;
        }

        fun getItemLayout(viewType : Int): Int {
            when(viewType){
                0 -> return R.layout.item_label
                1 -> return R.layout.item_track_entry
                2 -> return R.layout.item_text_field
                3 -> return R.layout.item_boolean
            }

            return -1;
        }

        fun bindViewHolder(itemView : View, item : RvItem) {
            item.attach(itemView);
        }
    }
}

