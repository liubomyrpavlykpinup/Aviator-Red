package com.unicostudio.usa.war.amer.starter

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.provider.Settings

class IntentConfigurationService : Service() {

    private val binder = LocalBinder()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    inner class LocalBinder : Binder() {
        fun getService(): IntentConfigurationService = this@IntentConfigurationService
    }

    fun getAndroid(): String {
        return Settings.Global.getString(contentResolver, Settings.Global.ADB_ENABLED)
    }
}