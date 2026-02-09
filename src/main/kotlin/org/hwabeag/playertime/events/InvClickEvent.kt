package org.hwabeag.playertime.events

import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.hwabeag.playertime.PlayerTime

class InvClickEvent(private val plugin: PlayerTime) : Listener {

    @EventHandler
    fun onClick(event: InventoryClickEvent) {
        val clicked = event.clickedInventory ?: return
        if (ChatColor.stripColor(event.view.title) != "플레이타임 순위") {
            return
        }

        event.isCancelled = true
        val player = event.whoClicked as? Player ?: return
        if (clicked == player.inventory) {
            return
        }

        player.sendMessage("${plugin.configManager.prefix} 순위 창에서는 아이템을 이동할 수 없습니다.")
    }
}
