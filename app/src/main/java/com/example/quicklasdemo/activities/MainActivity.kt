package com.example.quicklasdemo.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quicklasdemo.DatabaseHelper
import com.example.quicklasdemo.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = DatabaseHelper(this.applicationContext)

        iv_file_selector.setOnClickListener{ launchTrackSettingsActivity() }
    }

    private fun launchTrackSettingsActivity(){
        val intent = Intent(this, TrackConfigActivity::class.java)
        startActivity(intent)
    }
}