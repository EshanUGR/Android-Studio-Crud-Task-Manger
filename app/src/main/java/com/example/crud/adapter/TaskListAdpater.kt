package com.example.crud.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.crud.AddTaskActivity
import com.example.crud.R
import com.example.crud.model.TaskListModel

class TaskListAdapter(taskList: List<TaskListModel>, private val context: Context) :
    RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    private var taskList: List<TaskListModel> = ArrayList()

    init {
        this.taskList = taskList
    }

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.txt_name)
        var details: TextView = view.findViewById(R.id.txt_details)
        var btn_edit: Button = view.findViewById(R.id.btn_edit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_task_list, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val tasks = taskList[position]
        holder.name.text = tasks.name
        holder.details.text = tasks.details

        holder.btn_edit.setOnClickListener {
            val intent = Intent(context, AddTaskActivity::class.java)
            intent.putExtra("Mode", "E")
            intent.putExtra("Id", tasks.id)
            context.startActivity(intent) // Use startActivity instead of startActivities
        }
    }
}
