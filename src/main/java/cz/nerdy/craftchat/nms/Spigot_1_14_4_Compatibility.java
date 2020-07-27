package cz.nerdy.craftchat.nms;

import cz.nerdy.craftchat.utils.Colors;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class Spigot_1_14_4_Compatibility implements PluginCompatibility {

    @Override
    public String convertItemStackToJson(ItemStack itemStack) {
        net.minecraft.server.v1_14_R1.ItemStack nmsItemStact = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = new NBTTagCompound();
        compound = nmsItemStact.save(compound);
        return compound.toString();
    }

    @Override
    public String translateChatColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @Override
    public ChatColor resolveChatColor(String chatColor) {
        try {
            return ChatColor.valueOf(chatColor);
        } catch (Exception e) {
            return ChatColor.WHITE;
        }
    }
}
