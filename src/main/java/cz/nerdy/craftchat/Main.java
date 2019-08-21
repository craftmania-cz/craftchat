package cz.nerdy.craftchat;

import cz.nerdy.craftchat.listeners.ChatListener;
import cz.nerdy.craftchat.objects.ChatGroup;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    private static Main instance;
    private List<ChatGroup> chatGroups;

    @Override
    public void onEnable() {
        System.out.println("Loading CraftChat v" + this.getDescription().getVersion());
        instance = this;

        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        this.chatGroups = new ArrayList<>();
        this.loadChatGroups();

        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static Main getInstance() {
        return instance;
    }

    public List<ChatGroup> getChatGroups() {
        return chatGroups;
    }

    private void loadChatGroups() {

        ConfigurationSection configurationSection = this.getConfig().getConfigurationSection("groups");
        for (String key : configurationSection.getKeys(false)) {
            ConfigurationSection groupSection = configurationSection.getConfigurationSection(key);
            ChatGroup chatGroup = new ChatGroup(
                    groupSection.getInt("priority"),
                    groupSection.getString("prefix"),
                    groupSection.getString("suffix"),
                    groupSection.getString("name_format"),
                    groupSection.getString("chat_color"),
                    groupSection.getStringList("prefix_tooltip"),
                    groupSection.getStringList("name_tooltip"),
                    groupSection.getString("name_click_command")
            );
            this.chatGroups.add(chatGroup);
            System.out.println("Group " + groupSection.getName() + " loaded (prefix=" + chatGroup.getPrefix() + ",suffix=" + chatGroup.getSuffix() + ")");
        }

    }
}
