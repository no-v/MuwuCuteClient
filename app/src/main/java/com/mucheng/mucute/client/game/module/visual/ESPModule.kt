package com.mucheng.mucute.client.game.module.visual

import com.mucheng.mucute.client.game.InterceptablePacket
import com.mucheng.mucute.client.game.Module
import com.mucheng.mucute.client.game.ModuleCategory
import com.mucheng.mucute.client.game.entity.Player
import org.cloudburstmc.math.vector.Vector3f
import org.cloudburstmc.protocol.bedrock.packet.MoveEntityAbsolutePacket
import org.cloudburstmc.protocol.bedrock.packet.PlayerListPacket
import org.cloudburstmc.protocol.bedrock.packet.TextPacket

class ESPModule : Module("esp", ModuleCategory.Visual) {

    private val trackedPlayers = mutableMapOf<Long, String>()

    override fun beforePacketBound(interceptablePacket: InterceptablePacket) {
        if (!isEnabled) return

        val packet = interceptablePacket.packet

        // Track players from PlayerListPacket
        if (packet is PlayerListPacket) {
            packet.entries.forEach { entry ->
                trackedPlayers[entry.entityId] = entry.name
            }
        }

        // Highlight players when they move
        if (packet is MoveEntityAbsolutePacket) {
            val entityId = packet.runtimeEntityId
            val position = packet.position
            if (trackedPlayers.containsKey(entityId) || isPlayerEntity(entityId)) {
                val playerName = trackedPlayers[entityId] ?: "Unknown Player"
                sendESPMessage(playerName, position)
            }
        }
    }

    private fun isPlayerEntity(entityId: Long): Boolean {
        return session.level.entityMap[entityId] is Player
    }

    private fun sendESPMessage(playerName: String, position: Vector3f) {
        val textPacket = TextPacket().apply {
            type = TextPacket.Type.RAW
            isNeedsTranslation = false
            message = "§l§b[ESP]§r §e$playerName detected at §a(${position.x.toInt()}, ${position.y.toInt()}, ${position.z.toInt()})"
        }
        session.clientBound(textPacket)
    }
}