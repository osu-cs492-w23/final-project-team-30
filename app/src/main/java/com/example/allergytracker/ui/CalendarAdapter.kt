package com.example.allergytracker.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.allergytracker.R
import com.example.allergytracker.data.doseschedule.FoodDose
import com.example.allergytracker.data.doseschedule.FoodDoseSchedule

class CalendarAdapter : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {
    var scheduleDetails: MutableList<FoodDose> = mutableListOf()
    var scheduleItems: MutableList<FoodDoseSchedule> = mutableListOf()

    override fun getItemCount() = scheduleItems.size

    fun clearItems() {
        scheduleDetails.clear()
        scheduleItems.clear()
        notifyDataSetChanged()
    }

    fun addItem(details: FoodDose, item: FoodDoseSchedule) {
        scheduleDetails.add(details)
        scheduleItems.add(item)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.calendar_list_item, parent, false)

        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(scheduleDetails[position], scheduleItems[position])
    }

    class CalendarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val doseNameText: TextView = view.findViewById(R.id.tv_dose_name)
        private val doseAmountText: TextView = view.findViewById(R.id.tv_dose_amount)
        private val doseFreqText: TextView = view.findViewById(R.id.tv_dose_frequency)

        fun bind(details: FoodDose, item: FoodDoseSchedule) {
            doseNameText.text = details.name
            doseAmountText.text = "${item.amount} ${item.unit}"
            doseFreqText.text = item.frequency
        }
    }
}