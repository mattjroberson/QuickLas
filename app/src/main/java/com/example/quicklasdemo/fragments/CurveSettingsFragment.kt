package com.example.quicklasdemo.fragments

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quicklasdemo.R
import com.example.quicklasdemo.RvAdapter
import com.example.quicklasdemo.Toolbar
import com.example.quicklasdemo.Utils
import com.example.quicklasdemo.data.Curve
import com.example.quicklasdemo.rv_items.*
import kotlinx.android.synthetic.main.fragment_curve_settings.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CurveSettingsFragment : Fragment(R.layout.fragment_curve_settings) {
    private lateinit var curveData: Curve
    private lateinit var args: CurveSettingsFragmentArgs

    private var colorList: MutableList<Array<String>> = mutableListOf(
            arrayOf("Red", "FF0000"),
            arrayOf("Green", "00FF00"),
            arrayOf("Blue", "0000FF")
    )

    companion object {
        const val SCALE_MIN = 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args = CurveSettingsFragmentArgs.fromBundle(requireArguments())

        curveData = Json.decodeFromString(args.curveData)

        Toolbar(view, curveData.curveName, "Curve Settings",
                R.id.toolbar_curve_settings, R.menu.menu_settings, ::menuItemHandler)

        val curveList: MutableList<RvItem> = mutableListOf(
                RvDropdownItem("Line Style", R.array.line_style, getCurrStyleIndex()) { curveData.lineStyle = it},
                RvDropdownItem("Line Color", R.array.colors, getCurrColorIndex()) { setColor(it) },
                RvNumberFieldItem("Scale Min", curveData.scaleMin) { actionHandlerScaleMin(it.toFloat()) },
                RvNumberFieldItem("Scale Max", curveData.scaleMax) { actionHandlerScaleMax(it.toFloat()) })

        val trackSettingsAdapter = RvAdapter(curveList, view)

        rv_curve_settings.apply {
            adapter = trackSettingsAdapter
            layoutManager = LinearLayoutManager(view.context)
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            this.isEnabled = true
            navigateBackToCurveConfig()
        }
    }

    private fun menuItemHandler(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_save_changes -> navigateBackToCurveConfig(curveData)
            R.id.item_dont_save_changes -> navigateBackToCurveConfig()
        }
        return true
    }

    private fun navigateBackToCurveConfig(curve: Curve? = null) {
        val curveDataString = if(curve != null) Json.encodeToString(curve) else null

        val directions = CurveSettingsFragmentDirections.actionCurveSettingsFragmentToCurveListFragment(
                args.lasName, args.trackIndex, curveDataString, args.curveIndex, args.trackName, args.trackID
        )

        view?.findNavController()?.navigate(directions)
    }

    private fun setColor(color: String) {
        curveData.apply {
            colorList.forEach {
                if(it[0] == color) {
                    this.curveColor = it[1]
                    return
                }
            }
        }
    }

    private fun getCurrColorIndex(): Int {
        val colors = resources.getStringArray(R.array.colors)

        colorList.forEach {
            if(it[1] == curveData.curveColor){
                return colors.indexOf(it[0])
            }
        }
        return -1
    }

    private fun getCurrStyleIndex(): Int{
        val styles = resources.getStringArray(R.array.line_style)
        return styles.indexOf(curveData.lineStyle)
    }

    private fun actionHandlerScaleMin(value: Float): Boolean {
        if (value < SCALE_MIN) {
            Utils.printMessage(view?.context, "Min Scale must be greater than $SCALE_MIN")
            return false
        }
        if (value >= curveData.scaleMax) {
            Utils.printMessage(view?.context, "Min Scale must be less than Max Scale")
            return false
        }
        curveData.scaleMin = value
        return true
    }

    private fun actionHandlerScaleMax(value: Float): Boolean {
//        if (value > SCALE_MAX) {
//            Utils.printMessage(view?.context, "Max Scale must be less than $SCALE_MAX")
//            return false
//        }
//        if (value <= curveData.scaleMin) {
//            Utils.printMessage(view?.context, "Max Scale must be greater than Min Scale")
//            return false
//        }
        curveData.scaleMax = value
        return true
    }
}