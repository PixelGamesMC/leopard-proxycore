package eu.pixelgamesmc.minecraft.proxycore.command

import eu.pixelgamesmc.minecraft.proxycore.database.PixelDatabase
import eu.pixelgamesmc.minecraft.proxycore.database.collection.cache.CacheCollection
import eu.pixelgamesmc.minecraft.proxycore.database.collection.permission.user.PermissionUserCollection
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.plugin.Command

class PermissionCommand: Command("permission",  "pixelgamesmc.command.permission") {

    override fun execute(sender: CommandSender, args: Array<out String>) {
        if (sender.hasPermission("pixelgamesmc.command.permission")) {
            if (args.size == 5) {
                if (args[0] == "user") {
                    if (args[2] == "group") {
                        val userName = args[1]
                        val operationType = args[3]
                        val groupName = args[4]

                        val cacheCollection = PixelDatabase.getCollection(CacheCollection::class)
                        val cacheUser = cacheCollection.getCacheByName(userName)

                        if (cacheUser != null) {
                            val permissionUserCollection = PixelDatabase.getCollection(PermissionUserCollection::class)
                            val permissionUser = permissionUserCollection.getUser(cacheUser.uuid)

                            if (permissionUser != null) {
                                if (operationType == "add") {
                                    permissionUser.permissionGroups.add(groupName)
                                } else if (operationType == "remove") {
                                    permissionUser.permissionGroups.remove(groupName)
                                }
                                permissionUserCollection.update(permissionUser)
                            }
                        }
                    }
                }
            }
        }
    }
}