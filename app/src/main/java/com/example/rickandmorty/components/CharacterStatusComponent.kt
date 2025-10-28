package com.example.rickandmorty.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.network.model.domain.Character
import com.example.network.model.domain.CharacterStatus


@Composable
fun CharacterStatusComponent(characterStatus: CharacterStatus, modifier: Modifier = Modifier) {


    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color = characterStatus.color, shape = CircleShape)
        )
        Text(text = characterStatus.displayStatus, color = Color.White)
    }
}

@Preview
@Composable
fun CharacterStatusComponentPreview() {
    Column {
        CharacterStatusComponent(characterStatus = CharacterStatus.Alive)
        CharacterStatusComponent(characterStatus = CharacterStatus.Dead)
        CharacterStatusComponent(characterStatus = CharacterStatus.Unknown)
    }
}
