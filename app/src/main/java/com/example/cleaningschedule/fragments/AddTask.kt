package com.example.cleaningschedule.fragments

import android.content.Context
import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.cleaningschedule.viewmodels.AddTaskViewModel
import com.example.cleaningschedule.R
import kotlinx.android.synthetic.main.add_task_fragment.*


class AddTask : Fragment() {

    companion object {
        fun newInstance() = AddTask()
    }

    private lateinit var viewModel: AddTaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        taskLabel.setText("${R.string.task}*")
//        taskLabel.setTextColor(Color.RED)

        return inflater.inflate(R.layout.add_task_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddTaskViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        taskImageView.layoutParams.width = taskImageView.height
    }

    
}
