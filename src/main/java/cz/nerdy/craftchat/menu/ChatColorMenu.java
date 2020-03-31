package cz.nerdy.craftchat.menu;

import cz.craftmania.craftcore.spigot.builders.items.ItemBuilder;
import cz.craftmania.craftcore.spigot.inventory.builder.ClickableItem;
import cz.craftmania.craftcore.spigot.inventory.builder.content.InventoryContents;
import cz.craftmania.craftcore.spigot.inventory.builder.content.InventoryProvider;
import cz.nerdy.craftchat.Main;
import cz.nerdy.craftchat.objects.CraftChatPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ChatColorMenu implements InventoryProvider {

    @Override
    public void init(Player player, InventoryContents contents) {

        CraftChatPlayer craftChatPlayer = Main.getCraftChatPlayer(player);

        contents.fillRow(0, ClickableItem.of(new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§c").build(), item -> {}));
        contents.fillRow(5, ClickableItem.of(new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§c").build(), item -> {}));

        contents.set(2, 1, ClickableItem.of(new ItemBuilder(Material.RED_TULIP).setName("§c§lČervená").build(), click -> {
            craftChatPlayer.setChatColor(ChatColor.RED, 12);
            craftChatPlayer.getPlayer().sendMessage("§e§l[*] §eBarva psani nastavena na: §c§lČervená");
            craftChatPlayer.getPlayer().closeInventory();
        }));
        contents.set(2, 2, ClickableItem.of(new ItemBuilder(Material.CYAN_DYE).setName("§3§lTyrkysová").build(), click -> {
            craftChatPlayer.setChatColor(ChatColor.DARK_AQUA, 3);
            craftChatPlayer.getPlayer().sendMessage("§e§l[*] §eBarva psani nastavena na: 3§lTyrkysová");
            craftChatPlayer.getPlayer().closeInventory();
        }));
        contents.set(2, 3, ClickableItem.of(new ItemBuilder(Material.LIME_DYE).setName("§a§lZelená").build(), click -> {
            craftChatPlayer.setChatColor(ChatColor.GREEN, 10);
            craftChatPlayer.getPlayer().sendMessage("§e§l[*] §eBarva psani nastavena na: §a§lZelená");
            craftChatPlayer.getPlayer().closeInventory();
        }));
        contents.set(2, 4, ClickableItem.of(new ItemBuilder(Material.PINK_DYE).setName("§d§lRůžová").build(), click -> {
            craftChatPlayer.setChatColor(ChatColor.LIGHT_PURPLE, 13);
            craftChatPlayer.getPlayer().sendMessage("§e§l[*] §eBarva psani nastavena na: §d§lRůžová");
            craftChatPlayer.getPlayer().closeInventory();
        }));
        contents.set(2, 5, ClickableItem.of(new ItemBuilder(Material.GOLD_INGOT).setName("§6§lZlatá").build(), click -> {
            craftChatPlayer.setChatColor(ChatColor.GOLD, 6);
            craftChatPlayer.getPlayer().sendMessage("§e§l[*] §eBarva psani nastavena na: §6§lZlatá");
            craftChatPlayer.getPlayer().closeInventory();
        }));
        contents.set(2, 6, ClickableItem.of(new ItemBuilder(Material.PURPLE_DYE).setName("§5§lFialová").build(), click -> {
            craftChatPlayer.setChatColor(ChatColor.DARK_PURPLE, 5);
            craftChatPlayer.getPlayer().sendMessage("§e§l[*] §eBarva psani nastavena na: §5§lFialová");
            craftChatPlayer.getPlayer().closeInventory();
        }));
        contents.set(2, 7, ClickableItem.of(new ItemBuilder(Material.GREEN_DYE).setName("§2§lTmavě zelená").build(), click -> {
            craftChatPlayer.setChatColor(ChatColor.DARK_GREEN, 2);
            craftChatPlayer.getPlayer().sendMessage("§e§l[*] §eBarva psani nastavena na: §2§lTmavě zelená");
            craftChatPlayer.getPlayer().closeInventory();
        }));

        contents.set(3, 1, ClickableItem.of(new ItemBuilder(Material.LIGHT_GRAY_DYE).setName("§7§lSvětle šedá").build(), click -> {
            craftChatPlayer.setChatColor(ChatColor.GRAY, 7);
            craftChatPlayer.getPlayer().sendMessage("§e§l[*] §eBarva psani nastavena na: §7§lSvětle šedá");
            craftChatPlayer.getPlayer().closeInventory();
        }));
        contents.set(3, 2, ClickableItem.of(new ItemBuilder(Material.GRAY_DYE).setName("§8Šedá").build(), click -> {
            craftChatPlayer.setChatColor(ChatColor.DARK_GRAY, 8);
            craftChatPlayer.getPlayer().sendMessage("§e§l[*] §eBarva psani nastavena na: §8Šedá");
            craftChatPlayer.getPlayer().closeInventory();
        }));
        contents.set(3, 3, ClickableItem.of(new ItemBuilder(Material.WHITE_DYE).setName("§f§lBílá").build(), click -> {
            craftChatPlayer.setChatColor(ChatColor.WHITE, 15);
            craftChatPlayer.getPlayer().sendMessage("§e§l[*] §eBarva psani nastavena na: §f§lBílá");
            craftChatPlayer.getPlayer().closeInventory();
        }));
        contents.set(3, 4, ClickableItem.of(new ItemBuilder(Material.BLUE_DYE).setName("§9§lModrá").build(), click -> {
            craftChatPlayer.setChatColor(ChatColor.BLUE, 9);
            craftChatPlayer.getPlayer().sendMessage("§e§l[*] §eBarva psani nastavena na: §9§lModrá");
            craftChatPlayer.getPlayer().closeInventory();
        }));
        contents.set(3, 5, ClickableItem.of(new ItemBuilder(Material.LAPIS_LAZULI).setName("§1§lTmavě modrá").build(), click -> {
            craftChatPlayer.setChatColor(ChatColor.DARK_BLUE, 1);
            craftChatPlayer.getPlayer().sendMessage("§e§l[*] §eBarva psani nastavena na: §1§lTmavě modrá");
            craftChatPlayer.getPlayer().closeInventory();
        }));
        contents.set(3, 6, ClickableItem.of(new ItemBuilder(Material.LIGHT_BLUE_DYE).setName("§b§lSvětle modrá").build(), click -> {
            craftChatPlayer.setChatColor(ChatColor.AQUA, 11);
            craftChatPlayer.getPlayer().sendMessage("§e§l[*] §eBarva psani nastavena na: §b§lSvětle modrá");
            craftChatPlayer.getPlayer().closeInventory();
        }));
        contents.set(3, 7, ClickableItem.of(new ItemBuilder(Material.NETHER_WART).setName("§4§lTmavě červená").build(), click -> {
            craftChatPlayer.setChatColor(ChatColor.DARK_RED, 4);
            craftChatPlayer.getPlayer().sendMessage("§e§l[*] §eBarva psani nastavena na: §4§lTmavě červená");
            craftChatPlayer.getPlayer().closeInventory();
        }));

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
