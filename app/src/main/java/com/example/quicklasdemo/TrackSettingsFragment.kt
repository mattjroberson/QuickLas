package com.example.quicklasdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class TrackSettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_track_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Toolbar(view, "Well Name", "Track Settings",
            R.id.toolbar_track_settings, R.menu.menu_track_settings, ::menuItemHandler)
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