package com.mucheng.mucute.client.game.module.misc

import com.mucheng.mucute.client.game.InterceptablePacket
import com.mucheng.mucute.client.game.Module
import com.mucheng.mucute.client.game.ModuleCategory
import org.cloudburstmc.math.vector.Vector3i
import org.cloudburstmc.protocol.bedrock.data.PlayerActionType
import org.cloudburstmc.protocol.bedrock.packet.PlayerActionPacket

class InstaBreakModule : Module("instabreak", ModuleCategory.Misc) {

    override fun beforePacketBound(interceptablePacket: InterceptablePacket) {
        if (!isEnabled) return

        val packet = interceptablePacket.packet

        if (packet is PlayerActionPacket) {
            if (packet.action == PlayerActionType.START_BREAK) {
                var blockPosition = packet.blockPosition

                val breakPacket = PlayerActionPacket().apply {
                    runtimeEntityId = session.localPlayer.runtimeEntityId
                    action = PlayerActionType.STOP_BREAK
                    blockPosition = Vector3i.from(blockPosition.x, blockPosition.y, blockPosition.z)
                    face = packet.face
                }

                session.clientBound(breakPacket)

                interceptablePacket.intercept()
            }
        }
    }
}