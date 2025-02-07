package com.example.cleanarchitectureproject.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [CryptoCoinEntity::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)  // Add this line
abstract class CryptoDatabase : RoomDatabase() {
    abstract val cryptoDao: CryptoDao
}
