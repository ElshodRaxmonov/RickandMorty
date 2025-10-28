package com.example.rickandmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.model.domain.CharacterPage
import com.example.rickandmorty.repository.CharacterRepository
import com.example.rickandmorty.screens.HomeScreenViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {
    private val _viewState = MutableStateFlow<HomeScreenViewState>(HomeScreenViewState.Loading)
    val viewState: StateFlow<HomeScreenViewState> = _viewState.asStateFlow()

    private val fetchedCharacterPages = mutableListOf<CharacterPage>()

    fun fetchInitialPage() = viewModelScope.launch {
        if (fetchedCharacterPages.isNotEmpty()) return@launch
        val initialPage = characterRepository.fetchCharacterPage(page = 1)
        initialPage.onSuccess { characterPage ->
            fetchedCharacterPages.clear()
            fetchedCharacterPages.add(characterPage)

            _viewState.update {
                return@update HomeScreenViewState.GridDisplay(characters = characterPage.results)
            }
        }.onFailure {
            // todo
        }
    }

    fun fetchNextPage() = viewModelScope.launch {
        val nextPageIndex = fetchedCharacterPages.size + 1
        characterRepository.fetchCharacterPage(page = nextPageIndex).onSuccess { characterPage ->
            fetchedCharacterPages.add(characterPage)
            _viewState.update { currentState ->
                val currentCharacters =
                    (currentState as? HomeScreenViewState.GridDisplay)?.characters ?: emptyList()
                return@update HomeScreenViewState.GridDisplay(characters = currentCharacters + characterPage.results)
            }
        }.onFailure {
            // todo
        }
    }
}