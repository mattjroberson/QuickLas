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
import com.example.quicklasdemo.rv_items.*
import kotlinx.android.synthetic.main.fragment_curve_list.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CurveListFragment : Fragment(R.layout.fragment_curve_list) {
    private var curveItems = mutableListOf<RvCurveEntryItem>()
    private lateinit var curvesData: MutableList<Curve>
    private lateinit var database: DatabaseHelper
    private lateinit var args: CurveListFragmentArgs

    private val curveID
        get() = "${args.lasName}.${args.trackName}.curves"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args = CurveListFragmentArgs.fromBundle(requireArguments())

        database = DatabaseHelper(view.context, null)

        Toolbar(view, args.trackName, "Pick Curves",
                R.id.toolbar_curve_list, R.menu.menu_curve_list, ::menuItemHandler)

        loadCurvesDataFromDB()
        getDataFromSettingsFragment()

        curvesData.forEach {
            curveItems.add(RvCurveEntryItem(it, ::actionHandler))
        }

        connectRecyclerViewAdapter(view)
    }

    private fun connectRecyclerViewAdapter(view : View){
        val curveListAdapter = RvAdapter(curveItems, view)

        rv_curve_list.apply{
            adapter = curveListAdapter
            layoutManager = LinearLayoutManager(view.context)
        }
    }

    private fun loadCurvesDataFromDB(){
        curvesData = database.getCurveList(curveID, DatabaseHelper.TABLE_TRACK_LISTS) ?: mutableListOf()
    }

    private fun getDataFromSettingsFragment(){

    }

    private fun menuItemHandler(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_go_back_to_track -> navigateBackToTrackSettings()
        }
        return true
    }

    private fun actionHandler(item : RvCurveEntryItem){
//        when(type){
//            RvTrackEntryItem.Companion.ActionType.EDIT -> editTrack(item)
//            //RvTrackEntryItem.Companion.ActionType.DELETE -> deleteTrack(item)
//        }
    }

    private fun editTrack(item : RvTrackEntryItem) {
        //navigateIntoTrackSettings(item.trackData, trackItems.indexOf(item))
    }

    private fun addTrack(){
        //navigateIntoTrackSettings(Track("New Track"), -1)
    }

    private fun navigateBackToTrackSettings(){
        val selectedCurves = mutableListOf<Curve>()

        //Only send back the curves that were selected
        curveItems.forEach {
            if(it.isSelected) selectedCurves.add(it.curve)
        }

        val curvesDataString = Json.encodeToString(selectedCurves)

        val directions = CurveListFragmentDirections.actionCurveListFragmentToTrackSettingsFragment(
            null, curvesDataString, args.lasName, args.trackIndex, args.trackName
        )
        view?.findNavController()?.navigate(directions)
    }

    private fun navigateIntoTrackSettings(curve: Curve, index: Int) {
//        val trackDataString = Json.encodeToString(track)
//
//        val bundle = bundleOf("modifiedTrackData" to trackDataString,
//                "lasName" to currLasName,
//                "trackIndex" to index)
//
//        view?.findNavController()?.navigate(R.id.action_trackSetupFragment_to_trackSettingsFragment, bundle)
    }
}