package com.example.cleaningschedule.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.cleaningschedule.databinding.AddTaskFragmentBinding
import com.example.cleaningschedule.databinding.RemovableRoomViewBinding
import com.example.cleaningschedule.databinding.SelectRoomsListBinding
import com.example.cleaningschedule.helpers.DatabaseHandler
import com.example.cleaningschedule.models.Occurrence
import com.example.cleaningschedule.models.Task
import com.example.cleaningschedule.viewmodels.AddTaskViewModel


class UpdateTaskFragment : Fragment() {

    private var _binding: AddTaskFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AddTaskViewModel
    private lateinit var name: String
    private lateinit var extraDetails: String
    private lateinit var rooms: MutableList<String>
    private var occurrence = -1

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var roomsList: MutableSet<String>

    private lateinit var checkedItems: BooleanArray

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddTaskFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[AddTaskViewModel::class.java]

        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        roomsList =  sharedPreferences.getStringSet("rooms", setOf())!!

        checkedItems = BooleanArray(roomsList.size) {false}

        val args = arguments?.let { ViewTaskFragmentArgs.fromBundle(it) }
        val taskId = args?.taskId

        val databaseHandler = DatabaseHandler(requireActivity().applicationContext)
        val (taskInfo, initialRooms) = databaseHandler.getTask(taskId!!)

        binding.taskEditText.setText(taskInfo[0])
        binding.extraDetailsEditText.setText(taskInfo[1])

        for (i in roomsList.indices) {
            if (roomsList.elementAt(i).lowercase() in initialRooms) {
                checkedItems[i] = true
            }
        }

        updateRooms(inflater = layoutInflater)

        binding.occurrenceDropdown.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, Occurrence.values())

        binding.occurrenceDropdown.setSelection(taskInfo[2].toInt())

        binding.saveButton.setOnClickListener {
            name = binding.taskEditText.text.toString()
            extraDetails = binding.extraDetailsEditText.text.toString()
            rooms = mutableListOf()
            for (i in checkedItems.indices) {
                if(checkedItems[i]) {
                    rooms.add(roomsList.elementAt(i))

                }
            }
            occurrence = binding.occurrenceDropdown.selectedItemPosition

            val status = databaseHandler.updateTask(Task(name, extraDetails, rooms, occurrence), taskId)

//            if (status < 0) {
//                //TODO Display error message
//            }

            val navController = view.findNavController()
            val id = navController.currentDestination?.id
            navController.popBackStack(id!!, true)
        }

        binding.addRoomButton.setOnClickListener{
            showCustomDialog()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private lateinit var alertDialog: AlertDialog
    private fun showCustomDialog() {
        val inflater: LayoutInflater = layoutInflater
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val dialogLayout: SelectRoomsListBinding = SelectRoomsListBinding.inflate(inflater)

        val roomArray = arrayOfNulls<Pair <String, Boolean>>(roomsList.size)
        for (i in roomsList.indices) {
            roomArray[i] = Pair(roomsList.elementAt(i), checkedItems.elementAt(i))
        }

        dialogLayout.roomsList.adapter = ArrayAdapter<Pair <String, Boolean>>(requireContext(), android.R.layout.simple_list_item_multiple_choice, roomArray)
        dialogLayout.roomsList.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        for (i in checkedItems.indices) {
            if (checkedItems.elementAt(i)) {
                dialogLayout.roomsList.setItemChecked(i, true)
                dialogLayout.roomsList.setSelection(i)
            }
        }

        dialogLayout.roomsList.setOnItemClickListener{_, view, position, _ ->
            val v = view as CheckedTextView
            checkedItems[position] = v.isChecked
        }

        dialogBuilder.setTitle("Select Rooms")
        dialogBuilder.setPositiveButton("Done") { dialog, _ ->
            dialog.dismiss()
        }
        dialogBuilder.setView(dialogLayout.root)
        dialogBuilder.setOnDismissListener {
            binding.selectedRooms.removeAllViews()

            updateRooms(inflater)
        }

        dialogBuilder.show()
    }

    private fun updateRooms(inflater: LayoutInflater) {
        for (i in checkedItems.indices) {
            if (checkedItems[i]) {
                val roomView: RemovableRoomViewBinding = RemovableRoomViewBinding.inflate(inflater)
                roomView.roomText.text = roomsList.elementAt(i)
                roomView.removeButton.tag = i
                roomView.removeButton.setOnClickListener { v ->
                    checkedItems[v.tag.toString().toInt()] = false
                    binding.selectedRooms.removeViewAt(v.tag.toString().toInt())
                }
                binding.selectedRooms.addView(roomView.root)
            }
        }
    }

}
