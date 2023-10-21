package org.wit.macrocount.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.NumberPicker
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import org.wit.macrocount.R
import org.wit.macrocount.databinding.ActivityProfileBinding
import org.wit.macrocount.main.MainApp
import org.wit.macrocount.models.UserModel
import timber.log.Timber
import timber.log.Timber.Forest.i
import java.time.LocalDate

class UserProfileActivity : AppCompatActivity() {

    lateinit var app : MainApp
    private lateinit var binding: ActivityProfileBinding
    var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = "Profile"
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val radioGroup = findViewById<RadioGroup>(R.id.genderRadioGroup)
        var gender: String = ""

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.radioButtonOption1 -> {
                    gender = "male"
                }
                R.id.radioButtonOption2 -> {
                    gender = "female"
                }
            }
        }

        val numberPickerHeight = findViewById<NumberPicker>(R.id.numberPickerHeight)
        numberPickerHeight.minValue = 0
        numberPickerHeight.maxValue = 250
        numberPickerHeight.value = 150
        numberPickerHeight.setOnValueChangedListener{ picker, oldVal, newVal ->
            i("{newVal}")
            user.height = newVal.toString()
        }

        val numberPickerWeight = findViewById<NumberPicker>(R.id.numberPickerWeight)
        numberPickerWeight.minValue = 0
        numberPickerWeight.maxValue = 150
        numberPickerWeight.value = 75
        numberPickerWeight.setOnValueChangedListener{ picker, oldVal, newVal ->
            i("{newVal}")
            user.weight = newVal.toString()
        }

        var day: String = ""
        var month: String = ""
        var year: String = ""

        val numberPickerDay = findViewById<NumberPicker>(R.id.numberPickerDay)
        numberPickerDay.minValue = 1
        numberPickerDay.maxValue = 31
        numberPickerDay.value = 1
        numberPickerDay.setOnValueChangedListener{ picker, oldVal, newVal ->
            i("{newVal}")
            day = newVal.toString()
        }

        val numberPickerMonth = findViewById<NumberPicker>(R.id.numberPickerMonth)
        numberPickerMonth.minValue = 1
        numberPickerMonth.maxValue = 12
        numberPickerMonth.value = 1
        numberPickerMonth.setOnValueChangedListener{ picker, oldVal, newVal ->
            i("{newVal}")
            month = newVal.toString()
        }

        val numberPickerYear = findViewById<NumberPicker>(R.id.numberPickerYear)
        numberPickerYear.minValue = 1920
        numberPickerYear.maxValue = LocalDate.now().year
        numberPickerYear.value = 1990
        numberPickerYear.setOnValueChangedListener{ picker, oldVal, newVal ->
            i("{newVal}")
            year = newVal.toString()
        }

        binding.btnSave.setOnClickListener() {
            user.name = binding.userName.text.toString()
            user.gender = gender
            user.dob = day.toString() + "/" + month.toString() + "/" + year.toString()

            Timber.i("userProfile saved: $user.name")
            app.users.create(user.copy())
            Timber.i("Total userProfiles: ")
            for (i in app.users.findAll().indices) {
                Timber.i("userProfile[$i]:${app.users.findAll()[i]}")
            }
            setResult(RESULT_OK)
            finish()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_macrocount, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}