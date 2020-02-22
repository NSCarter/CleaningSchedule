package com.example.cleaningschedule.models

import com.example.cleaningschedule.R

enum class Occurrence(val occurrence: Int) {
    DAILY(R.string.daily),
    WEEKLY(R.string.weekly),
    MONTHLY(R.string.monthly),
    SEASONALLY(R.string.seasonally),
    YEARLY(R.string.yearly)
}