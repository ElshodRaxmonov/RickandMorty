package com.example.rickandmorty.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.rickandmorty.components.CharacterCard
import com.example.rickandmorty.components.DataPoint
import com.example.rickandmorty.components.DataPointComponent
import com.example.rickandmorty.components.LoadingState
import com.example.rickandmorty.components.SimpleToolbar
import com.example.rickandmorty.ui.theme.RickAction
import com.example.rickandmorty.viewmodel.CharacterDetailsScreenViewModel
import com.example.rickandmorty.viewmodel.CharacterDetailsScreenViewState

sealed interface CharacterDetailsViewState {
    object Loading : CharacterDetailsViewState
    data class Error(val message: String) : CharacterDetailsViewState
    data class Success(
        val character: Character,
        val characterDataPoints: List<DataPoint>
    ) : CharacterDetailsViewState
}
@Composable
fun CharacterDetailsScreen(
    characterId: Int,
    viewModel: CharacterDetailsScreenViewModel = hiltViewModel(),
    onEpisodeClicked: (Int) -> Unit,
    onBackClicked: () -> Unit
) {

    LaunchedEffect(key1 = Unit, block = {
        viewModel.fetchCharacter(characterId)
    })

    val state by viewModel.stateFlow.collectAsState()
    Column {
        SimpleToolbar(title = "Character details", onBackAction = onBackClicked)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(all = 16.dp)
        ) {
            when (val viewState = state) {
                is CharacterDetailsScreenViewState.Error -> {
                }

                is CharacterDetailsScreenViewState.Loading -> {
                    item {
                        LoadingState()
                    }
                }

                is CharacterDetailsScreenViewState.Success -> {

                    item {
                        CharacterCard(
                            viewState.character
                        )
                    }

                    item { Spacer(modifier = Modifier.height(8.dp)) }

                    items(viewState.dataPoints) { dataPoint ->
                        Spacer(modifier = Modifier.height(32.dp))
                        DataPointComponent(dataPoint = dataPoint)
                    }
                    // Button
                    item {
                        Text(
                            text = "View all episodes",
                            color = RickAction,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(horizontal = 32.dp)
                                .border(
                                    width = 1.dp,
                                    color = RickAction,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    onEpisodeClicked(characterId)
                                }
                                .padding(vertical = 8.dp)
                                .fillMaxWidth()
                        )
                    }
                    item { Spacer(modifier = Modifier.height(64.dp)) }
                }
            }

        }
    }
}



