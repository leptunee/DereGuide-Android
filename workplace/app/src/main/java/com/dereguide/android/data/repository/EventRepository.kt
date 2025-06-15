package com.dereguide.android.data.repository

import com.dereguide.android.data.api.DereGuideApiService
import com.dereguide.android.data.database.EventDao
import com.dereguide.android.data.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(
    private val apiService: DereGuideApiService,
    private val eventDao: EventDao
) {
    
    fun getAllEvents(): Flow<List<Event>> = eventDao.getAllEvents()
    
    suspend fun getEventById(eventId: Int): Event? {
        return eventDao.getEventById(eventId)
    }
    
    suspend fun getEventsByType(type: String): List<Event> {
        return eventDao.getEventsByType(type)
    }
    
    suspend fun searchEvents(query: String): List<Event> {
        return eventDao.searchEvents(query)
    }
    
    suspend fun getCurrentEvents(): Result<List<Event>> {
        return try {
            val response = apiService.getCurrentEvents()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to fetch current events: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun refreshEvents(): Result<Unit> {
        return try {
            val response = apiService.getAllEvents()
            if (response.isSuccessful) {
                response.body()?.let { events ->
                    eventDao.insertEvents(events)
                }
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to fetch events: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun refreshEventsIfEmpty() {
        val currentEvents = getAllEvents().first()
        if (currentEvents.isEmpty()) {
            refreshEvents()
        }
    }
    
    suspend fun insertEvent(event: Event) {
        eventDao.insertEvent(event)
    }
    
    suspend fun updateEvent(event: Event) {
        eventDao.updateEvent(event)
    }
    
    suspend fun deleteEvent(event: Event) {
        eventDao.deleteEvent(event)
    }
}
