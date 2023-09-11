package com.unicostudio.usa.war.amer.config

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.unicostudio.usa.war.amer.AviatorPurpleDatabase
import com.unicostudio.usa.war.amer.PlaneInfoStore
import com.unicostudio.usa.war.amer.R
import com.unicostudio.usa.war.amer.openAviatorPurpleFragment
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private const val TAG = "ConfigurationFragment"

class ConfigurationFragment : Fragment(R.layout.fragment_configuration) {

    private val viewModel by viewModels<ConfigurationViewModel> {
        object : ViewModelProvider.Factory {
            val database = AviatorPurpleDatabase.getInstance(requireContext())

            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ConfigurationViewModel(planeInfo = PlaneInfoStore(db = database)) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.setEvent(event = ConfigurationEvent.Configure(requireActivity()))
        observeState(state = viewModel.configurationState)
    }

    private fun observeState(state: StateFlow<ConfigurationState>) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                state.collect {
                    when (it) {
                        ConfigurationState.Loading -> {
                            Log.d(TAG, "Observing state: Loading...")
                        }

                        is ConfigurationState.Configured -> {
                            requireActivity().openAviatorPurpleFragment(name = it.name)
                        }
                    }
                }
            }
        }
    }
}