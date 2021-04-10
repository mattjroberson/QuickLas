package com.example.quicklasdemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quicklasdemo.rv_items.RvItem

class RvAdapter(var rvItems: List<RvItem>, private var view: View) : RecyclerView.Adapter<RvAdapter.RvViewHolder>(){
    inner class RvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemViewType(position: Int): Int {
        return RvItemFactory.getItemViewType(rvItems[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvViewHolder {
        val layout = RvItemFactory.getItemLayout(viewType)

        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return RvViewHolder(view)
    }

    override fun onBindViewHolder(holder: RvViewHolder, position: Int) {
        RvItemFactory.bindViewHolder(view, holder.itemView, rvItems[position])
    }

    override fun getItemCount(): Int {
        return rvItems.count()
    }
}