package com.example.rickandmorty.repository

import com.example.network.ApiOperation
import com.example.network.KtorClient
import com.example.network.model.domain.Episode
import jakarta.inject.Inject

class EpisodesRepository @Inject constructor(private val ktorClient: KtorClient) {

    suspend fun fetchAllEpisodes(): ApiOperation<List<Episode>> = ktorClient.getAllEpisodes()
}