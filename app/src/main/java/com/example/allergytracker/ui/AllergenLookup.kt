package com.example.allergytracker.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allergytracker.R
import com.example.allergytracker.data.AllergenViewModel
import com.example.allergytracker.data.FoodResult
import com.example.allergytracker.data.LoadingStatus
import com.google.android.material.progressindicator.CircularProgressIndicator
const val APP_ID: String = ""
const val APP_KEY: String = ""

class AllergenLookup : AppCompatActivity() {
    private val allergenAdapter = AllergenAdapter(::onAllergenResultClick)
    private val viewModel: AllergenViewModel by viewModels()

    private lateinit var allergenListRV: RecyclerView
    private lateinit var searchErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator
    private lateinit var searchItemDetails: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allergen_lookup)

        searchErrorTV = findViewById(R.id.tv_search_error)
        loadingIndicator = findViewById(R.id.loading_indicator)
        searchItemDetails = findViewById(R.id.allergen_details_frame)

        allergenListRV = findViewById(R.id.rv_allergen_list)
        allergenListRV.layoutManager = LinearLayoutManager(this)
        allergenListRV.setHasFixedSize(true)
        allergenListRV.adapter = allergenAdapter

        val searchET: EditText = findViewById(R.id.et_search_query)
        findViewById<Button>(R.id.btn_search).setOnClickListener {
            val query = searchET.text.toString()

            if (!TextUtils.isEmpty(query))
                viewModel.loadSearchResults(APP_ID, APP_KEY, query)
        }

        viewModel.searchResults.observe(this) {
            allergenAdapter.updateResults(it ?: listOf())
        }

        viewModel.foodDetails.observe(this) {
            Log.d("Main", "loaded details of ${it?.food?.get(0)?.parsed?.get(0)?.name ?: "n/a"}")
            if (it != null) {
                Log.d("Main", "loaded details of ${it.food[0].parsed[0].name}")
                searchItemDetails.visibility = View.VISIBLE
                findViewById<TextView>(R.id.tv_details_1).text = it.food[0].parsed[0].name
                var test: String = ""
                for (code in it.healthLabels)
                    test += "$code "
                findViewById<TextView>(R.id.tv_details_2).text = test
            }
        }

        viewModel.loadingStatus.observe(this) {
            when (it) {
                LoadingStatus.LOADING -> {
                    loadingIndicator.visibility = View.VISIBLE
                    allergenListRV.visibility = View.INVISIBLE
                    searchErrorTV.visibility = View.INVISIBLE
                    searchItemDetails.visibility = View.INVISIBLE
                }
                LoadingStatus.SUCCESS -> {
                    loadingIndicator.visibility = View.INVISIBLE
                    allergenListRV.visibility = View.VISIBLE
                    searchET.setText("")
                }
                LoadingStatus.ERROR -> {
                    loadingIndicator.visibility = View.INVISIBLE
                    searchErrorTV.visibility = View.VISIBLE
                }
            }
        }

        viewModel.errorMessage.observe(this) {
            searchErrorTV.text = getString(R.string.search_error, it ?: "unknown error")
        }
    }

    //private var lastClicked: FoodResult? = null
    private fun onAllergenResultClick(result: FoodResult) {
        /*if (result == lastClicked) {
            searchItemDetails.visibility = View.INVISIBLE
            lastClicked = null
            return
        }*/

        viewModel.loadAllergenDetails(APP_ID, APP_KEY, result)
    }
}