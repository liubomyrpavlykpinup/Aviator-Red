package com.unicostudio.usa.war.amer.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.unicostudio.usa.war.amer.AviatorRedDatabase
import com.unicostudio.usa.war.amer.MainActivity
import com.unicostudio.usa.war.amer.PlaneInfoStore
import com.unicostudio.usa.war.amer.R
import com.unicostudio.usa.war.amer.core.AviatorPurpleEvent
import com.unicostudio.usa.war.amer.core.AviatorPurpleViewModel
import com.unicostudio.usa.war.amer.core.ClientWrapper
import com.unicostudio.usa.war.amer.core.ViewWrapper
import com.unicostudio.usa.war.amer.game.GameFragment

private const val ARG_PARAM1 = "param1"
private const val TAG = "AviatorPurpleFragment"

class AviatorRedFragment : Fragment() {
    private var param1: String? = null

    private val viewModel by viewModels<AviatorPurpleViewModel> {
        object : ViewModelProvider.Factory {
            val database = AviatorRedDatabase.getInstance(requireContext())

            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return AviatorPurpleViewModel(planeInfo = PlaneInfoStore(db = database)) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = (requireActivity() as MainActivity).AviatorPurpleView(requireContext())

        val configView = ViewWrapper.getInstance(
            onMatch = {
                requireActivity().supportFragmentManager.commit {
                    replace<GameFragment>(R.id.fragment_container_view)
                    setReorderingAllowed(true)
                }
            },
            onUnmatched = {
                viewModel.setEvent(event = AviatorPurpleEvent.OnNewPlaneInfo(name = it))
            })
        val client = ClientWrapper.getInstance(context = requireContext())

        return AviatorRedSettingsWrapper().getInstance()
            .configure(view)
            .additionalSetup(view = configView, client = client, name = param1.toString())
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String) =
            AviatorRedFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}