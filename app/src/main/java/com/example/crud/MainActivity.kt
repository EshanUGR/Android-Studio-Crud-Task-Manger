package com.example.crud

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crud.adapter.TaskListAdapter
import com.example.crud.database.DatabaseHelper
import com.example.crud.model.TaskListModel

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerTask: RecyclerView
    private lateinit var btnAdd: Button
    private var taskListAdapter: TaskListAdapter? = null
    private var dbHandler: DatabaseHelper? = null
    private var taskList: MutableList<TaskListModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        recyclerTask = findViewById(R.id.rv_list)
        btnAdd = findViewById(R.id.ft_add_items)

        dbHandler = DatabaseHelper(this)

        fetchList() // Corrected method name

        btnAdd.setOnClickListener {
            // Handle add button click here

            val i=Intent(applicationContext,AddTaskActivity::class.java)
            startActivity(i)
        }
    }

    private fun fetchList() { // Renamed method to follow naming conventions
        taskList.clear() // Clear the list before fetching new data
        taskList.addAll(dbHandler?.getAllTask() ?: emptyList())

        taskListAdapter = TaskListAdapter(taskList, applicationContext)
        recyclerTask.layoutManager = LinearLayoutManager(this)
        recyclerTask.adapter = taskListAdapter
        taskListAdapter?.notifyDataSetChanged()
    }
}
