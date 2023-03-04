package com.example.allergytracker.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.allergytracker.R
import com.example.allergytracker.data.AllergenResult

class AllergenAdapter : RecyclerView.Adapter<AllergenAdapter.AllergenViewHolder>() {
    val results: MutableList<AllergenResult> = mutableListOf()

    override fun getItemCount(): Int = results.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllergenViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.allergen_list_item, parent, false)

        return AllergenViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllergenViewHolder, position: Int) {
        holder.bind(results[position])
    }

    class AllergenViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val resultText: TextView = view.findViewById(R.id.tv_allergen_result)
        private var currResult: AllergenResult? = null

        fun bind(result: AllergenResult) {
            currResult = result
            resultText.text = result.name
        }
    }
}