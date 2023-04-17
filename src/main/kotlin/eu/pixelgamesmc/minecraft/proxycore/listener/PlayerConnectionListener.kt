package eu.pixelgamesmc.minecraft.proxycore.listener

import eu.pixelgamesmc.minecraft.proxycore.database.PixelDatabase
import eu.pixelgamesmc.minecraft.proxycore.database.collection.PlayerCollection
import net.md_5.bungee.api.event.PreLoginEvent
import net.md_5.bungee.api.plugin.Listener
import org.bukkit.event.EventHandler

class PlayerConnectionListener: Listener {

    @EventHandler
    fun postLogin(event: PreLoginEvent) {
        PixelDatabase.getCollections(PlayerCollection::class).forEach { playerCollection ->
            playerCollection.playerLogin(event.connection.uniqueId, event.connection.name, "null")
        }
    }
}