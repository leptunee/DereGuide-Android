package com.dereguide.android.data.mapper

import com.dereguide.android.data.model.*

/**
 * API 数据转换器
 * 将 Starlight API 数据转换为本地数据模型
 */
object ApiDataMapper {
    
    fun mapCardListItemToCard(apiCard: CardListItem): Card {
        return Card(
            id = apiCard.id,
            name = apiCard.nameOnly,
            characterId = apiCard.charaId,
            rarity = apiCard.rarityDep.rarity,
            attribute = mapAttributeIdToString(apiCard.attribute),
            skill = null, // 需要额外API调用获取
            skillDescription = null,
            centerSkill = null,
            centerSkillDescription = null,
            maxLevel = apiCard.rarityDep.baseMaxLevel,
            maxLevel2 = apiCard.rarityDep.baseMaxLevel + apiCard.rarityDep.addMaxLevel,
            vocal = apiCard.vocalMin,
            dance = apiCard.danceMin,
            visual = apiCard.visualMin,            vocal2 = apiCard.vocalMax,
            dance2 = apiCard.danceMax,
            visual2 = apiCard.visualMax,
            imageUrl = buildCardImageUrl(apiCard.id), // 卡片图片
            cardImageUrl = buildCardImageUrl(apiCard.id),
            spreImageUrl = if (apiCard.hasSpread) buildCardImageUrl(apiCard.id) else null,
            iconImageUrl = buildIconImageUrl(apiCard.id), // 图标图片
            releaseDate = null,
            evolutionId = apiCard.evolutionId,
            hasSpread = apiCard.hasSpread,
            hasSign = false
        )
    }
    
    fun mapCardDetailToCard(apiCard: CardDetail): Card {
        return Card(
            id = apiCard.id,
            name = apiCard.name,
            characterId = apiCard.charaId,
            rarity = apiCard.rarity.rarity,
            attribute = apiCard.attribute,
            skill = apiCard.skill?.skillName,
            skillDescription = apiCard.skill?.explain,
            centerSkill = apiCard.leadSkill?.name,
            centerSkillDescription = apiCard.leadSkill?.explain,
            maxLevel = apiCard.rarity.baseMaxLevel,
            maxLevel2 = apiCard.rarity.baseMaxLevel + apiCard.rarity.addMaxLevel,
            vocal = apiCard.vocalMin,
            dance = apiCard.danceMin,
            visual = apiCard.visualMin,
            vocal2 = apiCard.vocalMax,
            dance2 = apiCard.danceMax,
            visual2 = apiCard.visualMax,
            imageUrl = apiCard.cardImageRef,
            cardImageUrl = apiCard.cardImageRef,
            spreImageUrl = apiCard.spreadImageRef,
            iconImageUrl = apiCard.iconImageRef,
            releaseDate = null,
            evolutionId = apiCard.evolutionId,
            hasSpread = apiCard.hasSpread,
            hasSign = apiCard.hasSign
        )
    }
    
    fun mapCharacterListItemToCharacter(apiChar: CharacterListItem): Character {
        return Character(
            id = apiChar.charaId,
            name = apiChar.kanjiSpaced,
            nameKana = apiChar.kanaSpaced,
            age = null, // 需要从详情API获取
            birthday = null,
            bloodType = null,
            height = null,
            weight = null,
            bust = null,
            waist = null,
            hip = null,
            attribute = "cute", // 默认值，需要从详情API获取
            hometown = null,
            hobby = null,
            constellation = null,
            cv = null,
            description = null,
            imageUrl = null,
            iconImageUrl = null
        )
    }
    
    fun mapCharacterDetailToCharacter(apiChar: CharacterDetail): Character {
        return Character(
            id = apiChar.charaId,
            name = apiChar.name,
            nameKana = apiChar.nameKana,
            age = apiChar.age,
            birthday = String.format("%02d-%02d", apiChar.birthMonth, apiChar.birthDay),
            bloodType = mapBloodTypeIdToString(apiChar.bloodType),
            height = apiChar.height,
            weight = apiChar.weight,
            bust = apiChar.bodySize1,
            waist = apiChar.bodySize2,
            hip = apiChar.bodySize3,
            attribute = apiChar.type,
            hometown = null, // 需要映射地区ID
            hobby = apiChar.favorite,
            constellation = null, // 需要映射星座ID
            cv = apiChar.voice,
            description = null,
            imageUrl = apiChar.iconImageRef,
            iconImageUrl = apiChar.iconImageRef
        )
    }
    
    private fun mapAttributeIdToString(attributeId: Int): String {
        return when (attributeId) {
            1 -> "cute"
            2 -> "cool"
            3 -> "passion"
            else -> "cute"
        }
    }
      private fun mapBloodTypeIdToString(bloodTypeId: Int): String {
        return when (bloodTypeId) {
            2001 -> "A"
            2002 -> "B"
            2003 -> "AB"
            2004 -> "O"
            else -> "O"
        }
    }
      /**
     * 构建 Starlight API 的卡片图片URL
     * 基于实际API响应的URL格式
     */
    private fun buildCardImageUrl(cardId: Int): String {
        return "https://hidamarirhodonite.kirara.ca/card/$cardId.png"
    }
    
    /**
     * 构建 Starlight API 的图标图片URL
     */
    private fun buildIconImageUrl(cardId: Int): String {
        return "https://hidamarirhodonite.kirara.ca/icon_card/$cardId.png"
    }
}
