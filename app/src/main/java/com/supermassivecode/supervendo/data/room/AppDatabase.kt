package com.supermassivecode.supervendo.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        GpsPoint::class,
        DwellLocation::class,
        Day::class
    ],
    version = 1
)
@TypeConverters(RoomTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gpsDao(): GpsDao
    abstract fun dwellDao(): DwellDao
    abstract fun dayDao(): DayDao

    companion object {

        @Volatile private var INSTANCE: AppDatabase? = null
        const val DB_NAME = "super_vendo_database"

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                ).fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}

