package eu.pixelgamesmc.minecraft.proxycore.database.collection.permission.user

import com.mongodb.client.MongoCollection
import eu.pixelgamesmc.minecraft.proxycore.database.collection.PixelCollection
import eu.pixelgamesmc.minecraft.proxycore.database.collection.PlayerCollection
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import redis.clients.jedis.JedisPool
import java.util.*

class PermissionUserCollection(
    jedisPool: JedisPool,
    collection: MongoCollection<PermissionUser>
): PixelCollection<PermissionUser>(
    jedisPool, collection
), PlayerCollection {

    fun update(permissionUser: PermissionUser) {
        updateCache("permission_user#${permissionUser.uuid}", permissionUser)
    }

    fun getUser(uuid: UUID): PermissionUser? {
        return getCache("permission_user#$uuid", PermissionUser::uuid eq uuid, PermissionUser::class)
    }

    override fun playerLogin(uuid: UUID, name: String, skin: String) {
        if (collection.findOne(PermissionUser::uuid eq uuid) == null) {
            collection.insertOne(PermissionUser(uuid, mutableListOf(), mutableListOf()))
        }
    }
}