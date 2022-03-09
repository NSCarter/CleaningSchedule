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

        onView(withText("task 1")).perform(click())
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
        onView(allOf(withText("task 1"), withId(R.id.taskNameTextView))).check(matches(isDisplayed()))
        onView(allOf(withText("extraDetails"), withId(R.id.taskExtraDetailsTextView))).check(matches(
            isDisplayed()))
        onView(allOf(withText("WEEKLY"), withId(R.id.taskOccurrenceTextView))).check(matches(
            isDisplayed()))
        onView(allOf(withText("kitchenbathroom"), withId(R.id.taskRoomsTextView))).check(matches(isDisplayed()))
    }

    @Test
    fun goBack() {
        onView(withText("task 1")).perform(click())
        onView(withContentDescription("Navigate up")).perform(click())

        onView(withId(R.id.toDoListFragment)).check(matches(isDisplayed()))
    }

    @Test
    fun update() {
        onView(withText("task 1")).perform(click())
        onView(withText(R.string.update)).perform(click())

        onView(withId(R.id.addTaskFragment)).check(matches(isDisplayed()))
    }

    @Test
    fun delete() {
        onView(withText("task 1")).perform(click())
        onView(withText(R.string.delete)).perform(click())

        onView(withText(R.string.delete_confirmation)).check(matches(isDisplayed()))
    }

    @Test
    fun deleteCancel() {
        onView(withText("task 1")).perform(click())
        onView(withText(R.string.delete)).perform(click())
        onView(withText(R.string.cancel)).perform(click())

        onView(withId(R.id.viewTaskFragment)).check(matches(isDisplayed()))
    }

    @Test
    fun deleteDelete() {
        onView(withText("task 1")).perform(click())
        onView(withText(R.string.delete)).perform(click())
        onView(withText(R.string.delete)).perform(click())

        onView(withId(R.id.toDoListFragment)).check(matches(isDisplayed()))
        onView(withText("task 1")).check(doesNotExist())
    }

    private fun refreshScreen() {
        onView(withContentDescription("Open navigation drawer")).perform(click())
        onView(allOf(withText(R.string.to_do), withResourceName("design_menu_item_text"))).perform(
            click()
        )
    }
}