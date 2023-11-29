package cz.craftmania.craftchat.listeners;

import cz.craftmania.craftchat.objects.CraftChatPlayer;
import cz.craftmania.craftchat.Main;
import cz.craftmania.craftchat.objects.Emote;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class AsyncChatListener implements Listener {

    private final Main plugin;

    public AsyncChatListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(@NotNull AsyncChatEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();

        String plainTextMessage = LegacyComponentSerializer.legacyAmpersand().serialize(event.message());
        //System.out.println("plain " + plainTextMessage);

        if (!Main.getInstance().isDisabledTags() && Main.getTagManager().isCreatingTag(player)) {
            if (plainTextMessage.equalsIgnoreCase("stop")) {
                Main.getTagManager().stopTagCreation(player);
            } else {
                Main.getTagManager().createTag(player, plainTextMessage);
            }
            event.setCancelled(true);
            return;
        }

        // Formatovani dle groups
        CraftChatPlayer craftChatPlayer = Main.getCraftChatPlayer(event.getPlayer());

        Component prefix = craftChatPlayer.getPrefix();
        Component nameFormat = craftChatPlayer.getNameWithHover();

        Component originalMessageAsComponent = LegacyComponentSerializer.legacyAmpersand()
                .deserialize(plainTextMessage)
                .color(craftChatPlayer.getSelectedChatColor());

        for (Emote emote : Main.getEmoteManager().getEmotes()) {
            if (plainTextMessage.contains(emote.getToReplace())) {
                if (emote.getType() == null || emote.getReplaceWith() == null) {
                    continue;
                }
                if (emote.getPermission() != null && !event.getPlayer().hasPermission(emote.getPermission())) {
                    continue;
                }
                originalMessageAsComponent = originalMessageAsComponent.replaceText(builder -> {
                    builder.matchLiteral(emote.getToReplace());
                    builder.replacement(
                            Component.text(emote.getReplaceWith())
                                    .hoverEvent(HoverEvent.showText(MiniMessage.miniMessage().deserialize("<gray>" + emote.getName() + "</gray> <white>" + emote.getToReplace() + "</white>")))
                                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, emote.getToReplace()))
                    );
                });
            }
        }


        event.message(originalMessageAsComponent);
        Component finalOriginalMessageAsComponent = originalMessageAsComponent;
        event.renderer(ChatRenderer.viewerUnaware((source, sourceDisplayName, msg) -> {
            return MiniMessage.miniMessage().deserialize(
                    PlaceholderAPI.setPlaceholders(
                            event.getPlayer(),
                            craftChatPlayer.getChatGroup().getMessageFormat()
                    ),
                    Placeholder.component("prefix", prefix),
                    Placeholder.component("name", nameFormat),
                    Placeholder.component("message", finalOriginalMessageAsComponent)
            );
        }));
    }
}
