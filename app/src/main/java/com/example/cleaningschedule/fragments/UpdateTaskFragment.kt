package com.example.cleaningschedule.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.cleaningschedule.R
import com.example.cleaningschedule.helpers.DatabaseHandler
import com.example.cleaningschedule.models.Occurrence
import com.example.cleaningschedule.models.Room
import com.example.cleaningschedule.models.Task
import com.example.cleaningschedule.viewmodels.AddTaskViewModel
import kotlinx.android.synthetic.main.add_task_fragment.*
import kotlinx.android.synthetic.main.removable_room_view.view.*
import kotlinx.android.synthetic.main.select_rooms_list.view.*


class UpdateTaskFragment : Fragment() {

    companion object {
        fun newInstance() = AddTaskFragment()
    }

    private lateinit var viewModel: AddTaskViewModel
    private lateinit var name: String
    private lateinit var extraDetails: String
    private lateinit var rooms: MutableList<String>
    private var occurrence = -1

    private val checkedItems = BooleanArray(Room.values().size) {false}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.add_task_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddTaskViewModel::class.java)

        val args = arguments?.let { ViewTaskFragmentArgs.fromBundle(it) }
        val taskId = args?.taskId

        val databaseHandler = DatabaseHandler(requireActivity().applicationContext)
        val (taskInfo, initialRooms) = databaseHandler.getTask(taskId!!)

        taskEditText.setText(taskInfo[0])
        extraDetailsEditText.setText(taskInfo[1])

        for (i in Room.values().indices) {
            if (getString(Room.values()[i].Room) in initialRooms) {
                checkedItems[i] = true
            }
        }

        updateRooms(inflater = layoutInflater)

        occurrenceDropdown.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, Occurrence.values())

        occurrenceDropdown.setSelection(taskInfo[2].toInt())

        saveButton.setOnClickListener {
            name = taskEditText.text.toString()
            extraDetails = extraDetailsEditText.text.toString()
            rooms = mutableListOf()
            for (i in checkedItems.indices) {
                if(checkedItems[i]) {
                    rooms.add(getString(Room.values()[i].Room))

                }
            }
            occurrence = occurrenceDropdown.selectedItemPosition

            // TODO - All rooms need to be deleted and re added
            val status = databaseHandler.updateTask(Task(name, extraDetails, rooms, occurrence), taskId)

//            if (status < 0) {
//                //TODO Display error message
//            }
            
            val action = UpdateTaskFragmentDirections.actionUpdateTaskToViewTask(taskId)
            view.findNavController().navigate(action)
        }

        addRoomButton.setOnClickListener{
            showCustomDialog()
        }
    }

    private lateinit var alertDialog: AlertDialog
    private fun showCustomDialog() {
        val inflater: LayoutInflater = layoutInflater
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val dialogLayout = inflater.inflate(R.layout.select_rooms_list, null)

        val roomArray = arrayOfNulls<Pair <String, Boolean>>(Room.values().size)
        for (i in Room.values().indices) {
            roomArray[i] = Pair(getString(Room.values()[i].Room), true)
        }

        dialogLayout.roomsList.adapter = ArrayAdapter<Pair <String, Boolean>>(requireContext(), android.R.layout.simple_list_item_multiple_choice, roomArray)
        dialogLayout.roomsList.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        dialogLayout.roomsList.setOnItemClickListener{_, view, position, _ ->
            view.isSelected = !view.isSelected
            checkedItems[position] = view.isSelected
        }

        dialogBuilder.setTitle("Select Rooms")
        dialogBuilder.setPositiveButton("Done") { dialog, _ ->
            dialog.dismiss()
        }
        dialogBuilder.setView(dialogLayout)
        dialogBuilder.setOnDismissListener {
            selectedRooms.removeAllViews()

            updateRooms(inflater)
        }

        dialogBuilder.show()
    }

    private fun updateRooms(inflater: LayoutInflater) {
        for (i in checkedItems.indices) {
            if (checkedItems[i]) {
                val roomView = inflater.inflate(R.layout.removable_room_view, null)
                roomView.roomText.text = getString(Room.values()[i].Room)
                roomView.removeButton.tag = i
                roomView.removeButton.setOnClickListener { v ->
                    checkedItems[v.tag.toString().toInt()] = false
                    selectedRooms.removeViewAt(v.tag.toString().toInt())
                }
                selectedRooms.addView(roomView)
            }
        }
    }

}
