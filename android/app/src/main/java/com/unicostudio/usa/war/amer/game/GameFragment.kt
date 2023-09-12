package com.unicostudio.usa.war.amer.game

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.unicostudio.usa.war.amer.LoseFragment
import com.unicostudio.usa.war.amer.R
import com.unicostudio.usa.war.amer.WinFragment
import com.unicostudio.usa.war.amer.databinding.FragmentGameBinding
import com.unicostudio.usa.war.amer.databinding.LayoutGameRulesBinding
import kotlinx.coroutines.launch

private const val TAG = "GameFragment"

class GameFragment : Fragment() {

    private lateinit var viewBinding: FragmentGameBinding
    private val viewModel by viewModels<GameViewModel>()

    private val memoryGame: MemoryGame = MemoryGame()

    private lateinit var adapt: BoardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setOrientation(1)
        hideSystemUI()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentGameBinding.inflate(inflater, container, false)

        viewBinding.gameRulesIcon.setOnClickListener {
            showAlertDialog()
        }

        adapt = BoardAdapter()
        adapt.onFlipped = {
            updateGameWithFlip(it)
        }

        adapt.memoryCards = memoryGame.memoryCards

        viewBinding.gameBoard.apply {
            this.adapter = adapt
            layoutManager = GridLayoutManager(requireContext(), 4)
            setHasFixedSize(true)
            addItemDecoration(BoardDecoration(4, 20, false))
        }

        return viewBinding.root
    }

    private fun updateGameWithFlip(position: Int) {
        if (memoryGame.isCardFacedUp(position)) {
            return
        }
        if (memoryGame.flipCard(position)) {
            if (memoryGame.wonGame()) {
                requireActivity().supportFragmentManager.commit {
                    add<WinFragment>(R.id.fragment_container_view)
                    setReorderingAllowed(true)
                }
            }
        }
        adapt.notifyDataSetChanged()
    }

    private fun setOrientation(orientation: Int) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.secondsRemaining.collect {

                    val min = it / 60
                    val sec = it % 60

                    if (sec < 10) {
                        viewBinding.timerText.text = "$min:0$sec"
                    } else {
                        viewBinding.timerText.text = "$min:$sec"
                    }

                    if (it == 0L) {
                        requireActivity().supportFragmentManager.commit {
                            add<LoseFragment>(R.id.fragment_container_view)
                            setReorderingAllowed(true)
                        }
                    }
                }
            }
        }

    }

    private fun showAlertDialog() {
        val dialog = Dialog(requireContext())

        val inflater: LayoutInflater =
            context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.layout_game_rules, null)
        val binding = LayoutGameRulesBinding.bind(view)
        dialog.setContentView(binding.root)

        binding.closeButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setLayout(1000, 800)

        dialog.show()
    }

    override fun onStart() {
        super.onStart()

        viewModel.startTimer()
        memoryGame.restore()
        adapt.memoryCards = memoryGame.memoryCards
        adapt.notifyDataSetChanged()
    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.setDecorFitsSystemWindows(false)
            if (requireActivity().window.insetsController != null) {
                requireActivity().window.insetsController
                    ?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                requireActivity().window.insetsController?.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            requireActivity().window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

//    private val board =
//        Array(FIELD_SIZE) { Array(FIELD_SIZE) { PuzzleBlock(0, R.drawable.puzzle1) } }

    companion object {
        const val FIELD_SIZE = 3
    }

}




