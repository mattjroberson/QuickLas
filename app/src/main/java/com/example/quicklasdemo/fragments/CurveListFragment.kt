package com.example.quicklasdemo.fragments

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
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

class CurveListFragment : Fragment(R.layout.fragment_track_list) {


//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        //TODO: rename menu_track_settings to menu_settings
//        Toolbar(view, "Track Name", "Pick Curves",
//                R.id.tool_curve_list, R.menu.menu_track_settings, ::menuItemHandler)
//
//        connectRecyclerViewAdapter(view)
//    }
//
//    private fun connectRecyclerViewAdapter(view : View){
//        val trackListAdapter = RvAdapter(trackItems, view)
//
//        rv_track_list.apply{
//            adapter = trackListAdapter
//            layoutManager = LinearLayoutManager(view.context)
//        }
//    }
//
//    private fun menuItemHandler(item: MenuItem): Boolean {
//        when(item.itemId){
//            R.id.item_save_changes -> navigateBackToTrackConfig(trackData, true)
//            R.id.item_dont_save_changes -> navigateBackToTrackConfig(oldTrackData, false)
//        }
//        return true
//    }
//
//    private fun actionHandler(item : RvTrackEntryItem, type : RvTrackEntryItem.Companion.ActionType){
//        when(type){
//            RvTrackEntryItem.Companion.ActionType.EDIT -> editTrack(item)
//            RvTrackEntryItem.Companion.ActionType.DELETE -> deleteTrack(item)
//        }
//    }
//
//    private fun editTrack(item : RvTrackEntryItem) {
//        navigateIntoTrackSettings(item.trackData, trackItems.indexOf(item))
//    }
//
//    private fun addTrack(){
//        navigateIntoTrackSettings(Track("New Track"), -1)
//    }
//
//    private fun navigateIntoTrackSettings(track: Track, index: Int) {
//        val trackDataString = Json.encodeToString(track)
//
//        val bundle = bundleOf("modifiedTrackData" to trackDataString,
//                "lasName" to currLasName,
//                "trackIndex" to index)
//
//        view?.findNavController()?.navigate(R.id.action_trackSetupFragment_to_trackSettingsFragment, bundle)
//    }
}