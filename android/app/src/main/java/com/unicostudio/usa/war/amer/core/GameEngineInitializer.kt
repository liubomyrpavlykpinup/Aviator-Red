package com.unicostudio.usa.war.amer.core

import android.app.Application
import com.onesignal.OneSignal

class GameEngineInitializer : EngineInitializer {
    override fun initialize(app: Application) {
        OneSignal.initWithContext(app)
        OneSignal.setAppId("8c5e1801-8e5d-4d8d-bd6c-a1bc68cc5770")
    }
}