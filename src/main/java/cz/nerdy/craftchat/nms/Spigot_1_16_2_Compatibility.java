package cz.nerdy.craftchat.nms;

import cz.nerdy.craftchat.utils.Colors;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R2.NBTTagCompound;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class Spigot_1_16_2_Compatibility implements PluginCompatibility {

    @Override
    public String convertItemStackToJson(ItemStack itemStack){
        net.minecraft.server.v1_16_R2.ItemStack nmsItemStact = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = new NBTTagCompound();
        compound = nmsItemStact.save(compound);
        return compound.toString();
    }

    @Override
    public String translateChatColor(String message) {
        return Colors.translateRGB(message);
    }

    @Override
    public ChatColor resolveChatColor(String chatColor) {
        return ChatColor.of(chatColor);
    }
}
