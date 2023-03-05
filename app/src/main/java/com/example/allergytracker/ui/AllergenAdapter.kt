package com.example.allergytracker.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.allergytracker.R
import com.example.allergytracker.data.AllergenResult
import com.example.allergytracker.data.FoodEntry
import com.example.allergytracker.data.FoodResult

class AllergenAdapter(private val onClick: (FoodResult) -> Unit) : RecyclerView.Adapter<AllergenAdapter.AllergenViewHolder>() {
    var results: List<FoodResult> = mutableListOf()

    override fun getItemCount(): Int = results.size

    fun updateResults(newResults: List<FoodResult>) {
        results = newResults ?: listOf()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllergenViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.allergen_list_item, parent, false)

        return AllergenViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: AllergenViewHolder, position: Int) {
        holder.bind(results[position])
    }

    class AllergenViewHolder(view: View, val onClick : (FoodResult) -> Unit) : RecyclerView.ViewHolder(view) {
        private val resultText: TextView = view.findViewById(R.id.tv_allergen_result)
        private var currResult: FoodResult? = null

        init {
            view.setOnClickListener {
                currResult?.let(onClick)
            }
        }

        fun bind(result: FoodResult) {
            currResult = result
            resultText.text = result.name
        }
    }
}