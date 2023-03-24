package com.example.allergytracker.ui

import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CalendarView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allergytracker.R
import com.example.allergytracker.data.doseschedule.FoodDose
import com.example.allergytracker.data.doseschedule.FoodDoseSchedule
import com.example.allergytracker.data.doseschedule.FoodDoseViewModel
import com.google.android.material.progressindicator.CircularProgressIndicator
import java.util.Date

class CalendarFragment : Fragment(R.layout.calendar_fragment) {
    private val viewModel: FoodDoseViewModel by viewModels()
    private val scheduleAdapter = CalendarAdapter()

    private lateinit var calendar: CalendarView
    private lateinit var loadingIndicator: CircularProgressIndicator
    private lateinit var foodDoseScheduleRV: RecyclerView

    //private var foodDosesWithSchedules: MutableList<FoodDoseWithSchedule> = mutableListOf()
    private var foodDoses: List<FoodDose>? = null
    private var foodDoseSchedules: List<FoodDoseSchedule>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendar = view.findViewById(R.id.calendar)
        loadingIndicator = view.findViewById(R.id.loading_indicator)

        foodDoseScheduleRV = view.findViewById(R.id.rv_dose_schedule)
        foodDoseScheduleRV.layoutManager = LinearLayoutManager(requireContext())
        foodDoseScheduleRV.setHasFixedSize(true)
        foodDoseScheduleRV.adapter = scheduleAdapter


        calendar.isFocusableInTouchMode = false
        loadingIndicator.visibility = View.VISIBLE

        //var foodDoses: List<FoodDose>? = null
        //var foodDoseSchedules: List<FoodDoseSchedule>? = null

        viewModel.foodDoses.observe(viewLifecycleOwner) {
            if (it != null) {
                foodDoses = it

                if (foodDoseSchedules != null)
                    loadFoodDosesWithSchedules(it, foodDoseSchedules!!)
            }
        }

        viewModel.allFoodDoseSchedules.observe(viewLifecycleOwner) {
            if (it != null) {
                foodDoseSchedules = it

                if (foodDoses != null) {
                    loadFoodDosesWithSchedules(foodDoses!!, it)
                }
            }
        }

        calendar.setOnDateChangeListener(::displayDateDetails)
    }

    private fun loadFoodDosesWithSchedules(foodDoses: List<FoodDose>, foodDoseSchedules: List<FoodDoseSchedule>) {
        /*foodDosesWithShedules.clear()
        foodDoses.forEach {
            val details = FoodDoseWithSchedule(it, mutableListOf(), Calendar.getInstance(), Calendar.getInstance())

            foodDoseSchedules.forEach {s ->
                if (it.id == s.foodId) {
                    details.doseSchedule.add(s)
                }
            }

            foodDosesWithShedules.add(details)
        }*/

        calendar.isFocusableInTouchMode = true
        loadingIndicator.visibility = View.INVISIBLE
    }

    private fun displayDateDetails(view: CalendarView, y: Int, m: Int, d: Int) {
        /*val date = Calendar.getInstance()
        date.set(Calendar.YEAR, y)
        date.set(Calendar.MONTH, m)
        date.set(Calendar.DAY_OF_MONTH, d)*/

        Log.d("Main", "$m/$d/$y")

        scheduleAdapter.clearItems()

        foodDoseSchedules?.forEach {
            /*val d = Calendar.getInstance()
            d.set(Calendar.YEAR, it.startYear)
            d.set(Calendar.MONTH, it.startMonth)
            d.set(Calendar.DAY_OF_MONTH, it.startDay)*/

            if (y == it.startYear && m == it.startMonth && d == it.startDay)
                foodDoses?.forEach { fd ->
                    if (fd.id == it.foodId)
                        scheduleAdapter.addItem(fd, it)
                }
        }
    }
}