package org.wit.macrocount.main

//import org.wit.macrocount.models.MacroCountModel
import android.app.Application
import org.wit.macrocount.models.MacroCountJSONStore
import org.wit.macrocount.models.MacroCountStore
import org.wit.macrocount.models.UserMemStore
import timber.log.Timber
import timber.log.Timber.Forest.i

class MainApp : Application() {

    lateinit var macroCounts: MacroCountStore
    val users = UserMemStore()
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        macroCounts = MacroCountJSONStore(applicationContext)
        i("MacroCount started")
    }
}