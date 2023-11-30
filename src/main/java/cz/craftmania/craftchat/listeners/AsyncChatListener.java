package cz.craftmania.craftchat.listeners;

import cz.craftmania.craftchat.objects.CraftChatPlayer;
import cz.craftmania.craftchat.Main;
import cz.craftmania.craftchat.objects.Emote;
import cz.craftmania.craftlibs.utils.ChatInfo;
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

        final @NotNull Player player = event.getPlayer();

        // Zpráva bez formátování
        String plainTextMessage = LegacyComponentSerializer.legacyAmpersand().serialize(event.message());

        // Blokování zpráv s blokovanými texturami
        for (String blockedTexture : Main.getEmoteManager().getBlockedTextures()) {
            if (plainTextMessage.contains(blockedTexture)) {
                event.setCancelled(true);
                ChatInfo.ERROR.send(player, "Tvoje zpráva obsahuje zablokovanou texturu!");
                return;
            }
        }

        // Vytváření tagů (pokud je aktivní)
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

        // Již vygenerované componenty s formátováním
        Component prefix = craftChatPlayer.getPrefix();
        Component nameFormat = craftChatPlayer.getNameWithHover();

        // Colorizace zprávy + vybraný chatcolor
        Component originalMessageAsComponent = LegacyComponentSerializer.legacyAmpersand()
                .deserialize(plainTextMessage)
                .color(craftChatPlayer.getSelectedChatColor());

        // Detekce emotes a nahrazení componentou
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

        // Nastavení formátu pro konzoli
        event.message(originalMessageAsComponent);

        // Nastavení renderu do chatu
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
