package com.creeperface.nukkit.lpchat

import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.player.PlayerChatEvent
import cn.nukkit.plugin.PluginBase
import cn.nukkit.utils.PluginException
import cn.nukkit.utils.TextFormat
import me.lucko.luckperms.LuckPerms
import me.lucko.luckperms.api.User

/**
 * @author CreeperFace
 */
class LPChat : PluginBase(), Listener {

    var chatFormat: String = "%prefix% {%0} ${TextFormat.GRAY}%suffix%: {%1}"
        private set

    override fun onLoad() {
        if (!KotlinLibDownloader.check(this)) {
            throw PluginException("KotlinLib could not be found")
        }
    }

    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)

        saveDefaultConfig()
        val cfg = config

        chatFormat = cfg.getString("format").replace('&', '§', true).replaceMap(mapOf("%player%" to "{%0}", "%message%" to "{%1}"))
        println(chatFormat)
    }

    @EventHandler
    private fun onChat(e: PlayerChatEvent) {
        val p = e.player
        var prefix = ""
        var suffix = ""

        val api = LuckPerms.getApi()
        val user: User? = api.getUser(p.uniqueId)

        user?.run {
            val data = cachedData.getMetaData(api.contextManager.getApplicableContexts(p))

            prefix = (data.prefix ?: "").replace('&', '§')
            suffix = (data.suffix ?: "").replace('&', '§')
        }

        e.format = chatFormat.replaceMap(mapOf("%suffix%" to suffix, "%prefix%" to prefix))
    }
}