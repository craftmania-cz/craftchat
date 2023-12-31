package cz.craftmania.craftchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import cz.craftmania.craftlibs.utils.ChatInfo;
import cz.craftmania.craftchat.Main;
import cz.craftmania.craftchat.objects.CraftChatPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("tags")
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
            ChatInfo.DANGER.send(player, "Na tomto serveru si nelze změnit tag.");
        } else {
            Main.getTagManager().openMainMenu(player);
        }
    }
    @HelpCommand
    public void helpCommand(CommandHelp help) {
        help.showHelp();
    }
}
