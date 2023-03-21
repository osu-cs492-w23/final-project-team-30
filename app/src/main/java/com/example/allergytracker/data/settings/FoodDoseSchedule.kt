package com.example.allergytracker.data.settings

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.allergytracker.data.Measurement
import java.math.BigInteger

@Entity(primaryKeys = ["foodId", "startDate"])
data class FoodDoseSchedule(
    val foodId: Int,
    val startDate: Long,
    val amount: Int,
    val unit: Measurement,
    val frequency: Int
) : java.io.Serializable
