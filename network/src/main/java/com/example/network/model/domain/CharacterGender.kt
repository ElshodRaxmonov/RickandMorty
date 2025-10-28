package com.example.network.model.domain


sealed class CharacterGender(val displayGender: String) {
    object Male : CharacterGender("Male")
    object Female : CharacterGender("Female")
    object Unknown : CharacterGender("Not specified")
    object Genderless : CharacterGender("No gender")
}