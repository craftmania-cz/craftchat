package cz.nerdy.craftchat.nms;

import org.bukkit.inventory.ItemStack;

public interface PluginCompatibility {

    String convertItemStackToJson(ItemStack itemStack);
}
