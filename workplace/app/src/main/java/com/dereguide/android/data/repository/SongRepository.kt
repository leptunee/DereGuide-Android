package com.dereguide.android.data.repository

import com.dereguide.android.data.api.DereGuideApiService
import com.dereguide.android.data.database.SongDao
import com.dereguide.android.data.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SongRepository @Inject constructor(
    private val apiService: DereGuideApiService,
    private val songDao: SongDao
) {
    
    fun getAllSongs(): Flow<List<Song>> = songDao.getAllSongs()
    
    suspend fun getSongById(songId: Int): Song? {
        return songDao.getSongById(songId)
    }
    
    suspend fun getSongsByAttribute(attribute: String): List<Song> {
        return songDao.getSongsByAttribute(attribute)
    }
    
    suspend fun searchSongs(query: String): List<Song> {
        return songDao.searchSongs(query)
    }
    
    suspend fun refreshSongs(): Result<Unit> {
        return try {
            val response = apiService.getAllSongs()
            if (response.isSuccessful) {
                response.body()?.let { songs ->
                    songDao.insertSongs(songs)
                }
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to fetch songs: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun refreshSongsIfEmpty() {
        val currentSongs = getAllSongs().first()
        if (currentSongs.isEmpty()) {
            refreshSongs()
        }
    }
    
    suspend fun insertSong(song: Song) {
        songDao.insertSong(song)
    }
    
    suspend fun updateSong(song: Song) {
        songDao.updateSong(song)
    }
    
    suspend fun deleteSong(song: Song) {
        songDao.deleteSong(song)
    }
}
