package com.example.quicklasdemo.fragments

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quicklasdemo.*
import com.example.quicklasdemo.data.Track
import com.example.quicklasdemo.rv_items.RvTrackEntryItem
import kotlinx.android.synthetic.main.fragment_graph_list.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class GraphListFragment : Fragment(R.layout.fragment_graph_list) {
    private var graphItems = mutableListOf<RvTrackEntryItem>()
    private lateinit var graphsList: MutableList<MutableList<Track>>
    private lateinit var db: DatabaseHelper

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        db = DatabaseHelper(view.context)
//
//        Toolbar(view, "Quick LAS", "Select Graph",
//                R.id.toolbar_track_list, R.menu.menu_track_list, ::menuItemHandler)
//
//        loadTracksDataFromDB()
//
//        //TODO: implement this correctly
////        tracksData.forEach{
////            trackItems.add(RvTrackEntryItem(it, ::actionHandler))
////        }
//
//        connectRecyclerViewAdapter(view)
//    }
//
//    private fun loadTracksDataFromDB(){
//        tracksData = db.getTrackList(args.lasName) ?: mutableListOf()
//    }
//
//    private fun connectRecyclerViewAdapter(view: View){
//        val trackListAdapter = RvAdapter(graphItems, view)
//
//        rv_graph_list.apply{
//            adapter = trackListAdapter
//            layoutManager = LinearLayoutManager(view.context)
//        }
//    }
//
//    private fun menuItemHandler(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.item_add_track -> addGraph()
//        }
//        return true
//    }
//
//    private fun actionHandler(item: RvTrackEntryItem, type: RvTrackEntryItem.Companion.ActionType){
//        when(type){
//            RvTrackEntryItem.Companion.ActionType.EDIT -> editTrack(item)
//            RvTrackEntryItem.Companion.ActionType.DELETE -> deleteTrack(item)
//        }
//    }
//
//    private fun editTrack(item: RvTrackEntryItem) {
//        navigateIntoTrackList(trackItems.indexOf(item))
//    }
//
//    private fun addGraph(){
//        //TODO: Add file picker thing here
//    }
//
//    private fun deleteTrack(item: RvTrackEntryItem) {
//        graphItems.apply {
//            val position = indexOf(item)
//            removeAt(position)
//            //TODO: delete graph from database
//
//            rv_graph_list.adapter?.apply {
//                notifyItemRemoved(position)
//                notifyItemRangeChanged(position, size)
//            }
//        }
//    }
//
//    //TODO: Call this in appropriate spot
//    private fun gotoGraph(){
//            //val intent = Intent(activity, REPLACE_WITH_GRAPH_ACTIVITY::class.java)
//            //intent.putExtra("lasName", args.lasName)
//            //startActivity(intent)
//        }
//    }
//
//    private fun navigateIntoTrackList(index: Int) {
//        val directions = GraphListFragmentDirections.actionGraphListFragmentToTrackSetupFragment()
//        view?.findNavController()?.navigate(directions)
//    }
}