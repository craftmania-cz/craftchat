package cz.nerdy.craftchat.listeners;

import cz.nerdy.craftchat.Main;
import cz.nerdy.craftchat.objects.CraftChatPlayer;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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

        String plainTextMessage = LegacyComponentSerializer.legacyAmpersand().serialize(event.message());
        System.out.println("plain " + plainTextMessage);

        // Formatovani dle groups
        CraftChatPlayer craftChatPlayer = Main.getCraftChatPlayer(event.getPlayer());

        Component prefix = craftChatPlayer.getPrefix();
        Component nameFormat = craftChatPlayer.getNameWithHover(); //MiniMessage.miniMessage().deserialize(PlaceholderAPI.setPlaceholders(event.getPlayer(), craftChatPlayer.getChatGroup().getNameFormat()), Placeholder.component("player", event.getPlayer().name()));
        //TODO Colors

        Component originalMessageAsComponent = LegacyComponentSerializer.legacyAmpersand()
                .deserialize(plainTextMessage)
                .color(craftChatPlayer.getSelectedChatColor());


        event.message(originalMessageAsComponent);
        event.renderer(ChatRenderer.viewerUnaware((source, sourceDisplayName, msg) -> {
            return MiniMessage.miniMessage().deserialize(
                    PlaceholderAPI.setPlaceholders(
                            event.getPlayer(),
                            craftChatPlayer.getChatGroup().getMessageFormat()
                    ),
                    Placeholder.component("prefix", prefix),
                    Placeholder.component("name", nameFormat),
                    Placeholder.component("message", originalMessageAsComponent)
            );
        }));
    }
}
