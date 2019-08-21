package cz.nerdy.craftchat.listeners;

import cz.nerdy.craftchat.Main;
import cz.nerdy.craftchat.objects.ChatGroup;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if(event.isCancelled()){
            return;
        }

        Player player = event.getPlayer();

        ChatGroup chatGroup = Main.getInstance().getChatGroups().get(0); // TODO getnout podle hrace (CraftChatPlayer)

        TextComponent space = new TextComponent(TextComponent.fromLegacyText(" "));

        TextComponent prefixComponent = new TextComponent(TextComponent.fromLegacyText(chatGroup.getPrefix()));
        String prefixTooltip = "";
        for (String line : chatGroup.getPrefixTooltip()){
            prefixTooltip += line + "\n";
        }
        prefixComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(prefixTooltip)));

        TextComponent nameComponent = new TextComponent(TextComponent.fromLegacyText(chatGroup.getNameFormat() + player.getName()));
        nameComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, chatGroup.getNameClickCommand()));
        String nickTooltip = "";
        for (String line : chatGroup.getNameTooltip()){
            nickTooltip += line + "\n";
        }
        nameComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(nickTooltip)));

        TextComponent suffixComponent = new TextComponent(TextComponent.fromLegacyText(chatGroup.getSuffix()));

        TextComponent messageComponent = new TextComponent(TextComponent.fromLegacyText(event.getMessage()));

        BaseComponent[] toSend = { prefixComponent, space, nameComponent, space, suffixComponent, space, messageComponent};

        event.setFormat(chatGroup.getPrefix() + " " + player.getName() + ": " + event.getMessage()); // pro konzoli

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()){
            if (!event.getRecipients().contains(onlinePlayer)) {
                continue;
            }

            event.getRecipients().remove(onlinePlayer);
            onlinePlayer.spigot().sendMessage(toSend);
        }
     }
}
