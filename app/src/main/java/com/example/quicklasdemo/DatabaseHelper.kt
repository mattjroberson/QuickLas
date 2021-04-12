package com.example.quicklasdemo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.quicklasdemo.data.Curve
import com.example.quicklasdemo.data.Track
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DatabaseHelper(context: Context,
                     factory: SQLiteDatabase.CursorFactory?) :
                     SQLiteOpenHelper(context, DB_NAME, factory, VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        createTable(db, TABLE_TRACK_LISTS)
        createTable(db, TABLE_CURVE_LISTS)
        createTable(db, TABLE_TRACK_OBJECTS)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop Table if exists $TABLE_TRACK_LISTS")
        db?.execSQL("drop Table if exists $TABLE_CURVE_LISTS")
        db?.execSQL("drop Table if exists $TABLE_TRACK_OBJECTS")
    }

    fun addTrackList(name: String, table: String, dataList: MutableList<Track>) {
        val serializedList = Json.encodeToString(dataList)
        addSerializedStringToList(name, table, serializedList)
    }

    fun addCurveList(name: String, table: String, dataList: MutableList<Curve>) {
        val serializedList = Json.encodeToString(dataList)
        addSerializedStringToList(name, table, serializedList)
    }

    fun addTrack(name: String, table: String, track: Track) {
        val serializedData = Json.encodeToString(track)
        addSerializedStringToList(name, table, serializedData)
    }

    private fun addSerializedStringToList(name: String, table: String, serializedList: String) {
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_SERIALIZED_LIST, serializedList)

        val db = this.writableDatabase
        db.insert(table, null, values)
        db.close()
    }

    fun getTrackList(name: String, table: String): MutableList<Track>? {
        val serializedList = getSerializedStringFromList(name, table)

        serializedList?.let {
            return Json.decodeFromString(it)
        }; return null
    }

    fun getCurveList(name: String, table: String): MutableList<Curve>? {
        val serializedList = getSerializedStringFromList(name, table)

        serializedList?.let {
            return Json.decodeFromString(it)
        }; return null
    }

    fun getTrack(name: String, table: String): Track?{
        val serializedList = getSerializedStringFromList(name, table)

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
//    fun deleteTracksList(lasName: String) : Boolean {
//        val query = "SELECT * FROM $TABLE_TRACK_LISTS WHERE $COLUMN_NAME = \"$lasName\""
//        var result = false
//
//        val db = this.writableDatabase
//        val cursor = db.rawQuery(query, null)
//
//        if(cursor.moveToFirst()){
//            db.delete(TABLE_TRACK_LISTS, "$COLUMN_NAME = ?", arrayOf(lasName))
//            cursor.close()
//            result = true
//        }
//
//        db.close()
//        return result
//    }

    private fun createTable(db: SQLiteDatabase?, tableName: String){
        val createTable = ("CREATE TABLE " + tableName + "(" +
                COLUMN_ID + " INT PRIMARY KEY," +
                COLUMN_NAME + " TEXT," +
                COLUMN_SERIALIZED_LIST + " TEXT," +
                "UNIQUE(" + COLUMN_NAME + ") ON CONFLICT REPLACE" + ")")

        db?.execSQL(createTable)
    }

    companion object{
        private const val VERSION = 1
        const val DB_NAME = "LasSettingsData.db"
        const val TABLE_TRACK_LISTS = "track_lists"
        const val TABLE_CURVE_LISTS = "curve_lists"
        const val TABLE_TRACK_OBJECTS = "track_objects"

        const val COLUMN_ID = "_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_SERIALIZED_LIST = "serialized_list"
    }
}