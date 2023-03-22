package org.jukeboxmc.world.gamerule

import com.nukkitx.protocol.bedrock.data.GameRuleData
import com.nukkitx.protocol.bedrock.packet.GameRulesChangedPacket
import java.util.EnumMap

class GameRules {
    private val gameRules: MutableMap<GameRule, Any> = EnumMap(org.jukeboxmc.world.gamerule.GameRule::class.java)
    private var changedPacket: GameRulesChangedPacket? = null

    init {
        for (gameRule in GameRule.values()) {
            gameRules[gameRule] = gameRule.defaultValue
        }
    }

    fun getGameRules(): List<GameRuleData<*>> {
        val entrySet: Set<Map.Entry<GameRule, Any>> = gameRules.entries
        val networkList: MutableList<GameRuleData<*>> = ArrayList(entrySet.size)
        for ((key, value) in entrySet) {
            networkList.add(GameRuleData(key.identifier, value))
        }
        return networkList
    }

    operator fun set(gameRule: GameRule, o: Any) {
        gameRules[gameRule] = o
        changedPacket = null
    }

    operator fun <V> get(gameRule: GameRule): V {
        return gameRules.getOrDefault(gameRule, gameRule.defaultValue) as V
    }

    fun requiresUpdate(): Boolean {
        return changedPacket == null
    }

    fun updatePacket(): GameRulesChangedPacket {
        if (!requiresUpdate()) {
            return changedPacket!!
        }
        changedPacket = GameRulesChangedPacket()
        changedPacket!!.gameRules.addAll(getGameRules())
        return changedPacket!!
    }
}
