package com.dereguide.android.data.repository

import com.dereguide.android.data.api.DereGuideApiService
import com.dereguide.android.data.database.CharacterDao
import com.dereguide.android.data.model.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepository @Inject constructor(
    private val apiService: DereGuideApiService,
    private val characterDao: CharacterDao
) {
    
    fun getAllCharacters(): Flow<List<Character>> = characterDao.getAllCharacters()
    
    suspend fun getCharacterById(characterId: Int): Character? {
        return characterDao.getCharacterById(characterId)
    }
    
    suspend fun getCharactersByAttribute(attribute: String): List<Character> {
        return characterDao.getCharactersByAttribute(attribute)
    }
    
    suspend fun searchCharacters(query: String): List<Character> {
        return characterDao.searchCharacters(query)
    }
    
    suspend fun getCharactersByBirthdayMonth(month: String): List<Character> {
        return characterDao.getCharactersByBirthdayMonth(month)
    }
    
    suspend fun refreshCharacters(): Result<Unit> {
        return try {
            val response = apiService.getAllCharacters()
            if (response.isSuccessful) {
                response.body()?.let { characters ->
                    characterDao.insertCharacters(characters)
                }
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to fetch characters: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun refreshCharactersIfEmpty() {
        val currentCharacters = getAllCharacters().first()
        if (currentCharacters.isEmpty()) {
            refreshCharacters()
        }
    }
    
    suspend fun insertCharacter(character: Character) {
        characterDao.insertCharacter(character)
    }
    
    suspend fun updateCharacter(character: Character) {
        characterDao.updateCharacter(character)
    }
    
    suspend fun deleteCharacter(character: Character) {
        characterDao.deleteCharacter(character)
    }
}
