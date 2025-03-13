package com.example.cleanarchitectureproject.data.local.portfolio

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cleanarchitectureproject.data.local.Converters

@Database(entities = [PortfolioEntity::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)  // Add this line
abstract class PortfolioDatabase : RoomDatabase() {
    abstract val portfolioDao: PortfolioDao
}