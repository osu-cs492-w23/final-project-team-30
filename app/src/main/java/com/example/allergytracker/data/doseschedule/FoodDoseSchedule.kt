package com.example.allergytracker.data.doseschedule

import androidx.room.Entity
import com.example.allergytracker.data.Measurement

@Entity(primaryKeys = ["foodId", "startDay"])
data class FoodDoseSchedule(
    val foodId: Int,
    val startDay: Int,
    val startMonth: Int,
    val startYear: Int,
    val amount: Int,
    val unit: Measurement,
    val frequency: Int
) : java.io.Serializable
