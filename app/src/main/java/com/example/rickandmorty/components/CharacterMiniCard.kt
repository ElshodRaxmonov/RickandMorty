package com.example.rickandmorty.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.rickandmorty.ui.theme.RickAction

@Composable
fun CharacterMiniCard(name: String, imageUrl: String) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(3f)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.SpaceAround
    ) {
        Card(
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1.5f)
        ) {
            AsyncImage(
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                model = imageUrl,
                contentDescription = "image of character"
            )
        }
        Text(text = name, color = RickAction, fontSize = 22.sp, fontWeight = FontWeight.Bold)
    }


}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun CharacterMiniCardPreview() {
    CharacterMiniCard("ELshod", "cdcscsd")
}