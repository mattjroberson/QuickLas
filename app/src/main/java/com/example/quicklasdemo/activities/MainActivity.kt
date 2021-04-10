package com.example.quicklasdemo.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.quicklasdemo.R
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

private const val FILE_PICKER_REQUEST_CODE = 2

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        iv_file_selector.setOnClickListener() { _ -> openFile() }
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.also { uri ->
                val lasFileData = readTextFromUri(uri)
                launchTrackSettingsActivity(lasFileData)
            }
        }
    }

    private fun launchTrackSettingsActivity(lasFileData: ArrayList<String>){
        val intent = Intent(this, TrackConfigActivity::class.java)
        //TODO: Pass lasData to the TrackConfigActivitySomehow
        //intent.putExtra("lasFileData", lasFileData)
        startActivity(intent)
    }

    private fun openFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/octet-stream"
        }

        startForResult.launch(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)

        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            resultData?.data?.also { uri ->
                //val lasFileData = readTextFromUri(uri)
                //launchTrackSettingsActivity(lasFileData)
            }
        }
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