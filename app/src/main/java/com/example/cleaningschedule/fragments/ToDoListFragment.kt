package com.example.cleaningschedule.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.cleaningschedule.R
import com.example.cleaningschedule.viewmodels.ToDoListViewModel


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
}
