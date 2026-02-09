package org.hwabeag.playertime.inventory

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta
import org.hwabeag.playertime.PlayerTime

class TopPlayerTimeGui(private val plugin: PlayerTime) {
    private val inventory: Inventory = Bukkit.createInventory(null, 54, "플레이타임 순위")

    init {
        render()
    }

    fun open(player: Player) {
        player.openInventory(inventory)
    }

    private fun render() {
        val slot = intArrayOf(13, 21, 23, 29, 31, 33, 37, 39, 41, 43)
        val top = plugin.storage.top(10)

        for (i in slot.indices) {
            if (i >= top.size) break
            val entry = top[i]
            inventory.setItem(slot[i], createHead(entry.name, entry.minutes, i + 1))
        }
    }

    private fun createHead(name: String, minutes: Int, rank: Int): ItemStack {
        val item = ItemStack(Material.PLAYER_HEAD)
        val meta = item.itemMeta as SkullMeta

        meta.owningPlayer = Bukkit.getOfflinePlayer(name)
        meta.setDisplayName(color("&a&l${rank}위 &f$name"))
        meta.lore = listOf(color("&7플레이타임: &a${minutes}분"))

        item.itemMeta = meta
        return item
    }

    private fun color(text: String): String = ChatColor.translateAlternateColorCodes('&', text)
}
