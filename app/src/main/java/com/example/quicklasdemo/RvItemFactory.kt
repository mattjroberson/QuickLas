package com.example.quicklasdemo

import android.view.View
import com.example.quicklasdemo.rv_items.RvItem
import com.example.quicklasdemo.rv_items.RvTrackItem

class RvItemFactory {
    companion object {
        fun getItemViewType(item : RvItem): Int {
            when(item) {
                is RvTrackItem -> return 1;
                is RvItem -> return 0;
            }

            return -1;
        }

        fun getItemLayout(viewType : Int): Int {
            when(viewType){
                0 -> return R.layout.item_label
                1 -> return R.layout.item_track
            }

            return -1;
        }

        fun bindViewHolder(mainView : View, itemView : View, item : RvItem) {
            item.attach(itemView);
        }
    }
}

