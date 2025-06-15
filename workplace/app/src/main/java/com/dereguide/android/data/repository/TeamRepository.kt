package com.dereguide.android.data.repository

import com.dereguide.android.data.database.TeamDao
import com.dereguide.android.data.model.Team
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeamRepository @Inject constructor(
    private val teamDao: TeamDao
) {
    
    fun getAllTeams(): Flow<List<Team>> = teamDao.getAllTeams()
    
    suspend fun getTeamById(teamId: Long): Team? {
        return teamDao.getTeamById(teamId)
    }
    
    suspend fun searchTeams(query: String): List<Team> {
        return teamDao.searchTeams(query)
    }
    
    suspend fun insertTeam(team: Team): Long {
        return teamDao.insertTeam(team)
    }
    
    suspend fun updateTeam(team: Team) {
        teamDao.updateTeam(team)
    }
    
    suspend fun deleteTeam(team: Team) {
        teamDao.deleteTeam(team)
    }
    
    suspend fun createTeam(name: String, cardIds: List<Int>, guestCardId: Int? = null): Long {
        val team = Team(
            name = name,
            cardIds = cardIds,
            guestCardId = guestCardId
        )
        return insertTeam(team)
    }
}
