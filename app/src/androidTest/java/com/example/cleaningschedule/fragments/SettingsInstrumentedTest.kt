package com.example.cleaningschedule.fragments

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.cleaningschedule.MainActivity
import com.example.cleaningschedule.R
import com.example.cleaningschedule.fragments.TestHelpers.checkByText
import com.example.cleaningschedule.fragments.TestHelpers.loadScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsInstrumentedTest {

    @Rule
    @JvmField
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun loadSettings() {
        loadScreen(R.string.settings)

        checkByText(R.string.messages_header)
    }
}