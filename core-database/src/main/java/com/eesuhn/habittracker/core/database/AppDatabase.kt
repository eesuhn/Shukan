package com.eesuhn.habittracker.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.eesuhn.habittracker.core.database.entity.Action
import com.eesuhn.habittracker.core.database.entity.Habit

@Database(
    entities = [
        Habit::class, Action::class
    ],
    version = 6,
    exportSchema = true,
)
@TypeConverters(EntityTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun habitDao(): HabitDao

}