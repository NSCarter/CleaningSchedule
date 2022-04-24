package com.example.cleaningschedule.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.cleaningschedule.R
import com.example.cleaningschedule.databinding.FragmentViewTaskBinding
import com.example.cleaningschedule.helpers.DatabaseHandler
import com.example.cleaningschedule.models.Occurrence

class ViewTaskFragment : Fragment() {

    private var _binding: FragmentViewTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments?.let { ViewTaskFragmentArgs.fromBundle(it) }
        val taskId = args?.taskId

        val databaseHandler = DatabaseHandler(requireActivity().applicationContext)
        val (taskInfo, rooms) = databaseHandler.getTask(taskId!!)

        binding.taskNameTextView.text = taskInfo[0]
        binding.taskExtraDetailsTextView.text = taskInfo[1]
        binding.taskOccurrenceTextView.text = Occurrence.values()[taskInfo[2].toInt()].toString()

        var roomsText = ""
        for (room in rooms) {
            roomsText += room
        }
        binding.taskRoomsTextView.text = roomsText

        binding.updateButton.setOnClickListener {
            val action = ViewTaskFragmentDirections.actionViewTaskToUpdateTask(taskId)
            view.findNavController().navigate(action)
        }

        binding.deleteButton.setOnClickListener {
            showDialog(taskId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDialog(id: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(getString(R.string.delete_confirmation))
        builder.setPositiveButton(R.string.delete)
            { _, _ ->
                val databaseHandler = DatabaseHandler(requireActivity().applicationContext)
                databaseHandler.deleteTask(id)
                val action = ViewTaskFragmentDirections.actionViewTaskToToDoList()
                view?.findNavController()?.navigate(action)
            }
        builder.setNegativeButton(R.string.cancel)
        { _, _ ->  }
        builder.show()
    }
}