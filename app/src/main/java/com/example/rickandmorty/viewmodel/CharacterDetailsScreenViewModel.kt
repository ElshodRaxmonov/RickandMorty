package com.example.rickandmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.model.domain.Character
import com.example.rickandmorty.components.DataPoint
import com.example.rickandmorty.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CharacterDetailsScreenViewModel @Inject constructor(private val repository: CharacterRepository) :
    ViewModel() {

    val internalStorageFlow =
        MutableStateFlow<CharacterDetailsScreenViewState>(CharacterDetailsScreenViewState.Loading)

    val stateFlow = internalStorageFlow.asStateFlow()

    fun fetchCharacter(characterId: Int) = viewModelScope.launch {

        repository.fetchCharacter(characterId).onSuccess { character ->
            val dataPoints = buildList {
                add(DataPoint("Last Known Location", character.location.name))
                add(DataPoint("Species", character.species))
                add(DataPoint("Gender", character.gender.displayGender))
                character.type.takeIf { it.isNotBlank() }?.let { type ->
                    add(DataPoint("Type", type))
                }
                add(DataPoint("Origin", character.origin.name))
                add(DataPoint("Episode count", character.episodeIds.size.toString()))

            }
            internalStorageFlow.value =
                CharacterDetailsScreenViewState.Success(character, dataPoints)
        }.onFailure {
            internalStorageFlow.value =
                CharacterDetailsScreenViewState.Error(it.message ?: "Unknown error occurred")

        }


    }


}

sealed class CharacterDetailsScreenViewState {
    object Loading : CharacterDetailsScreenViewState()
    data class Success(
        val character: Character,
        val dataPoints: List<DataPoint>
    ) : CharacterDetailsScreenViewState()

    data class Error(val message: String) : CharacterDetailsScreenViewState()
}