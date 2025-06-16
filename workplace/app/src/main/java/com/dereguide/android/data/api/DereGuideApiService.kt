package com.dereguide.android.data.api

import com.dereguide.android.data.model.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Starlight API 服务接口
 * 基于 https://starlight.kirara.ca/static/api.html
 */
interface DereGuideApiService {
    
    // API信息
    @GET("info")
    suspend fun getApiInfo(): Response<ApiInfo>
    
    // 卡片 API
    @GET("list/card_t")
    suspend fun getAllCards(): Response<ApiResponse<List<CardListItem>>>
    
    @GET("card_t/{id}")
    suspend fun getCard(@Path("id") cardId: Int): Response<ApiResponse<CardDetail>>
    
    // 角色 API
    @GET("list/char_t")
    suspend fun getAllCharacters(): Response<ApiResponse<List<CharacterListItem>>>
    
    @GET("char_t/{id}")
    suspend fun getCharacter(@Path("id") characterId: Int): Response<ApiResponse<CharacterDetail>>
    
    // 技能 API
    @GET("skill_t/{id}")
    suspend fun getSkill(@Path("id") skillId: Int): Response<ApiResponse<SkillDetail>>
    
    // 中心技能 API
    @GET("leader_skill_t/{id}")
    suspend fun getLeaderSkill(@Path("id") skillId: Int): Response<ApiResponse<LeaderSkillDetail>>
}
