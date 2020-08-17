package cz.nerdy.craftchat.nms;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.inventory.ItemStack;

public interface PluginCompatibility {

    String convertItemStackToJson(ItemStack itemStack);

    String translateChatColor(String message);

    ChatColor resolveChatColor(String chatColor);
}
