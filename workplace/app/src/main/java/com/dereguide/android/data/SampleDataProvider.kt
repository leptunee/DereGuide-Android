package com.dereguide.android.data

import com.dereguide.android.data.model.Card

object SampleDataProvider {
    
    private val attributes = listOf("cute", "cool", "passion")
    private val skillTypes = listOf(
        "Perfect Lock", "Concentration", "Score Bonus", "Combo Bonus", 
        "Damage Guard", "Overload", "Encore", "Alternate", "Refrain"
    )
    
    private val idolNames = listOf(
        "天海春香", "星井美希", "如月千早", "萩原雪歩", "高槻やよい",
        "菊地真", "水瀬伊織", "四条貴音", "秋月律子", "三浦あずさ",
        "双海亜美", "双海真美", "我那覇響", "春日未来", "最上静香",
        "伊吹翼", "田中琴葉", "島原エレナ", "佐竹美奈子", "所恵美",
        "徳川まつり", "箱崎星梨花", "野々原茜", "望月杏奈", "ロコ",
        "七尾百合子", "高山紗代子", "松田亜利沙", "高坂海美", "横山奈緒",
        "二階堂千鶴", "馬場このみ", "大神環", "豊川風花", "宮尾美也",
        "福田のり子", "真壁瑞希", "篠宮可憐", "百瀬莉緒", "永吉昴",
        "北沢志保", "周防桃子", "ジュリア", "白石紬", "桜守歌織",
        "渋谷凛", "神谷奈緒", "高垣楓", "前川みく", "新田美波",
        "アナスタシア", "神崎蘭子", "多田李衣菜", "川島瑞樹", "緒方智絵里"
    )
    
