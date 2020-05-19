package cz.nerdy.craftchat.commands;

import cz.craftmania.craftcore.spigot.inventory.builder.SmartInventory;
import cz.craftmania.craftcore.spigot.messages.chat.ChatInfo;
import cz.nerdy.craftchat.Main;
import cz.nerdy.craftchat.menu.ChatColorMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatColorCommand implements CommandExecutor {

    /*
        0 - cerna §0
        1 - modra §1
        2 - zelena §2
        3 - tmave-tyrkysova §3
        4 - tmave-cervena §4
        5 - fialova §5
        6 - zlata §6
        7 - seda §7
        8 - tmave-seda §8
        9 - modra §9
        a - lime §a (10)
        b - svetle-modra §b (11)
        c - cervena §c (12)
        d - ruzova §d (13)
        e - zluta §e (14) - AT
        f - bila §f (15 - default)
     */

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Tento příkaz může používat puze hráč!");
            return true;
        }

        Player player = (Player) sender;

        if (!Main.sqlEnabled) {
            ChatInfo.error(player, "ento příkaz teď není dostupný. Zkus to za chvíli.");
            return true;
        }

        if (player.hasPermission("craftchat.chatcolor")) {
            SmartInventory.builder().size(6, 9).title("Změna barvy psaní").provider(new ChatColorMenu()).build().open(player);
            return true;
        } else {
            player.sendMessage("§c§l[!] §cPro psani barevne v chatu musis vlastnit VIP!");
            return true;
        }
    }
}
