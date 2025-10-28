package com.example.network.model.remote


import com.example.network.model.domain.Character
import com.example.network.model.domain.CharacterGender
import com.example.network.model.domain.CharacterStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteCharacter(
    @SerialName("created")
    val created: String,
    @SerialName("episode")
    val episode: List<String>,
    @SerialName("gender")
    val gender: String,
    @SerialName("id")
    val id: Int,
    @SerialName("image")
    val image: String,
    @SerialName("location")
    val location: Location,
    @SerialName("name")
    val name: String,
    @SerialName("origin")
    val origin: Origin,
    @SerialName("species")
    val species: String,
    @SerialName("status")
    val status: String,
    @SerialName("type")
    val type: String
) {
    @Serializable
    data class Location(
        @SerialName("name")
        val name: String,
        @SerialName("url")
        val url: String
    )

    @Serializable
    data class Origin(
        @SerialName("name")
        val name: String,
        @SerialName("url")
        val url: String
    )
}

fun RemoteCharacter.toDomainCharacter(): Character {
    val characterGender = when (gender.lowercase()) {
        "male" -> CharacterGender.Male
        "female" -> CharacterGender.Female
        "genderless" -> CharacterGender.Genderless
        else -> CharacterGender.Unknown
    }
    val characterStatus = when (status.lowercase()) {
        "alive" -> CharacterStatus.Alive
        "dead" -> CharacterStatus.Dead
        else -> CharacterStatus.Unknown
    }

    return Character(
        created = created,
        episodeIds = episode.map { it.substring(it.lastIndexOf("/") + 1).toInt() },
        gender = characterGender,
        id = id,
        image = image,
        location = Character.Location(location.name, location.url),
        name = name,
        origin = Character.Origin(origin.name, origin.url),
        species = species,
        status = characterStatus,
        type = type
    )

}