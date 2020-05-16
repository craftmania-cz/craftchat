package cz.nerdy.craftchat.commands;

import cz.craftmania.craftcore.spigot.messages.chat.ChatInfo;
import cz.nerdy.craftchat.Main;
import cz.nerdy.craftchat.objects.CraftChatPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TagsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Jenom ze hry");
            return true;
        }

        Player player = (Player) sender;
        if (!Main.sqlEnabled) {
            ChatInfo.error(player, "Příkazy nejsou dostupné v offline režimu.");
            return true;
        }

        CraftChatPlayer craftChatPlayer = Main.getCraftChatPlayer(player);
        
        if (!craftChatPlayer.getChatGroup().isAllowTagChange()) {
            ChatInfo.error(player, "Tvá skupina má zakázanou změnu tagů");
            return true;
        }

        String type = "all";
        if (args.length > 0) {
            type = args[0];
        }
        Main.getTagManager().openMainMenu(player);

        return true;
    }
}
