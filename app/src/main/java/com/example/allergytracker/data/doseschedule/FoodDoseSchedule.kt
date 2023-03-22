package com.example.allergytracker.data.doseschedule

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.allergytracker.data.Measurement

@Entity()
data class FoodDoseSchedule(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var foodId: Long,
    var startDay: Int,
    var startMonth: Int,
    var startYear: Int,
    var amount: String,
    var unit: Measurement,
    var frequency: String
) : java.io.Serializable
