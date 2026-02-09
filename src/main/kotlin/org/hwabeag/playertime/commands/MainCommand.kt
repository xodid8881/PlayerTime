package org.hwabeag.playertime.commands

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.hwabeag.playertime.PlayerTime
import org.hwabeag.playertime.inventory.TopPlayerTimeGui

class MainCommand(private val plugin: PlayerTime) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val prefix = plugin.configManager.prefix

        if (args.isEmpty()) {
            sender.sendMessage("$prefix /플레이타임 정보 [닉네임]")
            sender.sendMessage("$prefix /플레이타임 순위")
            if (sender.isOp) {
                sender.sendMessage("$prefix /플레이타임 설정 <닉네임> <분>")
                sender.sendMessage("$prefix /플레이타임 지급 <닉네임> <분>")
                sender.sendMessage("$prefix /플레이타임 리로드")
            }
            return true
        }

        return when (args[0].lowercase()) {
            "정보" -> handleInfo(sender, args)
            "순위" -> handleTop(sender)
            "설정" -> handleSet(sender, args)
            "지급" -> handleAdd(sender, args)
            "리로드" -> handleReload(sender)
            else -> {
                sender.sendMessage("$prefix 알 수 없는 하위 명령어입니다.")
                true
            }
        }
    }

    private fun handleInfo(sender: CommandSender, args: Array<out String>): Boolean {
        val prefix = plugin.configManager.prefix
        val targetName = if (args.size >= 2) args[1] else (sender as? Player)?.name

        if (targetName == null) {
            sender.sendMessage("$prefix 콘솔에서는 닉네임을 함께 입력해야 합니다.")
            return true
        }

        val minutes = plugin.storage.getMinutesByName(targetName)
        if (minutes == null) {
            sender.sendMessage("$prefix $targetName 님의 데이터가 없습니다.")
            return true
        }

        sender.sendMessage("$prefix $targetName 님의 플레이타임은 ${minutes}분입니다.")
        return true
    }

    private fun handleTop(sender: CommandSender): Boolean {
        val prefix = plugin.configManager.prefix
        if (sender !is Player) {
            sender.sendMessage("$prefix 콘솔에서는 사용할 수 없습니다.")
            return true
        }

        TopPlayerTimeGui(plugin).open(sender)
        return true
    }

    private fun handleSet(sender: CommandSender, args: Array<out String>): Boolean {
        val prefix = plugin.configManager.prefix
        if (!sender.isOp) {
            sender.sendMessage("$prefix 권한이 없습니다.")
            return true
        }

        if (args.size < 3) {
            sender.sendMessage("$prefix 사용법: /플레이타임 설정 <닉네임> <분>")
            return true
        }

        val name = args[1]
        val minutes = args[2].toIntOrNull()
        if (minutes == null || minutes < 0) {
            sender.sendMessage("$prefix 분 값은 0 이상의 숫자여야 합니다.")
            return true
        }

        val online = Bukkit.getPlayerExact(name)
        if (online != null) {
            plugin.storage.ensurePlayer(online.uniqueId.toString(), online.name)
        }

        val changed = plugin.storage.setMinutesByName(name, minutes)
        if (!changed) {
            sender.sendMessage("$prefix $name 님의 데이터가 없어 설정하지 못했습니다. 한 번 이상 접속한 플레이어인지 확인하세요.")
            return true
        }

        sender.sendMessage("$prefix $name 님의 플레이타임을 ${minutes}분으로 설정했습니다.")
        return true
    }

    private fun handleAdd(sender: CommandSender, args: Array<out String>): Boolean {
        val prefix = plugin.configManager.prefix
        if (!sender.isOp) {
            sender.sendMessage("$prefix 권한이 없습니다.")
            return true
        }

        if (args.size < 3) {
            sender.sendMessage("$prefix 사용법: /플레이타임 지급 <닉네임> <분>")
            return true
        }

        val name = args[1]
        val minutes = args[2].toIntOrNull()
        if (minutes == null || minutes <= 0) {
            sender.sendMessage("$prefix 분 값은 1 이상의 숫자여야 합니다.")
            return true
        }

        val online = Bukkit.getPlayerExact(name)
        if (online != null) {
            plugin.storage.ensurePlayer(online.uniqueId.toString(), online.name)
        }

        val total = plugin.storage.addMinutesByName(name, minutes)
        if (total == null) {
            sender.sendMessage("$prefix $name 님의 데이터가 없어 지급하지 못했습니다. 한 번 이상 접속한 플레이어인지 확인하세요.")
            return true
        }

        sender.sendMessage("$prefix $name 님에게 ${minutes}분을 지급했습니다. 현재 ${total}분입니다.")
        return true
    }

    private fun handleReload(sender: CommandSender): Boolean {
        val prefix = plugin.configManager.prefix
        if (!sender.isOp) {
            sender.sendMessage("$prefix 권한이 없습니다.")
            return true
        }

        plugin.configManager.reload()
        sender.sendMessage("$prefix 설정을 다시 불러왔습니다.")
        return true
    }
}
