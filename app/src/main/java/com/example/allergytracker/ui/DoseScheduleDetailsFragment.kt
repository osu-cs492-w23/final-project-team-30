package com.example.allergytracker.ui

import android.content.ClipData.Item
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.allergytracker.R
import com.example.allergytracker.data.doseschedule.DoseScheduleMode
import com.example.allergytracker.data.doseschedule.FoodDose
import com.example.allergytracker.data.doseschedule.FoodDoseViewModel

class DoseScheduleDetailsFragment : Fragment(R.layout.dose_schedule_details_fragment) {
    private val args: DoseScheduleDetailsFragmentArgs by navArgs()
    private val viewModel: FoodDoseViewModel by viewModels()

    private var foodDose: FoodDose? = null//args.foodDose

    private var viewMode = DoseScheduleMode.View

    private lateinit var foodDoseName: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        foodDoseName = view.findViewById(R.id.dose_schedule_name)

        if (args.foodDose == null) {
            changeViewMode(DoseScheduleMode.Edit)
        } else {
            foodDose = args.foodDose
            displayFoodDoseData()
        }
    }

    private fun displayFoodDoseData() {
        if (foodDose != null) {
            foodDoseName.setText(foodDose!!.name)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dose_schedule_details_bar, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        menu.findItem(R.id.action_save_schedule).isVisible = viewMode == DoseScheduleMode.Edit
        menu.findItem(R.id.action_cancel_edit_schedule).isVisible = viewMode == DoseScheduleMode.Edit

        menu.findItem(R.id.action_edit_schedule).isVisible = viewMode == DoseScheduleMode.View
        menu.findItem(R.id.action_delete_schedule).isVisible = viewMode == DoseScheduleMode.View
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.action_edit_schedule -> {
            changeViewMode(DoseScheduleMode.Edit)
            true
        }
        R.id.action_delete_schedule -> {
            if (foodDose != null)
                viewModel.remFoodDose(foodDose!!)
            val directions = DoseScheduleDetailsFragmentDirections.navigateToDoseSchedule()
            findNavController().navigate(directions)
            true
        }
        R.id.action_save_schedule -> {
            changeViewMode(DoseScheduleMode.View)
            saveSchedule()
            true
        }
        R.id.action_cancel_edit_schedule -> {
            changeViewMode(DoseScheduleMode.View)
            displayFoodDoseData()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun changeViewMode(newMode: DoseScheduleMode) {
        viewMode = newMode
        requireActivity().invalidateOptionsMenu()
        setEditableViews(newMode == DoseScheduleMode.Edit)
    }

    private fun setEditableViews(canEdit: Boolean) {
        foodDoseName.isFocusableInTouchMode = canEdit
    }

    private fun saveSchedule() {
        if (foodDose != null)
            viewModel.remFoodDose(foodDose!!)

        foodDose = FoodDose(name = foodDoseName.text.toString(), isIndefinite = true)
        viewModel.addFoodDose(foodDose!!)
    }
}