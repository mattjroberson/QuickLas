package com.example.quicklasdemo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.quicklasdemo.data.Curve
import com.example.quicklasdemo.data.Track
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DatabaseHelper(context: Context):
                     SQLiteOpenHelper(context, DB_NAME, null, VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        createTable(db, TABLE_SETTINGS)
        createTable(db, TABLE_TEMP_TRACKS)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop Table if exists $TABLE_SETTINGS")
        db?.execSQL("drop Table if exists $TABLE_TEMP_TRACKS")
    }

    //region Add Data

    fun addTrackList(name: String, dataList: MutableList<Track>) {
        val serializedList = Json.encodeToString(dataList)
        addSerializedStringToList(name, TABLE_SETTINGS, serializedList)
    }

    fun addCurveList(name: String, dataList: MutableList<Curve>) {
        val serializedList = Json.encodeToString(dataList)
        addSerializedStringToList(name, TABLE_SETTINGS, serializedList)
    }

    fun addLasData(tableName: String, lasData: MutableMap<String, MutableList<Float>>){
       ensureTableCreated(tableName)

        lasData.forEach(){
            val serializedList = Json.encodeToString(it.value)
            addSerializedStringToList(it.key, tableName, serializedList)
        }
    }

    fun addTempTrack(name: String, track: Track) {
        val serializedData = Json.encodeToString(track)
        addSerializedStringToList(name, TABLE_TEMP_TRACKS, serializedData)
    }

    private fun addSerializedStringToList(name: String, table: String, serializedList: String) {
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_SERIALIZED_DATA, serializedList)

        val db = this.writableDatabase
        db.insert(table, null, values)
        db.close()
    }

    //endregion

    //region Get Data

    fun getTrackList(name: String): MutableList<Track>? {
        val serializedList = getSerializedStringFromList("$name.trackList", TABLE_SETTINGS)

        serializedList?.let {
            return Json.decodeFromString(it)
        }; return null
    }

    fun getCurveList(name: String): MutableList<Curve>? {
        val serializedList = getSerializedStringFromList(name, TABLE_SETTINGS)

        serializedList?.let {
            return Json.decodeFromString(it)
        }; return null
    }

    fun getLasData(lasName: String): MutableMap<String, MutableList<Float>>? {
        ensureTableCreated(lasName)

        val query = "SELECT * FROM $lasName"

        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        val lasData : MutableMap<String, MutableList<Float>> = mutableMapOf()

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val curveName = cursor.getString(1)

                val serializedList = cursor.getString(2)
                val curvePoints = Json.decodeFromString<MutableList<Float>>(serializedList)

                lasData[curveName] = curvePoints
                cursor.moveToNext()
            }

            cursor.close()
            db.close()
            return lasData
        }
        db.close()
        return null
    }

    fun getTempTrack(name: String): Track?{
        val serializedList = getSerializedStringFromList(name, TABLE_TEMP_TRACKS)

        serializedList?.let {
            return Json.decodeFromString(it)
        }; return null
    }

    private fun getSerializedStringFromList(name: String, table: String): String? {
        val query = "SELECT * FROM $table WHERE $COLUMN_NAME = \"$name\""
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        lateinit var serializedList: String

        if (cursor.moveToFirst()) {
            cursor.moveToFirst()
            serializedList = cursor.getString(2)

            cursor.close()
            db.close()
            return serializedList
        }
        db.close()
        return null
    }

    //endregion

    fun hasTable(tableName: String): Boolean{
        val cursor: Cursor = this.writableDatabase.rawQuery(
                ("select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                        + tableName) + "'", null)

        return cursor.count != 0;
    }

    private fun ensureTableCreated(tableName: String){
        if(!hasTable(tableName)){
            createTable(this.writableDatabase, tableName)
        }
    }

    private fun createTable(db: SQLiteDatabase?, tableName: String){
        val createTable = ("CREATE TABLE " + tableName + "(" +
                COLUMN_ID + " INT PRIMARY KEY," +
                COLUMN_NAME + " TEXT," +
                COLUMN_SERIALIZED_DATA + " TEXT," +
                "UNIQUE(" + COLUMN_NAME + ") ON CONFLICT REPLACE" + ")")

        db?.execSQL(createTable)
    }

    companion object{
        private const val VERSION = 1
        const val DB_NAME = "LasSettingsData.db"
        const val TABLE_SETTINGS = "settings"
        const val TABLE_TEMP_TRACKS = "temp_tracks"

        const val COLUMN_ID = "_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_SERIALIZED_DATA = "serialized_list"
    }
}