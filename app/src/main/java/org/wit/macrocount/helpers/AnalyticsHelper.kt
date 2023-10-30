package org.wit.macrocount.helpers

import android.content.Context
import org.wit.macrocount.models.UserModel
import timber.log.Timber.Forest.e



fun calcBmr(weight: Int, height: Int, age: Int, goal: String): Double {
    val bmr = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age)

    return when (goal) {
        "lose" -> {
            bmr - 600.0
        }
        "gain" -> {
            bmr + 600.0
        }
        else -> {
            bmr
        }
    }
}
