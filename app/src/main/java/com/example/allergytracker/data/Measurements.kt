package com.example.allergytracker.data

enum class Measurement {
    milligram, gram;

    companion object {
        fun ToString(m: Measurement) : String  = when (m) {
            milligram -> "mg"
            gram -> "g"
        }
    }
}