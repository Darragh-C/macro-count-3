package org.wit.macrocount.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.wit.macrocount.R
import org.wit.macrocount.databinding.ActivityMacrocountBinding
import org.wit.macrocount.helpers.DataValUtil
import org.wit.macrocount.main.MainApp
import org.wit.macrocount.models.MacroCountModel
import timber.log.Timber.Forest.i
import org.wit.macrocount.showImagePicker
import com.squareup.picasso.Picasso

class MacroCountActivity : AppCompatActivity() {

    lateinit var app : MainApp
    private lateinit var binding: ActivityMacrocountBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    var macroCount = MacroCountModel()
    var editMacro = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMacrocountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        i("MacroCount started..")

        //seekbar max and min
        val seekBarMin = 0
        val seekBarMax = 500

        //seekbar data value stores
        var calories: Int = 0
        var protein: Int = 0
        var carbs: Int = 0
        var fat: Int = 0


        if (intent.hasExtra("macrocount_edit")) {
            editMacro = true
            macroCount = intent.extras?.getParcelable("macrocount_edit")!!

            binding.macroCountTitle.setText(macroCount.title)
            binding.macroCountDescription.setText(macroCount.description)
            calories = initData(macroCount.calories).toInt()
            protein = initData(macroCount.protein).toInt()
            carbs = initData(macroCount.carbs).toInt()
            fat = initData(macroCount.fat).toInt()

            binding.btnAdd.setText(R.string.save_macroCount)
        }

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
            macroCount.title = binding.macroCountTitle.text.toString()
            macroCount.description = binding.macroCountDescription.text.toString()

            macroCount.calories = calories.toString()
            macroCount.protein = protein.toString()
            macroCount.carbs = carbs.toString()
            macroCount.fat = fat.toString()


            val validationChecks = listOf(
                Pair(macroCount.title.isEmpty(), R.string.snackbar_macroCountTitle),
            )

            var validationFailed = false

            for (check in validationChecks) {
                if (check.first) {
                    Snackbar
                        .make(it, check.second, Snackbar.LENGTH_LONG)
                        .show()
                    validationFailed = true
                    break
                }
            }

            if (!validationFailed) {
                if (editMacro) {
                    i("macroCount saved: $macroCount.title")
                    app.macroCounts.update(macroCount.copy())
                } else {

                    app.macroCounts.create(macroCount.copy())
                    i("macroCount added: $macroCount")
                }

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

        registerImagePickerCallback()

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }


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

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            macroCount.image = result.data!!.data!!
                            Picasso.get()
                                .load(macroCount.image)
                                .into(binding.macroCountImage)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}