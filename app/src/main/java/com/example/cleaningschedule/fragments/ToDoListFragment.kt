package com.example.cleaningschedule.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cleaningschedule.R
import com.example.cleaningschedule.databinding.ToDoListFragmentBinding
import com.example.cleaningschedule.helpers.DatabaseHandler
import com.example.cleaningschedule.helpers.TasksAdapter
import com.example.cleaningschedule.viewmodels.ToDoListViewModel


class ToDoListFragment : Fragment() {

    private lateinit var viewModel: ToDoListViewModel

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
        viewModel = ViewModelProvider(this)[ToDoListViewModel::class.java]
        setHasOptionsMenu(true)

        val databaseHandler = DatabaseHandler(requireActivity().applicationContext)
        val tasks = databaseHandler.getToDoTasks()

        val iterator = tasks.iterator()
        while(iterator.hasNext()){
            val nextTask = iterator.next()
            val (task, rooms) = nextTask

            var finished = true

            for (room in rooms) {
                if (!room.isChecked) finished = false
            }

            if (finished) {
                databaseHandler.completedTask(task)
                iterator.remove()
            }
        }

        val adapter = TasksAdapter(tasks)
        binding.tasksList.adapter = adapter
        binding.tasksList.layoutManager = LinearLayoutManager(requireActivity().applicationContext)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
