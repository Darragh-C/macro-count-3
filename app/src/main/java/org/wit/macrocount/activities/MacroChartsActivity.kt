package org.wit.macrocount.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import org.wit.macrocount.R

class MacroChartsActivity : AppCompatActivity() {
    private lateinit var caloriesProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charts)

        caloriesProgressBar = findViewById(R.id.caloriesProgressBar)
        caloriesProgressBar.setProgress(20)

    }


}
