package org.hwabeag.playertime.expansions

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer
import org.hwabeag.playertime.PlayerTime

class PlayerTimeExpansion(private val plugin: PlayerTime) : PlaceholderExpansion() {

    override fun persist(): Boolean = true

    override fun canRegister(): Boolean = true

    override fun getAuthor(): String = plugin.description.authors.joinToString(",")

    override fun getIdentifier(): String = "playertime"

    override fun getVersion(): String = plugin.description.version

    override fun onRequest(player: OfflinePlayer?, params: String): String? {
        val name = player?.name ?: return null
        return when (params.lowercase()) {
            "get", "player_get" -> (plugin.storage.getMinutesByName(name) ?: 0).toString()
            else -> null
        }
    }
}
