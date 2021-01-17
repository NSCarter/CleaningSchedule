package com.example.cleaningschedule.models

import com.example.cleaningschedule.R

enum class Room(val Room: Int, val isChecked: Boolean) {
    LIVING_ROOM(R.string.living_room, true),
    KITCHEN(R.string.kitchen, false),
    DINING_ROOM(R.string.dining_room, false),
    BATHROOM(R.string.bathroom, false),
    BEDROOM(R.string.bedroom, false),
    HALLWAY(R.string.hallway, false),
    OFFICE(R.string.office, false),
    PANTRY(R.string.pantry, false);
}