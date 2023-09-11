package com.unicostudio.usa.war.amer.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unicostudio.usa.war.amer.PlaneInfoStore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

sealed interface AviatorPurpleEvent {
    data class OnNewPlaneInfo(val name: String) : AviatorPurpleEvent
}

class AviatorPurpleViewModel(
    private val planeInfo: PlaneInfoStore
) : ViewModel() {

    private val events = MutableSharedFlow<AviatorPurpleEvent>()

    init {
        viewModelScope.launch {
            events.collect {
                if (it is AviatorPurpleEvent.OnNewPlaneInfo) {
                    saveLocally(it.name)
                }
            }
        }
    }

    fun setEvent(event: AviatorPurpleEvent) {
        viewModelScope.launch {
            events.emit(event)
        }
    }

    private fun saveLocally(name: String) {
        viewModelScope.launch {
            planeInfo.insert(name)
        }
    }
}