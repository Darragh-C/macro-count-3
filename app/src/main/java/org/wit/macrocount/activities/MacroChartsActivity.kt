package org.wit.macrocount.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import org.wit.macrocount.R
import org.wit.macrocount.main.MainApp
import org.wit.macrocount.models.UserModel
import org.wit.macrocount.models.UserRepo
import org.wit.macrocount.helpers.calcBmr
import org.wit.macrocount.models.MacroCountModel
import timber.log.Timber

class MacroChartsActivity : AppCompatActivity() {
    lateinit var app : MainApp
    private lateinit var caloriesProgressBar: ProgressBar
    private lateinit var userRepo: UserRepo
    private var user: UserModel? = null
    private var calorieGoal: Double = 0.0
    private var dailyCalories: Int = 0
    private var userMacros: List<MacroCountModel>? = null
    private var caloriesProgress: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charts)

        app = application as MainApp
        userRepo = UserRepo(applicationContext)

        val currentUserId = userRepo.userId
        if (currentUserId != null) {
            user = app.users.findById(currentUserId.toLong())
        }

        if (user != null && userMacros != null) {
            calorieGoal = calcBmr(user!!.weight.toInt(), user!!.height.toInt(), user!!.age.toInt(), user!!.goal)
            Timber.i("calorieGoal: $calorieGoal")
            userMacros = app.macroCounts.findByUserId(user!!.id)
            Timber.i("userMacros: $userMacros")
            dailyCalories = userMacros!!.sumOf { it.calories.toInt() }
            Timber.i("dailyCalories: $dailyCalories")
            caloriesProgress = (dailyCalories / calorieGoal) * (100 / 1)
            Timber.i("userMacros: $caloriesProgress")
        }



        caloriesProgressBar = findViewById(R.id.caloriesProgressBar)
        caloriesProgressBar.progress = caloriesProgress.toInt()

    }


}
