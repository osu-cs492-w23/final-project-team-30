package com.example.allergytracker.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AllergenResult(
    @Json(name="parsed") val topResult: List<FoodEntry>,
    @Json(name="hints") val results: List<FoodEntry>?
)

/*
{
        "text": "cheese",
    "parsed": [
        {
            "food": {
                "foodId": "food_bhppgmha1u27voagb8eptbp9g376",
                "label": "Cheese",
                "knownAs": "cheddar cheese",
                "nutrients": {
                    "ENERC_KCAL": 406,
                    "PROCNT": 24.04,
                    "FAT": 33.82,
                    "CHOCDF": 1.33,
                    "FIBTG": 0
                },
                "category": "Generic foods",
                "categoryLabel": "food",
                "image": "https://www.edamam.com/food-img/bcd/bcd94dde1fcde1475b5bf0540f821c5d.jpg"
            }
        }
    ],
    "hints": [
    {
        "food": {
            "foodId": "food_bhppgmha1u27voagb8eptbp9g376",
            "label": "Cheese",
            "knownAs": "cheddar cheese",
            "nutrients": {
                "ENERC_KCAL": 406,
                "PROCNT": 24.04,
                "FAT": 33.82,
                "CHOCDF": 1.33,
                "FIBTG": 0
            },
            "category": "Generic foods",
            "categoryLabel": "food",
            "image": "https://www.edamam.com/food-img/bcd/bcd94dde1fcde1475b5bf0540f821c5d.jpg"
        },
        "measures": [
            {
                "uri": "http://www.edamam.com/ontologies/edamam.owl#Measure_unit",
                "label": "Whole"
            },
            {
                "uri": "http://www.edamam.com/ontologies/edamam.owl#Measure_block",
                "label": "Block"
            }
        ]
    },
    {
        "food": {
        "foodId": "food_bhppgmha1u27voagb8eptbp9g376",
        "label": "Cheddar Cheese",
        "knownAs": "cheddar cheese",
        "nutrients": {
        "ENERC_KCAL": 406,
        "PROCNT": 24.04,
        "FAT": 33.82,
        "CHOCDF": 1.33,
        "FIBTG": 0
    },
        "category": "Generic foods",
        "categoryLabel": "food",
        "image": "https://www.edamam.com/food-img/bcd/bcd94dde1fcde1475b5bf0540f821c5d.jpg"
    },
        "measures": [
        {
            "uri": "http://www.edamam.com/ontologies/edamam.owl#Measure_unit",
            "label": "Whole"
        },
        {
            "uri": "http://www.edamam.com/ontologies/edamam.owl#Measure_block",
            "label": "Block"
        },
        {
            "uri": "http://www.edamam.com/ontologies/edamam.owl#Measure_piece",
            "label": "Piece"
        },
        {
            "uri": "http://www.edamam.com/ontologies/edamam.owl#Measure_serving",
            "label": "Serving"
        },
        {
            "uri": "http://www.edamam.com/ontologies/edamam.owl#Measure_slice",
            "label": "Slice"
        },
        {
            "uri": "http://www.edamam.com/ontologies/edamam.owl#Measure_chip",
            "label": "Chip"
        },
        {
            "uri": "http://www.edamam.com/ontologies/edamam.owl#Measure_package",
            "label": "Package"
        },
        {
            "uri": "http://www.edamam.com/ontologies/edamam.owl#Measure_gram",
            "label": "Gram"
        },
        {
            "uri": "http://www.edamam.com/ontologies/edamam.owl#Measure_ounce",
            "label": "Ounce"
        },
        {
            "uri": "http://www.edamam.com/ontologies/edamam.owl#Measure_pound",
            "label": "Pound"
        },
        {
            "uri": "http://www.edamam.com/ontologies/edamam.owl#Measure_kilogram",
            "label": "Kilogram"
        },
        {
            "uri": "http://www.edamam.com/ontologies/edamam.owl#Measure_cup",
            "label": "Cup",
            "qualified": [
            {
                "qualifiers": [
                {
                    "uri": "http://www.edamam.com/ontologies/edamam.owl#Qualifier_diced",
                    "label": "diced"
                }
                ]
            },
            {
                "qualifiers": [
                {
                    "uri": "http://www.edamam.com/ontologies/edamam.owl#Qualifier_shredded",
                    "label": "shredded"
                }
                ]
            },
            {
                "qualifiers": [
                {
                    "uri": "http://www.edamam.com/ontologies/edamam.owl#Qualifier_melted",
                    "label": "melted"
                }
                ]
            }
            ]
        },
 */