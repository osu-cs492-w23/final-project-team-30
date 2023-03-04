package com.example.allergytracker.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allergytracker.R
import com.example.allergytracker.api.AllergenLookupService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val APP_ID: String = ""
const val APP_KEY: String = ""

class AllergenLookup : AppCompatActivity() {
    private val allergenLookupService = AllergenLookupService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allergen_lookup)

        val allergenListRV: RecyclerView = findViewById(R.id.rv_allergen_list)
        allergenListRV.layoutManager = LinearLayoutManager(this)
        allergenListRV.setHasFixedSize(true)

        val adapter = AllergenAdapter()
        allergenListRV.adapter = adapter

        val searchET: EditText = findViewById(R.id.et_search_query)
        findViewById<Button>(R.id.btn_search).setOnClickListener {
            val query = searchET.text.toString()
            if (!TextUtils.isEmpty(query)) {
                doAllergenLookup(query)
                searchET.setText("")
            }
        }
    }

    private fun doAllergenLookup(ingredient: String) {
        allergenLookupService.searchFood(APP_ID, APP_KEY, ingredient)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.d("MainActivity", "Status code: ${response.code()}")
                    Log.d("MainActivity", "Response body: ${response.body()}")
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("MainActivity", "Error making API call: ${t.message}")
                }
            })
    }
}