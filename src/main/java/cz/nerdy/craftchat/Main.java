package cz.nerdy.craftchat;

import cz.nerdy.craftchat.listeners.ChatListener;
import cz.nerdy.craftchat.nms.PluginCompatibility;
import cz.nerdy.craftchat.nms.Spigot_1_14_4_Compatibility;
import cz.nerdy.craftchat.objects.ChatGroup;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    private static Main instance;
    private List<ChatGroup> chatGroups;
    private PluginCompatibility pluginCompatibility;

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

    public PluginCompatibility getPluginCompatibility(){
        return pluginCompatibility;
    }

    private boolean setupCompatibility(){
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
