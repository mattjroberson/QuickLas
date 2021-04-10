package com.example.quicklasdemo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quicklasdemo.R
import com.example.quicklasdemo.RvAdapter
import com.example.quicklasdemo.Toolbar
import com.example.quicklasdemo.rv_items.RvTrackItem
import kotlinx.android.synthetic.main.fragment_track_list.*

class TrackListFragment : Fragment() {
    private lateinit var trackList: MutableList<RvTrackItem>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_track_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Toolbar(view, "Well Name", "Pick Tracks",
            R.id.toolbar_track_list, R.menu.menu_track_list, ::menuItemHandler)

        trackList = mutableListOf(
            RvTrackItem("Track 1", ::actionHandler),
            RvTrackItem("Track 2", ::actionHandler),
            RvTrackItem("Track 3", ::actionHandler),
            RvTrackItem("Track 4", ::actionHandler)
        )

        val trackListAdapter = RvAdapter(trackList, view)

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

    private fun actionHandler(item : RvTrackItem, type : RvTrackItem.Companion.ActionType){
        when(type){
            RvTrackItem.Companion.ActionType.EDIT -> editTrack(item)
            RvTrackItem.Companion.ActionType.DELETE -> deleteTrack(item)
        }
    }

    private fun editTrack(item : RvTrackItem) {
        view?.findNavController()?.navigate(R.id.action_trackSetupFragment_to_trackSettingsFragment);
    }

    private fun addTrack(){

    }

    private fun deleteTrack(item : RvTrackItem) {
        trackList.apply {
            val position = indexOf(item)
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