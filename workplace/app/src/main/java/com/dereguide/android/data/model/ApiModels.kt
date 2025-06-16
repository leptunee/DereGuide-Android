package com.dereguide.android.data.model

import com.google.gson.annotations.SerializedName

/**
 * Starlight API 响应数据模型
 */

// API 通用响应格式
data class ApiResponse<T>(
    @SerializedName("result")
    val result: T
)

// API 信息
data class ApiInfo(
    @SerializedName("api_major")
    val apiMajor: Int,
    @SerializedName("api_revision")
    val apiRevision: Int,
    @SerializedName("truth_version")
    val truthVersion: String
)

// 卡片列表项
data class CardListItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("chara_id")
    val charaId: Int,
    @SerializedName("attribute")
    val attribute: Int, // 1=cute, 2=cool, 3=passion
    @SerializedName("name_only")
    val nameOnly: String,
    @SerializedName("rarity_dep")
    val rarityDep: RarityDep,
    @SerializedName("vocal_min")
    val vocalMin: Int,
    @SerializedName("vocal_max")
    val vocalMax: Int,
    @SerializedName("dance_min")
    val danceMin: Int,
    @SerializedName("dance_max")
    val danceMax: Int,
    @SerializedName("visual_min")
    val visualMin: Int,
    @SerializedName("visual_max")
    val visualMax: Int,
    @SerializedName("evolution_id")
    val evolutionId: Int?,
    @SerializedName("has_spread")
    val hasSpread: Boolean,
    @SerializedName("conventional")
    val conventional: String,
    @SerializedName("ref")
    val ref: String
)

// 卡片详情
data class CardDetail(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("chara_id")
    val charaId: Int,
    @SerializedName("attribute")
    val attribute: String,
    @SerializedName("rarity")
    val rarity: RarityDetail,
    @SerializedName("vocal_min")
    val vocalMin: Int,
    @SerializedName("vocal_max")
    val vocalMax: Int,
    @SerializedName("dance_min")
    val danceMin: Int,
    @SerializedName("dance_max")
    val danceMax: Int,
    @SerializedName("visual_min")
    val visualMin: Int,
    @SerializedName("visual_max")
    val visualMax: Int,
    @SerializedName("skill")
    val skill: SkillDetail?,
    @SerializedName("lead_skill")
    val leadSkill: LeaderSkillDetail?,
    @SerializedName("chara")
    val chara: CharacterDetail,
    @SerializedName("has_spread")
    val hasSpread: Boolean,
    @SerializedName("has_sign")
    val hasSign: Boolean,
    @SerializedName("evolution_id")
    val evolutionId: Int?,
    @SerializedName("card_image_ref")
    val cardImageRef: String?,
    @SerializedName("icon_image_ref")
    val iconImageRef: String?,
    @SerializedName("spread_image_ref")
    val spreadImageRef: String?,
    @SerializedName("sign_image_ref")
    val signImageRef: String?
)

// 角色列表项
data class CharacterListItem(
    @SerializedName("chara_id")
    val charaId: Int,
    @SerializedName("conventional")
    val conventional: String,
    @SerializedName("kanji_spaced")
    val kanjiSpaced: String,
    @SerializedName("kana_spaced")
    val kanaSpaced: String,
    @SerializedName("cards")
    val cards: List<Int>,
    @SerializedName("ref")
    val ref: String
)

// 角色详情
data class CharacterDetail(
    @SerializedName("chara_id")
    val charaId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("name_kana")
    val nameKana: String,
    @SerializedName("age")
    val age: Int,
    @SerializedName("height")
    val height: Int,
    @SerializedName("weight")
    val weight: Int,
    @SerializedName("body_size_1")
    val bodySize1: Int, // bust
    @SerializedName("body_size_2")
    val bodySize2: Int, // waist
    @SerializedName("body_size_3")
    val bodySize3: Int, // hip
    @SerializedName("birth_month")
    val birthMonth: Int,
    @SerializedName("birth_day")
    val birthDay: Int,
    @SerializedName("blood_type")
    val bloodType: Int,
    @SerializedName("favorite")
    val favorite: String,
    @SerializedName("voice")
    val voice: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("conventional")
    val conventional: String,
    @SerializedName("kanji_spaced")
    val kanjiSpaced: String,
    @SerializedName("kana_spaced")
    val kanaSpaced: String,
    @SerializedName("icon_image_ref")
    val iconImageRef: String?
)

// 稀有度依赖
data class RarityDep(
    @SerializedName("rarity")
    val rarity: Int,
    @SerializedName("base_max_level")
    val baseMaxLevel: Int,
    @SerializedName("add_max_level")
    val addMaxLevel: Int,
    @SerializedName("max_love")
    val maxLove: Int
)

// 稀有度详情
data class RarityDetail(
    @SerializedName("rarity")
    val rarity: Int,
    @SerializedName("base_max_level")
    val baseMaxLevel: Int,
    @SerializedName("add_max_level")
    val addMaxLevel: Int,
    @SerializedName("max_love")
    val maxLove: Int
)

// 技能详情
data class SkillDetail(
    @SerializedName("id")
    val id: Int,
    @SerializedName("skill_name")
    val skillName: String,
    @SerializedName("explain")
    val explain: String,
    @SerializedName("skill_type")
    val skillType: String,
    @SerializedName("condition")
    val condition: Int,
    @SerializedName("value")
    val value: Int,
    @SerializedName("max_chance")
    val maxChance: Int,
    @SerializedName("max_duration")
    val maxDuration: Int,
    @SerializedName("explain_en")
    val explainEn: String?
)

// 中心技能详情
data class LeaderSkillDetail(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("explain")
    val explain: String,
    @SerializedName("target_attribute")
    val targetAttribute: String,
    @SerializedName("target_param")
    val targetParam: String,
    @SerializedName("up_value")
    val upValue: Int,
    @SerializedName("explain_en")
    val explainEn: String?
)
