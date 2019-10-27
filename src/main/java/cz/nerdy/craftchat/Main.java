package cz.nerdy.craftchat;

import cz.nerdy.craftchat.commands.PlayerCommands;
import cz.nerdy.craftchat.listeners.ChatListener;
import cz.nerdy.craftchat.nms.PluginCompatibility;
import cz.nerdy.craftchat.nms.Spigot_1_14_4_Compatibility;
import cz.nerdy.craftchat.objects.ChatGroup;
import cz.nerdy.craftchat.objects.CraftChatPlayer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends JavaPlugin {

    private static Main instance;
    private List<ChatGroup> chatGroups;
    private PluginCompatibility pluginCompatibility;
    private ChatManager chatManager;
    private TagManager tagManager;
    private ChatGroupManager chatGroupManager;

    private HashMap<Player, CraftChatPlayer> craftChatPlayers;

    public static String SERVER;

    @Override
    public void onEnable() {
        System.out.println("Loading CraftChat v" + this.getDescription().getVersion());
        instance = this;

        if (!setupCompatibility()) {
            System.out.println("Nepodporovana verze serveru!");
            this.getPluginLoader().disablePlugin(this);
        }

        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        PlayerCommands playerCommands = new PlayerCommands();
        getCommand("tags").setExecutor(playerCommands);

        SERVER = getConfig().getString("server");

        this.chatManager = new ChatManager();
        this.tagManager = new TagManager();
        this.chatGroupManager = new ChatGroupManager();

        this.craftChatPlayers = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
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
            case "v1_14_R1":
                this.pluginCompatibility = new Spigot_1_14_4_Compatibility();
                break;
        }
        return this.pluginCompatibility != null;

    }

    public ChatManager getChatManager() {
        return chatManager;
    }

    public TagManager getTagManager() {
        return tagManager;
    }

    public ChatGroupManager getChatGroupManager() {
        return chatGroupManager;
    }

    public CraftChatPlayer getCraftChatPlayer(Player player) {
        return this.craftChatPlayers.getOrDefault(player, null);
    }

    public void registerCraftChatPlayer(Player player) {
        CraftChatPlayer craftChatPlayer = new CraftChatPlayer(player);
        this.craftChatPlayers.put(player, craftChatPlayer);
    }

    public void unregisterCraftChatPlayer(Player player) {
        this.craftChatPlayers.remove(player);
    }
}
