package org.wit.macrocount.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

        if (intent.hasExtra("macrocount_edit")) {
            editMacro = true
            macroCount = intent.extras?.getParcelable("macrocount_edit")!!
            binding.macroCountTitle.setText(macroCount.title)
            binding.macroCountDescription.setText(macroCount.description)
            binding.macroCountCalories.setText(macroCount.calories)
            binding.macroCountCarbs.setText(macroCount.carbs)
            binding.macroCountProtein.setText(macroCount.protein)
            binding.macroCountFat.setText(macroCount.fat)
            binding.btnAdd.setText(R.string.save_macroCount)
        }



        binding.btnAdd.setOnClickListener() {
            macroCount.title = binding.macroCountTitle.text.toString()
            macroCount.description = binding.macroCountDescription.text.toString()

            macroCount.carbs = binding.macroCountCarbs.text.toString()
            macroCount.protein = binding.macroCountProtein.text.toString()
            macroCount.fat = binding.macroCountFat.text.toString()
            macroCount.calories = binding.macroCountCalories.text.toString()


            val validationChecks = listOf(
                Pair(macroCount.title.isEmpty(), R.string.snackbar_macroCountTitle),
                Pair(macroCount.calories.isNotEmpty() && !DataValUtil.validNum(macroCount.calories), R.string.snackbar_macroCountCalories),
                Pair(macroCount.carbs.isNotEmpty() && !DataValUtil.validNum(macroCount.carbs), R.string.snackbar_macroCountCarbs),
                Pair(macroCount.protein.isNotEmpty() && !DataValUtil.validNum(macroCount.protein), R.string.snackbar_macroCountProtein),
                Pair(macroCount.fat.isNotEmpty() && !DataValUtil.validNum(macroCount.fat), R.string.snackbar_macroCountFat)
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
                    i("macroCount added: $macroCount.title")
                    app.macroCounts.create(macroCount.copy())
                }

                i("Total MacroCounts: ")
                for (i in app.macroCounts.findAll().indices) {
                    i("MacroCount[$i]:${app.macroCounts.findAll()[i]}")
                    setResult(RESULT_OK)
                    finish()
                }
            }
        }

        registerImagePickerCallback()

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
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