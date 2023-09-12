package com.unicostudio.usa.war.amer

import android.app.Application
import com.unicostudio.usa.war.amer.core.EngineInitializer
import com.unicostudio.usa.war.amer.core.GameEngineInitializer


class AviatorRedApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val initializer: EngineInitializer = GameEngineInitializer()
        initializer.initialize(this)
    }
}