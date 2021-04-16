package com.example.quicklasdemo.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.quicklasdemo.R
import kotlinx.android.synthetic.main.activity_track_config.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


class TrackConfigActivity : AppCompatActivity(R.layout.activity_track_config){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lasName = intent.getStringExtra("lasName")

        val bundle = bundleOf(
                "lasName" to lasName,
                "trackData" to null,
                "trackIndex" to -1)

        findNavController(R.id.nav_host_fragment_container)
                .setGraph(R.navigation.nav_graph, bundle)

    }
}
