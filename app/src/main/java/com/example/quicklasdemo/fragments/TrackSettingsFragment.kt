package com.example.quicklasdemo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quicklasdemo.R
import com.example.quicklasdemo.RvAdapter
import com.example.quicklasdemo.Toolbar
import com.example.quicklasdemo.rv_items.RvItem
import kotlinx.android.synthetic.main.fragment_track_settings.*

class TrackSettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_track_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Toolbar(view, "Well Name", "Track Settings",
            R.id.toolbar_track_settings, R.menu.menu_track_settings, ::menuItemHandler)

        val trackList = mutableListOf(
            RvItem("Track Name"),
            RvItem("Curves"),
//            TrackSettingBoolItem("Fill Between Curves"),
//            TrackSettingBoolItem("Color Cut-offs"),
//            TrackSettingBoolItem("Show Grid"),
//            TrackSettingBoolItem("Use Log Scale"),
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

    }

    private fun discardAndReturn(){

    }
}