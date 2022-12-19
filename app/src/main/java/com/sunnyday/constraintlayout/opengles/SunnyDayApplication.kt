package com.sunnyday.constraintlayout.opengles

import android.app.Application
import timber.log.Timber

/**
 * Create by SunnyDay /12/19 20:56:39
 */
class SunnyDayApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}