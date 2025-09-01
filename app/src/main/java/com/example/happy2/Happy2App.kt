package com.example.happy2

import android.app.Application
import com.example.happy2.data.AppDatabase

class Happy2App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.init(this)
    }
}
