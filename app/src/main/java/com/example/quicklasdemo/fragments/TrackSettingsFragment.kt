package com.example.quicklasdemo.fragments

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quicklasdemo.*
import com.example.quicklasdemo.data.Curve
import com.example.quicklasdemo.data.Track
import com.example.quicklasdemo.rv_items.*
import kotlinx.android.synthetic.main.fragment_track_settings.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TrackSettingsFragment : Fragment(R.layout.fragment_track_settings) {
    private lateinit var tracksList: MutableList<Track>
    private lateinit var trackData : Track
    private lateinit var db : DatabaseHelper
    private lateinit var toolbar : Toolbar
    private lateinit var args: TrackSettingsFragmentArgs

    companion object {
        private const val VERT_DIV_COUNT_MIN = 0
        private const val VERT_DIV_COUNT_MAX = 10
        private const val HOR_DIV_HEIGHT_MIN = 0
        private const val HOR_DIV_HEIGHT_MAX = 10
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args = TrackSettingsFragmentArgs.fromBundle(requireArguments())
        db = DatabaseHelper(view.context)

        toolbar = Toolbar(view, args.trackName, "Track Settings",
            R.id.toolbar_track_settings, R.menu.menu_settings, ::menuItemHandler)

        loadTrackDataFromDB()
        loadDataFromCurveListFragment()

        val trackList = mutableListOf(
            RvTextFieldItem(trackData.trackName) { actionHandlerTrackName(it) },
            RvClickableItem("Curves") { navigateToCurveList() },
            RvBooleanItem("Display Linear Graph",
                    trackData.isLinear) { trackData.isLinear = it },
            RvBooleanItem("Show Grid",
                    trackData.showGrid) { trackData.showGrid = it },
            RvNumberFieldItem("Vertical Divider Count",
                    trackData.verticalDivCount) { actionHandlerDivCount(it.toInt()) },
            RvNumberFieldItem("Horizontal Divider Height (ft)",
                    trackData.horizontalDivHeight) { actionHandlerDivHeight(it.toInt()) }
        )

        val trackSettingsAdapter = RvAdapter(trackList, view)

        rv_track_settings.apply{
            adapter = trackSettingsAdapter
            layoutManager = LinearLayoutManager(view.context)
        }
    }

    private fun loadTrackDataFromDB(){
        tracksList =  db.getTrackList(args.lasName) ?: mutableListOf()

        trackData = if(args.trackIndex == -1) Track(args.trackName)
                    else tracksList[args.trackIndex]
    }

    private fun loadDataFromCurveListFragment(){
        args.selectedCurves?.let{
            val curvesList = Json.decodeFromString<MutableList<Curve>>(it)
            trackData.curveList = curvesList
        }
    }

    private fun menuItemHandler(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_save_changes -> navigateBackToTrackConfig(trackData)
            R.id.item_dont_save_changes -> navigateBackToTrackConfig(null)
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

    private fun navigateBackToTrackConfig(track: Track?) {
        val trackDataString = if(track != null) Json.encodeToString(track) else null

        val directions = TrackSettingsFragmentDirections.actionTrackSettingsFragmentToTrackSetupFragment(
            args.lasName, trackDataString, args.trackIndex
        )

        view?.findNavController()?.navigate(directions)
    }

    private fun actionHandlerTrackName(value: String): Boolean{
        if(value == ""){
            Utils.printMessage(view?.context,"Track Name cant be empty")
            return false
        }
        tracksList.forEach {
            if(it.trackName == value){
                Utils.printMessage(view?.context,"Track name $value is already taken")
                return false
            }
        }

        trackData.trackName = value
        toolbar.setTitle(value)
        return true
    }

    private fun actionHandlerDivCount(value: Int):Boolean{
        //Only accept values in true, return result back to itemview
        if(value in (VERT_DIV_COUNT_MIN + 1) until VERT_DIV_COUNT_MAX){
            trackData.verticalDivCount = value
            return true
        }
        Utils.printMessage(view?.context,"Value must be greater than $VERT_DIV_COUNT_MIN amd less than $VERT_DIV_COUNT_MAX")
        return false
    }

    private fun actionHandlerDivHeight(value: Int):Boolean{
        //Only accept values in true, return result back to item view
        if(value in (HOR_DIV_HEIGHT_MIN + 1) until HOR_DIV_HEIGHT_MAX){
            trackData.horizontalDivHeight = value
            return true
        }
        Utils.printMessage(view?.context, "Value must be greater than $HOR_DIV_HEIGHT_MIN amd less than $HOR_DIV_HEIGHT_MAX")
        return false
    }


}