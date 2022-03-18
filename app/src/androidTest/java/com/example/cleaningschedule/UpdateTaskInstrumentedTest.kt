package com.example.cleaningschedule

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.cleaningschedule.helpers.DatabaseHandler
import com.example.cleaningschedule.models.Task
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.containsString
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UpdateTaskInstrumentedTest {

    @Rule
    @JvmField
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    private fun getActivity() = activityRule.activity

    @Before
    fun setUp() {
        addTask()
        refreshScreen()

        onView(withText("task 1")).perform(click())
        onView(withText(R.string.update)).perform(click())
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

    private fun refreshScreen() {
        onView(withContentDescription("Open navigation drawer")).perform(click())
        onView(allOf(withText(R.string.to_do), withResourceName("design_menu_item_text"))).perform(
            click()
        )
    }

    @Test
    fun updateName() {
        onView(withId(R.id.taskEditText)).perform(typeText("1"), closeSoftKeyboard())
        onView(withText(R.string.save)).perform(click())

        onView(allOf(withText("task 11"), withId(R.id.taskNameTextView))).check(matches(
            isDisplayed()))
    }

    @Test
    fun updateExtraDetails() {
        onView(withId(R.id.extraDetailsEditText)).perform(typeText(" updated"), closeSoftKeyboard())
        onView(withText(R.string.save)).perform(click())

        onView(allOf(withText("extraDetails updated"), withId(R.id.taskExtraDetailsTextView))).check(matches(
            isDisplayed()))
    }

    @Test
    fun updateOccurrence() {
        onView(withId(R.id.occurrenceDropdown)).perform(click())
        onView(withText("DAILY")).perform(click())

        onView(withText(R.string.save)).perform(click())

        onView(allOf(withText("DAILY"), withId(R.id.taskOccurrenceTextView))).check(matches(
            isDisplayed()))
    }

    @Test
    fun updateRoomsWILLFAIL() {
        onView(withId(R.id.addRoomButton)).perform(click())
        onView(withText(containsString("Kitchen"))).perform(click())
        onView(withText(containsString("Bathroom"))).perform(click())
        onView(withText(containsString("Office"))).perform(click())
        onView(withText(containsString("Pantry"))).perform(click())
        onView(withText("DONE")).perform(click())

        onView(withText(R.string.save)).perform(click())

        onView(allOf(withText(containsString("Office")), withId(R.id.taskRoomsTextView))).check(matches(
            isDisplayed()))
        onView(allOf(withText(containsString("Pantry")), withId(R.id.taskRoomsTextView))).check(matches(
            isDisplayed()))
        onView(allOf(withText(containsString("Kitchen")), withId(R.id.taskRoomsTextView))).check(doesNotExist())
        onView(allOf(withText(containsString("Bathroom")), withId(R.id.taskRoomsTextView))).check(doesNotExist())
    }

    @Test
    fun cancelWILLFAIL() {
        onView(withText(R.string.cancel)).perform(click())

        onView(withId(R.id.viewTaskFragment)).check(matches(isDisplayed()))
    }

    @Test
    fun cancelNameChangesWILLFAIL() {
        onView(withId(R.id.taskEditText)).perform(typeText("1"), closeSoftKeyboard())
        onView(withText(R.string.cancel)).perform(click())

        onView(allOf(withText("task"), withId(R.id.taskNameTextView))).check(matches(isDisplayed()))
    }

    @Test
    fun cancelExtraDetailsChangesWILLFAIL() {
        onView(withId(R.id.extraDetailsEditText)).perform(typeText(" updated"), closeSoftKeyboard())
        onView(withText(R.string.cancel)).perform(click())

        onView(allOf(withText("extraDetails"), withId(R.id.taskExtraDetailsTextView))).check(matches(
            isDisplayed()))
    }

    @Test
    fun cancelOccurrenceChangesWILLFAIL() {
        onView(withId(R.id.occurrenceDropdown)).perform(click())
        onView(withText("DAILY")).perform(click())

        onView(withText(R.string.cancel)).perform(click())

        onView(allOf(withText("WEEKLY"), withId(R.id.taskOccurrenceTextView))).check(matches(
            isDisplayed()))
    }

    @Test
    fun cancelRoomChangesWILLFAIL() {
        onView(withId(R.id.addRoomButton)).perform(click())
        onView(withText(containsString("Kitchen"))).perform(click())
        onView(withText(containsString("Bathroom"))).perform(click())
        onView(withText(containsString("Office"))).perform(click())
        onView(withText(containsString("Pantry"))).perform(click())
        onView(withText("DONE")).perform(click())

        onView(withText(R.string.cancel)).perform(click())

        onView(allOf(withText(containsString("Kitchen")), withId(R.id.taskRoomsTextView))).check(matches(
            isDisplayed()))
        onView(allOf(withText(containsString("Bathroom")), withId(R.id.taskRoomsTextView))).check(matches(
            isDisplayed()))
        onView(allOf(withText(containsString("Office")), withId(R.id.taskRoomsTextView))).check(doesNotExist())
        onView(allOf(withText(containsString("Pantry")), withId(R.id.taskRoomsTextView))).check(doesNotExist())
    }
}