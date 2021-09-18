package cz.nerdy.craftchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import cz.craftmania.craftcore.inventory.builder.SmartInventory;
import cz.nerdy.craftchat.menu.ChatColorMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

@CommandAlias("chatcolor")
@Description("Změna barvy")
public class ChatColorCommand extends BaseCommand {
    @Default // Deafult = Přkíaz bez argumentů -> /chatcolor
    public void showChatColorMenu(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Tento příkaz může používat pouze hráč!");
        }

        Player player = (Player) sender;

        if (player.hasPermission("craftchat.chatcolor")) {
            SmartInventory.builder().size(6, 9).title("Změna barvy psaní").provider(new ChatColorMenu()).build().open(player);
        } else {
            player.sendMessage("§c§l[!] §cPro psani barevne v chatu musis vlastnit VIP!");
        }
    }
    @HelpCommand
    public void helpCommand(CommandHelp help) {
        help.showHelp();
    }
}