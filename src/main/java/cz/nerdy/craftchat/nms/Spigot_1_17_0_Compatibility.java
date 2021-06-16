package cz.nerdy.craftchat.nms;

import cz.nerdy.craftchat.utils.Colors;
import net.md_5.bungee.api.ChatColor;

public class Spigot_1_17_0_Compatibility implements PluginCompatibility {

    @Override
    public String translateChatColor(String message) {
        return Colors.translateRGB(message);
    }

    @Override
    public ChatColor resolveChatColor(String chatColor) {
        return ChatColor.of(chatColor);
    }
}
