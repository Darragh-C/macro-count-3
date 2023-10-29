package org.wit.macrocount.activities
import android.os.Bundle
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import org.wit.macrocount.R
import org.wit.macrocount.adapters.MacroCountAdapter
import org.wit.macrocount.databinding.ActivityMacrocountSearchBinding
import org.wit.macrocount.main.MainApp
import org.wit.macrocount.models.MacroCountModel
import org.wit.macrocount.models.UserRepo
import timber.log.Timber.Forest.i

class MacroCountSearchActivity : AppCompatActivity() {

    private lateinit var app: MainApp
    private lateinit var binding: ActivityMacrocountSearchBinding
    private lateinit var userRepo: UserRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMacrocountSearchBinding.inflate(layoutInflater)

        setContentView(binding.root)

        app = application as MainApp
        userRepo = UserRepo(applicationContext)

        val currentUserId = userRepo.userId
        var macros: List<MacroCountModel> = emptyList()
        if (currentUserId != null) {
            macros = app.macroCounts.findByUserId(currentUserId.toLong())
        }

        val macroTitles = macros.map { it.title }

        val macroListAdapter : ArrayAdapter<String> = ArrayAdapter(
            this, android.R.layout.simple_list_item_1,
            macroTitles
        )

        binding.macroSearchList.adapter = macroListAdapter

        binding.macroSearchList.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = macroListAdapter.getItem(position)
            i("selectedItem: $selectedItem")

            val resultIntent = Intent()
            resultIntent.putExtra("selectedItem", selectedItem )
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        binding.macroSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.macroSearchView.clearFocus()
                if (macroTitles.contains(query)) {
                    macroListAdapter.filter.filter(query)
                    i("query: $query")
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                macroListAdapter.filter.filter(newText)
                return false
            }
        })
    }


}