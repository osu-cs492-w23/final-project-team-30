package com.example.allergytracker.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.allergytracker.R
import com.example.allergytracker.data.doseschedule.FoodDoseViewModel

class DoseScheduleFragment : Fragment(R.layout.dose_schedule_fragment) {
    private val viewModel: FoodDoseViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}