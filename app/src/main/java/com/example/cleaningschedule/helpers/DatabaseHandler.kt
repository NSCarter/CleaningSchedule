package com.example.cleaningschedule.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.cleaningschedule.models.Task

class DatabaseHandler(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "TaskDatabase"
        private const val TABLE_TASKS = "TaskTable"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_EXTRA_DETAILS = "extraDetails"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTasksTable = ("CREATE TABLE " + TABLE_TASKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT NOT NULL,"
                + KEY_EXTRA_DETAILS + " TEXT NOT NULL" + ")")
        db?.execSQL(createTasksTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")
        onCreate(db)
    }

    fun addTask(tmp: Task): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, tmp.taskName)
        contentValues.put(KEY_EXTRA_DETAILS, tmp.extraDetails)
        val success = db.insert(TABLE_TASKS, null, contentValues)
        db.close()
        Log.d("DATABASE", success.toString())
        return success
    }
}