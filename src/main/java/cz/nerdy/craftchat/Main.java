package cz.nerdy.craftchat;

import cz.nerdy.craftchat.listeners.ChatListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {

        System.out.println("Loading CraftChat v" + this.getDescription().getVersion());
        instance = this;

        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);

    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static Main getInstance() {
        return instance;
    }
}
