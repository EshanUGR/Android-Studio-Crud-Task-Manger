package com.example.crud.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.crud.model.TaskListModel

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "task"
        private const val DB_VERSION = 1
        private const val TABLE_NAME = "tasklist"
        private const val ID = "id"
        private const val TASK_NAME = "taskname"
        private const val TASK_DETAILS = "taskdetails"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY, $TASK_NAME TEXT, $TASK_DETAILS TEXT);"
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db.execSQL(DROP_TABLE)
        onCreate(db)
    }

    fun getAllTask(): ArrayList<TaskListModel> {
        val taskList = ArrayList<TaskListModel>()
        val db = readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        cursor.use {
            if (cursor != null && cursor.moveToFirst()) {
                val idIndex = cursor.getColumnIndex(ID)
                val nameIndex = cursor.getColumnIndex(TASK_NAME)
                val detailsIndex = cursor.getColumnIndex(TASK_DETAILS)

                do {
                    val tasks = TaskListModel()
                    tasks.id =
                        if (idIndex != -1) cursor.getInt(idIndex) else -1 // Default value if index not found
                    tasks.name = if (nameIndex != -1) cursor.getString(nameIndex) else ""
                    tasks.details = if (detailsIndex != -1) cursor.getString(detailsIndex) else ""
                    taskList.add(tasks)
                } while (cursor.moveToNext())
            }
        }

        return taskList
    }


    fun addTask(tasks: TaskListModel): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(TASK_NAME, tasks.name)
            put(TASK_DETAILS, tasks.details)
        }
        val _success = db.insert(TABLE_NAME, null, values)
        db.close()
        return _success != -1L
    }


    fun getTask(_id: Int): TaskListModel {
        val taskList = ArrayList<TaskListModel>()
        val tasks = TaskListModel()
        val db: SQLiteDatabase = writableDatabase
        val selectQuery = "SELECT *FROM $TABLE_NAME WHERE $ID=$_id"
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        cursor?.moveToFirst()
        val idIndex = cursor.getColumnIndex(ID)
        val nameIndex = cursor.getColumnIndex(TASK_NAME)
        val detailsIndex = cursor.getColumnIndex(TASK_DETAILS)



        tasks.id =
            if (idIndex != -1) cursor.getInt(idIndex) else -1 // Default value if index not found
        tasks.name = if (nameIndex != -1) cursor.getString(nameIndex) else ""
        tasks.details = if (detailsIndex != -1) cursor.getString(detailsIndex) else ""
      cursor.close()
        return tasks


    }
fun deleteTask(_id:Int):Boolean{

    val db:SQLiteDatabase=this.writableDatabase
    val _success:Long=db.delete(TABLE_NAME,ID+"=?", arrayOf(_id.toString())).toLong()
    db.close()
    return Integer.parseInt("$_success")!=-1
}




    fun updateTask(tasks:TaskListModel):Boolean
    {
        val db:SQLiteDatabase=this.writableDatabase
       val values=ContentValues()
        values.put(TASK_NAME,tasks.name)
        values.put(TASK_DETAILS,tasks.details)
val _success:Long=db.update(TABLE_NAME,values,ID+"?", arrayOf(tasks.id.toString())).toLong()
     db.close()
   return      Integer.parseInt("$_success")!=-1

    }







}
