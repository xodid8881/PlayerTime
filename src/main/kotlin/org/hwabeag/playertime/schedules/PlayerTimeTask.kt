package org.hwabeag.playertime.schedules

import org.bukkit.Bukkit
import org.hwabeag.playertime.PlayerTime

class PlayerTimeTask(private val plugin: PlayerTime) : Runnable {

    override fun run() {
        for (player in Bukkit.getOnlinePlayers()) {
            plugin.storage.incrementMinutes(player.uniqueId.toString(), player.name)
        }
    }
}
