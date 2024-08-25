package org.hwabeag.playertime.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.hwabeag.playertime.PlayerTime;

import java.util.*;

public class ConfigManager {
    private static final PlayerTime plugin = PlayerTime.getPlugin();

    private static final HashMap<String, ConfigMaker> configSet = new HashMap<>();


    public ConfigManager() {
        String path = plugin.getDataFolder().getAbsolutePath();
        configSet.put("playertime", new ConfigMaker(path, "PlayerTime.yml"));
        loadSettings();
        saveConfigs();
    }

    public static void reloadConfigs() {
        for (String key : configSet.keySet()){
            plugin.getLogger().info(key);
            configSet.get(key).reloadConfig();
        }
    }

    public static void saveConfigs(){
        for (String key : configSet.keySet())
            configSet.get(key).saveConfig();
    }

    public static FileConfiguration getConfig(String fileName) {
        return configSet.get(fileName).getConfig();
    }

    public void loadSettings(){
        FileConfiguration PlayerTimeConfig = getConfig("playertime");
        PlayerTimeConfig.options().copyDefaults(true);

        PlayerTimeConfig.addDefault("playertime.prefix", "&a&l[PlayerTime]&7");
    }


}