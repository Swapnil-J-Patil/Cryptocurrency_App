package com.example.cleanarchitectureproject.data.local.saved_coins

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cleanarchitectureproject.data.local.Converters

@Database(entities = [CryptoCoinEntity::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)  // Add this line
abstract class CryptoDatabase : RoomDatabase() {
    abstract val cryptoDao: CryptoDao
}
