package cz.nerdy.craftchat.utils;

import cz.craftmania.crafteconomy.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Logger {

    public static void info(String s) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[CraftChat] " + ChatColor.WHITE + s);
    }

    public static void danger(String s) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[CraftChat] " + ChatColor.RED + s);
    }

    public static void success(String s) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[CraftChat] " + ChatColor.GREEN + s);
    }

    public static void debug(String s) {
        if(Main.getInstance().isDebugActive()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[DEBUG - CraftChat] " + ChatColor.WHITE + s);
        }
    }
}
