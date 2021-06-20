package com.example.cleaningschedule.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cleaningschedule.R
import com.example.cleaningschedule.helpers.DatabaseHandler
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
        taskOccurrenceTextView.text = taskInfo[2]

        var roomsText = ""
        for (room in rooms) {
            roomsText += room
        }
        taskRoomsTextView.text = roomsText
    }
}