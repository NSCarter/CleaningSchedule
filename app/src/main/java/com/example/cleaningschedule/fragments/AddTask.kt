package com.example.cleaningschedule.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cleaningschedule.viewmodels.AddTaskViewModel
import com.example.cleaningschedule.R
import com.example.cleaningschedule.models.Occurrence
import kotlinx.android.synthetic.main.add_task_fragment.*


class AddTask : Fragment() {

    companion object {
        fun newInstance() = AddTask()
    }

    private lateinit var viewModel: AddTaskViewModel
    private lateinit var name: String
    private lateinit var extraDetails: String
    private lateinit var rooms: Array<String>
    private lateinit var occurrence: Occurrence

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

        saveButton.setOnClickListener {
            //TODO Create task object
            name = taskEditText.text.toString()
            extraDetails = extraDetailsEditText.text.toString()

            taskEditText.text.clear()
            extraDetailsEditText.text.clear()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        taskImageView.layoutParams.width = taskImageView.height
    }

    
}
