package com.example.crud

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.crud.database.DatabaseHelper
import com.example.crud.model.TaskListModel

class AddTaskActivity : AppCompatActivity() {

    lateinit var btn_save: Button
    lateinit var btn_del: Button
    lateinit var et_name: EditText
    lateinit var et_details: EditText
    var dbHandler: DatabaseHelper? = null
    var isEditMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_task)

        btn_save = findViewById(R.id.btn_save)
        btn_del = findViewById(R.id.btn_del)
        et_name = findViewById(R.id.et_name)
        et_details = findViewById(R.id.et_details)
        dbHandler = DatabaseHelper(this)

        if (intent != null && intent.getStringExtra("Mode") == "E") {
            // Update mode
            isEditMode = true
            btn_save.text = "Update Data"
            btn_del.visibility = View.VISIBLE

            val tasks: TaskListModel? = dbHandler?.getTask(intent.getIntExtra("Id", 0))
            tasks?.let {
                et_name.setText(it.name)
                et_details.setText(it.details)
            }
        } else {
            // Insert mode
            isEditMode = false
            btn_save.text = "Save Data"
            btn_del.visibility = View.GONE
        }

        btn_save.setOnClickListener {
            val success: Boolean
            val tasks = TaskListModel()
            tasks.name = et_name.text.toString()
            tasks.details = et_details.text.toString()

            success = if (isEditMode) {
                // Update mode
                tasks.id = intent.getIntExtra("Id", 0)
                dbHandler?.updateTask(tasks) ?: false
            } else {
                // Insert mode
                dbHandler?.addTask(tasks) ?: false
            }

            if (success) {
                val i = Intent(applicationContext, MainActivity::class.java)
                startActivity(i)
                finish()
            } else {
                Toast.makeText(applicationContext, "Something went wrong!", Toast.LENGTH_LONG).show()
            }
        }

        btn_del.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Info")
                .setMessage("Click yes if you want to delete the task")
                .setPositiveButton("Yes") { dialog, _ ->
                    val success = dbHandler?.deleteTask(intent.getIntExtra("Id", 0)) ?: false
                    if (success) {
                        finish()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(applicationContext, "Delete failed!", Toast.LENGTH_LONG).show()
                    }
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}
