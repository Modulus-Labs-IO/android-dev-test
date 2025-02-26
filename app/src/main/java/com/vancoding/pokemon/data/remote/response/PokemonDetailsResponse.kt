package com.vancoding.pokemon.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonDetailsResponse(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val abilities: List<PokemonAbility>,
    @SerialName("base_experience")
    val baseExperience: Int,
    val forms: List<PokemonAbilityInfo>,
    @SerialName("game_indices")
    val gameIndices: List<GameIndex>,
    @SerialName("held_items")
    val heldItems: List<PokemonAbilityInfo>,
    val sprites: PokemonSprites,
    val stats: List<PokemonStat>,
    val types: List<PokemonType>,
)

@Serializable
data class PokemonSprites(
    @SerialName("front_default")
    val frontDefault: String?,
    @SerialName("back_default")
    val backDefault: String?,
    @SerialName("front_shiny")
    val frontShiny: String?,
    @SerialName("back_shiny")
    val backShiny: String?,
)

@Serializable
data class PokemonAbility(
    val ability: PokemonAbilityInfo,
    @SerialName("is_hidden")
    val isHidden: Boolean,
    val slot: Int,
)

@Serializable
data class PokemonAbilityInfo(
    val name: String,
    val url: String,
)

@Serializable
data class PokemonStat(
    @SerialName("base_stat")
    val baseStat: Int,
    val effort: Int,
    val stat: PokemonAbilityInfo,
)

@Serializable
data class PokemonType(
    val slot: Int,
    val type: PokemonAbilityInfo,
)

@Serializable
data class GameIndex(
    @SerialName("game_index")
    val gameIndex: Int,
    val version: PokemonAbilityInfo,
)