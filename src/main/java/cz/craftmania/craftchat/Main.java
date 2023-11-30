package cz.craftmania.craftchat;

import co.aikar.commands.PaperCommandManager;
import cz.craftmania.craftchat.commands.ChatColorCommand;
import cz.craftmania.craftchat.commands.TagsCommand;
import cz.craftmania.craftchat.listeners.AsyncChatListener;
import cz.craftmania.craftchat.listeners.PlayerListener;
import cz.craftmania.craftchat.listeners.external.LandsChatListener;
import cz.craftmania.craftchat.managers.ChatGroupManager;
import cz.craftmania.craftchat.managers.EmoteManager;
import cz.craftmania.craftchat.managers.TagManager;
import cz.craftmania.craftchat.objects.ChatGroup;
import cz.craftmania.craftchat.objects.CraftChatPlayer;
import cz.craftmania.craftchat.utils.Logger;
import cz.craftmania.craftchat.utils.configs.Config;
import cz.craftmania.craftchat.utils.configs.ConfigAPI;
import cz.craftmania.craftlibs.utils.ChatInfo;
import cz.craftmania.craftchat.luckperms.GroupChangeListener;
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
    private static @Getter TagManager tagManager;
    private static @Getter ChatGroupManager chatGroupManager;
    private static @Getter EmoteManager emoteManager;
    private static @Getter LuckPerms luckPerms;
    private static @Getter HashMap<Player, CraftChatPlayer> craftChatPlayers;
    private @Getter ConfigAPI configAPI;

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

        // Config
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        // Nacteni config souboru
        configAPI = new ConfigAPI(this);
        loadConfiguration();

        // Aikar command manager
        manager = new PaperCommandManager(this);
        manager.enableUnstableAPI("help");

        SERVER = getConfig().getString("server");
        Logger.info("Server zaevidovany jako: " + SERVER);

        // Settings
        chatGroupManager = new ChatGroupManager();
        emoteManager = new EmoteManager();
        emoteManager.loadEmotes();

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
     * True když jsou tagy deaktivovány.
     * @return {@link Boolean}
     */
    public boolean isDisabledTags() {
        return disabledTags;
    }

    public Config getEmotesConfig() {
        return configAPI.getConfig("emotes");
    }

    private void loadConfiguration() {
        Config questFile = new Config(this.configAPI, "emotes");
        configAPI.registerConfig(questFile);
    }
}
