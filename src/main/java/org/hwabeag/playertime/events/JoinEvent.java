package org.hwabeag.playertime.events;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.hwabeag.playertime.config.ConfigManager;

import java.util.Objects;

public class JoinEvent implements Listener {
    FileConfiguration PlayerTimeConfig = ConfigManager.getConfig("playertime");
    String Prefix = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(PlayerTimeConfig.getString("playertime.prefix")));

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) {
        Player p = event.getPlayer();
        String Name = Objects.requireNonNull(p).getName();
        if (PlayerTimeConfig.getString("접속시간." + Name) == null) {
            PlayerTimeConfig.addDefault("접속시간." + Name, 0);
            ConfigManager.saveConfigs();
        }
        String TimeNumber = PlayerTimeConfig.getString("접속시간." + Name);
        p.sendMessage(Prefix + " 당신의 플레이 타임은 " + TimeNumber + " 분 입니다.");
    }
}
