package com.example.rickandmorty.screens

import android.R
import android.util.Log
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.network.KtorClient
import com.example.network.model.domain.Character
import com.example.network.model.domain.Episode
import com.example.rickandmorty.components.CharacterMiniCard
import com.example.rickandmorty.components.DataPoint
import com.example.rickandmorty.components.DataPointComponent
import com.example.rickandmorty.components.EpisodeRowComponent
import com.example.rickandmorty.components.LoadingState
import com.example.rickandmorty.components.SimpleToolbar
import com.example.rickandmorty.ui.theme.RickPrimary
import com.example.rickandmorty.ui.theme.RickTextPrimary
import kotlinx.coroutines.launch

@Composable
fun CharacterEpisodesScreen(characterId: Int, ktorClient: KtorClient, onBackClicked: () -> Unit) {


    var characterState by remember { mutableStateOf<Character?>(null) }
    var episodesState by remember { mutableStateOf<List<Episode>>(emptyList()) }
    LaunchedEffect(key1 = Unit, block = {
        ktorClient.getCharacter(characterId).onSuccess {
            characterState = it
            launch {
                Log.d("TAG", "CharacterEpisodesScreen:${it.episodeIds}")
                Log.d("TAG", "CharacterEpisodesScreen: ${ktorClient.getEpisodes(it.episodeIds)}")
                ktorClient.getEpisodes(it.episodeIds).onSuccess { episodes ->
                    episodesState = episodes
                }.onFailure {
// todo handle error
                }
            }
        }.onFailure {
//todo handle error
        }
    })
    characterState?.let { character ->
        MainScreen(character = character, episodes = episodesState, onBackClicked)
    } ?: LoadingState()
}

@Composable
fun MainScreen(character: Character, episodes: List<Episode>, onBackClicked: () -> Unit) {

    val episodeMapEntry = episodes.groupBy { it.seasonNumber }
    Column {
        SimpleToolbar(title = "Character episodes", onBackAction = onBackClicked)
        LazyColumn(modifier = Modifier.padding(16.dp)) {

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                LazyRow {
                    episodeMapEntry.forEach {
                        val title = "Season ${it.key}"
                        val description = "${it.value.size} ep"
                        item {
                            DataPointComponent(DataPoint(title, description))
                            Spacer(modifier = Modifier.width(32.dp))
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                CharacterMiniCard(character.name, character.image)
                Spacer(modifier = Modifier.height(16.dp))
            }
            Log.d("TAG", "MainScreen: $episodes")


            episodeMapEntry.forEach { mapEntry ->
                stickyHeader { SeasonHeader(mapEntry.key) }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                items(mapEntry.value) { episode ->
                    EpisodeRowComponent(episode)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}


@Composable
fun SeasonHeader(seasonNumber: Int) {
    Log.d("TAG", "SeasonHeader: $seasonNumber")

    Text(
        text = "Season $seasonNumber",
        color = RickTextPrimary,
        fontSize = 32.sp,
        lineHeight = 32.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(RickPrimary)
            .border(
                width = 1.dp, color = RickTextPrimary,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    )
}