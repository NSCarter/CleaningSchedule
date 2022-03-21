package com.example.cleaningschedule

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.cleaningschedule.TestHelpers.checkByContainsAndId
import com.example.cleaningschedule.TestHelpers.checkById
import com.example.cleaningschedule.TestHelpers.checkByTextAndId
import com.example.cleaningschedule.TestHelpers.checkDoesNotExistByContainsAndId
import com.example.cleaningschedule.TestHelpers.clickByContains
import com.example.cleaningschedule.TestHelpers.clickById
import com.example.cleaningschedule.TestHelpers.clickByText
import com.example.cleaningschedule.TestHelpers.refreshScreen
import com.example.cleaningschedule.TestHelpers.typeString
import com.example.cleaningschedule.helpers.DatabaseHandler
import com.example.cleaningschedule.models.Task
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

    @Test
    fun updateName() {
        typeString(R.id.taskEditText, "1")
        clickByText(R.string.save)

        checkByTextAndId("task 11", R.id.taskNameTextView)
    }

    @Test
    fun updateExtraDetails() {
        typeString(R.id.extraDetailsEditText, " updated")
        clickByText(R.string.save)

        checkByTextAndId("extraDetails updated", R.id.taskExtraDetailsTextView)
    }

    @Test
    fun updateOccurrence() {
        clickById(R.id.occurrenceDropdown)
        clickByText("DAILY")

        clickByText(R.string.save)

        checkByTextAndId("DAILY", R.id.taskOccurrenceTextView)
    }

    @Test
    fun updateRoomsWILLFAIL() {
        clickById(R.id.addRoomButton)
        clickByContains("Kitchen")
        clickByContains("Bathroom")
        clickByContains("Office")
        clickByContains("Pantry")
        clickByText("DONE")

        clickByText(R.string.save)

        checkByContainsAndId("Office", R.id.taskRoomsTextView)
        checkByContainsAndId("Pantry", R.id.taskRoomsTextView)
        checkDoesNotExistByContainsAndId("Kitchen", R.id.taskRoomsTextView)
        checkDoesNotExistByContainsAndId("Bathroom", R.id.taskRoomsTextView)
    }

    @Test
    fun cancelWILLFAIL() {
        clickByText(R.string.cancel)

        checkById(R.id.viewTaskFragment)
    }

    @Test
    fun cancelNameChangesWILLFAIL() {
        typeString(R.id.taskEditText, "1")
        clickByText(R.string.cancel)

        checkByTextAndId("task", R.id.taskNameTextView)
    }

    @Test
    fun cancelExtraDetailsChangesWILLFAIL() {
        typeString(R.id.extraDetailsEditText, " updated")
        clickByText(R.string.cancel)

        checkByTextAndId("extraDetails", R.id.taskExtraDetailsTextView)
    }

    @Test
    fun cancelOccurrenceChangesWILLFAIL() {
        clickById(R.id.occurrenceDropdown)
        clickByText("DAILY")

        clickByText(R.string.cancel)

        checkByTextAndId("WEEKLY", R.id.taskOccurrenceTextView)
    }

    @Test
    fun cancelRoomChangesWILLFAIL() {
        clickById(R.id.addRoomButton)
        clickByContains("Kitchen")
        clickByContains("Bathroom")
        clickByContains("Office")
        clickByContains("Pantry")
        clickByText("DONE")

        clickByText(R.string.cancel)

        checkByContainsAndId("Kitchen", R.id.taskRoomsTextView)
        checkByContainsAndId("Bathroom", R.id.taskRoomsTextView)
        checkDoesNotExistByContainsAndId("Office", R.id.taskRoomsTextView)
        checkDoesNotExistByContainsAndId("Pantry", R.id.taskRoomsTextView)
    }
}