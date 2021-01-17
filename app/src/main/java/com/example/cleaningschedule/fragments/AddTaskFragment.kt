package com.example.cleaningschedule.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.cleaningschedule.R
import com.example.cleaningschedule.models.Occurrence
import com.example.cleaningschedule.models.Room
import com.example.cleaningschedule.viewmodels.AddTaskViewModel
import kotlinx.android.synthetic.main.add_task_fragment.*
import kotlinx.android.synthetic.main.removable_room_view.view.*
import kotlinx.android.synthetic.main.select_rooms_list.view.*


class AddTaskFragment : Fragment() {

    companion object {
        fun newInstance() = AddTaskFragment()
    }

    private lateinit var viewModel: AddTaskViewModel
    private lateinit var name: String
    private lateinit var extraDetails: String
    private lateinit var rooms: Array<String>
    private lateinit var occurrence: Occurrence

    val checkedItems = BooleanArray(Room.values().size) {false}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.add_task_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddTaskViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        occurrenceDropdown.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, Occurrence.values())

        saveButton.setOnClickListener {
            //TODO Create task object
            name = taskEditText.text.toString()
            extraDetails = extraDetailsEditText.text.toString()

            //Probably wont need this
            taskEditText.text.clear()
            extraDetailsEditText.text.clear()
            viewModel.saveTask(view, name, extraDetails)
        }

        addRoomButton.setOnClickListener{
            showCustomDialog()
        }
    }

    private lateinit var alertDialog: AlertDialog
        fun showCustomDialog() {
            val inflater: LayoutInflater = layoutInflater
            val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context!!)
            val dialogLayout = inflater.inflate(R.layout.select_rooms_list, null)

            val roomArray = arrayOfNulls<Pair <String, Boolean>>(Room.values().size)
            for (i in Room.values().indices) {
                roomArray[i] = Pair(getString(Room.values()[i].Room), true)
            }

            dialogLayout.roomsList.adapter = ArrayAdapter<Pair <String, Boolean>>(context!!, android.R.layout.simple_list_item_multiple_choice, roomArray)
            dialogLayout.roomsList.choiceMode = ListView.CHOICE_MODE_MULTIPLE
            dialogLayout.roomsList.setOnItemClickListener{parent, view, position, id ->
                view.isSelected = !view.isSelected
                checkedItems[position] = view.isSelected
            }

            dialogBuilder.setTitle("Select Rooms")
            dialogBuilder.setPositiveButton("Done") { dialog, which ->
                dialog.dismiss()
            }
            dialogBuilder.setView(dialogLayout)
            dialogBuilder.setOnDismissListener(object  : DialogInterface.OnDismissListener {
                override fun onDismiss(dialog: DialogInterface?) {
                    selectedRooms.removeAllViews()

                    for (i in checkedItems.indices) {
                        if(checkedItems[i]) {
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
            })

            dialogBuilder.show()
        }
    
}
