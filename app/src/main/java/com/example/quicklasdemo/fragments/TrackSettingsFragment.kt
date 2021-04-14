package com.example.quicklasdemo.fragments

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
import com.example.quicklasdemo.data.Curve
import com.example.quicklasdemo.data.Track
import com.example.quicklasdemo.rv_items.*
import kotlinx.android.synthetic.main.fragment_track_settings.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TrackSettingsFragment : Fragment(R.layout.fragment_track_settings) {
    private lateinit var trackData : Track
    private lateinit var oldTrackData : Track
    private lateinit var db : DatabaseHelper
    private lateinit var args: TrackSettingsFragmentArgs

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = DatabaseHelper(view.context)

        args = TrackSettingsFragmentArgs.fromBundle(requireArguments())

        Toolbar(view, args.trackName, "Track Settings",
            R.id.toolbar_track_settings, R.menu.menu_settings, ::menuItemHandler)

        importTrackData()
        loadDataFromCurveListFragment()

        val trackList = mutableListOf(
            RvTextFieldItem(trackData.trackName) { trackData.trackName = it },
            RvClickableItem("Curves") { navigateToCurveList() },
            RvBooleanItem("Display Linear Graph",
                    trackData.isLinear) { trackData.isLinear = it },
            RvBooleanItem("Show Grid",
                    trackData.showGrid) { trackData.showGrid = it },
            RvNumberFieldItem("Vertical Divider Count",
                    trackData.verticalDivCount) { trackData.verticalDivCount = it.toInt()},
            RvNumberFieldItem("Horizontal Divider Height (ft)",
                    trackData.horizontalDivHeight) { trackData.horizontalDivHeight = it.toInt()}
        )

        val trackSettingsAdapter = RvAdapter(trackList, view)

        rv_track_settings.apply{
            adapter = trackSettingsAdapter
            layoutManager = LinearLayoutManager(view.context)
        }
    }

    private fun importTrackData(){
        val trackID = "${args.lasName}.${args.trackName}"

        trackData = if(args.trackData != null) {
            Json.decodeFromString(args.trackData!!)
        }else{
            db.getTempTrack(trackID)!!
        }

        oldTrackData = trackData.copy()
    }

    private fun loadDataFromCurveListFragment(){
        args.selectedCurves?.let{
            val curvesList = Json.decodeFromString<MutableList<Curve>>(it)
            trackData.curveList = curvesList
        }
    }

    private fun menuItemHandler(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_save_changes -> navigateBackToTrackConfig(trackData, true)
            R.id.item_dont_save_changes -> navigateBackToTrackConfig(oldTrackData, false)
        }
        return true
    }

    private fun navigateToCurveList(){
        val directions = TrackSettingsFragmentDirections.actionTrackSettingsFragmentToCurveListFragment(
                trackData.trackName, args.lasName, args.trackIndex, null, -1
        )

        val trackID = "${args.lasName}.${trackData.trackName}"
        db.addTempTrack(trackID, trackData)

        view?.findNavController()?.navigate(directions)
    }

    private fun navigateBackToTrackConfig(track: Track, save: Boolean) {
        val trackDataString = Json.encodeToString(track)

        val directions = TrackSettingsFragmentDirections.actionTrackSettingsFragmentToTrackSetupFragment(
            args.lasName, save, trackDataString, args.trackIndex
        )

        view?.findNavController()?.navigate(directions)
    }
}