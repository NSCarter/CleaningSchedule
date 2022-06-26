package com.example.cleaningschedule.helpers

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.cleaningschedule.MainActivity
import com.example.cleaningschedule.models.Task
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseHandlerTests {

    @Rule
    @JvmField
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    private fun getActivity() = activityRule.activity

    private lateinit var databaseHandler: DatabaseHandler
    @Before
    fun setUp() {
        databaseHandler = DatabaseHandler(getActivity().applicationContext)
    }

    @After
    fun tearDown() {
        InstrumentationRegistry.getInstrumentation().targetContext
            .deleteDatabase("TaskDatabase")
    }

    @Test
    fun addTask() {
        val result = databaseHandler.addTask(
            Task(
                "task 1",
                "extraDetails",
                mutableListOf("kitchen", "bathroom"),
                1
            )
        )

        assertNotEquals(result, -1)
    }

    @Test
    fun addTaskNoExtraDetails() {
        val result = databaseHandler.addTask(
            Task(
                "task 1",
                "",
                mutableListOf("kitchen", "bathroom"),
                1
            )
        )

        assertNotEquals(result, -1)
    }

    @Test
    fun addTaskOneRoom() {
        val result = databaseHandler.addTask(
            Task(
                "task 1",
                "extraDetails",
                mutableListOf("kitchen"),
                1
            )
        )

        assertNotEquals(result, -1)
    }

    @Test
    fun getTasks() {
        databaseHandler.addTask(
            Task(
                "task 1",
                "extraDetails",
                mutableListOf("kitchen", "bathroom"),
                1
            )
        )
        databaseHandler.addTask(
            Task(
                "task 2",
                "extraDetails",
                mutableListOf("kitchen", "bathroom"),
                1
            )
        )

        val result = databaseHandler.getTasks()

        assertEquals(result.size, 2)
    }

    @Test
    fun getToDoTasks() {
        databaseHandler.addTask(
            Task(
                "task 1",
                "extraDetails",
                mutableListOf("kitchen", "bathroom"),
                1
            )
        )
        databaseHandler.addTask(
            Task(
                "task 2",
                "extraDetails",
                mutableListOf("kitchen"),
                1
            )
        )
        databaseHandler.completedTask(mutableListOf("1", "task 2", "extraDetails", "1"))

        val result = databaseHandler.getToDoTasks()

        assertNotEquals(result, -1)
    }

    @Test
    fun getOnlyToDoTasks() {
        databaseHandler.addTask(
            Task(
                "task 1",
                "extraDetails",
                mutableListOf("kitchen", "bathroom"),
                1
            )
        )
        databaseHandler.addTask(
            Task(
                "task 2",
                "extraDetails",
                mutableListOf("kitchen"),
                1
            )
        )
        databaseHandler.completedTask(mutableListOf("2", "task 2", "extraDetails", "1"))

        val result = databaseHandler.getToDoTasks()

        assertEquals(result.size, 1)
        assertEquals(result[0].first[1], "task 1")
    }

    @Test
    fun getTaskById() {
        databaseHandler.addTask(
            Task(
                "task 1",
                "extraDetails",
                mutableListOf("kitchen", "bathroom"),
                1
            )
        )
        databaseHandler.addTask(
            Task(
                "task 2",
                "extraDetails",
                mutableListOf("kitchen"),
                1
            )
        )

        val result = databaseHandler.getTask(2)

        assertNotEquals(result.first[0], "Task 2")
    }

    @Test
    fun deleteTask() {
        databaseHandler.addTask(
            Task(
                "task 1",
                "extraDetails",
                mutableListOf("kitchen", "bathroom"),
                1
            )
        )

        val result = databaseHandler.deleteTask(1)

        assertEquals(result, 1)
    }

    @Test
    fun updateTask() {
        databaseHandler.addTask(
            Task(
                "task 1",
                "extraDetails",
                mutableListOf("kitchen", "bathroom"),
                1
            )
        )
        databaseHandler.updateTask(
            Task(
                "task updated",
                "extraDetails",
                mutableListOf("kitchen", "bathroom"),
                1
            ), 1
        )

        val result = databaseHandler.getTask(1)

        assertEquals(result.first[0], "task updated")
    }

    @Test
    fun updateTaskNoExtraDetails() {
        databaseHandler.addTask(
            Task(
                "task 1",
                "extraDetails",
                mutableListOf("kitchen", "bathroom"),
                1
            )
        )
        databaseHandler.updateTask(
            Task(
                "task updated",
                "",
                mutableListOf("kitchen", "bathroom"),
                1
            ), 1
        )

        val result = databaseHandler.getTask(1)

        assertEquals(result.first[0], "task updated")
    }

    @Test
    fun updateTaskAddRoom() {
        databaseHandler.addTask(
            Task(
                "task 1",
                "extraDetails",
                mutableListOf("kitchen", "bathroom"),
                1
            )
        )
        databaseHandler.updateTask(
            Task(
                "task 1",
                "extraDetails",
                mutableListOf("kitchen", "bathroom", "bedroom"),
                1
            ), 1
        )

        val result = databaseHandler.getTask(1)

        assertEquals(result.second.count(), 3)
    }

    @Test
    fun updateTaskRemoveRoom() {
        databaseHandler.addTask(
            Task(
                "task 1",
                "extraDetails",
                mutableListOf("kitchen", "bathroom"),
                1
            )
        )
        databaseHandler.updateTask(
            Task(
                "task 1",
                "extraDetails",
                mutableListOf("kitchen"),
                1
            ), 1
        )

        val result = databaseHandler.getTask(1)

        assertEquals(result.second.count(), 1)
    }

    @Test
    fun updateRoomDone() {
        databaseHandler.addTask(
            Task(
                "task 1",
                "extraDetails",
                mutableListOf("kitchen", "bathroom"),
                1
            )
        )
        databaseHandler.updateRoom(1, true)

        val result = databaseHandler.getTasks()

        assertTrue(result[0].second[0].isChecked)
    }

    @Test
    fun updateRoomNotDone() {
        databaseHandler.addTask(
            Task(
                "task 1",
                "extraDetails",
                mutableListOf("kitchen", "bathroom"),
                1
            )
        )
        databaseHandler.updateRoom(1, true)
        databaseHandler.updateRoom(1, false)

        val result = databaseHandler.getTasks()

        assertFalse(result[0].second[0].isChecked)
    }

    @Test
    fun completeDailyTask() {
        databaseHandler.addTask(
            Task(
                "task 1",
                "extraDetails",
                mutableListOf("kitchen"),
                0
            )
        )
        databaseHandler.updateRoom(1, true)

        val result = databaseHandler.getToDoTasks()

        assertNotEquals(result.count(), 0)
    }

    @Test
    fun completeWeeklyTask() {
        databaseHandler.addTask(
            Task(
                "task 1",
                "extraDetails",
                mutableListOf("kitchen"),
                1
            )
        )
        databaseHandler.updateRoom(1, true)

        val result = databaseHandler.getToDoTasks()

        assertNotEquals(result.count(), 0)
    }

    @Test
    fun completeMonthlyTask() {
        databaseHandler.addTask(
            Task(
                "task 1",
                "extraDetails",
                mutableListOf("kitchen"),
                2
            )
        )
        databaseHandler.updateRoom(1, true)

        val result = databaseHandler.getToDoTasks()

        assertNotEquals(result.count(), 0)
    }

    @Test
    fun completeSeasonalTask() {
        databaseHandler.addTask(
            Task(
                "task 1",
                "extraDetails",
                mutableListOf("kitchen"),
                3
            )
        )
        databaseHandler.updateRoom(1, true)

        val result = databaseHandler.getToDoTasks()

        assertNotEquals(result.count(), 0)
    }

    @Test
    fun completeYearlyTask() {
        databaseHandler.addTask(
            Task(
                "task 1",
                "extraDetails",
                mutableListOf("kitchen"),
                4
            )
        )
        databaseHandler.updateRoom(1, true)

        val result = databaseHandler.getToDoTasks()

        assertNotEquals(result.count(), 0)
    }

    @Test
    fun completingTasksUnchecksRooms() {
        databaseHandler.addTask(
            Task(
                "task 1",
                "extraDetails",
                mutableListOf("kitchen", "bathroom"),
                1
            )
        )
        databaseHandler.updateRoom(1, true)
        databaseHandler.updateRoom(2, true)

        val result = databaseHandler.getTasks()

        assertFalse(result[0].second[0].isChecked)
        assertFalse(result[0].second[1].isChecked)
    }
}