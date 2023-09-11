package com.unicostudio.usa.war.amer.game

import androidx.annotation.DrawableRes

data class PuzzleBlock(
    val id: Int,
    @DrawableRes val image: Int,
    var isVisible: Boolean = true
)
