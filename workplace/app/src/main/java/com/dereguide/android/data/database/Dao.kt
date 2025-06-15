package com.dereguide.android.data.database

import androidx.room.*
import com.dereguide.android.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    
    @Query("SELECT * FROM cards ORDER BY id DESC")
    fun getAllCards(): Flow<List<Card>>
    
    @Query("SELECT * FROM cards WHERE id = :cardId")
    suspend fun getCardById(cardId: Int): Card?
    
    @Query("SELECT * FROM cards WHERE characterId = :characterId")
    suspend fun getCardsByCharacter(characterId: Int): List<Card>
    
    @Query("SELECT * FROM cards WHERE attribute = :attribute")
    suspend fun getCardsByAttribute(attribute: String): List<Card>
    
    @Query("SELECT * FROM cards WHERE rarity = :rarity")
    suspend fun getCardsByRarity(rarity: Int): List<Card>
    
    @Query("SELECT * FROM cards WHERE name LIKE '%' || :query || '%'")
    suspend fun searchCards(query: String): List<Card>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCards(cards: List<Card>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: Card)
    
    @Update
    suspend fun updateCard(card: Card)
    
    @Delete
    suspend fun deleteCard(card: Card)
}

@Dao
interface CharacterDao {
    
    @Query("SELECT * FROM characters ORDER BY id")
    fun getAllCharacters(): Flow<List<Character>>
    
    @Query("SELECT * FROM characters WHERE id = :characterId")
    suspend fun getCharacterById(characterId: Int): Character?
    
    @Query("SELECT * FROM characters WHERE attribute = :attribute")
    suspend fun getCharactersByAttribute(attribute: String): List<Character>
    
    @Query("SELECT * FROM characters WHERE name LIKE '%' || :query || '%'")
    suspend fun searchCharacters(query: String): List<Character>
    
    @Query("SELECT * FROM characters WHERE birthday LIKE '%' || :month || '%'")
    suspend fun getCharactersByBirthdayMonth(month: String): List<Character>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<Character>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: Character)
    
    @Update
    suspend fun updateCharacter(character: Character)
    
    @Delete
    suspend fun deleteCharacter(character: Character)
}

@Dao
interface SongDao {
    
    @Query("SELECT * FROM songs ORDER BY id DESC")
    fun getAllSongs(): Flow<List<Song>>
    
    @Query("SELECT * FROM songs WHERE id = :songId")
    suspend fun getSongById(songId: Int): Song?
    
    @Query("SELECT * FROM songs WHERE attribute = :attribute")
    suspend fun getSongsByAttribute(attribute: String): List<Song>
    
    @Query("SELECT * FROM songs WHERE name LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%'")
    suspend fun searchSongs(query: String): List<Song>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongs(songs: List<Song>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: Song)
    
    @Update
    suspend fun updateSong(song: Song)
    
    @Delete
    suspend fun deleteSong(song: Song)
}

@Dao
interface EventDao {
    
    @Query("SELECT * FROM events ORDER BY startDate DESC")
    fun getAllEvents(): Flow<List<Event>>
    
    @Query("SELECT * FROM events WHERE id = :eventId")
    suspend fun getEventById(eventId: Int): Event?
    
    @Query("SELECT * FROM events WHERE type = :type")
    suspend fun getEventsByType(type: String): List<Event>
    
    @Query("SELECT * FROM events WHERE name LIKE '%' || :query || '%'")
    suspend fun searchEvents(query: String): List<Event>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<Event>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)
    
    @Update
    suspend fun updateEvent(event: Event)
    
    @Delete
    suspend fun deleteEvent(event: Event)
}

@Dao
interface TeamDao {
    
    @Query("SELECT * FROM teams ORDER BY createdAt DESC")
    fun getAllTeams(): Flow<List<Team>>
    
    @Query("SELECT * FROM teams WHERE id = :teamId")
    suspend fun getTeamById(teamId: Long): Team?
    
    @Query("SELECT * FROM teams WHERE name LIKE '%' || :query || '%'")
    suspend fun searchTeams(query: String): List<Team>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeam(team: Team): Long
    
    @Update
    suspend fun updateTeam(team: Team)
    
    @Delete
    suspend fun deleteTeam(team: Team)
}

@Dao
interface GachaPoolDao {
    
    @Query("SELECT * FROM gacha_pools ORDER BY startDate DESC")
    fun getAllGachaPools(): Flow<List<GachaPool>>
    
    @Query("SELECT * FROM gacha_pools WHERE id = :gachaId")
    suspend fun getGachaPoolById(gachaId: Int): GachaPool?
    
    @Query("SELECT * FROM gacha_pools WHERE type = :type")
    suspend fun getGachaPoolsByType(type: String): List<GachaPool>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGachaPools(gachaPools: List<GachaPool>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGachaPool(gachaPool: GachaPool)
    
    @Update
    suspend fun updateGachaPool(gachaPool: GachaPool)
    
    @Delete
    suspend fun deleteGachaPool(gachaPool: GachaPool)
}
