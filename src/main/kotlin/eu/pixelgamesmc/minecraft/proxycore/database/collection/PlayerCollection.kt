package eu.pixelgamesmc.minecraft.proxycore.database.collection

import java.util.UUID

interface PlayerCollection {

    fun playerLogin(uuid: UUID, name: String, skin: String)
}