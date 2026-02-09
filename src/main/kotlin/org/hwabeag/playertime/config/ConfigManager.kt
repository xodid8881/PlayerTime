package org.hwabeag.playertime.config

import org.bukkit.ChatColor
import org.bukkit.configuration.file.FileConfiguration
import org.hwabeag.playertime.PlayerTime

class ConfigManager(private val plugin: PlayerTime) {
    val config: FileConfiguration
        get() = plugin.config

    init {
        plugin.saveDefaultConfig()
        plugin.reloadConfig()
    }

    val prefix: String
        get() = color(config.getString("playertime.prefix") ?: "&a&l[플레이타임]&7")

    val databaseType: String
        get() = config.getString("database.type") ?: "sqlite"

    fun sqlitePath(): String = config.getString("database.sqlite.file") ?: "data/playertime.db"

    fun mysqlHost(): String = config.getString("database.mysql.host") ?: "127.0.0.1"
    fun mysqlPort(): Int = config.getInt("database.mysql.port", 3306)
    fun mysqlDatabase(): String = config.getString("database.mysql.database") ?: "playertime"
    fun mysqlUsername(): String = config.getString("database.mysql.username") ?: "root"
    fun mysqlPassword(): String = config.getString("database.mysql.password") ?: ""
    fun mysqlUseSsl(): Boolean = config.getBoolean("database.mysql.useSSL", false)

    fun reload() {
        plugin.reloadConfig()
    }

    fun color(text: String): String = ChatColor.translateAlternateColorCodes('&', text)
}
