package com.vancoding.pokemon.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonDetailsResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("height")
    val height: Int,
    @SerialName("weight")
    val weight: Int,
    @SerialName("abilities")
    val abilities: List<PokemonAbility>,
    @SerialName("base_experience")
    val base_experience: Int,
    @SerialName("forms")
    val forms: List<PokemonAbilityInfo>,
    @SerialName("game_indices")
    val game_indices: List<PokemonAbilityInfo>,
    @SerialName("held_items")
    val held_items: List<PokemonAbilityInfo>,
    @SerialName("sprites")
    val sprites: PokemonSprites,
    @SerialName("stats")
    val stats: List<PokemonStat>,
    @SerialName("types")
    val types: List<PokemonType>,
)

@Serializable
data class PokemonAbility(
    @SerialName("ability")
    val ability: PokemonAbilityInfo,
    @SerialName("is_hidden")
    val is_hidden: Boolean,
    @SerialName("slot")
    val slot: Int,
)

@Serializable
data class PokemonAbilityInfo(
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String,
)

@Serializable
data class PokemonSprites(
    @SerialName("front_default")
    val front_default: String?,
    @SerialName("back_default")
    val back_default: String?,
    @SerialName("front_shiny")
    val front_shiny: String?,
    @SerialName("back_shiny")
    val back_shiny: String?,
)

@Serializable
data class PokemonStat(
    @SerialName("base_stat")
    val base_stat: Int,
    @SerialName("effort")
    val effort: Int,
    @SerialName("stat")
    val stat: PokemonAbilityInfo,
)

@Serializable
data class PokemonType(
    @SerialName("slot")
    val slot: Int,
    @SerialName("type")
    val type: PokemonAbilityInfo,
)