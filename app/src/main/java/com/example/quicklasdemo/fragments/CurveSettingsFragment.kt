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
import kotlinx.android.synthetic.main.fragment_curve_settings.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CurveSettingsFragment : Fragment(R.layout.fragment_curve_settings) {
    private lateinit var curveData : Curve
    private lateinit var oldCurveData : Curve
    private lateinit var args: CurveSettingsFragmentArgs

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args = CurveSettingsFragmentArgs.fromBundle(requireArguments())

        curveData = Json.decodeFromString(args.curveData!!)
        oldCurveData = curveData.copy()

        Toolbar(view, curveData.curveName, "Curve Settings",
                R.id.toolbar_curve_settings, R.menu.menu_settings, ::menuItemHandler)

        val curveList: MutableList<RvItem> = mutableListOf(
//                RvTextFieldItem(trackData.trackName) { trackData.trackName = it },
//                RvClickableItem("Curves") { navigateToCurveList() },
//                RvBooleanItem("Display Linear Graph",
//                        trackData.isLinear) { trackData.isLinear = it },
//                RvBooleanItem("Show Grid",
//                        trackData.showGrid) { trackData.showGrid = it },
//                RvNumberFieldItem("Vertical Divider Count",
//                        trackData.verticalDivCount) { trackData.verticalDivCount = it },
//                RvNumberFieldItem("Horizontal Divider Height (ft)",
//                        trackData.horizontalDivHeight) { trackData.horizontalDivHeight = it }
        )

        val trackSettingsAdapter = RvAdapter(curveList, view)

        rv_curve_settings.apply{
            adapter = trackSettingsAdapter
            layoutManager = LinearLayoutManager(view.context)
        }
    }

    private fun menuItemHandler(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_save_changes -> navigateBackToCurveConfig(curveData)
            R.id.item_dont_save_changes -> navigateBackToCurveConfig(oldCurveData)
        }
        return true
    }

    private fun navigateBackToCurveConfig(curve: Curve) {
        val curveDataString = Json.encodeToString(curve)

        val directions = CurveSettingsFragmentDirections.actionCurveSettingsFragmentToCurveListFragment(
                args.trackName, args.lasName, args.trackIndex, curveDataString, args.curveIndex
        )

        view?.findNavController()?.navigate(directions)
    }
}