package com.example.network.model.domain

import androidx.compose.ui.graphics.Color


sealed class CharacterStatus(val displayStatus: String, val color: Color) {

    object Alive : CharacterStatus("Alive", Color.Green)
    object Dead : CharacterStatus("Dead", Color.Red)
    object Unknown : CharacterStatus("Unknown", Color.Yellow)


}