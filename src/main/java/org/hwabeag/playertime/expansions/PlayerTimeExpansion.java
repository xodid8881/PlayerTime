package org.hwabeag.playertime.expansions;

import org.bukkit.configuration.file.FileConfiguration;
import org.hwabeag.playertime.PlayerTime;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.hwabeag.playertime.config.ConfigManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PlayerTimeExpansion extends PlaceholderExpansion {


    FileConfiguration PlayerTimeConfig = ConfigManager.getConfig("playertime");
    private final PlayerTime plugin; //

    public PlayerTimeExpansion(PlayerTime plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "playertime";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }
    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("get")) { // %playertime.player.get%
            String Name = Objects.requireNonNull(player).getName();
            if (PlayerTimeConfig.getString("playertime." + Name) == null) {
                return null;
            } else {
                return PlayerTimeConfig.getString("playertime." + Name);
            }
        }
        return null; //
    }
}