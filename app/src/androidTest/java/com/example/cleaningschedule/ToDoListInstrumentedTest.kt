package com.example.cleaningschedule

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.cleaningschedule.TestHelpers.checkById
import com.example.cleaningschedule.TestHelpers.checkByText
import com.example.cleaningschedule.TestHelpers.checkByTextAndId
import com.example.cleaningschedule.TestHelpers.checkDoesNotExistById
import com.example.cleaningschedule.TestHelpers.clickByText
import com.example.cleaningschedule.TestHelpers.refreshScreen
import com.example.cleaningschedule.helpers.DatabaseHandler
import com.example.cleaningschedule.models.Task
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ToDoListInstrumentedTest {

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
        onView(withId(R.id.taskName)).check(doesNotExist())
    }

    @Test
    fun oneTask() {
        addTask(1)
        refreshScreen()

        checkById(R.id.taskName)
    }

    @Test
    fun multipleTasks() {
        addTask(1)
        addTask(2)
        refreshScreen()

        checkByText("task 1")
        checkByText("task 2")
    }

    @Test
    fun correctInfo() {
        addTask(1)
        refreshScreen()

        checkByTextAndId("task 1", R.id.taskName)
        checkByTextAndId("extraDetails", R.id.extraDetails)
        checkByText("kitchen")
        checkByTextAndId("WEEKLY", R.id.occurrence)
    }

    @Test
    fun finishTask() {
        addTask(1)
        refreshScreen()

        clickByText("kitchen")
        clickByText("bathroom")

        checkDoesNotExistById(R.id.taskName)
    }

    @Test
    fun selectTaskGoesToTaskView() {
        addTask(1)
        refreshScreen()

        clickByText("task 1")

        checkById(R.id.viewTaskFragment)
    }
}
