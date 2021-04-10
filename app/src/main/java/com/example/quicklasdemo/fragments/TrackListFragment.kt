package com.example.quicklasdemo.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quicklasdemo.R
import com.example.quicklasdemo.RvAdapter
import com.example.quicklasdemo.Toolbar
import com.example.quicklasdemo.data.Track
import com.example.quicklasdemo.rv_items.RvTrackEntryItem
import kotlinx.android.synthetic.main.fragment_track_list.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TrackListFragment : Fragment() {
    private var trackItems = mutableListOf<RvTrackEntryItem>()
    private var tracksData = mutableListOf(Track("Primary Track"))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //Dummy object representing current tracks
        //Theoretically will be replaced by saved data call

        return inflater.inflate(R.layout.fragment_track_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Toolbar(view, "Well Name", "Pick Tracks",
            R.id.toolbar_track_list, R.menu.menu_track_list, ::menuItemHandler)

        getDataFromSettingsFragment()

        tracksData.forEach{
            trackItems.add(RvTrackEntryItem(it, ::actionHandler))
        }

        connectRecyclerViewAdapter(view)
    }

    private fun getDataFromSettingsFragment(){
        val trackDataString = arguments?.getString("trackData")
        val tracksDataListString = arguments?.getString("tracksDataList")
        val trackIndex = arguments?.getInt("trackIndex")

        tracksDataListString?.let {
            tracksData = Json.decodeFromString(it)
        }

        trackDataString?.let {
            val track = Json.decodeFromString<Track>(it)

            //If index is not -1 (new track) replace old reference
            if(trackIndex != -1) {
                tracksData[trackIndex!!] = track
            }
            //Otherwise append new track
            else{
                tracksData.add(track)
            }
        }
    }

    private fun connectRecyclerViewAdapter(view : View){
        val trackListAdapter = RvAdapter(trackItems, view)

        rv_track_list.apply{
            adapter = trackListAdapter
            layoutManager = LinearLayoutManager(view.context)
        }
    }

    private fun menuItemHandler(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add_track -> addTrack()
            R.id.item_go_to_graph -> gotoGraph()
        }
        return true
    }

    private fun actionHandler(item : RvTrackEntryItem, type : RvTrackEntryItem.Companion.ActionType){
        when(type){
            RvTrackEntryItem.Companion.ActionType.EDIT -> editTrack(item)
            RvTrackEntryItem.Companion.ActionType.DELETE -> deleteTrack(item)
        }
    }

    private fun editTrack(item : RvTrackEntryItem) {
        val trackDataString = Json.encodeToString(item.trackData)
        val tracksDataListString = Json.encodeToString(tracksData)

        val bundle = bundleOf("modifiedTrackData" to trackDataString,
                                "tracksDataList" to tracksDataListString,
                                "trackIndex" to trackItems.indexOf(item))

        view?.findNavController()?.navigate(R.id.action_trackSetupFragment_to_trackSettingsFragment, bundle)
    }

    private fun addTrack(){
        val trackDataString = Json.encodeToString(Track("New Track"))
        val tracksDataListString = Json.encodeToString(tracksData)

        Log.i("TEST", tracksData.size.toString())

        val bundle = bundleOf("modifiedTrackData" to trackDataString,
                                    "tracksDataList" to tracksDataListString,
                                    "trackIndex" to -1)

        view?.findNavController()?.navigate(R.id.action_trackSetupFragment_to_trackSettingsFragment, bundle)
    }

    private fun deleteTrack(item : RvTrackEntryItem) {
        trackItems.apply {
            val position = indexOf(item)

            Log.i("TEST", tracksData.removeAt(position).toString())
            removeAt(position)

            rv_track_list.adapter?.apply {
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, size);
            }
        }
    }

    private fun gotoGraph(){

    }
}