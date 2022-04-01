package com.example.cleaningschedule

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.cleaningschedule.TestHelpers.checkById
import com.example.cleaningschedule.TestHelpers.checkByText
import com.example.cleaningschedule.TestHelpers.checkDoesNotExistById
import com.example.cleaningschedule.TestHelpers.loadScreen
import com.example.cleaningschedule.TestHelpers.refreshScreen
import com.example.cleaningschedule.helpers.DatabaseHandler
import com.example.cleaningschedule.models.Task
import org.junit.After
import org.junit.Rule
import org.junit.Test

class AllTasksInstrumentedTest {

@Rule
@JvmField
var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    private fun getActivity() = activityRule.activity

    @After
    fun tearDown() {
        InstrumentationRegistry.getInstrumentation().targetContext
            .deleteDatabase("TaskDatabase")
    }

    private fun addTask(id: Int) {
        val databaseHandler = DatabaseHandler(getActivity().applicationContext)
        databaseHandler.addTask(
            Task(
                "task $id",
                "extraDetails",
                mutableListOf("kitchen", "bathroom"),
                1
            )
        )
    }

    @Test
    fun noTasks() {
        loadScreen(R.string.all_tasks)
        checkDoesNotExistById(R.id.taskName)
    }

    @Test
    fun oneTask() {
        addTask(1)
        loadScreen(R.string.all_tasks)

        checkById(R.id.taskName)
    }

    @Test
    fun multipleTasks() {
        addTask(1)
        addTask(2)
        loadScreen(R.string.all_tasks)

        checkByText("task 1")
        checkByText("task 2")
    }

    @Test
    fun allTasks() {
        addTask(1)
        val databaseHandler = DatabaseHandler(getActivity().applicationContext)
        databaseHandler.addTask(
            Task(
                "task 2",
                "extraDetails",
                mutableListOf("bedroom"),
                1
            )
        )
        refreshScreen()
        TestHelpers.clickByText("kitchen")
        TestHelpers.clickByText("bathroom")

        loadScreen(R.string.all_tasks)

        checkByText("task 1")
        checkByText("task 2")
    }

    @Test
    fun selectTaskGoesToTaskView() {
        addTask(1)
        loadScreen(R.string.all_tasks)

        TestHelpers.clickByText("task 1")

        checkById(R.id.viewTaskFragment)
    }
}