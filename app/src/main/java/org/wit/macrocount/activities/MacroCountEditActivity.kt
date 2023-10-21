package org.wit.macrocount.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.wit.macrocount.R
import org.wit.macrocount.databinding.ActivityMacrocountEditBinding
import org.wit.macrocount.main.MainApp
import org.wit.macrocount.models.MacroCountModel
import timber.log.Timber.Forest.i

class MacroCountEditActivity : AppCompatActivity() {

    lateinit var app : MainApp
    private lateinit var binding: ActivityMacrocountEditBinding
    var macroCount = MacroCountModel()
    var editMacro = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        macroCount = intent.extras?.getParcelable("macrocount_edit")!!

        binding = ActivityMacrocountEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        i("MacroCount edit started..")

        //seekbar max and min
        val seekBarMin = 0
        val seekBarMax = 500

        //seekbar data value stores
        var calories = initData(macroCount.calories).toInt()
        var protein = initData(macroCount.protein).toInt()
        var carbs = initData(macroCount.carbs).toInt()
        var fat = initData(macroCount.fat).toInt()

        //seekbar viewers
        val calorieSeekBar = findViewById<SeekBar>(R.id.calorieSeekBar)
        val proteinSeekBar = findViewById<SeekBar>(R.id.proteinSeekBar)
        val carbsSeekBar = findViewById<SeekBar>(R.id.carbsSeekBar)
        val fatSeekBar = findViewById<SeekBar>(R.id.fatSeekBar)

        //views that contain data values
        val caloriesDataView = findViewById<TextView>(R.id.caloriesDataView)
        val proteinDataView = findViewById<TextView>(R.id.proteinDataView)
        val carbsDataView = findViewById<TextView>(R.id.carbsDataView)
        val fatDataView = findViewById<TextView>(R.id.fatDataView)


        //binding initial values to data views
        caloriesDataView.text = calories.toString()
        proteinDataView.text = protein.toString()
        carbsDataView.text = carbs.toString()
        fatDataView.text = fat.toString()

        // Set the SeekBar range
        calorieSeekBar.min = seekBarMin
        calorieSeekBar.max = seekBarMax
        proteinSeekBar.min = seekBarMin
        proteinSeekBar.max = seekBarMax
        carbsSeekBar.min = seekBarMin
        carbsSeekBar.max = seekBarMax
        fatSeekBar.min = seekBarMin
        fatSeekBar.max = seekBarMax

        //seekbar progresses
        calorieSeekBar.progress = calories
        proteinSeekBar.progress = protein
        carbsSeekBar.progress = carbs
        fatSeekBar.progress = fat

        binding.btnAdd.setOnClickListener() {

            macroCount.calories = calories.toString()
            macroCount.protein = protein.toString()
            macroCount.carbs = carbs.toString()
            macroCount.fat = fat.toString()

            i("macroCount saved: $macroCount.title")
            app.macroCounts.update(macroCount.copy())
            i("Total MacroCounts: ")
            for (i in app.macroCounts.findAll().indices) {
                i("MacroCount[$i]:${app.macroCounts.findAll()[i]}")
                setResult(RESULT_OK)
                finish()
            }
        }


        calorieSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(calorieSeekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                caloriesDataView.text = progress.toString()
                calories = progress
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        proteinSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(proteinSeekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                proteinDataView.text = progress.toString()
                protein = progress
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        carbsSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(carbsSeekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                carbsDataView.text = progress.toString()
                carbs = progress
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        fatSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(fatSeekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                fatDataView.text = progress.toString()
                fat = progress
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })


    }
    fun initData(value: String): String {
        return if (value.isNotEmpty()) value else "0"
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