package com.example.cleaningschedule.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.cleaningschedule.models.Room
import com.example.cleaningschedule.models.Task
import java.text.SimpleDateFormat
import java.util.*


interface DatabaseHandlerInterface {
    fun getTasks(): MutableList<Pair<MutableList<String>, MutableList<Room>>>
}
class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION), DatabaseHandlerInterface {
        companion object {
            private const val DATABASE_VERSION = 5
            private const val DATABASE_NAME = "TaskDatabase"
            private const val TABLE_TASKS = "TaskTable"
            private const val TABLE_ROOMS = "RoomTable"
            private const val KEY_ID = "id"
            private const val KEY_NAME = "name"
            private const val KEY_EXTRA_DETAILS = "extraDetails"
            private const val KEY_ROOM = "room"
            private const val KEY_OCCURRENCE = "occurrence"
            private const val KEY_TASK_ID = "taskId"
            private const val KEY_NEXT_OCCURRENCE = "nextOccurrence"
            private const val KEY_COMPLETED = "completed"
        }

        override fun onCreate(db: SQLiteDatabase?) {
            val createTasksTable = ("CREATE TABLE " + TABLE_TASKS + "("
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_NAME + " TEXT NOT NULL,"
                    + KEY_EXTRA_DETAILS + " TEXT,"
                    + KEY_OCCURRENCE + " INTEGER,"
                    + KEY_NEXT_OCCURRENCE + " DATE)")
            db?.execSQL(createTasksTable)

            val createRoomsTable = ("CREATE TABLE " + TABLE_ROOMS + "("
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_TASK_ID + " INTEGER NOT NULL,"
                    + KEY_ROOM + " TEXT NOT NULL,"
                    + KEY_COMPLETED + " BOOLEAN DEFAULT FALSE,"
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
            taskContentValues.put(KEY_NEXT_OCCURRENCE, getDate())
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

        override fun getTasks(): MutableList<Pair<MutableList<String>, MutableList<Room>>> {
            val db = this.readableDatabase

            val cursor = db.rawQuery("SELECT * FROM $TABLE_TASKS", null)
            val tasks = mutableListOf<Pair<MutableList<String>, MutableList<Room>>>()

            if (cursor.moveToFirst()) {
                do {
                    val task = mutableListOf<String>()
                    val id = cursor.getString(0)
                    task.add(id)
                    task.add(cursor.getString(1))
                    task.add(cursor.getString(2))
                    task.add(cursor.getString(3))
                    task.add(cursor.getString(4))

                    val roomsCursor =
                        db.rawQuery("SELECT * FROM $TABLE_ROOMS WHERE $KEY_TASK_ID=$id", null)
                    val rooms = mutableListOf<Room>()

                    if (roomsCursor.moveToFirst()) {
                        do {
                            rooms.add(
                                Room(
                                    roomsCursor.getInt(0),
                                    roomsCursor.getString(2),
                                    roomsCursor.getInt(3) > 0
                                )
                            )
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

        fun getToDoTasks(): MutableList<Pair<MutableList<String>, MutableList<Room>>> {
            val db = this.readableDatabase

            val cursor = db.rawQuery(
                "SELECT * FROM $TABLE_TASKS WHERE $KEY_NEXT_OCCURRENCE<=${getDate()}",
                null
            )
            val tasks = mutableListOf<Pair<MutableList<String>, MutableList<Room>>>()

            if (cursor.moveToFirst()) {
                do {
                    val task = mutableListOf<String>()
                    val id = cursor.getString(0)
                    task.add(id)
                    task.add(cursor.getString(1))
                    task.add(cursor.getString(2))
                    task.add(cursor.getString(3))
                    task.add(cursor.getString(4))

                    val roomsCursor =
                        db.rawQuery("SELECT * FROM $TABLE_ROOMS WHERE $KEY_TASK_ID=$id", null)
                    val rooms = mutableListOf<Room>()

                    if (roomsCursor.moveToFirst()) {
                        do {
                            rooms.add(
                                Room(
                                    roomsCursor.getInt(0),
                                    roomsCursor.getString(2),
                                    roomsCursor.getInt(3) > 0
                                )
                            )
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

            if (cursor.moveToFirst()) {
                do {
                    val task = mutableListOf<String>()
                    task.add(cursor.getString(1))
                    task.add(cursor.getString(2))
                    task.add(cursor.getString(3))

                    val roomsCursor =
                        db.rawQuery("SELECT * FROM $TABLE_ROOMS WHERE $KEY_TASK_ID=$id", null)
                    val rooms = mutableListOf<String>()

                    if (roomsCursor.moveToFirst()) {
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
            val taskUpdateResult = db.update(TABLE_TASKS, taskContentValues, "$KEY_ID=$id", null)

            db.delete(TABLE_ROOMS, "$KEY_TASK_ID=$id", null)

            val roomContentValues = ContentValues()
            var success = 0L
            for (room in tmp.rooms) {
                roomContentValues.put(KEY_TASK_ID, taskUpdateResult)
                roomContentValues.put(KEY_ROOM, room)
                success = db.insert(TABLE_ROOMS, null, roomContentValues)
            }

            db.close()
            return success
        }

        fun updateRoom(id: Int, completed: Boolean): Int {
            val db = this.writableDatabase
            val roomContentValues = ContentValues()
            roomContentValues.put(KEY_COMPLETED, completed)
            val roomUpdateResult = db.update(TABLE_ROOMS, roomContentValues, "$KEY_ID=$id", null)

            db.close()
            return roomUpdateResult
        }

        fun completedTask(task: MutableList<String>): Int {
            val db = this.writableDatabase
            val id = task[0]

            val nextOccurrence = when (task[3].toInt()) {
                0 -> increaseDate(1)
                1 -> increaseDate(7)
                2 -> increaseMonth(1)
                3 -> increaseMonth(3)
                else -> increaseYear()
            }

            val contentValues = ContentValues()
            contentValues.put(KEY_NEXT_OCCURRENCE, nextOccurrence)
            val taskUpdateResult = db.update(TABLE_TASKS, contentValues, "$KEY_ID=$id", null)

            db.close()
            return taskUpdateResult
        }

        private fun getDate(): String {
            val sdf = SimpleDateFormat("yyyMMdd", Locale.ENGLISH)
            return sdf.format(Date())
        }

        private fun increaseDate(amount: Int): String {
            val sdf = SimpleDateFormat("yyyMMdd", Locale.ENGLISH)
            val date = Calendar.getInstance()
            date.add(Calendar.DATE, amount)
            return sdf.format(date.time)
        }

        private fun increaseMonth(amount: Int): String {
            val sdf = SimpleDateFormat("yyyMMdd", Locale.ENGLISH)
            val date = Calendar.getInstance()
            date.add(Calendar.MONTH, amount)
            return sdf.format(date.time)
        }

        private fun increaseYear(): String {
            val sdf = SimpleDateFormat("yyyMMdd", Locale.ENGLISH)
            val date = Calendar.getInstance()
            date.add(Calendar.YEAR, 1)
            return sdf.format(date.time)
        }
    }