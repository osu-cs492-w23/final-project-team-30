package com.example.allergytracker.ui

import android.icu.util.Calendar
import com.example.allergytracker.data.doseschedule.FoodDose
import com.example.allergytracker.data.doseschedule.FoodDoseSchedule

data class FoodDoseWithSchedule(
    val foodDose: FoodDose,
    val doseSchedule: MutableList<FoodDoseSchedule>,
    val startDate: Calendar,
    val endDate: Calendar
)
