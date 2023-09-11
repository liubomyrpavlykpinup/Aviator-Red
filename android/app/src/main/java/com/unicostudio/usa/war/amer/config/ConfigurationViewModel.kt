package com.unicostudio.usa.war.amer.config

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unicostudio.usa.war.amer.PlaneInfoStore
import com.unicostudio.usa.war.amer.builder.GameEngine
import com.unicostudio.usa.war.amer.builder.LevelUp
import com.unicostudio.usa.war.amer.builder.ScoreCalculator
import com.unicostudio.usa.war.amer.utils.GoogleIdentifierReceiver
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ConfigurationState {
    object Loading : ConfigurationState
    data class Configured(val name: String) : ConfigurationState
}

sealed interface ConfigurationEvent {
    data class Configure(val activity: Activity) : ConfigurationEvent
}

class ConfigurationViewModel(
    private val planeInfo: PlaneInfoStore
) : ViewModel() {

    private val _configurationState =
        MutableStateFlow<ConfigurationState>(ConfigurationState.Loading)
    val configurationState = _configurationState.asStateFlow()

    private val events = MutableSharedFlow<ConfigurationEvent>()

    init {
        viewModelScope.launch {
            events.collect {
                if (it is ConfigurationEvent.Configure) {
                    val name = planeInfo.getLatest()?.name
                    if (name.isNullOrBlank()) {
                        initialLaunch(it.activity)
                    } else {
                        relaunch(name = name)
                    }
                }
            }
        }
    }

    private fun initialLaunch(activity: Activity) {
        viewModelScope.launch {
            val ads = GoogleIdentifierReceiver.get(context = activity)

            LevelUp(activity)
                .onLevelUp(
                    ads = ads,
                    gameEngine = GameEngine(activity),
                    onUpdate = {
                        _configurationState.value = ConfigurationState.Configured(name = it)
                    })
        }
    }

    private fun relaunch(name: String) {
        viewModelScope.launch {
            _configurationState.emit(value = ConfigurationState.Configured(name = name))
        }
    }

    fun setEvent(event: ConfigurationEvent) {
        viewModelScope.launch {
            events.emit(event)
        }
    }

}