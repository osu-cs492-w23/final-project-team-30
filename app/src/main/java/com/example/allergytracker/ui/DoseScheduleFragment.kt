package com.example.allergytracker.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allergytracker.R
import com.example.allergytracker.data.doseschedule.FoodDose
import com.example.allergytracker.data.doseschedule.FoodDoseViewModel

class DoseScheduleFragment : Fragment(R.layout.dose_schedule_fragment) {
    private val doseAdapter = FoodDoseAdapter(::onDoseItemClick)
    private val viewModel: FoodDoseViewModel by viewModels()

    private lateinit var foodDoseListRV: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        foodDoseListRV = view.findViewById(R.id.rv_dose_list)
        foodDoseListRV.layoutManager = LinearLayoutManager(requireContext())
        foodDoseListRV.setHasFixedSize(true)
        foodDoseListRV.adapter = doseAdapter

        viewModel.foodDoses.observe(viewLifecycleOwner) {
            if (it != null) {
                doseAdapter.updateDoses(it)
            }
        }
    }

    private fun onDoseItemClick(foodDose: FoodDose) {
        val directions = DoseScheduleFragmentDirections.navigateToDoseScheduleDetails(foodDose)
        findNavController().navigate(directions)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dose_schedule_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.action_add_dose_schedule -> {
            val directions = DoseScheduleFragmentDirections.navigateToDoseScheduleDetails(null)
            findNavController().navigate(directions)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}