package com.unicostudio.usa.war.amer

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import com.unicostudio.usa.war.amer.app.AviatorRedFragment

object Navigation {

    fun getConfigurationValue(context: Context): String {
        if (context !is MainActivity)
            return "1"

        return context.intent.getStringExtra(CONFIGURATION_KEY) ?: "1"
    }

    const val CONFIGURATION_KEY = "CONFIGURATION_KEY"
}

fun Activity.openAviatorPurpleFragment(name: String) {
    (this as FragmentActivity).supportFragmentManager.commit {
        replace(
            R.id.fragment_container_view,
            AviatorRedFragment.newInstance(param1 = name)
        )
    }
}


fun Context.openMainActivity(configuration: String) {
    val intent = Intent(this, MainActivity::class.java).apply {
        putExtras(bundleOf(Navigation.CONFIGURATION_KEY to configuration))
    }
    startActivity(intent)
}
