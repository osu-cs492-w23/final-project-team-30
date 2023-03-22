package com.example.allergytracker.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allergytracker.R
import com.example.allergytracker.data.Measurement
import com.example.allergytracker.data.doseschedule.DoseScheduleMode
import com.example.allergytracker.data.doseschedule.FoodDose
import com.example.allergytracker.data.doseschedule.FoodDoseSchedule
import com.example.allergytracker.data.doseschedule.FoodDoseViewModel
import com.google.android.material.progressindicator.CircularProgressIndicator
import java.time.LocalDate

class DoseScheduleDetailsFragment : Fragment(R.layout.dose_schedule_details_fragment) {
    private val args: DoseScheduleDetailsFragmentArgs by navArgs()
    private val viewModel: FoodDoseViewModel by viewModels()
    private val scheduleAdapter = FoodDoseScheduleAdapter(::onDoseScheduleClick)

    private var foodDose: FoodDose? = null//args.foodDose
    private var foodDoseSchedule: MutableList<FoodDoseSchedule> = mutableListOf()

    private var viewMode = DoseScheduleMode.View

    private lateinit var foodDoseName: EditText
    private lateinit var addDoseBtn: Button
    private lateinit var foodDoseScheduleRV: RecyclerView
    private lateinit var doseEntryDate: DatePicker
    private lateinit var doseEntryAmount: EditText
    private lateinit var doseEntryFreq: EditText

    private var currDoseEdit: FoodDoseSchedule? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        foodDoseScheduleRV = view.findViewById(R.id.rv_dose_schedule)
        foodDoseScheduleRV.layoutManager = LinearLayoutManager(requireContext())
        foodDoseScheduleRV.setHasFixedSize(true)
        foodDoseScheduleRV.adapter = scheduleAdapter

        foodDoseName = view.findViewById(R.id.dose_schedule_name)
        addDoseBtn = view.findViewById(R.id.btn_add_dose)
        doseEntryDate = view.findViewById(R.id.dose_start_cal)
        doseEntryAmount = view.findViewById(R.id.et_dose_amount)
        doseEntryFreq = view.findViewById(R.id.et_dose_freq)

        val loadingSymbol: CircularProgressIndicator = view.findViewById(R.id.loading_indicator)
        viewModel.loading.observe(viewLifecycleOwner) {
            Log.d("Main", "Loading: $it")
            when (it) {
                true -> {
                    loadingSymbol.visibility = View.VISIBLE
                }
                false -> {
                    loadingSymbol.visibility = View.INVISIBLE
                }
            }
        }

        if (args.foodDose == null) {
            changeViewMode(DoseScheduleMode.Edit)
        } else {
            foodDose = args.foodDose

            viewModel.loadFoodDoseSchedules(foodDose!!.id)

            viewModel.loadFoodDoseSchedules(foodDose!!.id).observe(viewLifecycleOwner) {
                foodDoseSchedule.addAll(it ?: listOf())
                displayFoodDoseData()
            }
        }

        addDoseBtn.setOnClickListener {
            val current = LocalDate.now()
            currDoseEdit = FoodDoseSchedule(
                0,
                0,
                current.dayOfMonth,
                current.monthValue,
                current.year,
                "",
                Measurement.milligram,
                ""
            )
            scheduleAdapter.addDoseScheduleItem(currDoseEdit!!)
            onDoseScheduleClick(currDoseEdit!!)
        }

        doseEntryDate.setOnDateChangedListener { _, year, month, day ->
            if (currDoseEdit != null) {
                currDoseEdit!!.startYear = year
                currDoseEdit!!.startMonth = month
                currDoseEdit!!.startDay = day
                scheduleAdapter.notifyDataSetChanged()
            }
        }
        doseEntryAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (currDoseEdit != null) {
                    currDoseEdit!!.amount = s.toString()
                    scheduleAdapter.notifyDataSetChanged()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        doseEntryFreq.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (currDoseEdit != null) {
                    currDoseEdit!!.frequency = s.toString()
                    scheduleAdapter.notifyDataSetChanged()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun displayFoodDoseData() {
        if (foodDose != null) {
            foodDoseName.setText(foodDose!!.name)
        }
        scheduleAdapter.setDoseScheduleItems(foodDoseSchedule)
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
        addDoseBtn.visibility = if (canEdit) View.VISIBLE else View.INVISIBLE
        doseEntryDate.visibility = if (canEdit) View.VISIBLE else View.INVISIBLE
        doseEntryAmount.visibility = if (canEdit) View.VISIBLE else View.INVISIBLE
        doseEntryFreq.visibility = if (canEdit) View.VISIBLE else View.INVISIBLE

        currDoseEdit = null
    }

    private fun saveSchedule() {
        if (foodDose != null)
            viewModel.remFoodDose(foodDose!!)

        foodDose = FoodDose(
            if (foodDose != null) foodDose!!.id else System.currentTimeMillis(),
            foodDoseName.text.toString(),
            true
        )
        viewModel.addFoodDose(foodDose!!)

        foodDoseSchedule = mutableListOf()
        foodDoseSchedule.addAll(scheduleAdapter.scheduleItems)
        for (dose in foodDoseSchedule) {
            dose.foodId = foodDose!!.id
            Log.d("Main", "${dose.foodId} ${dose.startDay} ${dose.startMonth} ${dose.startYear}")
            viewModel.addFoodDoseSchedule(dose)
        }

        currDoseEdit = null
    }

    private fun onDoseScheduleClick(foodDoseSchedule: FoodDoseSchedule) {
        currDoseEdit = foodDoseSchedule

        doseEntryDate.updateDate(
            foodDoseSchedule.startYear,
            foodDoseSchedule.startMonth,
            foodDoseSchedule.startDay
        )
        doseEntryAmount.setText(foodDoseSchedule.amount)
        doseEntryFreq.setText(foodDoseSchedule.frequency)
    }
}