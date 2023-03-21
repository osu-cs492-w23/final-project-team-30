package com.example.allergytracker.data.doseschedule

import androidx.room.Entity
import com.example.allergytracker.data.Measurement

@Entity(primaryKeys = ["foodId", "startDate"])
data class FoodDoseSchedule(
    val foodId: Int,
    val startDate: Long,
    val amount: Int,
    val unit: Measurement,
    val frequency: Int
) : java.io.Serializable
