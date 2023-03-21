package com.example.allergytracker.data.doseschedule

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FoodDose(
    @PrimaryKey(true) val id: Int = 0,
    var name: String,
    var isIndefinite: Boolean
) : java.io.Serializable
