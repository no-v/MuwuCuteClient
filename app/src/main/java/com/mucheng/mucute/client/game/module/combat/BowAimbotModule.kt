package com.mucheng.mucute.client.game.module.combat

import com.mucheng.mucute.client.game.InterceptablePacket
import com.mucheng.mucute.client.game.Module
import com.mucheng.mucute.client.game.ModuleCategory
import com.mucheng.mucute.client.game.entity.Entity
import com.mucheng.mucute.client.game.entity.Player
import org.cloudburstmc.math.vector.Vector3f
import org.cloudburstmc.protocol.bedrock.packet.MovePlayerPacket
import org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket
import kotlin.math.atan2
import kotlin.math.sqrt

class BowAimbotModule : Module("bow_aimbot", ModuleCategory.Combat) {

    private var range by floatValue("range", 50.0f, 10.0f..100.0f)
    private var playersOnly by boolValue("players_only", true)

    override fun beforePacketBound(interceptablePacket: InterceptablePacket) {
        if (!isEnabled) return

        val packet = interceptablePacket.packet
        if (packet is PlayerAuthInputPacket) {
            val closestTarget = findClosestTarget()
            if (closestTarget != null) {
                aimAtTarget(packet, closestTarget)
            }
        }
    }

    private fun findClosestTarget(): Entity? {
        return session.level.entityMap.values
            .filter { it != session.localPlayer && it.isTarget() && it.distance(session.localPlayer) <= range }
            .minByOrNull { it.distance(session.localPlayer) }
    }

    private fun aimAtTarget(packet: PlayerAuthInputPacket, target: Entity) {
        val playerPos = session.localPlayer.vec3Position
        val targetPos = target.vec3Position.add(0.0f, 1.5f, 0.0f) // Aim at head

        val dx = targetPos.x - playerPos.x
        val dy = targetPos.y - playerPos.y
        val dz = targetPos.z - playerPos.z

        val distance = sqrt(dx * dx + dz * dz)
        val yaw = Math.toDegrees(atan2(dz, dx).toDouble()).toFloat() - 90f
        val pitch = -Math.toDegrees(atan2(dy, distance).toDouble()).toFloat()

        packet.rotation = Vector3f.from(pitch, yaw, 0f)
    }

    private fun Entity.isTarget(): Boolean {
        return when (this) {
            is Player -> !playersOnly || !this.isBot()
            else -> !playersOnly
        }
    }

    private fun Player.isBot(): Boolean {
        return username.startsWith("Bot_", ignoreCase = true) || uuid.toString().startsWith("0000")
    }
}