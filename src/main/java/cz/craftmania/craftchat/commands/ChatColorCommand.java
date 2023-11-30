package cz.craftmania.craftchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import cz.craftmania.craftchat.menu.ChatColorMenu;
import cz.craftmania.craftchat.objects.CraftChatPlayer;
import cz.craftmania.craftcore.inventory.builder.SmartInventory;
import cz.craftmania.craftlibs.utils.ChatInfo;
import cz.craftmania.craftchat.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

import static co.aikar.commands.ACFBukkitUtil.sendMsg;

@CommandAlias("chatcolor")
@Description("Změna barvy")
@CommandPermission("craftchat.chatcolor")
public class ChatColorCommand extends BaseCommand {

    private static final Pattern pattern = Pattern.compile("^#[a-fA-F0-9]{6}$");

    @Default
    public void showChatColorMenu(CommandSender sender) {
        if (sender instanceof Player player) {
            SmartInventory.builder().size(6, 9).title("Změna barvy psaní").provider(new ChatColorMenu()).build().open(player);
        }
    }

    @Default
    @CommandPermission("craftchat.chatcolor.custom")
    @CommandCompletion("#RRGGBB")
    @Description("Změna na custom barvu")
    public void setCustomColor(Player player, String colorCode) {
        if (pattern.matcher(colorCode).matches()) {
            CraftChatPlayer craftChatPlayer = Main.getCraftChatPlayer(player);
            craftChatPlayer.setCustomChatColor(colorCode.substring(1));
            ChatInfo.SUCCESS.send(player, "Barva psani nastavena na: " + ChatColor.of(colorCode) + colorCode);
        } else {
            ChatInfo.ERROR.send(player, "Nesprávný formát! Použij například §f/chatcolor #3DA1CD");
        }
    }

    @HelpCommand
    public void helpCommand(Player player, CommandHelp help) {
        help.showHelp();
    }
}