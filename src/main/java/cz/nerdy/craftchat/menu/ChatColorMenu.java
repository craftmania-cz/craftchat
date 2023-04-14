package cz.nerdy.craftchat.menu;

import cz.craftmania.craftcore.builders.items.ItemBuilder;
import cz.craftmania.craftcore.inventory.builder.ClickableItem;
import cz.craftmania.craftcore.inventory.builder.content.InventoryContents;
import cz.craftmania.craftcore.inventory.builder.content.InventoryProvider;
import cz.craftmania.craftlibs.utils.ChatInfo;
import cz.nerdy.craftchat.Main;
import cz.nerdy.craftchat.objects.CraftChatPlayer;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ChatColorMenu implements InventoryProvider {

    @Override
    public void init(Player player, InventoryContents contents) {

        CraftChatPlayer craftChatPlayer = Main.getCraftChatPlayer(player);

        contents.fillRow(0, ClickableItem.of(new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§c").build(), item -> {}));
        contents.fillRow(5, ClickableItem.of(new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§c").build(), item -> {}));

        contents.set(2, 1, ClickableItem.of(new ItemBuilder(Material.RED_TULIP).setName("§c§lČervená").build(), click -> {
            craftChatPlayer.setChatColor(NamedTextColor.RED, 12);
            craftChatPlayer.getPlayer().closeInventory();
            ChatInfo.INFO.send(player, "Barva psani nastavena na: §c§lČervená");
        }));
        contents.set(2, 2, ClickableItem.of(new ItemBuilder(Material.CYAN_DYE).setName("§3§lTyrkysová").build(), click -> {
            craftChatPlayer.setChatColor(NamedTextColor.DARK_AQUA, 3);
            craftChatPlayer.getPlayer().closeInventory();
            ChatInfo.INFO.send(player, "Barva psani nastavena na: 3§lTyrkysová");
        }));
        contents.set(2, 3, ClickableItem.of(new ItemBuilder(Material.LIME_DYE).setName("§a§lZelená").build(), click -> {
            craftChatPlayer.setChatColor(NamedTextColor.GREEN, 10);
            craftChatPlayer.getPlayer().closeInventory();
            ChatInfo.INFO.send(player, "Barva psani nastavena na: §a§lZelená");
        }));
        contents.set(2, 4, ClickableItem.of(new ItemBuilder(Material.PINK_DYE).setName("§d§lRůžová").build(), click -> {
            craftChatPlayer.setChatColor(NamedTextColor.LIGHT_PURPLE, 13);
            craftChatPlayer.getPlayer().closeInventory();
            ChatInfo.INFO.send(player, "Barva psani nastavena na: §d§lRůžová");
        }));
        contents.set(2, 5, ClickableItem.of(new ItemBuilder(Material.GOLD_INGOT).setName("§6§lZlatá").build(), click -> {
            craftChatPlayer.setChatColor(NamedTextColor.GOLD, 6);
            craftChatPlayer.getPlayer().closeInventory();
            ChatInfo.INFO.send(player, "Barva psani nastavena na: §6§lZlatá");
        }));
        contents.set(2, 6, ClickableItem.of(new ItemBuilder(Material.PURPLE_DYE).setName("§5§lFialová").build(), click -> {
            craftChatPlayer.setChatColor(NamedTextColor.DARK_PURPLE, 5);
            craftChatPlayer.getPlayer().closeInventory();
            ChatInfo.INFO.send(player, "Barva psani nastavena na: §5§lFialová");
        }));
        contents.set(2, 7, ClickableItem.of(new ItemBuilder(Material.GREEN_DYE).setName("§2§lTmavě zelená").build(), click -> {
            craftChatPlayer.setChatColor(NamedTextColor.DARK_GREEN, 2);
            craftChatPlayer.getPlayer().closeInventory();
            ChatInfo.INFO.send(player, "Barva psani nastavena na: §2§lTmavě zelená");
        }));

        contents.set(3, 1, ClickableItem.of(new ItemBuilder(Material.LIGHT_GRAY_DYE).setName("§7§lSvětle šedá").build(), click -> {
            craftChatPlayer.setChatColor(NamedTextColor.GRAY, 7);
            craftChatPlayer.getPlayer().closeInventory();
            ChatInfo.INFO.send(player, "Barva psani nastavena na: §7§lSvětle šedá");
        }));
        contents.set(3, 2, ClickableItem.of(new ItemBuilder(Material.GRAY_DYE).setName("§8Šedá").build(), click -> {
            craftChatPlayer.setChatColor(NamedTextColor.DARK_GRAY, 8);
            craftChatPlayer.getPlayer().closeInventory();
            ChatInfo.INFO.send(player, "Barva psani nastavena na: §8Šedá");
        }));
        contents.set(3, 3, ClickableItem.of(new ItemBuilder(Material.WHITE_DYE).setName("§f§lBílá").build(), click -> {
            craftChatPlayer.setChatColor(NamedTextColor.WHITE, 15);
            craftChatPlayer.getPlayer().closeInventory();
            ChatInfo.INFO.send(player, "Barva psani nastavena na: §f§lBílá");
        }));
        contents.set(3, 4, ClickableItem.of(new ItemBuilder(Material.BLUE_DYE).setName("§9§lModrá").build(), click -> {
            craftChatPlayer.setChatColor(NamedTextColor.BLUE, 9);
            craftChatPlayer.getPlayer().closeInventory();
            ChatInfo.INFO.send(player, "Barva psani nastavena na: §9§lModrá");
        }));
        contents.set(3, 5, ClickableItem.of(new ItemBuilder(Material.LAPIS_LAZULI).setName("§1§lTmavě modrá").build(), click -> {
            craftChatPlayer.setChatColor(NamedTextColor.DARK_BLUE, 1);
            craftChatPlayer.getPlayer().closeInventory();
            ChatInfo.INFO.send(player, "Barva psani nastavena na: §1§lTmavě modrá");
        }));
        contents.set(3, 6, ClickableItem.of(new ItemBuilder(Material.LIGHT_BLUE_DYE).setName("§b§lSvětle modrá").build(), click -> {
            craftChatPlayer.setChatColor(NamedTextColor.AQUA, 11);
            craftChatPlayer.getPlayer().closeInventory();
            ChatInfo.INFO.send(player, "Barva psani nastavena na: §b§lSvětle modrá");
        }));
        contents.set(3, 7, ClickableItem.of(new ItemBuilder(Material.NETHER_WART).setName("§4§lTmavě červená").build(), click -> {
            craftChatPlayer.setChatColor(NamedTextColor.DARK_RED, 4);
            craftChatPlayer.getPlayer().closeInventory();
            ChatInfo.INFO.send(player, "Barva psani nastavena na: §4§lTmavě červená");
        }));

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
