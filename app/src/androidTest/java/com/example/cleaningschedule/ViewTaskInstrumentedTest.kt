package com.example.cleaningschedule

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.cleaningschedule.TestHelpers.checkById
import com.example.cleaningschedule.TestHelpers.checkByText
import com.example.cleaningschedule.TestHelpers.checkByTextAndId
import com.example.cleaningschedule.TestHelpers.checkDoesNotExistByText
import com.example.cleaningschedule.TestHelpers.clickByContentDescription
import com.example.cleaningschedule.TestHelpers.clickByText
import com.example.cleaningschedule.TestHelpers.refreshScreen
import com.example.cleaningschedule.helpers.DatabaseHandler
import com.example.cleaningschedule.models.Task
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ViewTaskInstrumentedTest {

    @Rule
    @JvmField
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    private fun getActivity() = activityRule.activity

    @Before
    fun setUp() {
        addTask()
        refreshScreen()

        clickByText("task 1")
    }

    @After
    fun tearDown() {
        InstrumentationRegistry.getInstrumentation().targetContext
            .deleteDatabase("TaskDatabase")
    }

    private fun addTask() {
        val databaseHandler = DatabaseHandler(getActivity().applicationContext)
        databaseHandler.addTask(
            Task(
                "task 1",
                "extraDetails",
                mutableListOf("kitchen", "bathroom"),
                1
            )
        )
    }

    @Test
    fun viewTask() {
        checkByTextAndId("task 1", R.id.taskNameTextView)
        checkByTextAndId("extraDetails", R.id.taskExtraDetailsTextView)
        checkByTextAndId("WEEKLY", R.id.taskOccurrenceTextView)
        checkByTextAndId("kitchenbathroom", R.id.taskRoomsTextView)
    }

    @Test
    fun goBack() {
        clickByContentDescription("Navigate up")

        checkById(R.id.toDoListFragment)
    }

    @Test
    fun update() {
        clickByText(R.string.update)

        checkById(R.id.addTaskFragment)
    }

    @Test
    fun delete() {
        clickByText(R.string.delete)

        checkByText(R.string.delete_confirmation)
    }

    @Test
    fun deleteCancel() {
        clickByText(R.string.delete)
        clickByText(R.string.cancel)

        checkById(R.id.viewTaskFragment)
    }

    @Test
    fun deleteDelete() {
        clickByText(R.string.delete)
        clickByText(R.string.delete)

        checkById(R.id.toDoListFragment)
        checkDoesNotExistByText("task 1")
    }
}