    fun getSampleCards(): List<Card> {
        val cards = mutableListOf<Card>()
        
        // 生成基础卡片
        cards.addAll(generateBasicCards())
        
        // 生成扩展卡片到500+张
        cards.addAll(generateExtendedCards())
        
        return cards
    }    private fun generateBasicCards(): List<Card> = listOf(
        Card(
            id = 1,
            name = "天海春香",
            characterId = 1,
            rarity = 4,
            attribute = "cute",
            skill = "Perfect Lock",
            skillDescription = "7秒間 PERFECTタップ以外でもPERFECTになる",
            centerSkill = "Cute Focus",
            centerSkillDescription = "キュート属性のアイドルの全パラメータが12%アップ",
            maxLevel = 60,
            maxLevel2 = 80,
            vocal = 3420,
            dance = 3210,
            visual = 4560,
            vocal2 = 4120,
            dance2 = 3860,
            visual2 = 5480,
            imageUrl = "https://starlight.kirara.ca/api/v1/image_url/card/1",
            cardImageUrl = "https://starlight.kirara.ca/api/v1/image_url/card/1/card",
            spreImageUrl = null,
            iconImageUrl = "https://starlight.kirara.ca/api/v1/image_url/card/1/icon",
            releaseDate = "2023-01-01",
            evolutionId = null,
            hasSpread = false,
            hasSign = false,
            skillType = "Perfect Lock"
        ),
        Card(
            id = 2,
            name = "星井美希",
            characterId = 2,
            rarity = 5,
            attribute = "cute",
            skill = "Concentration",
            skillDescription = "7秒間、他のアイドルの特技効果を受けなくなり、スコアが25%アップ",
            centerSkill = "Cute Princess",
            centerSkillDescription = "キュート属性のアイドルの全パラメータが15%アップ",
            maxLevel = 70,
            maxLevel2 = 90,
            vocal = 4210,
            dance = 3890,
            visual = 4560,
            vocal2 = 5050,
            dance2 = 4670,
            visual2 = 5470,
            imageUrl = "https://starlight.kirara.ca/api/v1/image_url/card/2",
            cardImageUrl = "https://starlight.kirara.ca/api/v1/image_url/card/2/card",
            spreImageUrl = "https://starlight.kirara.ca/api/v1/image_url/card/2/spread",
            iconImageUrl = "https://starlight.kirara.ca/api/v1/image_url/card/2/icon",
            releaseDate = "2023-01-12",
            evolutionId = null,
            hasSpread = true,
            hasSign = true,
            skillType = "Concentration"
        ),
        Card(
            id = 3,
            name = "如月千早",
            characterId = 3,
            rarity = 4,
            attribute = "cool",
            skill = "Score Bonus",
            skillDescription = "7秒間 スコアが17%アップ",
            centerSkill = "Cool Focus",
            centerSkillDescription = "クール属性のアイドルの全パラメータが12%アップ",
            maxLevel = 60,
            maxLevel2 = 80,
            vocal = 4320,
            dance = 2890,
            visual = 3950,
            vocal2 = 5190,
            dance2 = 3470,
            visual2 = 4740,
            imageUrl = "https://starlight.kirara.ca/api/v1/image_url/card/3",
            cardImageUrl = "https://starlight.kirara.ca/api/v1/image_url/card/3/card",
            spreImageUrl = null,
            iconImageUrl = "https://starlight.kirara.ca/api/v1/image_url/card/3/icon",
            releaseDate = "2023-01-20",
            evolutionId = null,
            hasSpread = false,
            hasSign = false,
            skillType = "Score Bonus"
        ),        // 添加一个明确的5星卡片测试
        Card(
            id = 999,
            name = "【测试】渋谷凛",
            characterId = 999,
            rarity = 5,
            attribute = "cool",
            skill = "Limited Perfect Lock",
            skillDescription = "9秒間 PERFECTタップ以外でもPERFECTになり、さらにスコアが30%アップ",
            centerSkill = "Cool Master",
            centerSkillDescription = "クール属性のアイドルの全パラメータが18%アップし、特技発動確率が15%アップ",
            maxLevel = 80,
            maxLevel2 = 90,
            vocal = 5210,
            dance = 4890,
            visual = 5560,
            vocal2 = 6050,
            dance2 = 5670,
            visual2 = 6470,
            imageUrl = "https://starlight.kirara.ca/api/v1/image_url/card/999",
            cardImageUrl = "https://starlight.kirara.ca/api/v1/image_url/card/999/card",
            spreImageUrl = "https://starlight.kirara.ca/api/v1/image_url/card/999/spread",
            iconImageUrl = "https://starlight.kirara.ca/api/v1/image_url/card/999/icon",
            releaseDate = "2023-12-01",
            evolutionId = null,
            hasSpread = true,
            hasSign = true,
            skillType = "Perfect Lock"
        ),
        // 添加一些超高稀有度测试卡片
        Card(
            id = 998,
            name = "【限定SSR】新田美波",
            characterId = 998,
            rarity = 6,
            attribute = "passion",
            skill = "Ultimate Score Boost",
            skillDescription = "11秒間 スコアが35%アップし、コンボ数に応じてさらにスコアがアップ",
            centerSkill = "Passion Legend",
            centerSkillDescription = "パッション属性のアイドルの全パラメータが20%アップし、特技発動確率が20%アップ",
            maxLevel = 90,
            maxLevel2 = 100,
            vocal = 6210,
            dance = 5890,
            visual = 6560,
            vocal2 = 7050,
            dance2 = 6670,
            visual2 = 7470,
            imageUrl = "https://starlight.kirara.ca/api/v1/image_url/card/998",
            cardImageUrl = "https://starlight.kirara.ca/api/v1/image_url/card/998/card",
            spreImageUrl = "https://starlight.kirara.ca/api/v1/image_url/card/998/spread",
            iconImageUrl = "https://starlight.kirara.ca/api/v1/image_url/card/998/icon",
            releaseDate = "2024-01-01",
            evolutionId = null,
            hasSpread = true,
            hasSign = true,
            skillType = "Score Bonus"
        ),
        Card(
            id = 997,
            name = "【Fes限定】高垣楓",
            characterId = 997,
            rarity = 7,
            attribute = "cool",
            skill = "Legendary Perfect Lock",
            skillDescription = "13秒間 PERFECTタップ以外でもPERFECTになり、さらにライフも回復する",
            centerSkill = "Cool Goddess",
            centerSkillDescription = "全属性のアイドルの全パラメータが22%アップし、特技発動確率が25%アップ",
            maxLevel = 100,
            maxLevel2 = 110,
            vocal = 7210,
            dance = 6890,
            visual = 7560,
            vocal2 = 8050,
            dance2 = 7670,
            visual2 = 8470,
            imageUrl = "https://starlight.kirara.ca/api/v1/image_url/card/997",
            cardImageUrl = "https://starlight.kirara.ca/api/v1/image_url/card/997/card",
            spreImageUrl = "https://starlight.kirara.ca/api/v1/image_url/card/997/spread",
            iconImageUrl = "https://starlight.kirara.ca/api/v1/image_url/card/997/icon",
            releaseDate = "2024-06-01",
            evolutionId = null,
            hasSpread = true,
            hasSign = true,
            skillType = "Perfect Lock"
        )
    )

    private fun generateExtendedCards(): List<Card> {
        val cards = mutableListOf<Card>()
        var cardId = 4
        
        // 为每个偶像生成多个不同稀有度的卡片
        idolNames.forEachIndexed { nameIndex, idolName ->
            val characterId = nameIndex + 1
            
            // 为每个偶像生成2-4张不同稀有度的卡片
            val cardCount = (2..4).random()
            repeat(cardCount) { _ ->
                val rarity = listOf(2, 3, 4, 5).random()
                val attribute = attributes.random()
                val skillType = skillTypes.random()
                
                val baseStats = generateBaseStats(rarity)
                val awakenedStats = generateAwakenedStats(baseStats, rarity)
                
                cards.add(
                    Card(
                        id = cardId++,
                        name = idolName,
                        characterId = characterId,
                        rarity = rarity,
                        attribute = attribute,
                        skill = skillType,
                        skillDescription = generateSkillDescription(skillType),
                        centerSkill = generateCenterSkill(attribute, rarity),
                        centerSkillDescription = generateCenterSkillDescription(attribute, rarity),
                        maxLevel = getMaxLevel(rarity),
                        maxLevel2 = getMaxLevel2(rarity),
                        vocal = baseStats.first,
                        dance = baseStats.second,
                        visual = baseStats.third,
                        vocal2 = awakenedStats?.first,
                        dance2 = awakenedStats?.second,
                        visual2 = awakenedStats?.third,
                        imageUrl = "https://starlight.kirara.ca/api/v1/image_url/card/$cardId",
                        cardImageUrl = "https://starlight.kirara.ca/api/v1/image_url/card/$cardId/card",
                        spreImageUrl = if (rarity >= 4) "https://starlight.kirara.ca/api/v1/image_url/card/$cardId/spread" else null,
                        iconImageUrl = "https://starlight.kirara.ca/api/v1/image_url/card/$cardId/icon",
                        releaseDate = generateReleaseDate(cardId),
                        evolutionId = null,
                        hasSpread = rarity >= 4,
                        hasSign = rarity == 5 && (1..10).random() <= 3,
                        skillType = skillType
                    )
                )
            }
        }
        
        return cards
    }
    
