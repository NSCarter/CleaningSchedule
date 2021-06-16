package com.example.cleaningschedule.helpers

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cleaningschedule.R

class TasksAdapter (private val tasks: MutableList<Pair<MutableList<String>, MutableList<String>>>) : RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.taskName)
        val extraDetailsTextView: TextView = itemView.findViewById(R.id.extraDetails)
        val roomsList: LinearLayout = itemView.findViewById(R.id.rooms)
        val occurrenceTextView: TextView = itemView.findViewById(R.id.occurrence)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val taskView = inflater.inflate(R.layout.task_card, parent, false)
        return ViewHolder(taskView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (task, rooms) = tasks[position]
        holder.nameTextView.text = task[1]
        holder.extraDetailsTextView.text = task[2]
        holder.occurrenceTextView.text = task[3]

        for (room in rooms) {
            val checkBox = CheckBox(holder.itemView.context)
            checkBox.text = room
            checkBox.setOnClickListener{
                if(checkBox.isChecked) {
                    checkBox.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    checkBox.paintFlags = 0
                }
            }
            holder.roomsList.addView(checkBox)
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}
