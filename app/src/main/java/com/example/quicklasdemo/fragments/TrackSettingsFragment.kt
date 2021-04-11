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
import com.example.quicklasdemo.rv_items.*
import kotlinx.android.synthetic.main.fragment_track_settings.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TrackSettingsFragment : Fragment() {
    private lateinit var trackData : Track
    private lateinit var oldTrackData : Track

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_track_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Toolbar(view, "Well Name", "Track Settings",
            R.id.toolbar_track_settings, R.menu.menu_track_settings, ::menuItemHandler)

        val trackDataString = arguments?.getString("modifiedTrackData")

        //TODO: I am not totally comfortable with this forced unwrapping, maybe add exception case later
        trackData = Json.decodeFromString(trackDataString!!)
        oldTrackData = trackData.copy()

        val trackList = mutableListOf(
            RvTextFieldItem(trackData.trackName) {trackData.trackName = it},
            RvClickableItem("Curves") {onCurveLabelClicked()},
            RvBooleanItem("Display Linear Graph",
                    trackData.isLinear) {trackData.isLinear = it},
            RvBooleanItem("Show Grid",
                    trackData.showGrid) {trackData.showGrid = it},
            RvNumberFieldItem("Vertical Divider Count",
                    trackData.verticalDivCount) {trackData.verticalDivCount = it},
            RvNumberFieldItem("Horizontal Divider Height (ft)",
                    trackData.horizontalDivHeight) {trackData.horizontalDivHeight = it},
            RvDropdownItem("TEST") {Log.i("TEST", it)}
        )

        val trackSettingsAdapter = RvAdapter(trackList, view)

        rv_track_settings.apply{
            adapter = trackSettingsAdapter
            layoutManager = LinearLayoutManager(view.context)
        }
    }

    private fun menuItemHandler(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_save_changes -> navigateBackToTrackConfig(trackData, true)
            R.id.item_dont_save_changes -> navigateBackToTrackConfig(oldTrackData, false)
        }
        return true
    }

    private fun onCurveLabelClicked(){
        //navigate to curve list
    }

    private fun navigateBackToTrackConfig(track: Track, save: Boolean) {
        val trackDataString = Json.encodeToString(track)
        val bundle = bundleOf("trackData" to trackDataString,
                "lasName" to arguments?.getString("lasName"),
                "save" to save,
                "trackIndex" to arguments?.getInt("trackIndex"))

        view?.findNavController()?.navigate(R.id.action_trackSettingsFragment_to_trackSetupFragment, bundle)
    }
}