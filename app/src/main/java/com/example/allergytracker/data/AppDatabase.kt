package com.example.allergytracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.allergytracker.data.settings.FoodDose
import com.example.allergytracker.data.settings.FoodDoseDao
import com.example.allergytracker.data.settings.FoodDoseSchedule

const val DATABASE_NAME = "food-dose-db"

@Database(entities = [FoodDose::class, FoodDoseSchedule::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodDoseDao(): FoodDoseDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }
    }
}