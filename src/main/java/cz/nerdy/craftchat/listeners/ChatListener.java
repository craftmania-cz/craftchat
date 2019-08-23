package cz.nerdy.craftchat.listeners;

import cz.nerdy.craftchat.Main;
import cz.nerdy.craftchat.objects.ChatGroup;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.ArrayUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();
        String message = event.getMessage();

        ChatGroup chatGroup = Main.getInstance().getChatGroups().get(0); // TODO getnout podle hrace (CraftChatPlayer)


        HashMap<String, String> replacements = Main.getInstance().getChatManager().getReplacements();
        if (player.hasPermission("craftchat.replacements")) {
            for (String replacement : replacements.keySet()) {
                if (message.contains(replacement)) {
                    message = message.replaceAll(replacement, replacements.get(replacement));
                }
            }
        }

        TextComponent space = new TextComponent(TextComponent.fromLegacyText(" "));

        ComponentBuilder messageComponentBuilder = new ComponentBuilder("");
        String[] parts = message.split(" ");
        for (String part : parts) {
            TextComponent partComponent = new TextComponent(TextComponent.fromLegacyText(part));
            partComponent.setColor(chatGroup.getChatColor());

            if (part.matches("^(?i)(:item:)$")) {
                ItemStack handItem = player.getEquipment().getItemInMainHand();
                if (handItem.hasItemMeta() && handItem.getItemMeta().hasDisplayName()) {
                    partComponent = new TextComponent(TextComponent.fromLegacyText(part.replaceAll("(?i):item:", handItem.getItemMeta().getDisplayName())));
                    partComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new BaseComponent[]{new TextComponent(Main.getInstance().getPluginCompatibility().convertItemStackToJson(handItem))}));
                    partComponent.setItalic(true);
                }
            }

            messageComponentBuilder.append(partComponent);
            messageComponentBuilder.append(space);
        }


        TextComponent prefixComponent = new TextComponent(TextComponent.fromLegacyText(chatGroup.getPrefix()));
        String prefixTooltip = "";
        for (String line : chatGroup.getPrefixTooltip()) {
            prefixTooltip += line + "§r\n";
        }
        prefixComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(prefixTooltip)));

        TextComponent nameComponent = new TextComponent(TextComponent.fromLegacyText(chatGroup.getNameFormat() + player.getName()));
        nameComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, chatGroup.getNameClickCommand()));
        String nickTooltip = "";
        for (String line : chatGroup.getNameTooltip()) {
            nickTooltip += line + "§r\n";
        }
        nameComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(nickTooltip)));

        TextComponent suffixComponent = new TextComponent(TextComponent.fromLegacyText(chatGroup.getSuffix()));

        BaseComponent[] toSend = {prefixComponent, space, nameComponent, space, suffixComponent, space};
        toSend = ArrayUtils.addAll(toSend, messageComponentBuilder.create());

        event.setFormat(ChatColor.stripColor(chatGroup.getPrefix() + " " + player.getName() + ": " + event.getMessage())); // pro konzoli

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!event.getRecipients().contains(onlinePlayer)) {
                continue;
            }

            event.getRecipients().remove(onlinePlayer);
            onlinePlayer.spigot().sendMessage(toSend);
        }
    }
}
