package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.utils.setDebouncedClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDarkModeSwitch()
        setupShareButton()
        setupSupportButton()
        setupUserAgreementButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupDarkModeSwitch() {
        viewModel.isNightMode.observe(viewLifecycleOwner) { isNightMode ->
            binding.switchDarkMode.isChecked = isNightMode
        }
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.changeNightMode(isChecked)
        }
    }

    private fun setupShareButton() {
        binding.buttonSettingsShare.setDebouncedClickListener {
            viewModel.shareApp()
        }
    }

    private fun setupSupportButton() {
        binding.buttonSettingsWriteToSupp.setDebouncedClickListener {
            viewModel.goToHelp()
        }
    }

    private fun setupUserAgreementButton() {
        binding.buttonSettingsUserAgreement.setDebouncedClickListener {
            viewModel.seeUserAgreement()
        }
    }
}