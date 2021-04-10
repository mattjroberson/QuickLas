package com.example.quicklasdemo.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.example.quicklasdemo.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        iv_file_selector.setOnClickListener() { _ -> openFile() }
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.also { uri ->
                //TODO: Pass raw las on to app somehow
                val lasFileData = readTextFromUri(uri)
                launchTrackSettingsActivity()
            }
        }
    }

    private fun launchTrackSettingsActivity(){
        val intent = Intent(this, TrackConfigActivity::class.java)
        startActivity(intent)
    }

    private fun openFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/octet-stream"
        }

        startForResult.launch(intent)
    }

    @Throws(IOException::class)
    private fun readTextFromUri(uri: Uri): ArrayList<String> {
        val stringList = ArrayList<String>()
        contentResolver?.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                var line: String? = reader.readLine()
                while (line != null) {
                    stringList.add(line)
                    line = reader.readLine()
                }
            }
        }
        return stringList
    }
}