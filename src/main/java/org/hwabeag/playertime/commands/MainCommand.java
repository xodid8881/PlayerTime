package org.hwabeag.playertime.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.hwabeag.playertime.config.ConfigManager;
import org.hwabeag.playertime.inventorys.TopPlayerTimeGUI;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MainCommand implements CommandExecutor {

    FileConfiguration PlayerTimeConfig = ConfigManager.getConfig("playertime");
    String Prefix = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(PlayerTimeConfig.getString("playertime.prefix")));

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player p)) {
            Bukkit.getConsoleSender().sendMessage(Prefix + " 인게임에서만 사용이 가능합니다.");
            if (args.length == 0) {
                Bukkit.getConsoleSender().sendMessage(Prefix + " /플레이타임 정보 [닉네임] - 나의 정보 혹 다른 유저의 정보를 확인합니다.");
                Bukkit.getConsoleSender().sendMessage(Prefix + " /플레이타임 설정 [닉네임] [분] - 유저의 정보를 설정합니다.");
                Bukkit.getConsoleSender().sendMessage(Prefix + " /플레이타임 리로드 - 콘피그 정보를 리로드 합니다.");
                return true;
            }
            if (args[0].equalsIgnoreCase("정보")) {
                if (args.length == 1) {
                    Bukkit.getConsoleSender().sendMessage(Prefix + " /플레이타임 정보 [닉네임] - 나의 정보 혹 다른 유저의 정보를 확인합니다.");
                    return true;
                }
                if (args.length == 2) {
                    Player target = Bukkit.getServer().getPlayerExact(args[1]);
                    String Name = Objects.requireNonNull(target).getName();
                    if (PlayerTimeConfig.getString("접속시간." + Name) == null) {
                        Bukkit.getConsoleSender().sendMessage(Prefix + " 해당 닉네임의 정보는 존재하지 않습니다.");
                        return true;
                    }
                    String TimeNumber = PlayerTimeConfig.getString("접속시간." + Name);
                    Bukkit.getConsoleSender().sendMessage(Prefix + " 해당 유저의 플레이 타임은 " + TimeNumber + " 분 입니다.");
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("설정")) {
                if (args.length == 1) {
                    Bukkit.getConsoleSender().sendMessage(Prefix + " /플레이타임 설정 [닉네임] [분] - 유저의 정보를 설정합니다.");
                    return true;
                }
                if (args.length == 2) {
                    Bukkit.getConsoleSender().sendMessage(Prefix + " /플레이타임 설정 [닉네임] [분] - 유저의 정보를 설정합니다.");
                    return true;
                }
                if (Bukkit.getServer().getPlayerExact(args[1]) != null) {
                    Player target = Bukkit.getServer().getPlayerExact(args[1]);
                    String Name = Objects.requireNonNull(target).getName();
                    if (PlayerTimeConfig.getString("접속시간." + Name) != null) {
                        PlayerTimeConfig.set("접속시간." + Name, args[2]);
                        Bukkit.getConsoleSender().sendMessage(Prefix + " " + Name + " 님의 시간을 " + args[2] + "분으로 설정했습니다.");
                        ConfigManager.saveConfigs();
                    } else {
                        PlayerTimeConfig.addDefault("접속시간." + Name, 0);
                        Bukkit.getConsoleSender().sendMessage(Prefix + " " + Name + " 님의 플레이타임 정보를 생성했습니다.");
                        ConfigManager.saveConfigs();
                    }
                } else {
                    Bukkit.getConsoleSender().sendMessage(Prefix + " " + args[1] + " 닉네임의 유저가 존재하지 않습니다.");
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("리로드")) {
                ConfigManager.reloadConfigs();
                Bukkit.getConsoleSender().sendMessage(Prefix + " 플레이타임 콘피그가 리로드 되었습니다.");
                return true;
            }
            Bukkit.getConsoleSender().sendMessage(Prefix + " /플레이타임 정보 [닉네임] - 나의 정보 혹 다른 유저의 정보를 확인합니다.");
            Bukkit.getConsoleSender().sendMessage(Prefix + " /플레이타임 설정 [닉네임] [분] - 유저의 정보를 설정합니다.");
            Bukkit.getConsoleSender().sendMessage(Prefix + " /플레이타임 리로드 - 콘피그 정보를 리로드 합니다.");
            return false;
        }
        if (args.length == 0) {
            p.sendMessage(Prefix + " /플레이타임 정보 [닉네임] - 나의 정보 혹 다른 유저의 정보를 확인합니다.");
            p.sendMessage(Prefix + " /플레이타임 순위 - 접속시간 순위를 확인합니다.");
            if (p.isOp()) {
                p.sendMessage(Prefix + " /플레이타임 설정 [닉네임] [분] - 유저의 정보를 설정합니다.");
                p.sendMessage(Prefix + " /플레이타임 리로드 - 콘피그 정보를 리로드 합니다.");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("순위")) {
            if (args.length == 1) {
                TopPlayerTimeGUI inv = new TopPlayerTimeGUI();
                inv.open(p);
                return true;
            }
        }
        /* String TimeNumber = PlaceholderAPI.setPlaceholders(p, "%playertime_get%"); */
        // p.sendMessage(Prefix + " 당신의 플레이 타임은 " + TimeNumber + " 분 입니다."); */
        if (args[0].equalsIgnoreCase("정보")) {
            if (args.length == 1) {
                String Name = Objects.requireNonNull(p).getName();
                if (PlayerTimeConfig.getString("접속시간." + Name) == null) {
                    p.sendMessage(Prefix + " 당신의 정보가 존재하지 않습니다.");
                    return true;
                }
                String TimeNumber = PlayerTimeConfig.getString("접속시간." + Name);
                p.sendMessage(Prefix + " 당신의 플레이 타임은 " + TimeNumber + " 분 입니다.");
                return true;
            }
            if (args.length == 2) {
                Player target = Bukkit.getServer().getPlayerExact(args[1]);
                String Name = Objects.requireNonNull(target).getName();
                if (PlayerTimeConfig.getString("접속시간." + Name) == null) {
                    p.sendMessage(Prefix + " 해당 닉네임의 정보는 존재하지 않습니다.");
                    return true;
                }
                String TimeNumber = PlayerTimeConfig.getString("접속시간." + Name);
                p.sendMessage(Prefix + " 해당 유저의 플레이 타임은 " + TimeNumber + " 분 입니다.");
                return true;
            }
        }
        if (p.isOp()) {
            if (args[0].equalsIgnoreCase("설정")) {
                if (args.length == 1) {
                    p.sendMessage(Prefix + " /플레이타임 설정 [닉네임] [분] - 유저의 정보를 설정합니다.");
                    return true;
                }
                if (args.length == 2) {
                    p.sendMessage(Prefix + " /플레이타임 설정 [닉네임] [분] - 유저의 정보를 설정합니다.");
                    return true;
                }
                if (Bukkit.getServer().getPlayerExact(args[1]) != null) {
                    Player target = Bukkit.getServer().getPlayerExact(args[1]);
                    String Name = Objects.requireNonNull(target).getName();
                    if (PlayerTimeConfig.getString("접속시간." + Name) != null) {
                        PlayerTimeConfig.set("접속시간." + Name, args[2]);
                        p.sendMessage(Prefix + " " + Name + " 님의 시간을 " + args[2] + "분으로 설정했습니다.");
                        ConfigManager.saveConfigs();
                    } else {
                        PlayerTimeConfig.addDefault("접속시간." + Name, 0);
                        p.sendMessage(Prefix + " " + Name + " 님의 플레이타임 정보를 생성했습니다.");
                        ConfigManager.saveConfigs();
                    }
                } else {
                    p.sendMessage(Prefix + " " + args[1] + " 닉네임의 유저가 존재하지 않습니다.");
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("리로드")) {
                ConfigManager.reloadConfigs();
                p.sendMessage(Prefix + " 플레이타임 콘피그가 리로드 되었습니다.");
                return true;
            }
            p.sendMessage(Prefix + " /플레이타임 정보 [닉네임] - 나의 정보 혹 다른 유저의 정보를 확인합니다.");
            if (p.isOp()) {
                p.sendMessage(Prefix + " /플레이타임 설정 [닉네임] [분] - 유저의 정보를 설정합니다.");
                p.sendMessage(Prefix + " /플레이타임 리로드 - 콘피그 정보를 리로드 합니다.");
                Bukkit.getConsoleSender().sendMessage(Prefix + " /플레이타임 papi - papi 테스트 명령어");
            }
        } else {
            p.sendMessage(Prefix + " 당신은 권한이 없습니다.");
        }
        return true;
    }
}