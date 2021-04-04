package com.example.quicklasdemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_track.view.*

class TrackListAdapter(var trackItems: List<TrackItem>, private var view: View) : RecyclerView.Adapter<TrackListAdapter.TrackListViewHolder>(){
    inner class TrackListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.itemView.apply{
            tv_title.text = trackItems[position].title
            edit_track_button.setOnClickListener { _ ->
                editTrack(trackItems[position])

            }
        }
    }

    override fun getItemCount(): Int {
        return trackItems.count()
    }

    private fun editTrack(track: TrackItem){
        view.findNavController().navigate(R.id.action_trackSetupFragment_to_trackSettingsFragment)
    }
}