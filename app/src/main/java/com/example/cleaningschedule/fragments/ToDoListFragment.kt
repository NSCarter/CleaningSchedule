package com.example.cleaningschedule.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.cleaningschedule.R
import com.example.cleaningschedule.helpers.DatabaseHandler
import com.example.cleaningschedule.viewmodels.ToDoListViewModel
import kotlinx.android.synthetic.main.task_card.view.*
import kotlinx.android.synthetic.main.to_do_list_fragment.*


class ToDoListFragment : Fragment() {

    companion object {
        fun newInstance() = ToDoListFragment()
    }

    private lateinit var viewModel: ToDoListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.to_do_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ToDoListViewModel::class.java)
        setHasOptionsMenu(true)
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

        val databaseHandler = DatabaseHandler(activity!!.applicationContext)
        val tasks = databaseHandler.getTasks()

        for(task in tasks) {
            val taskView = LayoutInflater.from(activity!!.applicationContext).inflate(R.layout.task_card, tasksList, false)
            taskView.taskName.text = task[1]
            taskView.extraDetails.text = task[2]
            taskView.occurrence.text = task[3]
            // TODO Use recycler view instead
            tasksList.addView(taskView)
        }
    }
}
