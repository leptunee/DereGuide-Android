package com.dereguide.android.data.api

/**
 * 数据源配置
 * 基于 Starlight API (starlight.kirara.ca)
 * API 文档: https://starlight.kirara.ca/static/api.html
 */
object DataSourceConfig {
    
    // 主要数据源 - 已验证可用
    const val STARLIGHT_API_BASE = "https://starlight.kirara.ca/api/v1/"
    const val IMAGE_BASE_URL = "https://hidamarirhodonite.kirara.ca/"
    
    // 图片资源URL
    const val ICON_CARD_BASE_URL = "${IMAGE_BASE_URL}icon_card/"
    const val CARD_BASE_URL = "${IMAGE_BASE_URL}card/"
    const val ICON_CHAR_BASE_URL = "${IMAGE_BASE_URL}icon_char/"
    const val CHARA_BASE_URL = "${IMAGE_BASE_URL}chara2/"
    
    // 图片URL生成函数
    fun getCardIconUrl(cardId: Int): String = "${ICON_CARD_BASE_URL}${cardId}.png"
    fun getCardImageUrl(cardId: Int): String = "${CARD_BASE_URL}${cardId}.png"
    fun getCharacterIconUrl(characterId: Int): String = "${ICON_CHAR_BASE_URL}${characterId}.png"
    fun getCharacterImageUrl(characterId: Int, pose: Int = 1): String = "${CHARA_BASE_URL}${characterId}/${pose}.png"
}
