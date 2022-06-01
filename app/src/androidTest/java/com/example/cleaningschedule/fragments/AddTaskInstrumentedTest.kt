package com.example.cleaningschedule.fragments

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.cleaningschedule.MainActivity
import com.example.cleaningschedule.R
import com.example.cleaningschedule.fragments.TestHelpers.checkById
import com.example.cleaningschedule.fragments.TestHelpers.checkByText
import com.example.cleaningschedule.fragments.TestHelpers.checkByTextAndId
import com.example.cleaningschedule.fragments.TestHelpers.clickByContains
import com.example.cleaningschedule.fragments.TestHelpers.clickByContentDescription
import com.example.cleaningschedule.fragments.TestHelpers.clickById
import com.example.cleaningschedule.fragments.TestHelpers.clickByText
import com.example.cleaningschedule.fragments.TestHelpers.typeString
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddTaskInstrumentedTests {

    @Rule
    @JvmField
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        clickByContentDescription(R.string.add_task)
    }

    @After
    fun tearDown() {
        InstrumentationRegistry.getInstrumentation().targetContext
            .deleteDatabase("TaskDatabase")
    }

    @Test
    fun addTask() {
        typeString(R.id.taskEditText, "task name")
        typeString(R.id.extraDetailsEditText, "extra details")
        clickById(R.id.addRoomButton)
        clickByContains("Bedroom")
        clickByText("DONE")

        clickById(R.id.saveButton)

        checkById(R.id.toDoListFragment)
        checkByTextAndId("task name", R.id.taskName)
        checkByTextAndId("extra details", R.id.extraDetails)
        checkByText("Bedroom")
    }

    @Test
    fun noName() {
        typeString(R.id.extraDetailsEditText, "extra details")
        clickById(R.id.addRoomButton)
        clickByContains("Bedroom")
        clickByText("DONE")

        clickById(R.id.saveButton)

        checkById(R.id.addTaskFragment)
    }

    @Test
    fun noExtraDetails() {
        typeString(R.id.taskEditText, "task name")
        clickById(R.id.addRoomButton)
        clickByContains("Bedroom")
        clickByText("DONE")

        clickById(R.id.saveButton)

        checkById(R.id.toDoListFragment)
        checkByTextAndId("task name", R.id.taskName)
    }

    @Test
    fun noRooms() {
        typeString(R.id.taskEditText, "task name")
        typeString(R.id.extraDetailsEditText, "extra details")

        clickById(R.id.saveButton)

        checkById(R.id.addTaskFragment)
    }
}