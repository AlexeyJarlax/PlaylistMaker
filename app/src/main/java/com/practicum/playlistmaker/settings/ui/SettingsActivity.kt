package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.utils.setDebouncedClickListener
import com.practicum.playlistmaker.utils.bindGoBackButton
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindGoBackButton()

        // КНОПКА НОЧНОЙ И ДНЕВНОЙ ТЕМЫ
        viewModel.isNightMode.observe(this) { binding.switchDarkMode.isChecked = it }
        binding.switchDarkMode.setOnCheckedChangeListener { _, value ->
            viewModel.changeNightMode(
                value
            )
        }

        // КНОПКА ПОДЕЛИТЬСЯ
        binding.buttonSettingsShare.setDebouncedClickListener {
            viewModel.shareApp()
        }

        // КНОПКА ТЕХПОДДЕРЖКИ
        binding.buttonSettingsWriteToSupp.setDebouncedClickListener {
            viewModel.goToHelp()
        }

        // КНОПКА ПОЛЬЗОВАТЕЛЬСКОГО СОГЛАШЕНИЯ
        binding.buttonSettingsUserAgreement.setDebouncedClickListener {
            viewModel.seeUserAgreement()
        }
    }
}
