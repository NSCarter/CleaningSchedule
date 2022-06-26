package com.example.cleaningschedule.fragments

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.cleaningschedule.R
import org.hamcrest.BaseMatcher
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.Description
import org.hamcrest.Matcher

object TestHelpers {
    fun refreshScreen() {
        onView(withContentDescription("Open navigation drawer")).perform(click())
        onView(
            allOf(
                withText(R.string.to_do),
                withResourceName("design_menu_item_text")
            )
        ).perform(
            click()
        )
    }

    fun loadScreen(id: Int) {
        onView(withContentDescription("Open navigation drawer")).perform(click())
        onView(
            allOf(
                withText(id),
                withResourceName("design_menu_item_text")
            )
        ).perform(
            click()
        )
    }

    fun checkById(id: Int) {
        onView(withId(id)).check(matches(isDisplayed()))
    }

    fun checkByText(string: String) {
        onView(withText(string)).check(matches(isDisplayed()))
    }

    fun checkByText(id: Int) {
        onView(withText(id)).check(matches(isDisplayed()))
    }

    fun checkByTextAndId(string: String, id: Int) {
        onView(
            allOf(
                withText(string),
                withId(id)
            )
        ).check(matches(isDisplayed()))
    }

    fun checkByContainsAndId(string: String, id: Int) {
        onView(allOf(withText(containsString(string)), withId(id))).check(matches(isDisplayed()))
    }

    fun checkDoesNotExistById(id: Int) {
        onView(withId(id)).check(doesNotExist())
    }

    fun checkDoesNotExistByText(string: String) {
        onView(withText(string)).check(doesNotExist())
    }

    fun checkDoesNotExistByContainsAndId(string: String, id: Int) {
        onView(allOf(withText(containsString(string)), withId(id))).check(doesNotExist())
    }

    fun checkNotChecked(string: String) {
        onView(withText(string)).check(matches(isNotChecked()))
    }

    fun clickByText(string: String) {
        onView(withText(string)).perform(click())
    }

    fun clickByText(id: Int) {
        onView(withText(id)).perform(click())
    }

    fun clickById(id: Int) {
        onView(withId(id)).perform(click())
    }

    fun clickByContains(string: String) {
        onView(withText(containsString(string))).perform(click())
    }

    fun clickByContentDescription(string: String) {
        onView(withContentDescription(string)).perform(click())
    }

    fun clickByContentDescription(id: Int) {
        onView(withContentDescription(id)).perform(click())
    }

    fun clickFirstById(id: Int) {
        onView(first(withId(id))).perform(click())
    }

    fun typeString(id: Int, string: String) {
        onView(withId(id)).perform(typeText(string), closeSoftKeyboard())
    }

    private fun <T> first(matcher: Matcher<T>): Matcher<T> {
        return object : BaseMatcher<T>() {
            var isFirst = true
            override fun matches(item: Any?): Boolean {
                if (isFirst && matcher.matches(item)) {
                    isFirst = false
                    return true
                }
                return false
            }

            override fun describeTo(description: Description) {
                description.appendText("should return first matching item")
            }
        }
    }
}