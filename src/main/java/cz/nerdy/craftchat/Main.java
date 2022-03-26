package cz.nerdy.craftchat;

import co.aikar.commands.PaperCommandManager;
import cz.nerdy.craftchat.commands.*;
import cz.nerdy.craftchat.listeners.ChatListener;
import cz.nerdy.craftchat.listeners.PlayerListener;
import cz.nerdy.craftchat.luckperms.GroupChangeListener;
import cz.nerdy.craftchat.nms.*;
import cz.nerdy.craftchat.objects.ChatGroup;
import cz.nerdy.craftchat.objects.CraftChatPlayer;
import cz.nerdy.craftchat.utils.Logger;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Main extends JavaPlugin {

    private static Main instance;
    private List<ChatGroup> chatGroups;
    private PluginCompatibility pluginCompatibility;
    private static ChatManager chatManager;
    private static TagManager tagManager;
    private static ChatGroupManager chatGroupManager;
    private static IgnoreManager ignoreManager;

    private static LuckPerms luckPerms;

    private static HashMap<Player, CraftChatPlayer> craftChatPlayers;

    public static String SERVER;
    private boolean disabledTags = false;

    // Commands manager
    private PaperCommandManager manager;

    @Override
    public void onEnable() {
        Logger.info("Loading CraftChat v" + this.getDescription().getVersion());
        instance = this;

        setupCompatibility();

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
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        new GroupChangeListener(this, luckPerms);
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

    public static Main getInstance() {
        return instance;
    }

    public PluginCompatibility getPluginCompatibility() {
        return pluginCompatibility;
    }

    private void setupCompatibility() {
        String serverVersion = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        Logger.info("Detekovana verze serveru: " + serverVersion);
        this.pluginCompatibility = new Spigot_1_17_0_Compatibility();
    }

    public static ChatManager getChatManager() {
        return chatManager;
    }

    public static TagManager getTagManager() {
        return tagManager;
    }

    public static ChatGroupManager getChatGroupManager() {
        return chatGroupManager;
    }

    public static IgnoreManager getIgnoreManager() {
        return ignoreManager;
    }

    @Nullable
    public static CraftChatPlayer getCraftChatPlayer(Player player) {
        return craftChatPlayers.getOrDefault(player, null);
    }

    public void registerCraftChatPlayer(Player player) {
        CraftChatPlayer craftChatPlayer = new CraftChatPlayer(player);
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
     * True když jsou tagy deaktivovány.
     * @return {@link Boolean}
     */
    public boolean isDisabledTags() {
        return disabledTags;
    }
}
