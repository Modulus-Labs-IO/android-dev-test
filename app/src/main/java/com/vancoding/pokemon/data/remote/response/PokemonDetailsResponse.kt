package com.vancoding.pokemon.data.remote.response

data class PokemonDetailsResponse(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val abilities: List<PokemonAbility>,
    val base_experience: Int,
    val forms: List<PokemonAbilityInfo>,
    val sprites: PokemonSprites,
    val stats: List<PokemonStat>,
    val types: List<PokemonType>,
)

data class PokemonAbility(
    val ability: PokemonAbilityInfo,
    val is_hidden: Boolean,
    val slot: Int,
)

data class PokemonAbilityInfo(
    val name: String,
    val url: String,
)

data class PokemonSprites(
    val front_default: String?,
    val back_default: String?,
    val front_shiny: String?,
    val back_shiny: String?,
)

data class PokemonStat(
    val base_stat: Int,
    val effort: Int,
    val stat: PokemonAbilityInfo,
)

data class PokemonType(
    val slot: Int,
    val type: PokemonAbilityInfo,
)