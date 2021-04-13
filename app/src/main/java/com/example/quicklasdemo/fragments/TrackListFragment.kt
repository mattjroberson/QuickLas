package com.example.quicklasdemo.fragments

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quicklasdemo.DatabaseHelper
import com.example.quicklasdemo.R
import com.example.quicklasdemo.RvAdapter
import com.example.quicklasdemo.Toolbar
import com.example.quicklasdemo.data.Track
import com.example.quicklasdemo.rv_items.RvTrackEntryItem
import kotlinx.android.synthetic.main.fragment_track_list.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TrackListFragment : Fragment(R.layout.fragment_track_list) {
    private var trackItems = mutableListOf<RvTrackEntryItem>()
    private lateinit var tracksData: MutableList<Track>
    private lateinit var database: DatabaseHelper
    private lateinit var currLasName: String
    private lateinit var args: TrackListFragmentArgs

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args = TrackListFragmentArgs.fromBundle(requireArguments())

        currLasName = args.lasName

        database = DatabaseHelper(view.context)

        Toolbar(view, currLasName, "Pick Tracks",
                R.id.toolbar_track_list, R.menu.menu_track_list, ::menuItemHandler)

        loadTracksDataFromDB()
        getDataFromSettingsFragment()

        tracksData.forEach{
            trackItems.add(RvTrackEntryItem(it, ::actionHandler))
        }

        connectRecyclerViewAdapter(view)
    }

    private fun loadTracksDataFromDB(){
        tracksData = database.getTrackList(currLasName) ?: mutableListOf()
    }

    private fun storeTracksDataInDB(){
        database.addTrackList(currLasName, tracksData)
    }

    private fun getDataFromSettingsFragment(){
        if(!args.saveSettings) return

        val trackDataString = args.trackData
        val trackIndex = args.trackIndex

        trackDataString?.let {
            val track = Json.decodeFromString<Track>(it)
            //If index is not -1 (new track) replace old reference
            if (trackIndex != -1) {
                tracksData[trackIndex] = track
            } else { //Otherwise append new track
                tracksData.add(track)
            }
        }

        storeTracksDataInDB()
    }

    private fun connectRecyclerViewAdapter(view: View){
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

    private fun actionHandler(item: RvTrackEntryItem, type: RvTrackEntryItem.Companion.ActionType){
        when(type){
            RvTrackEntryItem.Companion.ActionType.EDIT -> editTrack(item)
            RvTrackEntryItem.Companion.ActionType.DELETE -> deleteTrack(item)
        }
    }

    private fun editTrack(item: RvTrackEntryItem) {
        storeTracksDataInDB()
        navigateIntoTrackSettings(item.trackData, trackItems.indexOf(item))
    }

    private fun addTrack(){
        storeTracksDataInDB()
        navigateIntoTrackSettings(Track("New Track"), -1)
    }

    private fun deleteTrack(item: RvTrackEntryItem) {
        trackItems.apply {
            val position = indexOf(item)

            tracksData.removeAt(position)
            removeAt(position)

            rv_track_list.adapter?.apply {
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, size)
            }
        }
        storeTracksDataInDB()
    }

    private fun gotoGraph(){
        //val intent = Intent(activity, REPLACE_WITH_GRAPH_ACTIVITY::class.java)
        //intent.putExtra("lasName", args.lasName)
        //startActivity(intent)
    }

    private fun navigateIntoTrackSettings(track: Track, index: Int) {
        val trackDataString = Json.encodeToString(track)

        val directions = TrackListFragmentDirections.actionTrackSetupFragmentToTrackSettingsFragment(
                trackDataString, null, currLasName, index, track.trackName)
        view?.findNavController()?.navigate(directions)
    }
}