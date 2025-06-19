package com.dereguide.android.utils

import com.dereguide.android.data.model.Card

/**
 * 稀有度转换工具类
 * 将数字稀有度转换为传统卡牌游戏分级
 */
object RarityUtils {
    
    enum class RarityTier(val displayName: String, val rarityRange: IntRange, val color: Long) {
        N("N", 1..2, 0xFF9E9E9E), // 灰色
        R("R", 3..3, 0xFF4CAF50), // 绿色
        SR("SR", 4..5, 0xFF2196F3), // 蓝色
        SSR("SSR", 6..Int.MAX_VALUE, 0xFFFF9800) // 橙色
    }
    
    /**
     * 根据数字稀有度获取稀有度等级
     */
    fun getRarityTier(rarity: Int): RarityTier {
        return RarityTier.values().find { rarity in it.rarityRange } ?: RarityTier.N
    }
    
    /**
     * 获取稀有度显示文本
     */
    fun getRarityDisplayText(rarity: Int): String {
        return getRarityTier(rarity).displayName
    }
    
    /**
     * 获取稀有度颜色
     */
    fun getRarityColor(rarity: Int): Long {
        return getRarityTier(rarity).color
    }
    
    /**
     * 获取所有稀有度等级选项
     */
    fun getAllRarityTiers(): List<RarityTier> {
        return RarityTier.values().toList()
    }
    
    /**
     * 根据稀有度等级筛选卡片
     */
    fun filterCardsByRarityTier(cards: List<Card>, rarityTier: RarityTier?): List<Card> {
        return if (rarityTier == null) {
            cards
        } else {
            cards.filter { card -> card.rarity in rarityTier.rarityRange }
        }
    }
    
    /**
     * 根据字符串获取稀有度等级
     */
    fun getRarityTierByName(name: String): RarityTier? {
        return RarityTier.values().find { it.displayName == name }
    }
}
