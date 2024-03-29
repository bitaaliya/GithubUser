package com.dicoding.socialsnap.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.dicoding.socialsnap.R
import com.dicoding.socialsnap.factory.DarkModeFactory
import com.dicoding.socialsnap.viewmodel.DarkModeViewModel
import com.google.android.material.switchmaterial.SwitchMaterial

class DarkModeActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dark_mode)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val switchTheme = findViewById<SwitchMaterial>(R.id.switch_theme)
        val pref = SettingPreference.getInstance(application.applicationContext)
        val darkModeViewModel = ViewModelProvider(this, DarkModeFactory(pref))[DarkModeViewModel::class.java]

        darkModeViewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
                supportActionBar?.title = "Dark Mode"
                switchTheme.text = "Dark Mode"
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
                supportActionBar?.title = "Light Mode"
                switchTheme.text = "Light Mode"
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            darkModeViewModel.saveThemeSetting(isChecked)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}