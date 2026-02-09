package org.hwabeag.playertime

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.hwabeag.playertime.commands.MainCommand
import org.hwabeag.playertime.config.ConfigManager
import org.hwabeag.playertime.events.InvClickEvent
import org.hwabeag.playertime.events.JoinEvent
import org.hwabeag.playertime.expansions.PlayerTimeExpansion
import org.hwabeag.playertime.schedules.PlayerTimeTask
import org.hwabeag.playertime.storage.MySqlTimeStorage
import org.hwabeag.playertime.storage.SqliteTimeStorage
import org.hwabeag.playertime.storage.TimeStorage

class PlayerTime : JavaPlugin() {
    lateinit var configManager: ConfigManager
        private set

    lateinit var storage: TimeStorage
        private set

    override fun onEnable() {
        instance = this
        logger.info("[PlayerTime] Enable")

        configManager = ConfigManager(this)
        storage = createStorage()
        storage.initialize()

        registerCommands()
        registerEvents()

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, PlayerTimeTask(this), 20L * 60L, 20L * 60L)

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            PlayerTimeExpansion(this).register()
            logger.info("[PlayerTime] PlaceholderAPI 연동 활성화")
        }
    }

    override fun onDisable() {
        logger.info("[PlayerTime] Disable")
        storage.close()
    }

    private fun registerCommands() {
        getCommand("플레이타임")?.setExecutor(MainCommand(this))
    }

    private fun registerEvents() {
        server.pluginManager.registerEvents(JoinEvent(this), this)
        server.pluginManager.registerEvents(InvClickEvent(this), this)
    }

    private fun createStorage(): TimeStorage {
        return when (configManager.databaseType.lowercase()) {
            "mysql" -> MySqlTimeStorage(this)
            else -> SqliteTimeStorage(this)
        }
    }

    companion object {
        lateinit var instance: PlayerTime
            private set
    }
}
