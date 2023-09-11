package com.unicostudio.usa.war.amer.starter

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface StarterEvent {
    class Started(private val context: Context?) : StarterEvent {

        fun getConfigurationService(): IntentConfigurationService? {
            return context as? IntentConfigurationService
        }
    }

}

sealed interface StarterState {
    object Empty : StarterState
    data class Configured(val isAndroid: String) : StarterState
}

class StarterViewModel : ViewModel() {

    private val _state = MutableStateFlow<StarterState>(StarterState.Empty)
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<StarterEvent>()

    init {
        subscribeEvents()
    }

    private fun subscribeEvents() {
        viewModelScope.launch {
            _events.collect {
                if (it is StarterEvent.Started) {
                    val service = it.getConfigurationService()

                    _state.emit(StarterState.Configured(isAndroid = service?.getAndroid() ?: "1"))
                }
            }
        }
    }

    fun setEvent(event: StarterEvent) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }
}