package com.example.cleaningschedule.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.cleaningschedule.models.Task

class DatabaseHandler(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 4
        private const val DATABASE_NAME = "TaskDatabase"
        private const val TABLE_TASKS = "TaskTable"
        private const val TABLE_ROOMS = "RoomTable"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_EXTRA_DETAILS = "extraDetails"
        private const val KEY_ROOM = "room"
        private const val KEY_OCCURRENCE = "occurrence"
        private const val KEY_TASK_ID = "taskId"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTasksTable = ("CREATE TABLE " + TABLE_TASKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT NOT NULL,"
                + KEY_EXTRA_DETAILS + " TEXT,"
                + KEY_OCCURRENCE + " INTEGER)")
        db?.execSQL(createTasksTable)

        val createRoomsTable = ("CREATE TABLE " + TABLE_ROOMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TASK_ID + " INTEGER NOT NULL,"
                + KEY_ROOM + " TEXT NOT NULL,"
                + "FOREIGN KEY(" + KEY_TASK_ID + ") REFERENCES " + TABLE_TASKS + "(" + KEY_ID + ") ON DELETE CASCADE)")
        db?.execSQL(createRoomsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ROOMS")
        onCreate(db)
    }

    fun addTask(tmp: Task): Long {
        val db = this.writableDatabase

        val taskContentValues = ContentValues()
        taskContentValues.put(KEY_NAME, tmp.taskName)
        taskContentValues.put(KEY_EXTRA_DETAILS, tmp.extraDetails)
        taskContentValues.put(KEY_OCCURRENCE, tmp.occurrence)
        val taskInsertResult = db.insert(TABLE_TASKS, null, taskContentValues)

        val roomContentValues = ContentValues()
        var success = 0L
        for (room in tmp.rooms) {
            roomContentValues.put(KEY_TASK_ID, taskInsertResult)
            roomContentValues.put(KEY_ROOM, room)
            success = db.insert(TABLE_ROOMS, null, roomContentValues)
        }

        db.close()
        return success
    }

    fun getTasks(): MutableList<Pair<MutableList<String>, MutableList<String>>> {
        val db = this.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM $TABLE_TASKS", null)
        val tasks = mutableListOf<Pair<MutableList<String>, MutableList<String>>>()

        if(cursor.moveToFirst()) {
            do {
                val task = mutableListOf<String>()
                val id = cursor.getString(0)
                task.add(id)
                task.add(cursor.getString(1))
                task.add(cursor.getString(2))
                task.add(cursor.getString(3))

                val roomsCursor = db.rawQuery("SELECT * FROM $TABLE_ROOMS WHERE $KEY_TASK_ID=$id", null)
                val rooms = mutableListOf<String>()

                if(roomsCursor.moveToFirst()) {
                    do {
                        rooms.add(roomsCursor.getString(2))
                    } while (roomsCursor.moveToNext())
                }
                roomsCursor.close()

                tasks.add(Pair(task, rooms))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return tasks
    }

    fun getTask(id: Int): Pair<MutableList<String>, MutableList<String>> {
        val db = this.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM $TABLE_TASKS WHERE $KEY_ID=$id", null)
        var taskInfo = Pair(mutableListOf(String()), mutableListOf(String()))

        if(cursor.moveToFirst()) {
            do {
                val task = mutableListOf<String>()
                task.add(cursor.getString(1))
                task.add(cursor.getString(2))
                task.add(cursor.getString(3))

                val roomsCursor = db.rawQuery("SELECT * FROM $TABLE_ROOMS WHERE $KEY_TASK_ID=$id", null)
                val rooms = mutableListOf<String>()

                if(roomsCursor.moveToFirst()) {
                    do {
                        rooms.add(roomsCursor.getString(2))
                    } while (roomsCursor.moveToNext())
                }
                roomsCursor.close()
                taskInfo = Pair(task, rooms)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return taskInfo
    }

    fun deleteTask(id: Int): Int {
        val db = this.readableDatabase

        val result = db.delete(TABLE_TASKS, "$KEY_ID=$id", null)

        db.close()
        return result
    }

    fun updateTask(tmp: Task, id: Int): Long {
        val db = this.writableDatabase

        val taskContentValues = ContentValues()
        taskContentValues.put(KEY_NAME, tmp.taskName)
        taskContentValues.put(KEY_EXTRA_DETAILS, tmp.extraDetails)
        taskContentValues.put(KEY_OCCURRENCE, tmp.occurrence)
        val taskInsertResult = db.update(TABLE_TASKS, taskContentValues, "$KEY_ID=$id", null)

        val roomContentValues = ContentValues()
        var success = 0L
        for (room in tmp.rooms) {
            roomContentValues.put(KEY_TASK_ID, taskInsertResult)
            roomContentValues.put(KEY_ROOM, room)
            success = db.insert(TABLE_ROOMS, null, roomContentValues)
        }

        db.close()
        return success
    }
}