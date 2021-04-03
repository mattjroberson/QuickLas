package com.example.quicklasdemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_track.view.*

class TrackListAdapter(var trackItems: List<TrackItem>) : RecyclerView.Adapter<TrackListAdapter.TrackListViewHolder>(){
    inner class TrackListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.itemView.apply{
            tv_title.text = trackItems[position].title
        }
    }

    override fun getItemCount(): Int {
        return trackItems.count()
    }
}