package org.hwabeag.playertime.tasks;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.hwabeag.playertime.config.ConfigManager;

import java.util.Objects;

public class TimeTask implements Runnable {

    FileConfiguration PlayerTimeConfig = ConfigManager.getConfig("playertime");

    @Override
    public void run() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            int TimeNumber;
            String Name = Objects.requireNonNull(p).getName();
            if (PlayerTimeConfig.getString("접속시간." + Name) != null) {
                TimeNumber = PlayerTimeConfig.getInt("접속시간." + Name);
                PlayerTimeConfig.set("접속시간." + Name, TimeNumber + 1);
                ConfigManager.saveConfigs();
            } else {
                PlayerTimeConfig.addDefault("접속시간." + Name, 0);
                ConfigManager.saveConfigs();
            }
        }
    }
}
