package cz.nerdy.craftchat.utils;


import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Colors {

    public static NamedTextColor resolveColorById(int id) {
        return switch (id) {
            case 1 -> NamedTextColor.DARK_BLUE;
            case 2 -> NamedTextColor.DARK_GREEN;
            case 3 -> NamedTextColor.DARK_AQUA;
            case 4 -> NamedTextColor.DARK_RED;
            case 5 -> NamedTextColor.DARK_PURPLE;
            case 6 -> NamedTextColor.GOLD;
            case 7 -> NamedTextColor.GRAY;
            case 8 -> NamedTextColor.DARK_GRAY;
            case 9 -> NamedTextColor.BLUE;
            case 10 -> NamedTextColor.GREEN;
            case 11 -> NamedTextColor.AQUA;
            case 12 -> NamedTextColor.RED;
            case 13 -> NamedTextColor.LIGHT_PURPLE;
            default -> NamedTextColor.WHITE;
        };
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
