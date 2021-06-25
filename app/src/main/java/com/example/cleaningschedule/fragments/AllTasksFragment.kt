package com.example.cleaningschedule.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cleaningschedule.R
import com.example.cleaningschedule.helpers.DatabaseHandler
import com.example.cleaningschedule.helpers.TasksAdapter
import kotlinx.android.synthetic.main.to_do_list_fragment.*

class AllTasksFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.to_do_list_fragment, container, false)
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
        tasksList.adapter = adapter
        tasksList.layoutManager = LinearLayoutManager(requireActivity().applicationContext)
    }
}
