package org.hwabeag.playertime.events

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.hwabeag.playertime.PlayerTime

class JoinEvent(private val plugin: PlayerTime) : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        plugin.storage.ensurePlayer(player.uniqueId.toString(), player.name)

        val minutes = plugin.storage.getMinutesByName(player.name) ?: 0
        player.sendMessage("${plugin.configManager.prefix} 현재 플레이타임은 ${minutes}분입니다.")
    }
}
