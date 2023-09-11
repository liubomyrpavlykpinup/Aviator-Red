package com.unicostudio.usa.war.amer.starter

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.unicostudio.usa.war.amer.openMainActivity
import kotlinx.coroutines.launch

class StarterActivity : AppCompatActivity() {

    private val viewModel by viewModels<StarterViewModel>()

    private var configurationService: IntentConfigurationService? = null
    private var bound: Boolean = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as IntentConfigurationService.LocalBinder
            configurationService = binder.getService()

            viewModel.setEvent(StarterEvent.Started(context = configurationService))
            bound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            configurationService = null
            bound = false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Intent(this, IntentConfigurationService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
        viewModel.setEvent(StarterEvent.Started(context = configurationService))

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.state.collect {
                    when (it) {
                        StarterState.Empty -> {}
                        is StarterState.Configured -> {
                            openMainActivity(it.isAndroid)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }
}