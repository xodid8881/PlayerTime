package org.hwabeag.playertime;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.hwabeag.playertime.commands.MainCommand;
import org.hwabeag.playertime.config.ConfigManager;
import org.hwabeag.playertime.events.JoinEvent;

import org.hwabeag.playertime.events.InvClickEvent;
import org.hwabeag.playertime.expansions.PlayerTimeExpansion;
import org.hwabeag.playertime.tasks.TimeTask;

import java.util.Objects;

public final class PlayerTime extends JavaPlugin {
    private static ConfigManager configManager;

    private FileConfiguration config;

    public static PlayerTime getPlugin() {
        return JavaPlugin.getPlugin(PlayerTime.class);
    }

    public static void getConfigManager() {
        if (configManager == null)
            configManager = new ConfigManager();
    }

    private void registerEvents(){
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        getServer().getPluginManager().registerEvents(new InvClickEvent(), this);
    }

    private void registerCommands(){
        Objects.requireNonNull(getServer().getPluginCommand("플레이타임")).setExecutor(new MainCommand());
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("[PlayerTime] Enable");
        getConfigManager();
        registerCommands();
        registerEvents();
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new TimeTask(), 20*60, 20*60);
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlayerTimeExpansion(this).register();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("[PlayerTime] Disable");
        ConfigManager.saveConfigs();
    }
}
