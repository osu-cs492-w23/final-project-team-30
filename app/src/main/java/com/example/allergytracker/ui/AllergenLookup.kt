package com.example.allergytracker.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allergytracker.R
import com.example.allergytracker.data.AllergenViewModel
import com.example.allergytracker.data.FoodResult
import com.example.allergytracker.data.LoadingStatus
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.snackbar.Snackbar

const val APP_ID: String = ""
const val APP_KEY: String = ""

class AllergenLookup : AppCompatActivity() {
    private val allergenAdapter = AllergenAdapter(::onAllergenResultClick)
    private val viewModel: AllergenViewModel by viewModels()

    private lateinit var allergenListRV: RecyclerView
    private lateinit var loadingIndicator: CircularProgressIndicator
    private lateinit var searchItemDetails: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allergen_lookup)

        loadingIndicator = findViewById(R.id.loading_indicator)
        searchItemDetails = findViewById(R.id.allergen_details_frame)

        allergenListRV = findViewById(R.id.rv_allergen_list)
        allergenListRV.layoutManager = LinearLayoutManager(this)
        allergenListRV.setHasFixedSize(true)
        allergenListRV.adapter = allergenAdapter

        val searchET: EditText = findViewById(R.id.et_search_query)
        // From https://stackoverflow.com/questions/1109022/how-to-close-hide-the-android-soft-keyboard-programmatically
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        fun attemptSearch() {
            val query = searchET.text.toString()

            if (!TextUtils.isEmpty(query)) {
                viewModel.loadSearchResults(APP_ID, APP_KEY, query)
                imm?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
            }
        }

        findViewById<Button>(R.id.btn_search).setOnClickListener {
            attemptSearch()
        }

        viewModel.searchResults.observe(this) {
            allergenAdapter.updateResults(it ?: listOf())
        }

        viewModel.foodDetails.observe(this) {
            if (it != null) {
                searchItemDetails.visibility = View.VISIBLE
                findViewById<TextView>(R.id.tv_details_1).text = it.food[0].parsed[0].name
                var test: String = ""
                for (code in it.healthLabels)
                    test += "$code "
                findViewById<TextView>(R.id.tv_details_2).text = test
            }
        }

        viewModel.searchLoadingStatus.observe(this) {
            when (it) {
                LoadingStatus.LOADING -> {
                    loadingIndicator.visibility = View.VISIBLE
                    allergenListRV.visibility = View.INVISIBLE
                    searchItemDetails.visibility = View.INVISIBLE
                }
                LoadingStatus.SUCCESS -> {
                    loadingIndicator.visibility = View.INVISIBLE
                    allergenListRV.visibility = View.VISIBLE
                    searchET.setText("")
                }
                LoadingStatus.ERROR -> {
                    loadingIndicator.visibility = View.INVISIBLE
                }
            }
        }

        viewModel.detailsLoadingStatus.observe(this) {
            when (it) {
                LoadingStatus.LOADING -> {
                    loadingIndicator.visibility = View.VISIBLE
                }
                LoadingStatus.SUCCESS -> {
                    loadingIndicator.visibility = View.INVISIBLE
                    searchItemDetails.visibility = View.VISIBLE
                }
                LoadingStatus.ERROR -> {
                    loadingIndicator.visibility = View.INVISIBLE
                }
            }
        }

        val coordinatorLayout: CoordinatorLayout = findViewById(R.id.lookup_coordinator_layout)
        viewModel.errorMessage.observe(this) {
            if (it != null) {
                val sb = Snackbar.make(
                    coordinatorLayout,
                    getString(R.string.search_error, it),
                    Snackbar.LENGTH_LONG
                )
                /*sb.setAction("RETRY") {
                    attemptSearch()
                    sb.dismiss()
                }*/
                sb.show()
            }
        }
    }

    private var lastClicked: FoodResult? = null
    private fun onAllergenResultClick(result: FoodResult) {
        if (result.foodId == lastClicked?.foodId) {
            searchItemDetails.visibility = View.INVISIBLE
            lastClicked = null
            return
        }

        viewModel.loadAllergenDetails(APP_ID, APP_KEY, result)
        lastClicked = result
    }
}