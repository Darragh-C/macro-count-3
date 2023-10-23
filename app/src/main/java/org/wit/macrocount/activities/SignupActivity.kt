package org.wit.macrocount.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.wit.macrocount.main.MainApp
import org.wit.macrocount.databinding.ActivitySignUpBinding
import timber.log.Timber

class SignupActivity: AppCompatActivity() {

    lateinit var app : MainApp
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        Timber.i("Sign up started..")
    }

    fun onLinkClick(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

}