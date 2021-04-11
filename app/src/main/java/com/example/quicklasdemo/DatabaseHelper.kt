package com.example.quicklasdemo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.quicklasdemo.data.Track
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DatabaseHelper(context: Context,
                     factory: SQLiteDatabase.CursorFactory?) :
                     SQLiteOpenHelper(context, DB_NAME, factory, VERSION) {

    override fun onCreate(db: SQLiteDatabase?){
        val createTable = ("CREATE TABLE " + TABLE_TRACK_LISTS + "(" +
                            COLUMN_ID + " INT PRIMARY KEY," +
                            COLUMN_NAME + " TEXT," +
                            COLUMN_SERIALIZED_LIST + " TEXT," +
                            "UNIQUE(" + COLUMN_NAME + ") ON CONFLICT REPLACE" + ")")

        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop Table if exists $TABLE_TRACK_LISTS")
    }

    fun addTracksList(lasName: String, tracksDataList : MutableList<Track>){
        val serializedTrackList = Json.encodeToString(tracksDataList)

        val values = ContentValues()
        values.put(COLUMN_NAME, lasName)
        values.put(COLUMN_SERIALIZED_LIST, serializedTrackList)

        val db = this.writableDatabase
        db.insert(TABLE_TRACK_LISTS, null, values)
        db.close()
    }

    fun getTracksList(lasName: String): MutableList<Track>?{
        val query = "SELECT * FROM $TABLE_TRACK_LISTS WHERE $COLUMN_NAME = \"$lasName\""
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        lateinit var serializedTrackList : String

        if(cursor.moveToFirst()) {
            cursor.moveToFirst()
            serializedTrackList = cursor.getString(2)

            val tracksDataList : MutableList<Track> = Json.decodeFromString(serializedTrackList)

            cursor.close()
            db.close()
            return tracksDataList
        }

        db.close()
        return null
    }

    fun deleteTracksList(lasName: String) : Boolean {
        val query = "SELECT * FROM $TABLE_TRACK_LISTS WHERE $COLUMN_NAME = \"$lasName\""
        var result = false

        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        if(cursor.moveToFirst()){
            db.delete(TABLE_TRACK_LISTS, "$COLUMN_NAME = ?", arrayOf(lasName))
            cursor.close()
            result = true
        }

        db.close()
        return result
    }

    companion object{
        private const val VERSION = 1
         const val DB_NAME = "LasSettingsData.db"
        const val TABLE_TRACK_LISTS = "track_lists"

        const val COLUMN_ID = "_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_SERIALIZED_LIST = "serialized_list"
    }
}