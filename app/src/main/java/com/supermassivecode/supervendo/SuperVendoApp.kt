package com.supermassivecode.supervendo

import android.app.Application
import com.supermassivecode.supervendo.data.room.AppDatabase

class SuperVendoApp: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            deleteDatabase(AppDatabase.DB_NAME)
        }
    }
}