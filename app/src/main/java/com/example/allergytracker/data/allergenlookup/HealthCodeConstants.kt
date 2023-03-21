package com.example.allergytracker.data.allergenlookup

// https://developer.edamam.com/food-database-api-docs#/
object HealthCodeConstants {
    const val alcohol_free = "ALCOHOL_FREE";        const val alcohol = "Alcohol"
    //Immune-Supportive
    const val celery_free = "CELERY_FREE";          const val celery = "Celery"
    const val crustacean_free = "CRUSTACEAN_FREE";  const val crustacean = "Crustacean"
    const val dairy_free = "DAIRY_FREE";            const val dairy = "Dairy"
    // DASH
    const val egg_free = "EGG_FREE";                const val egg = "Eggs"
    const val fish_free = "FISH_FREE";              const val fish = "Fish"
    // FODMAP
    const val gluten_free = "GLUTEN_FREE";          const val gluten = "Gluten"
    //const val keto = "KETO-FRIENDLY"
    // Kidney-Friendly
    const val kosher = "KOSHER"
    // Low potassium
    const val lupine_free = "LUPINE_FREE";          const val lupine = "Lupine/derivatives"
    // Mediterranean
    const val mustard_free = "MUSTARD_FREE";        const val mustard = "Mustard"
    // Low-fat-abs
    const val oil_free = "NO_OIL_ADDED";            const val oil = "Oil"
    //const val low_sugar = "LOW-SUGAR"
    // Paleo
    const val peanut_free = "PEANUT_FREE";          const val peanut = "Peanuts"
    const val pescatarian = "PESCATARIAN"
    const val pork_free = "PORK_FREE";              const val pork = "Pork"
    const val red_meat_free = "RED_MEAT_FREE";      const val red_meat = "Red meat"
    const val sesame_free = "SESAME_FREE";          const val sesame = "Sesame Seed/derivatives"
    const val shellfish_free = "SHELLFISH_FREE";    const val shellfish = "Shellfish"
    const val soy_free = "SOY_FREE";                const val soy = "Soy"
    // Sugar-Conscious
    const val tree_nut_free = "TREE_NUT_FREE";      const val tree_nut = "Tree nuts"
    const val vegan = "VEGAN"
    const val vegetarian = "VEGETARIAN"
    const val wheat_free = "WHEAT_FREE";            const val wheat = "Wheat"

    private val readableConstants: Map<String, String> = mapOf(
        alcohol_free to alcohol,
        celery_free to celery,
        crustacean_free to crustacean,
        dairy_free to dairy,
        egg_free to egg,
        fish_free to fish,
        gluten_free to gluten,
        lupine_free to lupine,
        mustard_free to mustard,
        oil_free to oil,
        peanut_free to peanut,
        pork_free to pork,
        red_meat_free to red_meat,
        sesame_free to sesame,
        shellfish_free to shellfish,
        soy_free to soy,
        tree_nut_free to tree_nut,
        wheat_free to wheat
    )

    fun readHealthInfo(allergenDetails: FoodAllergenDetails) : HealthInfo {
        val allergens: MutableList<String> = mutableListOf()

        for (key in readableConstants.keys)
            if (key !in allergenDetails.healthLabels)
                allergens.add(readableConstants[key]!!)

        return HealthInfo(
            allergens,
            kosher in allergenDetails.healthLabels,
            pescatarian in allergenDetails.healthLabels,
            vegan in allergenDetails.healthLabels,
            vegetarian in allergenDetails.healthLabels
        )
    }

    data class HealthInfo (
        val allergens: List<String>,
        val kosher: Boolean,
        val pescatarian: Boolean,
        val vegan: Boolean,
        val vegetarian: Boolean
    )
}