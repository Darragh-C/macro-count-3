package org.wit.macrocount.activities
import android.os.Bundle
import android.app.Activity
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import org.wit.macrocount.R
import org.wit.macrocount.adapters.MacroCountAdapter
import org.wit.macrocount.databinding.ActivityMacrocountSearchBinding
import org.wit.macrocount.main.MainApp
import timber.log.Timber.Forest.i

class MacroCountSearchActivity : AppCompatActivity() {

    private lateinit var app: MainApp
    private lateinit var adapter: MacroCountAdapter
    private lateinit var binding: ActivityMacrocountSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMacrocountSearchBinding.inflate(layoutInflater)

        setContentView(binding.root)

        app = application as MainApp

        //val macros = arrayOf("Steak", "Chicken", "Salmon", "Tofu", "Pasta", "Broccoli", "Quinoa", "Eggs", "Spinach", "Brown Rice", "Pork", "Lentils", "Shrimp", "Avocado", "Cauliflower", "Turkey", "Sweet Potato", "Asparagus", "Beans", "Zucchini")
        val macros = app.macroCounts.findAll()
        val macroTitles = macros.map { it.title }

        val macroListAdapter : ArrayAdapter<String> = ArrayAdapter(
            this, android.R.layout.simple_list_item_1,
            macroTitles
        )

        binding.macroSearchList.adapter = macroListAdapter

        binding.macroSearchList.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = macroListAdapter.getItem(position)
            i("selectedItem: $selectedItem")
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
