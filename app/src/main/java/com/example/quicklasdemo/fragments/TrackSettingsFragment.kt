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
import com.example.quicklasdemo.rv_items.RvBooleanItem
import com.example.quicklasdemo.rv_items.RvItem
import com.example.quicklasdemo.rv_items.RvTextFieldItem
import kotlinx.android.synthetic.main.fragment_track_settings.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TrackSettingsFragment : Fragment() {
    private lateinit var trackData : Track

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_track_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Toolbar(view, "Well Name", "Track Settings",
            R.id.toolbar_track_settings, R.menu.menu_track_settings, ::menuItemHandler)

        //TODO: I am not totally comfortable with this forced unwrapping, maybe add exception case later
        val trackDataString = arguments?.getString("modifiedTrackData")
        trackData = Json.decodeFromString(trackDataString!!)

        val trackList = mutableListOf(
            RvTextFieldItem(trackData.trackName) {trackData.trackName = it},
            RvItem("Curves"),
            RvBooleanItem("Display Linear Graph", trackData.isLinear) {trackData.isLinear = it},
            RvBooleanItem("Show Grid", trackData.showGrid) {trackData.showGrid = it},
//            TrackSettingDigitItem("Min Depth"),
//            TrackSettingDigitItem("Max Depth"),

        )

        val trackSettingsAdapter = RvAdapter(trackList, view)

        rv_track_settings.apply{
            adapter = trackSettingsAdapter
            layoutManager = LinearLayoutManager(view.context)
        }
    }

    private fun menuItemHandler(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_save_changes -> saveAndReturn()

            R.id.item_dont_save_changes -> discardAndReturn()
        }
        return true
    }

    private fun saveAndReturn(){
        val trackDataString = Json.encodeToString(trackData)
        val bundle = bundleOf("trackData" to trackDataString)

        view?.findNavController()?.navigate(R.id.action_trackSettingsFragment_to_trackSetupFragment, bundle)
    }

    private fun discardAndReturn(){
        view?.findNavController()?.navigate(R.id.action_trackSettingsFragment_to_trackSetupFragment)
    }
}