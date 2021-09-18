package cz.nerdy.craftchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import cz.craftmania.craftcore.inventory.builder.SmartInventory;
import cz.nerdy.craftchat.Main;
import cz.nerdy.craftchat.menu.ChatColorMenu;
import cz.nerdy.craftchat.objects.CraftChatPlayer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

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
@CommandPermission("craftchat.chatcolor")
public class ChatColorCommand extends BaseCommand {
    @Default // Deafult = Přkíaz bez argumentů -> /chatcolor
    public void showChatColorMenu(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Tento příkaz může používat pouze hráč!");
        }

        Player player = (Player) sender;

        SmartInventory.builder().size(6, 9).title("Změna barvy psaní").provider(new ChatColorMenu()).build().open(player);
    }

    private static final Pattern pattern = Pattern.compile("^#[a-fA-F0-9]{6}$");

    @Subcommand("custom")
    @CommandPermission("craftchat.chatcolor.custom")
    @CommandAlias("chatcolor")
    @CommandCompletion("[#RRGGBB]")
    @Description("Změna na custom barvu")
    public void setCustomColor(Player player, String colorCode) {
        if (pattern.matcher(colorCode).matches()) {
            CraftChatPlayer craftChatPlayer = Main.getCraftChatPlayer(player);
            craftChatPlayer.setCustomChatColor(colorCode.substring(1));
            craftChatPlayer.getPlayer().sendMessage("§e§l[*] §eBarva psani nastavena na: " + ChatColor.of(colorCode) + colorCode);
        } else {
            player.sendMessage("§c§l[!] §cNesprávný formát! Použij například /chatcolor #3DA1CD");
        }
    }

    @HelpCommand
    public void helpCommand(CommandHelp help) {
        help.showHelp();
    }
}