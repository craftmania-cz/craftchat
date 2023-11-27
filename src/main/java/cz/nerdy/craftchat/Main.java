package cz.nerdy.craftchat;

import co.aikar.commands.PaperCommandManager;
import cz.craftmania.craftlibs.utils.ChatInfo;
import cz.nerdy.craftchat.commands.*;
import cz.nerdy.craftchat.listeners.AsyncChatListener;
import cz.nerdy.craftchat.listeners.PlayerListener;
import cz.nerdy.craftchat.listeners.external.LandsChatListener;
import cz.nerdy.craftchat.luckperms.GroupChangeListener;
import cz.nerdy.craftchat.objects.ChatGroup;
import cz.nerdy.craftchat.objects.CraftChatPlayer;
import cz.nerdy.craftchat.utils.Logger;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Main extends JavaPlugin {

    private static @Getter Main instance;
    private List<ChatGroup> chatGroups;
    private static @Getter ChatManager chatManager;
    private static @Getter TagManager tagManager;
    private static @Getter ChatGroupManager chatGroupManager;
    private static @Getter IgnoreManager ignoreManager;
    private static @Getter LuckPerms luckPerms;
    private static @Getter HashMap<Player, CraftChatPlayer> craftChatPlayers;

    public static String SERVER;
    private boolean disabledTags = false;

    // Commands manager
    private PaperCommandManager manager;

    @Override
    public void onEnable() {
        Logger.info("Loading CraftChat v" + this.getDescription().getVersion());
        instance = this;

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            Logger.danger("Na serveru se nenachazi PlacerholderAPI. Plugin nelze spustit.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        //Config
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        // Aikar command manager
        manager = new PaperCommandManager(this);
        manager.enableUnstableAPI("help");

        SERVER = getConfig().getString("server");
        Logger.info("Server zaevidovany jako: " + SERVER);

        // Settings
        chatManager = new ChatManager();
        chatGroupManager = new ChatGroupManager();
        ignoreManager = new IgnoreManager();

        disabledTags = getConfig().getBoolean("settings.disable-tags", false);
        if (!disabledTags) {
            tagManager = new TagManager();
            Logger.info("Aktivace manageru: Tags");
        } else {
            Logger.info("Tagy jsou deaktivovany. Plugin bude pouzivat default prefixy.");
        }

        luckPerms = LuckPermsProvider.get();
        craftChatPlayers = new HashMap<>();

        // Register příkazů
        loadCommands(manager);

        // Events
        //Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new AsyncChatListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        new GroupChangeListener(this, luckPerms);

        if (Objects.equals(SERVER, "survival")) {
            Bukkit.getPluginManager().registerEvents(new LandsChatListener(), this);
        }
    }

    private void loadCommands(PaperCommandManager manager) {
        manager.registerCommand(new ChatColorCommand());
        manager.registerCommand(new IgnoreCommand());
        if (!disabledTags) {
            manager.registerCommand(new TagsCommand());
        }
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static CraftChatPlayer getCraftChatPlayer(Player player) {
        if (craftChatPlayers.containsKey(player)) {
            return craftChatPlayers.get(player);
        }
        return new CraftChatPlayer(player, getChatGroupManager().getDefaultChatGroup());
    }

    public void registerCraftChatPlayer(Player player) {
        CraftChatPlayer craftChatPlayer = new CraftChatPlayer(player);
        if (craftChatPlayer.getChatGroup() == null) {
            ChatInfo.ERROR.send(player, "Nastala chyba při načítání tvého chat profilu. Zkus se přihlásit znovu v případě opakování nahlaš toto na našem Discord serveru.");
            craftChatPlayer = new CraftChatPlayer(player, getChatGroupManager().getDefaultChatGroup());
        }
        craftChatPlayers.put(player, craftChatPlayer);
    }

    public void unregisterCraftChatPlayer(Player player) {
        craftChatPlayers.remove(player);
    }

    public void updatePlayer(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            Logger.danger("Update dat hrace skrz UUID: Hrac je null -> UUID: " + uuid);
            return;
        }
        Logger.info("Update dat hrace " + player.getName() + " (" + uuid + ").");
        this.unregisterCraftChatPlayer(player);
        this.registerCraftChatPlayer(player);
    }

    /**
     * Creates a tag resolver capable of resolving PlaceholderAPI tags for a given player.
     *
     * @param player the player
     * @return the tag resolver
     */
    public @NotNull TagResolver papiTag(final @NotNull Player player) {
        return TagResolver.resolver("papi", (argumentQueue, context) -> {
            // Get the string placeholder that they want to use.
            final String papiPlaceholder = argumentQueue.popOr("papi tag requires an argument").value();

            // Then get PAPI to parse the placeholder for the given player.
            final String parsedPlaceholder = PlaceholderAPI.setPlaceholders(player, '%' + papiPlaceholder + '%');
            System.out.println(parsedPlaceholder);

            // We need to turn this ugly legacy string into a nice component.
            final Component componentPlaceholder = LegacyComponentSerializer.legacySection().deserialize(parsedPlaceholder);

            // Finally, return the tag instance to insert the placeholder!
            return Tag.selfClosingInserting(componentPlaceholder);
        });
    }

    /**
     * True když jsou tagy deaktivovány.
     * @return {@link Boolean}
     */
    public boolean isDisabledTags() {
        return disabledTags;
    }
}
