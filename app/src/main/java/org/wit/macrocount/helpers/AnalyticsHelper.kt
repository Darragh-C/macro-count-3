package org.wit.macrocount.helpers

import timber.log.Timber
import kotlin.math.roundToInt



fun calcBmr(weight: Int, height: Int, age: Int, goal: String): Int {
    val bmr = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age)

    return when (goal) {
        "Lose" -> {
            (bmr - 600.0).roundToInt()
        }
        "Gain" -> {
            (bmr + 600.0).roundToInt()
        }
        else -> {
            bmr.roundToInt()
        }
    }
}


