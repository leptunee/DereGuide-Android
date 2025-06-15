package com.dereguide.android.data.api

import com.dereguide.android.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface DereGuideApiService {
    
    // Cards API
    @GET("cards")
    suspend fun getAllCards(): Response<List<Card>>
    
    @GET("cards/{id}")
    suspend fun getCard(@Path("id") cardId: Int): Response<Card>
    
    @GET("cards/character/{characterId}")
    suspend fun getCardsByCharacter(@Path("characterId") characterId: Int): Response<List<Card>>
    
    // Characters API
    @GET("characters")
    suspend fun getAllCharacters(): Response<List<Character>>
    
    @GET("characters/{id}")
    suspend fun getCharacter(@Path("id") characterId: Int): Response<Character>
    
    // Songs API
    @GET("songs")
    suspend fun getAllSongs(): Response<List<Song>>
    
    @GET("songs/{id}")
    suspend fun getSong(@Path("id") songId: Int): Response<Song>
    
    // Events API
    @GET("events")
    suspend fun getAllEvents(): Response<List<Event>>
    
    @GET("events/{id}")
    suspend fun getEvent(@Path("id") eventId: Int): Response<Event>
    
    @GET("events/current")
    suspend fun getCurrentEvents(): Response<List<Event>>
    
    // Gacha API
    @GET("gacha")
    suspend fun getAllGachaPools(): Response<List<GachaPool>>
    
    @GET("gacha/{id}")
    suspend fun getGachaPool(@Path("id") gachaId: Int): Response<GachaPool>
    
    @GET("gacha/current")
    suspend fun getCurrentGachaPools(): Response<List<GachaPool>>
    
    // Search API
    @GET("search/cards")
    suspend fun searchCards(@Query("q") query: String): Response<List<Card>>
    
    @GET("search/characters")
    suspend fun searchCharacters(@Query("q") query: String): Response<List<Character>>
    
    @GET("search/songs")
    suspend fun searchSongs(@Query("q") query: String): Response<List<Song>>
}
