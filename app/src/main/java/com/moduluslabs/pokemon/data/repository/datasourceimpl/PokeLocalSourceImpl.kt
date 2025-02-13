package com.moduluslabs.pokemon.data.repository.datasourceimpl

import com.moduluslabs.pokemon.data.db.PokeDao
import com.moduluslabs.pokemon.data.repository.datasource.PokeLocalSource
import com.moduluslabs.pokemon.domain.model.PokeResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PokeLocalSourceImpl @Inject constructor(
    private val pokeDao: PokeDao
): PokeLocalSource {
    override suspend fun savePokemon(pokeResult: PokeResult)
        = pokeDao.savePokemon(pokeResult)

    override fun getSavedPokemon(): Flow<List<PokeResult>>
        = pokeDao.getSavedPokemon()
}