package cz.nerdy.craftchat.utils;


import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Colors {

    public static ChatColor resolveColorById(int id) {
        switch (id) {
            case 1:
                return ChatColor.DARK_BLUE;
            case 2:
                return ChatColor.DARK_GREEN;
            case 3:
                return ChatColor.DARK_AQUA;
            case 4:
                return ChatColor.DARK_RED;
            case 5:
                return ChatColor.DARK_PURPLE;
            case 6:
                return ChatColor.GOLD;
            case 7:
                return ChatColor.GRAY;
            case 8:
                return ChatColor.DARK_GRAY;
            case 9:
                return ChatColor.BLUE;
            case 10:
                return ChatColor.GREEN;
            case 11:
                return ChatColor.AQUA;
            case 12:
                return ChatColor.RED;
            case 13:
                return ChatColor.LIGHT_PURPLE;
            case 14:
            case 15:
                return ChatColor.WHITE;
            default:
                return ChatColor.WHITE;
        }
    }

    private static final Pattern pattern = Pattern.compile("(?<!\\\\)(&#[a-fA-F0-9]{6})");

    public static String translateRGB(String message) {
        Matcher matcher = pattern.matcher(message);
        message = ChatColor.translateAlternateColorCodes('&', message);
        String translatedMessage = message;
        while (matcher.find()) {
            String color = matcher.group();
            try {
                translatedMessage = translatedMessage.replace(color, "" + ChatColor.of(color.substring(1)));
            } catch (IllegalArgumentException ignored) {
            }
        }
        return translatedMessage;
    }
}
