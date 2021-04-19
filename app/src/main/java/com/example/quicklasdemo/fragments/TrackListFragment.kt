package com.example.quicklasdemo.fragments

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quicklasdemo.*
import com.example.quicklasdemo.activities.ChartActivity
import com.example.quicklasdemo.data.Track
import com.example.quicklasdemo.rv_items.RvTrackEntryItem
import kotlinx.android.synthetic.main.fragment_track_list.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class TrackListFragment : Fragment(R.layout.fragment_track_list) {
    private var trackItems = mutableListOf<RvTrackEntryItem>()
    private lateinit var tracksData: MutableList<Track>
    private lateinit var args: TrackListFragmentArgs
    private lateinit var db: DatabaseHelper

    companion object{
        const val MAX_TRACK_COUNT = 4
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args = TrackListFragmentArgs.fromBundle(requireArguments())
        db = DatabaseHelper(view.context)

        Toolbar(view, args.lasName, "Pick Tracks",
                R.id.toolbar_track_list, R.menu.menu_track_list, ::menuItemHandler)

        loadTracksDataFromDB()
        getDataFromSettingsFragment()

        tracksData.forEach{
            trackItems.add(RvTrackEntryItem(it, ::actionHandler))
        }

        connectRecyclerViewAdapter(view)
    }

    private fun loadTracksDataFromDB(){
        tracksData = db.getTrackList(args.lasName) ?: mutableListOf()
    }

    private fun storeTracksDataInDB(){
        db.addTrackList(args.lasName, tracksData)
    }

    private fun getDataFromSettingsFragment(){
        args.trackData?.let {
            val trackIndex = args.trackIndex
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
        navigateIntoTrackSettings(trackItems.indexOf(item))
    }

    private fun addTrack(){
        if(tracksData.size < MAX_TRACK_COUNT){
            storeTracksDataInDB()
            navigateIntoTrackSettings(-1)
        }
        else{
            Utils.printMessage(view?.context,"Only $MAX_TRACK_COUNT tracks allowed")
        }
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
        if(tracksData.size > 0) {
            val intent = Intent(activity, ChartActivity::class.java)
            intent.putExtra("lasName", args.lasName)
            startActivity(intent)
        }
        else{
            Utils.printMessage(view?.context,"Must have at least one track")
        }
    }

    private fun navigateIntoTrackSettings(index: Int) {

        val directions = TrackListFragmentDirections.actionTrackSetupFragmentToTrackSettingsFragment(
                null, args.lasName, index, -1)
        view?.findNavController()?.navigate(directions)
    }
}