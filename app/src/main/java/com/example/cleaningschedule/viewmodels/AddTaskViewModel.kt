package com.example.cleaningschedule.viewmodels

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.cleaningschedule.fragments.AddTaskFragmentDirections

class AddTaskViewModel : ViewModel() {

    // Put info in list or dict
    fun saveTask(view: View, name: String, extraDetails: String) {
        val action = AddTaskFragmentDirections.actionAddTaskToToDoList()
        view?.findNavController()?.navigate(action)
    }
}
