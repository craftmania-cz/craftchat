package cz.nerdy.craftchat;

import co.aikar.commands.PaperCommandManager;
import cz.nerdy.craftchat.commands.*;
import cz.nerdy.craftchat.listeners.ChatListener;
import cz.nerdy.craftchat.listeners.PlayerListener;
import cz.nerdy.craftchat.luckperms.GroupChangeListener;
import cz.nerdy.craftchat.nms.*;
import cz.nerdy.craftchat.objects.ChatGroup;
import cz.nerdy.craftchat.objects.CraftChatPlayer;
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
        System.out.println("Loading CraftChat v" + this.getDescription().getVersion());
        instance = this;

        if (!setupCompatibility()) {
            System.out.println("Nepodporovana verze serveru!");
            this.getPluginLoader().disablePlugin(this);
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            throw new RuntimeException("Could not find PlaceholderAPI!! Plugin can not work without it!");
        }

        //Config
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        // Aikar command manager
        manager = new PaperCommandManager(this);
        manager.enableUnstableAPI("help");

        // Register příkazů
        loadCommands(manager);

        SERVER = getConfig().getString("server");
        disabledTags = getConfig().getBoolean("settings.disable-tags", false);

        chatManager = new ChatManager();
        chatGroupManager = new ChatGroupManager();
        ignoreManager = new IgnoreManager();
        if (!disabledTags) {
            tagManager = new TagManager();
        }

        luckPerms = LuckPermsProvider.get();

        craftChatPlayers = new HashMap<>();

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

    private boolean setupCompatibility() {
        String s;
        try {
            s = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        } catch (ArrayIndexOutOfBoundsException ex) {
            return false;
        }
        switch (s) {
            case "v1_16_R3":
                this.pluginCompatibility = new Spigot_1_16_3_Compatibility();
                break;
            case "v1_17_R1":
            case "v1_18_R1":
                this.pluginCompatibility = new Spigot_1_17_0_Compatibility();
                break;
        }
        return this.pluginCompatibility != null;
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
            System.out.println("[CraftChat] updating player: Player not found .. uuid: " + uuid.toString());
            return;
        }
        System.out.println("[CraftChat] updating player: uuid: " + uuid.toString());
        this.unregisterCraftChatPlayer(player);
        this.registerCraftChatPlayer(player);
    }

    public boolean isDisabledTags() {
        return disabledTags;
    }
}
