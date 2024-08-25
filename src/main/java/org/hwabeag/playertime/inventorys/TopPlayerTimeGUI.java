package org.hwabeag.playertime.inventorys;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import java.util.ArrayList;
import java.util.Objects;

import static org.hwabeag.playertime.config.ConfigManager.getConfig;

public class TopPlayerTimeGUI implements Listener {
    private final Inventory inv;
    FileConfiguration PlayerTimeConfig = getConfig("playertime");

    private ItemStack getHead(String name, int i) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        skull.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&l" + name + " &7님의 플레이타임 &a[" + (i + 1) + "위]"));
        skull.setOwner(name);

        String TimeNumber = PlayerTimeConfig.getString("접속시간." + name);
        ArrayList<String> loreList = new ArrayList<>();
        loreList.add(ChatColor.translateAlternateColorCodes('&', "&7&l- &a&l" + TimeNumber + " &7분 동안 플레이중"));
        skull.setLore(loreList);

        item.setItemMeta(skull);
        return item;
    }

    private void initItemSetting() {
        int N = 0;
        for (String ignored : Objects.requireNonNull(PlayerTimeConfig.getConfigurationSection("접속시간")).getKeys(false)) {
            N = N + 1;
        }
        int[] score;
        String[] name;
        score = new int [N];
        name = new String [N];
        N = 0;
        for (String key : Objects.requireNonNull(PlayerTimeConfig.getConfigurationSection("접속시간")).getKeys(false)) {
            int TimeNumber = Integer.parseInt(Objects.requireNonNull(PlayerTimeConfig.getString("접속시간." + key)));
            score[N] = TimeNumber;
            name[N] = key;
            N = N + 1;
        }

        int temp1;
        String temp2;
        for(int i = 0; i < score.length; i++) {
            for(int j = i+1; j < score.length; j++) {
                if(score[i] < score[j]) {
                    temp1 = score[i];
                    score[i] = score[j];
                    score[j] = temp1;

                    temp2 = name[i];
                    name[i] = name[j];
                    name[j] = temp2;
                }
            }
        }

        int[] slot;
        slot = new int[] { 13, 21, 23, 29, 31, 33, 37, 39, 41, 43 };

        for(int i = 0; i < 10; i++) {
            String PlayerName = name[i];
            inv.setItem(slot[i],getHead(PlayerName, i));
        }


    }

    public TopPlayerTimeGUI() {
        this.inv = Bukkit.createInventory(null,54,"TopPlayerTimeGUI");
        initItemSetting();
    }

    public void open(Player player){
        player.openInventory(inv);
    }

}
