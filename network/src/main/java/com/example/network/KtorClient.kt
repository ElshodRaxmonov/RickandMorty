package com.example.network


import com.example.network.model.domain.Character
import com.example.network.model.domain.CharacterPage
import com.example.network.model.domain.Episode
import com.example.network.model.domain.EpisodePage
import com.example.network.model.remote.RemoteCharacter
import com.example.network.model.remote.RemoteCharacterPage
import com.example.network.model.remote.RemoteEpisode
import com.example.network.model.remote.RemoteEpisodePage
import com.example.network.model.remote.toDomainCharacter
import com.example.network.model.remote.toDomainCharacterPage
import com.example.network.model.remote.toDomainEpisode
import com.example.network.model.remote.toDomainEpisodePage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

class KtorClient {
    lateinit var json: Json
    private val client = HttpClient(OkHttp) {
        defaultRequest {
            url("https://rickandmortyapi.com/api/")
        }
        install(Logging) {
            logger = Logger.SIMPLE
        }

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        json = Json {
            ignoreUnknownKeys = true
        }
    }

    private var cacheCharacter = mutableMapOf<Int, Character>()
    suspend fun getCharacter(id: Int): ApiOperation<Character> {
        cacheCharacter[id]?.let { character ->
            return ApiOperation.Success(character)
        }
        return safeApiCall {
            client.get("character/$id").body<RemoteCharacter>().toDomainCharacter().also {
                cacheCharacter[id] = it
            }
        }
    }


    suspend fun getCharacterByPage(
        pageNumber: Int, queryParams: Map<String, String>
    ): ApiOperation<CharacterPage> {
        return safeApiCall {
            client.get("character") {
                url {
                    parameters.append("page", pageNumber.toString())
                    queryParams.forEach { parameters.append(it.key, it.value) }
                }
            }.body<RemoteCharacterPage>().toDomainCharacterPage()
        }
    }

    suspend fun getEpisodesByPage(pageIndex: Int): ApiOperation<EpisodePage> {
        return safeApiCall {
            client.get("episode") {
                url {
                    parameters.append("page", pageIndex.toString())
                }
            }.body<RemoteEpisodePage>().toDomainEpisodePage()
        }
    }

    suspend fun searchAllCharactersByName(searchQuery: String): ApiOperation<List<Character>> {
        val data = mutableListOf<Character>()
        var exception: Exception? = null

        getCharacterByPage(
            pageNumber = 1, queryParams = mapOf("name" to searchQuery)
        ).onSuccess { firstPage ->
            val totalPageCount = firstPage.info.pages
            data.addAll(firstPage.results)

            repeat(totalPageCount - 1) { index ->
                getCharacterByPage(
                    pageNumber = index + 2, queryParams = mapOf("name" to searchQuery)
                ).onSuccess { nextPage ->
                    data.addAll(nextPage.results)
                }.onFailure { error ->
                    exception = error
                }

                if (exception != null) {
                    return@onSuccess
                }
            }
        }.onFailure {
            exception = it
        }

        return exception?.let { ApiOperation.Failure(it) } ?: ApiOperation.Success(data)
    }

    suspend fun getAllEpisodes(): ApiOperation<List<Episode>> {
        val data = mutableListOf<Episode>()
        var exception: Exception? = null

        getEpisodesByPage(pageIndex = 1).onSuccess { firstPage ->
            val totalPageCount = firstPage.info.pages
            data.addAll(firstPage.episodes)

            repeat(totalPageCount - 1) { index ->

                getEpisodesByPage(pageIndex = index + 2).onSuccess { nextPage ->
                    data.addAll(nextPage.episodes)
                }.onFailure { error ->
                    exception = error
                }

                if (exception != null) {
                    return@onSuccess
                }
            }
        }.onFailure {
            exception = it
        }

        return exception?.let { ApiOperation.Failure(it) } ?: ApiOperation.Success(data)
    }

    suspend fun getEpisodes(episodeIds: List<Int>): ApiOperation<List<Episode>> {
        val idsCommaSeparated = episodeIds.joinToString(",")
        return safeApiCall {
            val responseText = client.get("episode/$idsCommaSeparated").bodyAsText()

            val json = Json { ignoreUnknownKeys = true }

            val episodes: List<RemoteEpisode> = try {
                json.decodeFromString<List<RemoteEpisode>>(responseText)
            } catch (e: SerializationException) {
                listOf(json.decodeFromString<RemoteEpisode>(responseText))
            }

            episodes.map { it.toDomainEpisode() }
        }
    }


    // Calling api safely with try catch
    private inline fun <T> safeApiCall(apiCall: () -> T): ApiOperation<T> {
        return try {
            ApiOperation.Success(apiCall())
        } catch (exception: Exception) {
            ApiOperation.Failure(exception = exception)
        }

    }
}

// Error handling for Ktor
sealed interface ApiOperation<T> {
    data class Success<T>(val data: T) : ApiOperation<T>
    data class Failure<T>(val exception: Exception) : ApiOperation<T>

    suspend fun onSuccess(block: suspend (T) -> Unit): ApiOperation<T> {
        if (this is Success) block(this.data)
        return this
    }

    fun onFailure(block: (Exception) -> Unit): ApiOperation<T> {
        if (this is Failure) block(this.exception)
        return this
    }
}

