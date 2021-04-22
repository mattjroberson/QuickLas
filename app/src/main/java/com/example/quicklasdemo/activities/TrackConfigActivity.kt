package com.example.quicklasdemo.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.example.quicklasdemo.R

class TrackConfigActivity : AppCompatActivity(R.layout.activity_track_config){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val bundle = bundleOf(
                "trackData" to null,
                "trackIndex" to -1)

        findNavController(R.id.nav_host_fragment_container)
                .setGraph(R.navigation.nav_graph, bundle)

    }
}
