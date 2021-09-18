package cz.nerdy.craftchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import cz.craftmania.craftcore.messages.chat.ChatInfo;
import cz.nerdy.craftchat.Main;
import cz.nerdy.craftchat.objects.CraftChatPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("tags|tagy")
@Description("Zobrazi všechny tagy, které jsou k dispozici")
public class TagsCommand extends BaseCommand {
    @Default
    public void showTagsMenu(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Jenom ze hry");
        }

        Player player = (Player) sender;
        CraftChatPlayer craftChatPlayer = Main.getCraftChatPlayer(player);
        
        if (!craftChatPlayer.getChatGroup().isAllowTagChange()) {
            ChatInfo.error(player, "Tvá skupina má zakázanou změnu tagů");
        } else {
            Main.getTagManager().openMainMenu(player);
        }
    }
    @HelpCommand
    public void helpCommand(CommandHelp help) {
        help.showHelp();
    }
}
