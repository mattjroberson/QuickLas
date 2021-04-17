package com.example.quicklasdemo.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quicklasdemo.*
import com.example.quicklasdemo.rv_items.RvEntryItem
import kotlinx.android.synthetic.main.fragment_graph_list.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class GraphListFragment : Fragment(R.layout.fragment_graph_list) {
    private var graphItems = mutableListOf<RvEntryItem>()
    private var graphNames = mutableListOf<String>()
    private lateinit var db: DatabaseHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = DatabaseHelper(view.context)

        Toolbar(view, "Quick LAS", "Select Graph",
                R.id.toolbar_graph_list, R.menu.menu_track_list, ::menuItemHandler)

        graphNames = db.getGraphNames()

        graphNames.forEach{
            graphItems.add(RvEntryItem(it, ::actionHandler))
        }

        connectRecyclerViewAdapter(view)
    }

        private fun connectRecyclerViewAdapter(view: View){
        val graphListAdapter = RvAdapter(graphItems, view)

        rv_graph_list.apply{
            adapter = graphListAdapter
            layoutManager = LinearLayoutManager(view.context)
        }
    }

    private fun menuItemHandler(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add_track -> openFile()
        }
        return true
    }

    private fun actionHandler(item: RvEntryItem, type: RvEntryItem.Companion.ActionType){
        when(type){
            RvEntryItem.Companion.ActionType.LABEL_ACTION -> gotoGraph(item)
            RvEntryItem.Companion.ActionType.EDIT -> editGraph(item)
            RvEntryItem.Companion.ActionType.DELETE -> deleteGraph(item)
        }
    }

    private fun addGraph(){
        graphItems.add(RvEntryItem(graphNames.last(), ::actionHandler))

        rv_graph_list.adapter?.apply {
            notifyItemInserted(graphItems.size)
            notifyItemRangeChanged(graphItems.size, graphItems.size)
        }
    }

    private fun editGraph(item: RvEntryItem){
        val position = graphItems.indexOf(item)
        navigateIntoTrackList(position)
    }

    private fun deleteGraph(item: RvEntryItem) {
        graphItems.apply {
            val position = indexOf(item)
            removeAt(position)

            db.deleteTable(graphNames[position])
            graphNames = db.getGraphNames()

            rv_graph_list.adapter?.apply {
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, size)
            }
        }
    }

    fun gotoGraph(item: RvEntryItem){
        val tracksData = db.getTrackList(item.title)!!

        if(tracksData.size > 0) {
            //val intent = Intent(activity, REPLACE_WITH_GRAPH_ACTIVITY::class.java)
            //intent.putExtra("lasName", args.lasName)
            //startActivity(intent)
        }
        else{
            Utils.printMessage(view?.context,"Must have at least one track")
        }
    }

    private fun navigateIntoTrackList(index: Int) {
        val directions = GraphListFragmentDirections.actionGraphListFragmentToTrackSetupFragment(graphNames[index], null, -1)
        view?.findNavController()?.navigate(directions)
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.also { uri ->

                val lasName = uri.path!!.substringAfterLast("/").split(".")[0]

                //Read LAS from file if not already in a database
                if (!db.hasTable(lasName)) {
                    readLasDataIntoDB(lasName, uri)
                    graphNames = db.getGraphNames()

                    addGraph()
                }
            }
        }
    }

    private fun openFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/octet-stream"
        }

        startForResult.launch(intent)
    }

    private fun readLasDataIntoDB(lasName: String, uri: Uri) {
        val lasFileData = readTextFromUri(uri)
        val curves = LasParser.parse(lasFileData)

        db.addLasData(lasName, curves)
    }

    @Throws(IOException::class)
    private fun readTextFromUri(uri: Uri): ArrayList<String> {
        val stringList = ArrayList<String>()
        view?.context?.contentResolver?.openInputStream(uri)?.use { inputStream ->
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