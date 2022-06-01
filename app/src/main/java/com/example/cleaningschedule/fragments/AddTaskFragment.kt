package com.example.cleaningschedule.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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


class AddTaskFragment : Fragment() {

    private lateinit var viewModel: AddTaskViewModel
    private lateinit var name: String
    private lateinit var extraDetails: String
    private lateinit var rooms: MutableList<String>
    private var occurrence = -1

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var roomsList: MutableSet<String>

    private lateinit var checkedItems: BooleanArray

    private var _binding: AddTaskFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddTaskFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[AddTaskViewModel::class.java]

        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        roomsList =  sharedPreferences.getStringSet("rooms", setOf())!!

        checkedItems = BooleanArray(roomsList.size) {false}

        binding.occurrenceDropdown.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, Occurrence.values())

        binding.saveButton.setOnClickListener {
            name = binding.taskEditText.text.toString()
            rooms = mutableListOf()
            for (i in checkedItems.indices) {
                if(checkedItems[i]) {
                    rooms.add(roomsList.elementAt(i))
                }
            }

            if (name == "" || rooms.isEmpty()) {
                // show error
            } else {
                extraDetails = binding.extraDetailsEditText.text.toString()
                occurrence = binding.occurrenceDropdown.selectedItemPosition

                val databaseHandler = DatabaseHandler(requireActivity().applicationContext)
                val status = databaseHandler.addTask(Task(name, extraDetails, rooms, occurrence))

//                if (status < 0) {
//                    //TODO Display error message
//                }

                val action = AddTaskFragmentDirections.actionAddTaskToToDoList()
                view.findNavController().navigate(action)
            }
        }

        binding.addRoomButton.setOnClickListener{
            showCustomDialog()
        }
    }

    private lateinit var alertDialog: AlertDialog
        private fun showCustomDialog() {
            val inflater: LayoutInflater = layoutInflater
            val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            val dialogLayout: SelectRoomsListBinding = SelectRoomsListBinding.inflate(inflater)
            val roomArray = arrayOfNulls<Pair <String, Boolean>>(roomsList.size)
            for (i in roomsList.indices) {
                roomArray[i] = Pair(roomsList.elementAt(i), true)
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
            dialogBuilder.setView(dialogLayout.root)
            dialogBuilder.setOnDismissListener {
                binding.selectedRooms.removeAllViews()

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

            dialogBuilder.show()
        }
    
}
