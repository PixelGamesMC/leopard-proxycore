package eu.pixelgamesmc.minecraft.proxycore.listener

import eu.pixelgamesmc.minecraft.proxycore.database.PixelDatabase
import eu.pixelgamesmc.minecraft.proxycore.database.collection.permission.group.PermissionGroupCollection
import eu.pixelgamesmc.minecraft.proxycore.database.collection.permission.user.PermissionUserCollection
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.PermissionCheckEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class PlayerPermissionListener: Listener {

    @EventHandler
    fun permissionCheck(event: PermissionCheckEvent) {
        val sender = event.sender

        if (sender is ProxiedPlayer) {
            fun hasPermission(name: String): Boolean {
                val userCollection = PixelDatabase.getCollection(PermissionUserCollection::class)
                val groupCollection = PixelDatabase.getCollection(PermissionGroupCollection::class)
                val permissionUser = userCollection.getUser(sender.uniqueId)
                if (permissionUser != null) {
                    val permissionGroups = permissionUser.permissionGroups.mapNotNull { groupCollection.getGroup(it) } + groupCollection.getDefaultGroups()
                    permissionGroups.forEach { permissionGroup ->
                        if (permissionGroup.hasPermission(name)) {
                            return true
                        }
                    }
                    return permissionUser.hasPermission(name)
                }
                return false
            }
            event.setHasPermission(hasPermission(event.permission))
        }
    }
}