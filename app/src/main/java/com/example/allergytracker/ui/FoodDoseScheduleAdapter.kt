package com.example.allergytracker.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.allergytracker.R
import com.example.allergytracker.data.doseschedule.FoodDoseSchedule

class FoodDoseScheduleAdapter(private val onClick: (FoodDoseSchedule) -> Unit) : RecyclerView.Adapter<FoodDoseScheduleAdapter.DoseScheduleViewHolder>() {
    var scheduleItems: MutableList<FoodDoseSchedule> = mutableListOf()

    override fun getItemCount(): Int = scheduleItems.size

    fun setDoseScheduleItems(newItems: MutableList<FoodDoseSchedule>) {
        Log.d("Main", "Setting RV to ${newItems.size}, currently has ${scheduleItems.size}")
        scheduleItems.clear()
        scheduleItems.addAll(newItems)
        Log.d("Main", "RV now has ${scheduleItems.size}")
        notifyDataSetChanged()
    }

    fun addDoseScheduleItem(newItem: FoodDoseSchedule) {
        Log.d("Main", "RV has ${scheduleItems.size}, adding 1")
        scheduleItems.add(newItem)
        Log.d("Main", "RV now has ${scheduleItems.size}")
        notifyDataSetChanged()
    }

    fun remDoseScheduleItem(item: FoodDoseSchedule) {
        Log.d("Main", "RV has ${scheduleItems.size}, removing 1")
        scheduleItems.remove(item)
        Log.d("Main", "RV now has ${scheduleItems.size}")
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoseScheduleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dose_schedule_list_item, parent, false)

        return  DoseScheduleViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: DoseScheduleViewHolder, position: Int) {
        holder.bind(scheduleItems[position])
    }

    class DoseScheduleViewHolder(view: View, val onClick: (FoodDoseSchedule) -> Unit) : RecyclerView.ViewHolder(view) {
        private var currDose: FoodDoseSchedule? = null

        private val startDateText: TextView = view.findViewById(R.id.tv_food_dose_start)
        private val doseAmountText: TextView = view.findViewById(R.id.tv_food_dose_amount)
        private val doseFreqText: TextView = view.findViewById(R.id.tv_food_dose_frequency)

        init {
            view.setOnClickListener {
                currDose?.let(onClick)
            }
        }

        fun bind(dose: FoodDoseSchedule) {
            currDose = dose

            startDateText.text = "${dose.startMonth}/${dose.startDay}/${dose.startYear}"
            doseAmountText.text = dose.amount + dose.unit.toString()
            doseFreqText.text = dose.frequency
        }
    }
}