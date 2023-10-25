package org.wit.macrocount.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.macrocount.R
import org.wit.macrocount.adapters.MacroCountAdapter
import org.wit.macrocount.adapters.MacroCountListener
import org.wit.macrocount.databinding.ActivityMacrocountListBinding
import org.wit.macrocount.main.MainApp
import org.wit.macrocount.models.MacroCountModel
import timber.log.Timber

class MacroCountListActivity : AppCompatActivity(), MacroCountListener {
    lateinit var app: MainApp
    private lateinit var binding: ActivityMacrocountListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMacrocountListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = MacroCountAdapter(app.macroCounts.findByCurrentUser(), this)
        var currentUserMacros = app.macroCounts.findByCurrentUser()
        Timber.i("findByCurrentUser() at onCreate: $currentUserMacros")

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, MacroCountActivity::class.java)
                getResult.launch(launcherIntent)
                var currentUserMacros = app.macroCounts.findByCurrentUser()
                Timber.i("findByCurrentUser() at on item add: $currentUserMacros")
            }
        }
        when (item.itemId) {
            R.id.item_profile -> {
                val launcherIntent = Intent(this, UserProfileActivity::class.java)
                getResult.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.macroCounts.findByCurrentUser().size)
            }
        }

    override fun onMacroCountClick(macroCount: MacroCountModel) {
        val launcherIntent = Intent(this, MacroCountActivity::class.java)
        launcherIntent.putExtra("macrocount_edit", macroCount)
        getClickResult.launch(launcherIntent)
    }

    override fun onMacroDeleteClick(macroCount: MacroCountModel) {
        Timber.i("deleting macroCount: $macroCount.title")
        val position = app.macroCounts.index(macroCount)
        app.macroCounts.delete(macroCount)
        binding.recyclerView.adapter?.notifyItemRemoved(position)
    }

    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.macroCounts.findByCurrentUser().size)
            }
        }
}


