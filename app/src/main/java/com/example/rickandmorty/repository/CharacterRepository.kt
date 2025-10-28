package com.example.rickandmorty.repository

import com.example.network.ApiOperation
import com.example.network.KtorClient
import com.example.network.model.domain.Character
import com.example.network.model.domain.CharacterPage
import javax.inject.Inject

class CharacterRepository @Inject constructor(private val client: KtorClient) {

    suspend fun fetchCharacterPage(
        page: Int,
        params: Map<String, String> = emptyMap()
    ): ApiOperation<CharacterPage> {
        return client.getCharacterByPage(pageNumber = page, queryParams = params)
    }

    suspend fun fetchCharacter(characterId: Int) = client.getCharacter(characterId)

    suspend fun fetchAllCharactersByName(searchQuery: String): ApiOperation<List<Character>> {
        return client.searchAllCharactersByName(searchQuery)
    }
}