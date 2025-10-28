package com.example.rickandmorty.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.network.model.domain.CharacterGender
import com.example.network.model.domain.CharacterStatus
import com.example.rickandmorty.R
import com.example.rickandmorty.ui.theme.RickAction


@Composable
fun CharacterGridItem(
    modifier: Modifier,
    character: com.example.network.model.domain.Character,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(listOf(Color.Transparent, RickAction)),
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .fillMaxWidth()
                .weight(2f)
        ) {

            AsyncImage(
                contentScale = ContentScale.Crop,
                model = character.image,
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .fillMaxWidth()
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 14.dp))
                    .background(color = Color(0x99000000))
                    .align(Alignment.BottomEnd)

            ) {
                CharacterStatusComponent(
                    characterStatus = character.status,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(4.dp),
            Arrangement.Center,
            Alignment.CenterHorizontally,

            ) {
            Text(
                text = character.name,
                color = RickAction,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                lineHeight = 26.sp,
                maxLines = 1,
                textAlign = TextAlign.Center
            )

            GenderImage(gender = character.gender)

        }
    }
}

@Composable
fun GenderImage(gender: CharacterGender) {

    val image = when (gender) {
        CharacterGender.Female -> R.drawable.female
        CharacterGender.Male -> R.drawable.male
        CharacterGender.Genderless -> R.drawable.genderless
        CharacterGender.Unknown -> R.drawable.unknown
    }
    Image(
        painter = painterResource(id = image),
        contentDescription = null,
        modifier = Modifier.padding(4.dp)
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun CharacterGridItemPreview() {
    CharacterGridItem(
        modifier = Modifier.fillMaxWidth(),
        character = com.example.network.model.domain.Character(
            created = "timestamp",
            episodeIds = listOf(1, 2, 3, 4, 5),
            gender = CharacterGender.Male,
            id = 123,
            image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
            location = com.example.network.model.domain.Character.Location(
                name = "Earth",
                url = ""
            ),
            name = "Morty Smith",
            origin = com.example.network.model.domain.Character.Origin(
                name = "Earth",
                url = ""
            ),
            species = "Human",
            status = CharacterStatus.Alive,
            type = "Human"
        ),
        onClick = {}
    )
}