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


class MacroCountSearchActivity : AppCompatActivity() {

    private lateinit var app: MainApp
    private lateinit var adapter: MacroCountAdapter
    private lateinit var binding: ActivityMacrocountSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMacrocountSearchBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val macros = arrayOf("Steak", "Chicken", "Salmon", "Tofu", "Pasta", "Broccoli", "Quinoa", "Eggs", "Spinach", "Brown Rice", "Pork", "Lentils", "Shrimp", "Avocado", "Cauliflower", "Turkey", "Sweet Potato", "Asparagus", "Beans", "Zucchini")

        val macroListAdapter : ArrayAdapter<String> = ArrayAdapter(
            this, android.R.layout.simple_list_item_1,
            macros
        )

        binding.macroSearchList.adapter = macroListAdapter

        binding.macroSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.macroSearchView.clearFocus()
                if (macros.contains(query)) {
                    macroListAdapter.filter.filter(query)
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
