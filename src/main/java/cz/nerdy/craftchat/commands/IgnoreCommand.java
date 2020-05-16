package cz.nerdy.craftchat.commands;

import cz.craftmania.craftcore.spigot.messages.chat.ChatInfo;
import cz.nerdy.craftchat.Main;
import cz.nerdy.craftchat.objects.CraftChatPlayer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class IgnoreCommand implements CommandExecutor {

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

        if (args.length == 0) {
            sendList(player);
            return true;
        }

        String arg = args[0];

        if (arg.equalsIgnoreCase("help")) {
            player.sendMessage("§c§lNápověda");
            this.sendHelpComponent(player, "§c/ignore §8- §7zobrazí seznam ignorovaných hráčů", "Zobrazí seznam ignorovaných hráčů", "/ignore");
            this.sendHelpComponent(player, "§c/ignore <hráč> §8- §7zablokovat/odblokovat hráče", "Zablokuje nebo odblokuje hráče", "/ignore ");
            this.sendHelpComponent(player, "§c/ignore help §8- §7zobrazí nápovědu", "Zobrazí tuto nápovědu", "/ignore help");
            return true;
        }

        Player ignoredPlayer = Bukkit.getPlayer(arg);
        if (ignoredPlayer == null) {
            ChatInfo.error(player, "Hráč musí být online");
            return true;
        }

        if(ignoredPlayer.getName().equals(player.getName())){
            ChatInfo.error(player, "Nemůžeš ignorovat sám sebe");
            return true;
        }

        CraftChatPlayer craftChatPlayer = Main.getCraftChatPlayer(player);
        if (craftChatPlayer.hasIgnored(ignoredPlayer)) {
            craftChatPlayer.removeIgnoredPlayer(ignoredPlayer);
            ChatInfo.success(player, "Hráč byl odebrán ze seznamu");
        } else {
            craftChatPlayer.addIgnoredPlayer(ignoredPlayer);
            ChatInfo.success(player, "Od teď ignoruješ hráče " + ignoredPlayer.getName());
        }

        return true;
    }

    private void sendList(Player player) {
        player.sendMessage("§c§lIgnorovaní hráči §c- /ignore help");

        // - nick [✘](Klikni pro odblokování)     ✗
        CraftChatPlayer craftChatPlayer = Main.getCraftChatPlayer(player);
        Set<String> ignoredNicks = craftChatPlayer.getIgnoredPlayers();
        for (String nick : ignoredNicks) {
            TextComponent removeComponent = new TextComponent(TextComponent.fromLegacyText("§7[§c✘§7]"));
            removeComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Klikni pro obdlokování")));
            removeComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ignore " + nick));

            TextComponent playerComponent = new TextComponent("§8- §7" + nick + " ");
            BaseComponent[] baseComponent = {playerComponent, removeComponent};
            player.spigot().sendMessage(baseComponent);
        }
    }

    private void sendHelpComponent(Player player, String text, String hoverText, String suggestCommand) {
        TextComponent textComponent = new TextComponent(TextComponent.fromLegacyText(text));
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(hoverText)));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, suggestCommand));

        player.spigot().sendMessage(textComponent);
    }
}
