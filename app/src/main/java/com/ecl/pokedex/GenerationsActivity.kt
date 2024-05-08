package com.ecl.pokedex

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ecl.pokedex.databinding.ActivityGenerationsBinding

class GenerationsActivity: AppCompatActivity() {
    private lateinit var binding: ActivityGenerationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(Globals.themeID)
        binding = ActivityGenerationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NavDrawer(binding.navView, this)
    }

}