package com.dereguide.android.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "cards")
data class Card(
    @PrimaryKey
    val id: Int,
    val name: String,
    val characterId: Int,
    val rarity: Int,
    val attribute: String, // cute, cool, passion
    val skill: String?,
    val skillDescription: String?,
    val centerSkill: String?,
    val centerSkillDescription: String?,
    val maxLevel: Int,
    val maxLevel2: Int?,
    val vocal: Int,
    val dance: Int,
    val visual: Int,
    val vocal2: Int?,
    val dance2: Int?,
    val visual2: Int?,
    val imageUrl: String?,
    val cardImageUrl: String?,
    val spreImageUrl: String?,
    val iconImageUrl: String?,
    val releaseDate: String?,
    val evolutionId: Int?,
    val hasSpread: Boolean = false,
    val hasSign: Boolean = false,
    val isFavorite: Boolean = false, // 新增收藏字段
    val skillType: String? = null, // 新增技能类型字段
    val totalStats: Int = vocal + dance + visual, // 新增总属性值字段
    val totalStats2: Int? = null // 新增觉醒后总属性值字段
) : Parcelable {
    // 计算觉醒后总属性值
    val maxTotalStats: Int
        get() = (vocal2 ?: vocal) + (dance2 ?: dance) + (visual2 ?: visual)
}

@Parcelize
@Entity(tableName = "characters")
data class Character(
    @PrimaryKey
    val id: Int,
    val name: String,
    val nameKana: String?,
    val age: Int?,
    val birthday: String?,
    val bloodType: String?,
    val height: Int?,
    val weight: Int?,
    val bust: Int?,
    val waist: Int?,
    val hip: Int?,
    val attribute: String,
    val hometown: String?,
    val hobby: String?,
    val constellation: String?,
    val cv: String?,
    val description: String?,
    val imageUrl: String?,
    val iconImageUrl: String?
) : Parcelable

@Parcelize
@Entity(tableName = "songs")
data class Song(
    @PrimaryKey
    val id: Int,
    val name: String,
    val artist: String?,
    val composer: String?,
    val lyricist: String?,
    val bpm: Int?,
    val duration: Int?, // in seconds
    val attribute: String?,
    val difficulty: Map<String, Int>?, // debut, regular, pro, master, master+
    val levelValue: Map<String, Int>?,
    val noteCounts: Map<String, Int>?,
    val jacketUrl: String?,
    val previewUrl: String?,
    val releaseDate: String?,
    val eventId: Int?,
    val type: String? // original, cover, remix, etc.
) : Parcelable

@Parcelize
@Entity(tableName = "events")
data class Event(
    @PrimaryKey
    val id: Int,
    val name: String,
    val type: String, // token, points, caravan, etc.
    val startDate: String,
    val endDate: String,
    val bannerUrl: String?,
    val logoUrl: String?,
    val description: String?,
    val songId: Int?,
    val rewardCards: List<Int>?,
    val pointRewards: List<EventReward>?,
    val rankingRewards: List<EventReward>?
) : Parcelable

@Parcelize
data class EventReward(
    val points: Int?,
    val rank: Int?,
    val rewardType: String,
    val rewardId: Int?,
    val quantity: Int
) : Parcelable

@Parcelize
@Entity(tableName = "teams")
data class Team(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val cardIds: List<Int>,
    val guestCardId: Int?,
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable

@Parcelize
@Entity(tableName = "gacha_pools")
data class GachaPool(
    @PrimaryKey
    val id: Int,
    val name: String,
    val type: String, // limited, permanent, special
    val startDate: String?,
    val endDate: String?,
    val bannerUrl: String?,
    val featuredCards: List<Int>?,
    val rates: Map<String, Double>?, // SSR, SR, R rates
    val guarantees: List<GachaGuarantee>?
) : Parcelable

@Parcelize
data class GachaGuarantee(
    val pulls: Int,
    val guaranteedRarity: String
) : Parcelable

// 卡片统计数据类
@Parcelize
data class CardStatistics(
    val totalCount: Int,
    val favoriteCount: Int,
    val attributeCounts: Map<String, Int>,
    val rarityCounts: Map<Int, Int>
) : Parcelable

// 用于数据库查询的轻量级卡片列表项（避免与ApiModels中的CardListItem冲突）
@Parcelize
data class CardListViewItem(
    val id: Int,
    val name: String,
    val rarity: Int,
    val attribute: String,
    val imageUrl: String?,
    val iconImageUrl: String?,
    val isFavorite: Boolean = false
) : Parcelable
