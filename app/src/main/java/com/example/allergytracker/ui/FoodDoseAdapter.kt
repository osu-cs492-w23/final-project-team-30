package com.example.allergytracker.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.allergytracker.R
import com.example.allergytracker.data.doseschedule.FoodDose

class FoodDoseAdapter(private val onClick: (FoodDose) -> Unit) : RecyclerView.Adapter<FoodDoseAdapter.FoodDoseViewHolder>() {
    var foodDoses: List<FoodDose> = mutableListOf()

    override fun getItemCount() = foodDoses.size

    fun updateDoses(newDoses: List<FoodDose>) {
        foodDoses = newDoses
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodDoseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fooddose_list_item, parent, false)

        return FoodDoseViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: FoodDoseViewHolder, position: Int) {
        holder.bind(foodDoses[position])
    }

    class FoodDoseViewHolder(view: View, val onClick: (FoodDose) -> Unit) : RecyclerView.ViewHolder(view) {
        private var foodDose: FoodDose? = null

        private val doseNameText: TextView = view.findViewById(R.id.tv_food_dose_name)
        private val doseDescText: TextView = view.findViewById(R.id.tv_food_dose_details)

        init {
            view.setOnClickListener {
                foodDose?.let(onClick)
            }
        }

        fun bind(foodDose: FoodDose) {
            this.foodDose = foodDose

            doseNameText.text = foodDose.name
            doseDescText.text = foodDose.id.toString()
        }
    }
}