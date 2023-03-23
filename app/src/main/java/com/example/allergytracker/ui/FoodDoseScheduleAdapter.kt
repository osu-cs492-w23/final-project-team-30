package com.example.allergytracker.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.allergytracker.R
import com.example.allergytracker.data.doseschedule.FoodDoseSchedule
import kotlinx.coroutines.currentCoroutineContext

class FoodDoseScheduleAdapter(private val onClick: (FoodDoseSchedule, LinearLayout?) -> Unit) : RecyclerView.Adapter<FoodDoseScheduleAdapter.DoseScheduleViewHolder>() {
    var scheduleItems: MutableList<FoodDoseSchedule> = mutableListOf()

    override fun getItemCount(): Int = scheduleItems.size

    fun setDoseScheduleItems(newItems: MutableList<FoodDoseSchedule>) {
        scheduleItems.clear()
        scheduleItems.addAll(newItems)
        notifyDataSetChanged()
    }

    fun addDoseScheduleItem(newItem: FoodDoseSchedule) {
        scheduleItems.add(newItem)
        notifyDataSetChanged()
    }

    fun remDoseScheduleItem(item: FoodDoseSchedule) {
        scheduleItems.remove(item)
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

    class DoseScheduleViewHolder(view: View, val onClick: (FoodDoseSchedule, LinearLayout?) -> Unit) : RecyclerView.ViewHolder(view) {
        private var currDose: FoodDoseSchedule? = null

        private val layout: LinearLayout = view.findViewById(R.id.dose_schedule_list_item)
        private val startDateText: TextView = view.findViewById(R.id.tv_food_dose_start)
        private val doseAmountText: TextView = view.findViewById(R.id.tv_food_dose_amount)
        private val doseFreqText: TextView = view.findViewById(R.id.tv_food_dose_frequency)

        init {
            view.setOnClickListener {
                if (currDose != null)
                    onClick(currDose!!, null)
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