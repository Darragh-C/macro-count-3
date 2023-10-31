package org.wit.macrocount.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import org.wit.macrocount.R
import org.wit.macrocount.databinding.ActivityChartsBinding
import org.wit.macrocount.main.MainApp
import org.wit.macrocount.models.UserModel
import org.wit.macrocount.models.UserRepo
import org.wit.macrocount.helpers.calcBmr
import org.wit.macrocount.helpers.calcProtein
import org.wit.macrocount.models.MacroCountModel
import timber.log.Timber
import kotlin.math.roundToInt

class MacroChartsActivity : AppCompatActivity() {
    lateinit var app : MainApp
    private lateinit var caloriesProgressBar: ProgressBar
    private lateinit var proteinProgressBar: ProgressBar
    private lateinit var userRepo: UserRepo
    private var user: UserModel? = null
    private var calorieGoal: Int = 0
    private var proteinGoal: Int = 0
    private var dailyCalories: Int = 0
    private var dailyProtein: Int = 0
    private var userMacros: List<MacroCountModel>? = null
    private var caloriesProgress: Int = 0
    private var proteinProgress: Int = 0
    private lateinit var binding: ActivityChartsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChartsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp
        userRepo = UserRepo(applicationContext)

        val currentUserId = userRepo.userId
        Timber.i("currentUserId at charts: $currentUserId")
        if (currentUserId != null) {
            user = app.users.findById(currentUserId.toLong())
        }
        Timber.i("user at charts: $user")

        userMacros = app.macroCounts.findByUserId(user!!.id)
        Timber.i("userMacros: $userMacros")

        if (user != null && userMacros != null) {
            calorieGoal = calcBmr(
                user!!.weight.toInt(),
                user!!.height.toInt(),
                user!!.age.toInt(),
                user!!.goal
            )

            proteinGoal = calcProtein(
                user!!.weight.toInt(),
                user!!.goal
            )

            dailyCalories = userMacros!!.sumOf { it.calories.toInt() }
            Timber.i("dailyCalories: $dailyCalories")

            dailyProtein = userMacros!!.sumOf { it.protein.toInt() }
            Timber.i("dailyCalories: $dailyProtein")

            var calorieFraction = "$dailyCalories/$calorieGoal"
            binding.caloriesProgressFraction.text = calorieFraction
            var proteinFraction = "$dailyProtein/$proteinGoal"
            binding.proteinProgressFraction.text = proteinFraction

            caloriesProgress = ((dailyCalories.toDouble() / calorieGoal.toDouble()) * 100).roundToInt()
            Timber.i("caloriesProgress: $caloriesProgress")
            proteinProgress = ((dailyProtein.toDouble() / proteinGoal.toDouble()) * 100).roundToInt()
            Timber.i("caloriesProgress: $proteinProgress")

        }


        caloriesProgressBar = binding.caloriesProgressBar
        caloriesProgressBar.progress = caloriesProgress

        proteinProgressBar = binding.proteinProgressBar
        proteinProgressBar.progress = proteinProgress

    }


}
