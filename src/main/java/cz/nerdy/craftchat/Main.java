package cz.nerdy.craftchat;

import cz.nerdy.craftchat.commands.IgnoreCommand;
import cz.nerdy.craftchat.commands.TagsCommand;
import cz.nerdy.craftchat.listeners.ChatListener;
import cz.nerdy.craftchat.listeners.PlayerListener;
import cz.nerdy.craftchat.nms.PluginCompatibility;
import cz.nerdy.craftchat.nms.Spigot_1_14_4_Compatibility;
import cz.nerdy.craftchat.objects.ChatGroup;
import cz.nerdy.craftchat.objects.CraftChatPlayer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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

        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        TagsCommand playerCommands = new TagsCommand();
        getCommand("tags").setExecutor(playerCommands);
        getCommand("ignore").setExecutor(new IgnoreCommand());

        SERVER = getConfig().getString("server");

        chatManager = new ChatManager();
        tagManager = new TagManager();
        chatGroupManager = new ChatGroupManager();
        ignoreManager = new IgnoreManager();

        luckPerms = LuckPermsProvider.get();

        craftChatPlayers = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
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

    public void updatePlayer(UUID uuid){
        Player player = Bukkit.getPlayer(uuid);
        if(player == null) {
            System.out.println("Player not found .. uuid: "  + uuid.toString());
            return;
        }
        this.unregisterCraftChatPlayer(player);
        this.registerCraftChatPlayer(player);
    }
}
