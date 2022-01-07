package cz.nerdy.craftchat.listeners;

import cz.craftmania.craftcore.messages.chat.ChatInfo;
import cz.nerdy.craftchat.Main;
import cz.nerdy.craftchat.objects.ChatGroup;
import cz.nerdy.craftchat.objects.CraftChatPlayer;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();
        String message = event.getMessage();

        if (!Main.getInstance().isDisabledTags() && Main.getTagManager().isCreatingTag(player)) {
            if (message.equalsIgnoreCase("stop")) {
                Main.getTagManager().stopTagCreation(player);
            } else {
                Main.getTagManager().createTag(player, message);
            }
            event.setCancelled(true);
            return;
        }

        CraftChatPlayer craftChatPlayer = Main.getCraftChatPlayer(player);

        if (craftChatPlayer == null) {
            ChatInfo.error(player, "Nepodařilo se načíst tvé data, zkus to později.");
            return;
        }

        ChatGroup chatGroup = craftChatPlayer.getChatGroup();

        if (message.startsWith("ú") && message.length() > 1 && craftChatPlayer.isCheckForSlashMistake()) {
            player.sendMessage("");
            player.sendMessage("§7Nepřepsal jsi se? Nechtěl jsi toto napsat jako příkaz? §8(" + message + ")");
            player.sendMessage("");
            player.sendMessage("§8§o(Klikni pro výběr možnosti)");
            player.playSound(player.getEyeLocation(), Sound.ENTITY_CAT_HISS, 1.0F, 1.0F);
            TextComponent sendCommand = new TextComponent(TextComponent.fromLegacyText("Chci odeslat příkaz"));
            sendCommand.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, message.replaceFirst("ú", "/")));
            sendCommand.setColor(ChatColor.GREEN);
            player.spigot().sendMessage(sendCommand);
            sendCommand.setBold(true);
            player.sendMessage("");
            TextComponent messageCommand = new TextComponent(TextComponent.fromLegacyText("Chci odeslat jako zprávu"));
            messageCommand.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, message));
            messageCommand.setColor(ChatColor.RED);
            messageCommand.setBold(true);
            player.spigot().sendMessage(messageCommand);
            player.sendMessage("");

            craftChatPlayer.setCheckForSlashMistake(false);
            event.setCancelled(true);
            return;
        } else {
            craftChatPlayer.setCheckForSlashMistake(true);
        }

        // Free replacementy
        HashMap<String, String> freeReplacements = Main.getChatManager().getFreeReplacements();
        for (String replacement : freeReplacements.keySet()) {
            if (message.contains(replacement)) {
                message = message.replaceAll(Pattern.quote(replacement), freeReplacements.get(replacement) + craftChatPlayer.getSelectedChatColor());
            }
        }

        // VIP replacementy
        HashMap<String, String> premiumReplacements = Main.getChatManager().getPremiumReplacements();
        for (String replacement : premiumReplacements.keySet()) {
            if (message.contains(replacement)) {
                if (player.hasPermission("craftchat.replacements")) {
                    message = message.replaceAll(Pattern.quote(replacement), premiumReplacements.get(replacement) + craftChatPlayer.getSelectedChatColor());
                } else {
                    player.sendMessage("§c§l[!] §cNelze používat replacementy, když nemáš VIP!");
                    event.setCancelled(true);
                    return;
                }
            }
        }

        for (String value : premiumReplacements.values()) {
            if (message.contains(ChatColor.stripColor(value)) && !player.hasPermission("craftchat.replacements")) {
                player.sendMessage("§c§l[!] §cNelze používat replacementy, když nemáš VIP!");
                event.setCancelled(true);
                return;
            }
        }

        List<String> blockedTags = Main.getChatManager().getBlockedTexts();
        for (String tag : blockedTags) {
            if (message.contains(tag)) {
                player.sendMessage("§c§l[!] §cNelze používat prefixy, tagy a extra znaky v chatu!");
                event.setCancelled(true);
                return;
            }
        }

        if (player.hasPermission("craftchat.color")) {
            if (!player.hasPermission("craftchat.color.format")) {
                message = message.replaceAll("(?i)&l", "");
                message = message.replaceAll("(?i)&n", "");
                message = message.replaceAll("(?i)&m", "");
                message = message.replaceAll("(?i)&o", "");
                message = message.replaceAll("(?i)&k", "");
            }
            if (player.hasPermission("craftchat.at")) {
                message = "&e" + message; // zluta barva default pro at
            } else {
                message = message.replaceAll("(?i)&e", "");
            }
            message = Main.getInstance().getPluginCompatibility().translateChatColor(message);
        }

        TextComponent space = new TextComponent(TextComponent.fromLegacyText(" "));

        // Prefix
        String prefix = PlaceholderAPI.setPlaceholders(player, craftChatPlayer.getPrefix());
        String strippedPrefix = ChatColor.stripColor(prefix); // PlaceholderAPI generuje již obarvený default prefix, je potřeba barvy odstranit
        TextComponent prefixComponent = new TextComponent(TextComponent.fromLegacyText(strippedPrefix));
        prefixComponent.setColor(chatGroup.getPrefixColor());
        String prefixTooltip = "";
        for (String line : chatGroup.getPrefixTooltip()) {
            prefixTooltip += line + "§r\n";
        }
        prefixComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(prefixTooltip)));

        // Name
        TextComponent nameComponent = new TextComponent(TextComponent.fromLegacyText(player.getName()));
        nameComponent.setColor(chatGroup.getNameColor());
        nameComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                PlaceholderAPI.setPlaceholders(player, chatGroup.getNameClickCommand().replace("%player_name%", player.getName()))));
        String nickTooltip = "";
        for (String line : chatGroup.getNameTooltip()) {
            nickTooltip += PlaceholderAPI.setPlaceholders(player, line.replace("%player_name%", player.getName())) + "§r\n";
        }
        nameComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(nickTooltip)));

        // Suffix
        TextComponent suffixComponent = new TextComponent(TextComponent.fromLegacyText(chatGroup.getSuffix()));
        suffixComponent.setColor(chatGroup.getSuffixColor());

        // Text zprávy
        TextComponent playerMessage = new TextComponent(TextComponent.fromLegacyText(message));
        playerMessage.setColor(craftChatPlayer.getSelectedChatColor());

        BaseComponent[] toSend = {prefixComponent, nameComponent, space, suffixComponent, space, playerMessage};

        event.setFormat(prefix + " %s: %s"); // pro konzoli

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!event.getRecipients().contains(onlinePlayer)) {
                continue;
            }
            event.getRecipients().remove(onlinePlayer);

            if (Main.getIgnoreManager().hasIgnored(onlinePlayer, player)) {
                continue;
            }
            onlinePlayer.spigot().sendMessage(toSend);
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().startsWith("/")) {
            CraftChatPlayer player = Main.getCraftChatPlayer(event.getPlayer());
            if (!player.isCheckForSlashMistake()) {
                player.setCheckForSlashMistake(true);
            }
        }
    }
}
