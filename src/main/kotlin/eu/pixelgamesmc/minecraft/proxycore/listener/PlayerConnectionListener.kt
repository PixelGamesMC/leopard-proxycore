package eu.pixelgamesmc.minecraft.proxycore.listener

import eu.pixelgamesmc.minecraft.proxycore.database.PixelDatabase
import eu.pixelgamesmc.minecraft.proxycore.database.collection.PlayerCollection
import net.md_5.bungee.api.event.LoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class PlayerConnectionListener: Listener {

    @EventHandler
    fun postLogin(event: LoginEvent) {
        PixelDatabase.getCollections(PlayerCollection::class).forEach { playerCollection ->
            playerCollection.playerLogin(event.connection.uniqueId, event.connection.name, "null")
        }
    }
}