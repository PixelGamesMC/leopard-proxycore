package eu.pixelgamesmc.minecraft.proxycore

import eu.pixelgamesmc.minecraft.proxycore.command.PermissionCommand
import eu.pixelgamesmc.minecraft.proxycore.listener.PlayerConnectionListener
import eu.pixelgamesmc.minecraft.proxycore.listener.PlayerPermissionListener
import eu.pixelgamesmc.minecraft.proxycore.utility.PluginUtil
import eu.pixelgamesmc.minecraft.proxycore.database.Credentials
import eu.pixelgamesmc.minecraft.proxycore.database.PixelDatabase
import eu.pixelgamesmc.minecraft.proxycore.database.collection.cache.CacheCollection
import eu.pixelgamesmc.minecraft.proxycore.database.collection.currency.CurrencyCollection
import eu.pixelgamesmc.minecraft.proxycore.database.collection.language.LanguageCollection
import eu.pixelgamesmc.minecraft.proxycore.database.collection.permission.group.PermissionGroupCollection
import eu.pixelgamesmc.minecraft.proxycore.database.collection.permission.user.PermissionUserCollection
import net.kyori.adventure.platform.bungeecord.BungeeAudiences
import net.md_5.bungee.api.plugin.Plugin
import org.litote.kmongo.getCollection

class ProxyCore: Plugin() {

    override fun onEnable() {
        BungeeAudiences.create(this)

        PixelDatabase.connect(
            PluginUtil.loadConfiguration(this, "database", Credentials(
                Credentials.Mongo(true, "", "database"),
                Credentials.Redis(true, "", 2324, "", "")
            ))
        )

        PixelDatabase.registerCollections { mongoDatabase ->
            listOf(
                CacheCollection(mongoDatabase.getCollection()),
                LanguageCollection(mongoDatabase.getCollection()),
                PermissionUserCollection(mongoDatabase.getCollection()),
                PermissionGroupCollection(mongoDatabase.getCollection()),
                CurrencyCollection(mongoDatabase.getCollection())
            )
        }

        PluginUtil.registerEvents(this, PlayerConnectionListener(), PlayerPermissionListener())
        PluginUtil.registerCommands(this, PermissionCommand())
    }
}