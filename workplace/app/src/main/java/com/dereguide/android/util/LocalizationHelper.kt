package com.dereguide.android.util

import android.content.Context
import com.dereguide.android.R

object LocalizationHelper {
    
    fun getAttributeDisplayName(context: Context, attribute: String): String {
        return when (attribute.lowercase()) {
            "cute" -> context.getString(R.string.cute)
            "cool" -> context.getString(R.string.cool)
            "passion" -> context.getString(R.string.passion)
            else -> attribute
        }
    }
    
    fun getRarityDisplayName(context: Context, rarity: Int): String {
        return when (rarity) {
            1 -> context.getString(R.string.normal_cards)
            2 -> context.getString(R.string.rare_cards)
            3 -> context.getString(R.string.super_rare_cards)
            else -> rarity.toString()
        }
    }
    
    fun getStatDisplayName(context: Context, statType: String): String {
        return when (statType.lowercase()) {
            "vocal" -> context.getString(R.string.card_vocal)
            "dance" -> context.getString(R.string.card_dance)
            "visual" -> context.getString(R.string.card_visual)
            else -> statType
        }
    }
    
    fun getDifficultyDisplayName(context: Context, difficulty: String): String {
        return when (difficulty.lowercase()) {
            "debut" -> context.getString(R.string.difficulty_debut)
            "regular" -> context.getString(R.string.difficulty_regular)
            "pro" -> context.getString(R.string.difficulty_pro)
            "master" -> context.getString(R.string.difficulty_master)
            "master+" -> context.getString(R.string.difficulty_master_plus)
            else -> difficulty
        }
    }
    
    fun getSortDisplayName(context: Context, sortType: String): String {
        return when (sortType.lowercase()) {
            "id" -> context.getString(R.string.sort_by_id)
            "name" -> context.getString(R.string.sort_by_name)
            "rarity" -> context.getString(R.string.sort_by_rarity)
            "attribute" -> context.getString(R.string.sort_by_attribute)
            "total_stats" -> context.getString(R.string.sort_by_total_stats)
            "vocal" -> context.getString(R.string.sort_by_vocal)
            "dance" -> context.getString(R.string.sort_by_dance)
            "visual" -> context.getString(R.string.sort_by_visual)
            else -> sortType
        }
    }
    
    fun getLanguageDisplayName(context: Context, languageCode: String): String {
        return when (languageCode) {
            "system" -> context.getString(R.string.follow_system)
            "en" -> context.getString(R.string.language_english)
            "zh" -> context.getString(R.string.language_chinese)
            "ja" -> context.getString(R.string.language_japanese)
            else -> languageCode
        }
    }
}
