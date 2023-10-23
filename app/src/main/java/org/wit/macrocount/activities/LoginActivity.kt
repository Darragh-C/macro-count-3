package org.wit.macrocount.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.wit.macrocount.main.MainApp
import org.wit.macrocount.databinding.ActivityLogInBinding
import timber.log.Timber



class LoginActivity: AppCompatActivity() {

    lateinit var app : MainApp
    private lateinit var binding: ActivityLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        Timber.i("Log in started..")
    }

    fun onLinkClick(view: View) {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }

}