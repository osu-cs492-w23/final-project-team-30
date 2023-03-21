package com.example.allergytracker.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allergytracker.BuildConfig
import com.example.allergytracker.R
import com.example.allergytracker.data.allergenlookup.AllergenViewModel
import com.example.allergytracker.data.allergenlookup.FoodResult
import com.example.allergytracker.data.allergenlookup.HealthCodeConstants
import com.example.allergytracker.data.allergenlookup.LoadingStatus
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.snackbar.Snackbar

class AllergenLookupFragment : Fragment(R.layout.allergen_lookup_fragment) {
    private val allergenAdapter = AllergenAdapter(::onAllergenResultClick)
    private val viewModel: AllergenViewModel by viewModels()

    private lateinit var allergenListRV: RecyclerView
    private lateinit var loadingIndicator: CircularProgressIndicator
    private lateinit var searchItemDetails: ConstraintLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        loadingIndicator = view.findViewById(R.id.loading_indicator)
        searchItemDetails = view.findViewById(R.id.allergen_details_frame)

        allergenListRV = view.findViewById(R.id.rv_allergen_list)
        allergenListRV.layoutManager = LinearLayoutManager(requireContext())
        allergenListRV.setHasFixedSize(true)
        allergenListRV.adapter = allergenAdapter

        val searchET: EditText = view.findViewById(R.id.et_search_query)
        // From https://stackoverflow.com/questions/1109022/how-to-close-hide-the-android-soft-keyboard-programmatically
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        fun attemptSearch() {
            val query = searchET.text.toString()

            if (!TextUtils.isEmpty(query)) {
                viewModel.loadSearchResults(BuildConfig.EDAMAM_APP_ID, BuildConfig.EDAMAM_APP_KEY, query)
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        view.findViewById<Button>(R.id.btn_search).setOnClickListener {
            attemptSearch()
        }

        viewModel.searchResults.observe(viewLifecycleOwner) {
            allergenAdapter.updateResults(it ?: listOf())
        }

        val allergenDetailsFoodName: TextView = view.findViewById(R.id.tv_food_name)
        val allergenDetails: TextView = view.findViewById(R.id.tv_food_allergens)
        val allergenKosher: TextView = view.findViewById(R.id.tv_kosher)
        val allergenVegetarian: TextView = view.findViewById(R.id.tv_vegetarian)
        val allergenVegan: TextView = view.findViewById(R.id.tv_vegan)
        val allergenPescatarian: TextView = view.findViewById(R.id.tv_pescatarian)
        viewModel.foodDetails.observe(viewLifecycleOwner) {
            if (it != null) {
                searchItemDetails.visibility = View.VISIBLE
                Log.d("Main", "Showing details")
                allergenDetailsFoodName.text = it.food[0].parsed[0].name

                val healthInfo = HealthCodeConstants.readHealthInfo(it)

                allergenDetails.text = healthInfo.allergens.joinToString(", ")

                allergenKosher.visibility = if (healthInfo.kosher) View.VISIBLE else View.INVISIBLE
                allergenVegetarian.visibility = if (healthInfo.vegetarian) View.VISIBLE else View.INVISIBLE
                allergenVegan.visibility = if (healthInfo.vegan) View.VISIBLE else View.INVISIBLE
                allergenPescatarian.visibility = if (healthInfo.pescatarian) View.VISIBLE else View.INVISIBLE

            }
        }

        viewModel.searchLoadingStatus.observe(viewLifecycleOwner) {
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

        viewModel.detailsLoadingStatus.observe(viewLifecycleOwner) {
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

        val coordinatorLayout: CoordinatorLayout = view.findViewById(R.id.lookup_coordinator_layout)
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

        viewModel.loadAllergenDetails(BuildConfig.EDAMAM_APP_ID, BuildConfig.EDAMAM_APP_KEY, result)
        lastClicked = result
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.activity_allergen_lookup_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                shareResult()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun shareResult() {
        if (lastClicked != null && viewModel.foodDetails.value != null) {

            val healthInfo = HealthCodeConstants.readHealthInfo(viewModel.foodDetails.value!!)

            val text = getString(R.string.share_text,
                lastClicked!!.name,
                healthInfo.allergens.joinToString(", "))

            val intent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(intent, null))
        }
    }
}