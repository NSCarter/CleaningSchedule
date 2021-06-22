package com.example.cleaningschedule.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.cleaningschedule.R
import com.example.cleaningschedule.helpers.DatabaseHandler
import com.example.cleaningschedule.models.Occurrence
import kotlinx.android.synthetic.main.fragment_view_task.*

class ViewTaskFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments?.let { ViewTaskFragmentArgs.fromBundle(it) }
        val taskId = args?.taskId

        val databaseHandler = DatabaseHandler(requireActivity().applicationContext)
        val (taskInfo, rooms) = databaseHandler.getTask(taskId!!)

        taskNameTextView.text = taskInfo[0]
        taskExtraDetailsTextView.text = taskInfo[1]
        taskOccurrenceTextView.text = Occurrence.values()[taskInfo[2].toInt()].toString()

        var roomsText = ""
        for (room in rooms) {
            roomsText += room
        }
        taskRoomsTextView.text = roomsText

        updateButton.setOnClickListener {
            val action = ViewTaskFragmentDirections.actionViewTaskToUpdateTask(taskId)
            view.findNavController().navigate(action)
        }

        deleteButton.setOnClickListener {
            showDialog(taskId)
        }
    }

    private fun showDialog(id: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Are you sure you want to delete this task?")
        builder.setPositiveButton("Delete")
            { _, _ ->
                val databaseHandler = DatabaseHandler(requireActivity().applicationContext)
                databaseHandler.deleteTask(id)
                val action = ViewTaskFragmentDirections.actionViewTaskToToDoList()
                view?.findNavController()?.navigate(action)
            }
        builder.setNegativeButton("Cancel")
        { _, _ ->  }
        builder.show()
    }
}