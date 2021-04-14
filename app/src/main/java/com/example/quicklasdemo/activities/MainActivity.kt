package com.example.quicklasdemo.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.quicklasdemo.DatabaseHelper
import com.example.quicklasdemo.LasParser
import com.example.quicklasdemo.R
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = DatabaseHelper(this.applicationContext)

        iv_file_selector.setOnClickListener() { _ -> openFile() }
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.also { uri ->

                val lasName = uri.path!!.substringAfterLast("/").split(".")[0]

                //Read LAS from file if not already in a database
                if(!db.hasTable(lasName)){
                    readLasDataIntoDB(lasName, uri)
                }

                launchTrackSettingsActivity(lasName)
            }
        }
    }

    private fun launchTrackSettingsActivity(fileName: String){
        val intent = Intent(this, TrackConfigActivity::class.java)
        intent.putExtra("lasName", fileName)
        startActivity(intent)
    }

    private fun openFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/octet-stream"
        }

        startForResult.launch(intent)
    }

    private fun readLasDataIntoDB(lasName: String, uri: Uri){
        val lasFileData = readTextFromUri(uri)
        val curves = LasParser.parse(lasFileData)

        db.addLasData(lasName, curves);
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