package com.example.cleaningschedule.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cleaningschedule.R
import com.example.cleaningschedule.databinding.ToDoListFragmentBinding
import com.example.cleaningschedule.helpers.DatabaseHandler
import com.example.cleaningschedule.helpers.TasksAdapter

class AllTasksFragment : Fragment() {

    private var _binding: ToDoListFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ToDoListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.add_item -> {
                val action = ToDoListFragmentDirections.actionToDoListToAddTask()
                view?.findNavController()?.navigate(action)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val databaseHandler = DatabaseHandler(requireActivity().applicationContext)
        val tasks = databaseHandler.getTasks()
        val adapter = TasksAdapter(tasks)
        binding.tasksList.adapter = adapter
        binding.tasksList.layoutManager = LinearLayoutManager(requireActivity().applicationContext)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
