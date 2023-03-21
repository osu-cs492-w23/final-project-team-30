package com.example.allergytracker.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.allergytracker.R
import com.example.allergytracker.data.settings.FoodDoseViewModel

class SettingsFragment : Fragment(R.layout.settings_fragment) {
    private val viewModel: FoodDoseViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}