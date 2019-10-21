package cz.nerdy.craftchat.commands;

import cz.nerdy.craftchat.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Jenom ze hry");
            return true;
        }

        Player player = (Player) sender;

        String command = cmd.getName().toLowerCase();

        switch (command) {
            case "tags":
                String type = "all";
                if (args.length > 0) {
                    type = args[0];
                }
                Main.getInstance().getTagManager().openMenu(player, type);
        }
        return true;
    }
}
