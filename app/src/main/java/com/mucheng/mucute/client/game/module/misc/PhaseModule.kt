package com.mucheng.mucute.client.game.module.misc

import com.mucheng.mucute.client.game.InterceptablePacket
import com.mucheng.mucute.client.game.Module
import com.mucheng.mucute.client.game.ModuleCategory
import org.cloudburstmc.math.vector.Vector3f
import org.cloudburstmc.protocol.bedrock.data.PlayerAuthInputData
import org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket
import org.cloudburstmc.protocol.bedrock.packet.MovePlayerPacket

class PhaseModule : Module("phase", ModuleCategory.Misc) {

    private var phaseSpeed by floatValue("phase_speed", 0.5f, 0.1f..2.0f)

    override fun beforePacketBound(interceptablePacket: InterceptablePacket) {
        if (!isEnabled) return

        val packet = interceptablePacket.packet
        if (packet is PlayerAuthInputPacket) {
            if (packet.inputData.contains(PlayerAuthInputData.SNEAKING)) {
                val player = session.localPlayer
                val movePacket = MovePlayerPacket().apply {
                    runtimeEntityId = player.runtimeEntityId
                    position = player.vec3Position.add(0f, -0.01f, phaseSpeed)
                    rotation = player.vec3Rotation
                    mode = MovePlayerPacket.Mode.TELEPORT
                    isOnGround = false
                }
                session.clientBound(movePacket)
            }
        }
    }
}
