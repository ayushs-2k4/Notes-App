package com.ayushsinghal.notes

import android.app.Application
import com.google.android.material.color.DynamicColors
import com.google.firebase.database.FirebaseDatabase

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}