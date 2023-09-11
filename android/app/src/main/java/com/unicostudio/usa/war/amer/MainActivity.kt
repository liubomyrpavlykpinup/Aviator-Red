package com.unicostudio.usa.war.amer

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.unicostudio.usa.war.amer.config.ConfigurationFragment
import com.unicostudio.usa.war.amer.game.GameFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            FragmentBuilder().build(this@MainActivity)
        }
    }

    inner class AviatorPurpleView(context: Context) : WebView(context) {
        init {
            onBackPressedDispatcher.addCallback(
                this@MainActivity,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (canGoBack()) {
                            goBack()
                        }
                    }
                })
        }
    }
}

class FragmentBuilder {

    fun build(activity: Activity) {
        val main = activity as? MainActivity ?: return
        val isAndroid = Navigation.getConfigurationValue(main)

        main.supportFragmentManager.commit {
            if (isAndroid.asBoolean()) {
                add<GameFragment>(R.id.fragment_container_view)
            } else {
                add<ConfigurationFragment>(R.id.fragment_container_view)
            }
        }
    }
}

fun String.asBoolean(): Boolean {
    return this == "1"
}