package com.sample.androidfundamental2.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.sample.androidfundamental2.ViewModelFactory
import com.sample.androidfundamental2.databinding.FragmentSettingsBinding
import com.sample.androidfundamental2.worker.DailyReminderWorker
import java.util.concurrent.TimeUnit

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeThemeSettings()
        observeNotificationSettings()
        setupListeners()
    }

    private fun observeThemeSettings() {
        viewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive ->
            binding.switchTheme.isChecked = isDarkModeActive
        }
    }

    private fun observeNotificationSettings() {
        viewModel.getNotificationSettings().observe(viewLifecycleOwner) { isNotificationActive ->
            binding.switchNotification.isChecked = isNotificationActive
        }
    }

    private fun setupListeners() {
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveThemeSetting(isChecked)
            updateTheme(isChecked)
        }

        binding.switchNotification.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveNotificationSetting(isChecked)
            if (isChecked) {
                startDailyReminder()
            } else {
                stopDailyReminder()
            }
        }
    }

    private fun updateTheme(isDarkModeActive: Boolean) {
        if (isDarkModeActive) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun startDailyReminder() {
        val workRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(
            1, TimeUnit.DAYS
        ).build()

        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            DailyReminderWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    private fun stopDailyReminder() {
        WorkManager.getInstance(requireContext()).cancelUniqueWork(DailyReminderWorker.WORK_NAME)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
