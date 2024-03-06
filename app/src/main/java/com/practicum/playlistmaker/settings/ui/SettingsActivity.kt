package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.utils.setDebouncedClickListener
import com.practicum.playlistmaker.utils.bindGoBackButton

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModelProvider: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModelProvider = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]
        bindGoBackButton()

        // КНОПКА НОЧНОЙ И ДНЕВНОЙ ТЕМЫ
        viewModelProvider.isNightMode.observe(this) { binding.switchDarkMode.isChecked = it }
        binding.switchDarkMode.setOnCheckedChangeListener { _, value ->
            viewModelProvider.changeNightMode(
                value
            )
        }

        // КНОПКА ПОДЕЛИТЬСЯ
        binding.buttonSettingsShare.setDebouncedClickListener {
            viewModelProvider.shareApp()
        }

        // КНОПКА ТЕХПОДДЕРЖКИ
        binding.buttonSettingsWriteToSupp.setDebouncedClickListener {
            viewModelProvider.goToHelp()
        }

        // КНОПКА ПОЛЬЗОВАТЕЛЬСКОГО СОГЛАШЕНИЯ
        binding.buttonSettingsUserAgreement.setDebouncedClickListener {
            viewModelProvider.seeUserAgreement()
        }
    }
}