    private fun generateBaseStats(rarity: Int): Triple<Int, Int, Int> {
        val baseRange = when (rarity) {
            2 -> 1500..2500
            3 -> 2000..3500
            4 -> 3000..4500
            5 -> 4000..5500
            else -> 1000..2000
        }
        
        return Triple(
            baseRange.random(),
            baseRange.random(),
            baseRange.random()
        )
    }
    
    private fun generateAwakenedStats(baseStats: Triple<Int, Int, Int>, rarity: Int): Triple<Int, Int, Int>? {
        if (rarity < 3) return null
        
        val multiplier = when (rarity) {
            3 -> 1.2
            4 -> 1.25
            5 -> 1.3
            else -> 1.15
        }
        
        return Triple(
            (baseStats.first * multiplier).toInt(),
            (baseStats.second * multiplier).toInt(),
            (baseStats.third * multiplier).toInt()
        )
    }
    
    private fun getMaxLevel(rarity: Int): Int = when (rarity) {
        2 -> 40
        3 -> 50
        4 -> 60
        5 -> 70
        else -> 30
    }
    
    private fun getMaxLevel2(rarity: Int): Int? = when (rarity) {
        3 -> 70
        4 -> 80
        5 -> 90
        else -> null
    }
    
    private fun generateSkillDescription(skillType: String): String = when (skillType) {
        "Perfect Lock" -> "7秒間 PERFECTタップ以外でもPERFECTになる"
        "Concentration" -> "7秒間、他のアイドルの特技効果を受けなくなり、スコアが25%アップ"
        "Score Bonus" -> "7秒間 スコアが17%アップ"
        "Combo Bonus" -> "7秒間 COMBOボーナスが18%アップ"
        "Damage Guard" -> "7秒間 ライフが減少しなくなる"
        "Overload" -> "13秒間 スコアが18%アップ、ライフが毎秒20減少"
        "Encore" -> "発動時に他の特技をもういちど発動させる"
        "Alternate" -> "7秒間 PERFECTのスコアが18%アップ、GREAT以下でライフが減少"
        "Refrain" -> "7秒間 異なる特技の効果を同時に発動（重複不可）"
        else -> "特技効果の説明"
    }
    
    private fun generateCenterSkill(attribute: String, rarity: Int): String {
        val power = when (rarity) {
            2, 3 -> "Focus"
            4 -> "Step"
            5 -> "Princess"
            else -> "Focus"
        }
        
        return when (attribute) {
            "cute" -> "Cute $power"
            "cool" -> "Cool $power"
            "passion" -> "Passion $power"
            else -> "All $power"
        }
    }
    
    private fun generateCenterSkillDescription(attribute: String, rarity: Int): String {
        val percentage = when (rarity) {
            2, 3 -> "12"
            4 -> "15"
            5 -> "18"
            else -> "10"
        }
        
        val targetAttribute = when (attribute) {
            "cute" -> "キュート"
            "cool" -> "クール"
            "passion" -> "パッション"
            else -> "全"
        }
        
        return "${targetAttribute}属性のアイドルの全パラメータが${percentage}%アップ"
    }
      private fun generateReleaseDate(cardId: Int): String {
        val baseYear = 2023
        val month = (cardId % 12) + 1
        val day = (cardId % 28) + 1
        return String.format("%d-%02d-%02d", baseYear, month, day)
    }
    
    // 为CardRepository添加参数化的生成方法
    fun generateSampleCards(count: Int): List<Card> {
        val allCards = getSampleCards()
        return if (count <= allCards.size) {
            allCards.take(count)
        } else {
            // 如果需要的数量比现有卡片多，重复一些卡片
            val repeated = mutableListOf<Card>()
            var currentId = allCards.size + 1
            
            repeat(count - allCards.size) { index ->
                val baseCard = allCards[index % allCards.size]
                repeated.add(
                    baseCard.copy(
                        id = currentId++,
                        name = "${baseCard.name} (Extra ${index + 1})"
                    )
                )
            }
            
            allCards + repeated
        }
    }
}