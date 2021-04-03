package com.example.quicklasdemo

import android.os.Bundle
import android.view.*
import android.view.Menu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_track_setup.*

class TrackSetupFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_track_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val trackList = mutableListOf(
            TrackItem("Track 1"),
            TrackItem("Track 2"),
            TrackItem("Track 3"),
            TrackItem("Track 4")
        )

        val adapter = TrackListAdapter(trackList)
        rv_track_list.adapter = adapter
        rv_track_list.layoutManager = LinearLayoutManager(view.context)

    }
}