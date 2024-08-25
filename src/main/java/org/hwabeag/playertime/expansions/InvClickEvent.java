package org.hwabeag.playertime.expansions;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.hwabeag.playertime.config.ConfigManager;

import java.util.Objects;

public class InvClickEvent implements Listener {

    FileConfiguration PlayerTimeConfig = ConfigManager.getConfig("playertime");
    String Prefix = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(PlayerTimeConfig.getString("playertime.prefix")));

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if(e.getClickedInventory() == null)
            return;
        if(ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("TopPlayerTimeGUI")){
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            player.sendMessage(Prefix + " 인벤토리 클릭이 취소되었습니다.");
        }
    }

}
