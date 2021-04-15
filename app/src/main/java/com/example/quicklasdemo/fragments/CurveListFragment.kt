package com.example.quicklasdemo.fragments

import android.os.Bundle
import android.util.Log
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
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CurveListFragment : Fragment(R.layout.fragment_curve_list) {
    private var curveItems = mutableListOf<RvCurveEntryItem>()
    private lateinit var curvesData: MutableList<Curve>
    private lateinit var db: DatabaseHelper
    private lateinit var args: CurveListFragmentArgs

    private val curveID
        get() = "${args.lasName}.${args.trackName}.curveList"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args = CurveListFragmentArgs.fromBundle(requireArguments())
        db = DatabaseHelper(view.context)

        Log.i("TEST", "TEST")

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
        curvesData = db.getCurveList(curveID) ?: readCurvesFromLas()
    }

    private fun getDataFromSettingsFragment() {
        val curveDataString = args.curveData
        val curveIndex = args.curveIndex

        curveDataString?.let {
            val curve = Json.decodeFromString<Curve>(it)
            curvesData[curveIndex] = curve
        }
    }

        private fun menuItemHandler(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_go_back_to_track -> navigateBackToTrackSettings()
        }
        return true
    }

    private fun actionHandler(item : RvCurveEntryItem){
        navigateIntoCurveSettings(item.curve, curveItems.indexOf(item))
    }

    private fun readCurvesFromLas(): MutableList<Curve>{
        val lasData = db.getLasData(args.lasName)
        val curveList = mutableListOf<Curve>()

        lasData?.forEach(){
            val curve = Curve(it.key)
            curveList.add(curve)
        }

        return curveList
    }

    private fun navigateBackToTrackSettings(){
        val selectedCurves = mutableListOf<Curve>()
        db.addCurveList(curveID, curvesData)

        //Only send back the curves that were selected
        curveItems.forEach {
            if(it.curve.picked) selectedCurves.add(it.curve)
        }

        val curvesDataString = Json.encodeToString(selectedCurves)

        val directions = CurveListFragmentDirections.actionCurveListFragmentToTrackSettingsFragment(
            curvesDataString, args.lasName, args.trackIndex, args.trackName
        )
        view?.findNavController()?.navigate(directions)
    }

    private fun navigateIntoCurveSettings(curve: Curve, index: Int) {
        val curveDataString = Json.encodeToString(curve)

        val directions = CurveListFragmentDirections.actionCurveListFragmentToCurveSettingsFragment(
                curveDataString, args.trackName,args.lasName,args.trackIndex,index)

        view?.findNavController()?.navigate(directions)
    }
}