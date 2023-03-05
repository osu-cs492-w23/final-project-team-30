package com.example.allergytracker.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allergytracker.R
import com.example.allergytracker.api.AllergenLookupService
import com.example.allergytracker.data.AllergenResult
import com.example.allergytracker.data.FoodResult
import com.google.android.material.progressindicator.CircularProgressIndicator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val APP_ID: String = ""
const val APP_KEY: String = ""

class AllergenLookup : AppCompatActivity() {
    private val allergenLookupService = AllergenLookupService.create()
    private val allergenAdapter = AllergenAdapter(::onAllergenResultClick)

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
            if (!TextUtils.isEmpty(query)) {
                loadingIndicator.visibility = View.VISIBLE
                allergenListRV.visibility = View.INVISIBLE
                searchErrorTV.visibility = View.INVISIBLE
                searchItemDetails.visibility = View.INVISIBLE
                doAllergenLookup(query)
                searchET.setText("")
            }
        }
    }

    private var lastClicked: FoodResult? = null
    private fun onAllergenResultClick(result: FoodResult) {
        if (result == lastClicked) {
            searchItemDetails.visibility = View.INVISIBLE
            lastClicked = null
            return
        }

        searchItemDetails.visibility = View.VISIBLE
        findViewById<TextView>(R.id.tv_details_1).text = result.name
        findViewById<TextView>(R.id.tv_details_2).text = result.label
    }

    private fun doAllergenLookup(ingredient: String) {
        allergenLookupService.searchFood(APP_ID, APP_KEY, ingredient)
            .enqueue(object : Callback<AllergenResult> {
                override fun onResponse(call: Call<AllergenResult>, response: Response<AllergenResult>) {
                    loadingIndicator.visibility = View.INVISIBLE

                    if (response.isSuccessful) {
                        allergenListRV.visibility = View.VISIBLE
                        val results: MutableList<FoodResult> = mutableListOf()

                        if (response.body() != null) {
                            if (response.body()?.topResult?.isNotEmpty() == true)
                                results.add(response.body()!!.topResult[0].data)
                            if (response.body()?.results?.isNotEmpty() == true) {
                                for (entry in response.body()!!.results) {
                                    results.add(entry.data)
                                }
                            }

                            allergenAdapter.updateResults(results)
                        }
                    }
                    else {
                        searchErrorTV.visibility = View.VISIBLE
                        searchErrorTV.text = getString(
                            R.string.search_error,
                            response.errorBody()?.string() ?: "unknown error"
                        )
                    }
                }

                override fun onFailure(call: Call<AllergenResult>, t: Throwable) {
                    loadingIndicator.visibility = View.INVISIBLE
                    searchErrorTV.visibility = View.VISIBLE
                    searchErrorTV.text = getString(
                        R.string.search_error,
                        t.message
                    )
                }
            })
    }
}