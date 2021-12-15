package com.example.cleaningschedule

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.cleaningschedule.helpers.DatabaseHandler
import com.example.cleaningschedule.models.Task
import org.hamcrest.CoreMatchers.allOf
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

        onView(withId(R.id.taskName)).check(matches(isDisplayed()))
    }

    @Test
    fun multipleTasks() {
        addTask(1)
        addTask(2)
        refreshScreen()

        onView(withText("task 1")).check(matches(isDisplayed()))
        onView(withText("task 2")).check(matches(isDisplayed()))
    }

    @Test
    fun correctInfo() {
        addTask(1)
        refreshScreen()

        onView(allOf(withText("task 1"), withId(R.id.taskName))).check(matches(isDisplayed()))
        onView(
            allOf(
                withText("extraDetails"),
                withId(R.id.extraDetails)
            )
        ).check(matches(isDisplayed()))
        onView(withText("kitchen")).check(matches(isDisplayed()))
        onView(allOf(withText("WEEKLY"), withId(R.id.occurrence))).check(matches(isDisplayed()))
    }

    @Test
    fun finishTask() {
        addTask(1)
        refreshScreen()

        onView(withText("kitchen")).perform(click())
        onView(withText("bathroom")).perform(click())

        onView(withId(R.id.taskName)).check(doesNotExist())
    }

    @Test
    fun selectTaskGoeToTaskView() {
        addTask(1)
        refreshScreen()

        onView(withText("task 1")).perform(click())

        onView(withText("UPDATE")).check(matches(isDisplayed()))
    }

    private fun refreshScreen() {
        onView(withContentDescription("Open navigation drawer")).perform(click())
        onView(allOf(withText(R.string.to_do), withResourceName("design_menu_item_text"))).perform(
            click()
        )
    }
}
