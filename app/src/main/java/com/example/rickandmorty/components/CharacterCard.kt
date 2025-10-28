package com.example.rickandmorty.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.network.model.domain.Character
import com.example.network.model.domain.CharacterStatus
import com.example.rickandmorty.R

@Composable
fun CharacterCard(character: Character) {

    Card(
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.4f)
            .padding(8.dp)
            .clip(RoundedCornerShape(20.dp)),

        ) {
        Box(Modifier.fillMaxSize()) {
            AsyncImage(

                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Yellow),
                model = character.image,
                contentDescription = "Image of Character",
                placeholder = painterResource(R.drawable.image)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp) // Adjust shadow height
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.8f) // Shadow opacity
                            )
                        )
                    )
            )

            // Text on top of shadow
            Text(
                text = character.name,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )
            CharacterStatusComponent(
                character.status,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            )
        }
    }


}
