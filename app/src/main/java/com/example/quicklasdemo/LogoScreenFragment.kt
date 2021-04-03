package com.example.quicklasdemo

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_logo_screen.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

private const val FILE_PICKER_REQUEST_CODE = 2

class LogoScreenFragment : Fragment(R.layout.fragment_logo_screen) {
    private var lasFileData: MutableList<String>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button.setOnClickListener { _ -> openFile() }
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int, resultData: Intent?) {
        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            resultData?.data?.also { uri ->
                lasFileData = readTextFromUri(uri)
                view?.findNavController()?.navigate(R.id.action_logoScreenFragment_to_trackSetupFragment)
            }
        }
    }

    private fun openFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/octet-stream"
        }

        startActivityForResult(intent, FILE_PICKER_REQUEST_CODE)
    }

    @Throws(IOException::class)
    private fun readTextFromUri(uri: Uri): MutableList<String> {
        var stringList = mutableListOf<String>()
        context?.contentResolver?.openInputStream(uri)?.use { inputStream ->
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