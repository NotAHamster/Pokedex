package com.ecl.pokedex

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.ecl.pokedex.adapters.SP_ThemeAdapter
import com.ecl.pokedex.adapters.ThemeDetails
import com.ecl.pokedex.databinding.ActivitySettingsBinding

class SettingsActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var adapter: SP_ThemeAdapter
    private var intent = Intent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(Globals.themeID)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = SP_ThemeAdapter(getThemeData(), this@SettingsActivity)
        binding.themeSpinner.adapter = adapter
        binding.themeSpinner.setSelection(adapter.getPositionOf(Globals.themeID))
        binding.themeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position) as ThemeDetails
                if (selectedItem.themeId != Globals.themeID) {
                    Globals.themeID = selectedItem.themeId
                    intent.putExtra("themeChanged", true)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                //do nothing
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Custom back button behavior
                setResult(1, intent)
                finish()
            }
        })
    }

    private fun getThemeData(): List<ThemeDetails> {
        return listOf(
            ThemeDetails("Blue", R.style.Base_Theme_Pokedex),
            ThemeDetails("Red", R.style.Theme_Pokedex_Red)
        )
    }
}