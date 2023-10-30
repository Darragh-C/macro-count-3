package org.wit.macrocount.helpers

import android.content.Context
import org.wit.macrocount.models.UserModel
import timber.log.Timber.Forest.e

fun calcBmr(weight: Int, height: Int, age: Int): Double {
    return 88.362 + (13.397 * weight) + (4.799 * height ) - (5.677 * age)
}

fun calcWeightLossGoal(bmr: Double): Double {
    return bmr - 600
}

fun calcWeightGainGoal(bmr: Double): Double {
    return bmr + 600
}