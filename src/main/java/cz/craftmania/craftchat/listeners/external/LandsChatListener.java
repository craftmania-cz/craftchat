package cz.craftmania.craftchat.listeners.external;

import cz.craftmania.craftlibs.CraftLibs;
import cz.craftmania.craftchat.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Debilní funkce roku, když hráč napíše /lands chat tak se uloží stav.
 * Přepsat v budoucnosti aby to nebylo L.
 */
public class LandsChatListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLandsChatCommand(PlayerCommandPreprocessEvent event) {
        if (!event.getMessage().equals("/lands chat")
                && !event.getMessage().equals("/land chat")
                && !event.getMessage().equals("/nation chat")
                && !event.getMessage().equals("/nations chat")
                && !event.getMessage().equals("/chat")
                && !event.getMessage().equals("/lands:lands chat")
                && !event.getMessage().equals("/lands:land chat")
                && !event.getMessage().equals("/lands:nation chat")
                && !event.getMessage().equals("/lands:nations chat")
                && !event.getMessage().equals("/lands:chat")
        ) {
            return;
        }
        Player player = event.getPlayer();
        boolean activeLandChat = Main.getChatManager().getActiveLandChat().contains(player);
        if (activeLandChat) {
            Main.getChatManager().getActiveLandChat().remove(player);
            CraftLibs.getSqlManager()
                    .query("UPDATE minigames.player_settings SET lands_chat = 0 WHERE Nick = ?", player.getName());
            return;
        }
        Main.getChatManager().getActiveLandChat().add(player);
        CraftLibs.getSqlManager()
                .query("UPDATE minigames.player_settings SET lands_chat = 1 WHERE Nick = ?", player.getName());
    }

    @EventHandler
    public void onJoinWithLandChat(PlayerJoinEvent event) {
        CraftLibs.getSqlManager().query("SELECT lands_chat FROM minigames.player_settings WHERE Nick = ?", event.getPlayer().getName()).thenAcceptAsync((dbRows -> {
            int enabledLandChat = dbRows.get(0).getInt("lands_chat");
            if (enabledLandChat == 1) {
                Main.getChatManager().getActiveLandChat().add(event.getPlayer());
            }
        }));
    }
}
