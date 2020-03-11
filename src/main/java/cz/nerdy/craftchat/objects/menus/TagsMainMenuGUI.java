package cz.nerdy.craftchat.objects.menus;

import cz.craftmania.craftcore.spigot.builders.items.ItemBuilder;
import cz.craftmania.craftcore.spigot.inventory.builder.ClickableItem;
import cz.craftmania.craftcore.spigot.inventory.builder.content.InventoryContents;
import cz.craftmania.craftcore.spigot.inventory.builder.content.InventoryProvider;
import cz.craftmania.craftcore.spigot.inventory.builder.content.Pagination;
import cz.craftmania.craftcore.spigot.inventory.builder.content.SlotIterator;
import cz.craftmania.craftcore.spigot.messages.chat.ChatInfo;
import cz.nerdy.craftchat.Main;
import cz.nerdy.craftchat.objects.CraftChatPlayer;
import cz.nerdy.craftchat.objects.Tag;
import cz.nerdy.craftchat.objects.TagMenuType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TagsMainMenuGUI implements InventoryProvider {

    @Override
    public void init(Player player, InventoryContents contents) {

        ItemBuilder allTagsItem = new ItemBuilder(Material.NAME_TAG).setName("§d§lZakoupit tag").hideAllFlags();
        ClickableItem allTags = ClickableItem.of(allTagsItem.build(), e -> {
            Main.getTagManager().openMenu((Player) e.getWhoClicked(), TagMenuType.BUY);
        });
        contents.set(2, 2, allTags);

        ItemBuilder mineTagsItem = new ItemBuilder(Material.FEATHER).setName("§e§lTvé zakoupené tagy").hideAllFlags();
        ClickableItem mineTags = ClickableItem.of(mineTagsItem.build(), e -> {
            Main.getTagManager().openMenu((Player) e.getWhoClicked(), TagMenuType.OWNED);
        });
        contents.set(2, 3, mineTags);

        ItemBuilder achievementTagsItem = new ItemBuilder(Material.DIAMOND).setName("§b§lTagy za achievementy").hideAllFlags();
        ClickableItem achievementTags = ClickableItem.of(achievementTagsItem.build(), e -> {
            Main.getTagManager().openMenu((Player) e.getWhoClicked(), TagMenuType.ACHIEVEMENT);
        });
        contents.set(2, 4, achievementTags);

        ItemBuilder ctTagsItem = new ItemBuilder(Material.BOOK).setName("§e§lVytvořené tagy za CT").hideAllFlags();
        ClickableItem ctTags = ClickableItem.of(ctTagsItem.build(), e -> {
            Main.getTagManager().openMenu((Player) e.getWhoClicked(), TagMenuType.SELF_CREATED);
        });
        contents.set(2, 5, ctTags);

        ItemBuilder specialTagsItem = new ItemBuilder(Material.NETHER_STAR).setName("§c§lSpeciální tagy").hideAllFlags();
        ClickableItem specialTags = ClickableItem.of(specialTagsItem.build(), e -> {
            Main.getTagManager().openMenu((Player) e.getWhoClicked(), TagMenuType.SPECIAL);
        });
        contents.set(2, 6, specialTags);

        ItemBuilder crateTagItem = new ItemBuilder(Material.ANVIL).setName("§aVytvořit vlastní tag").setLore("", "§7Cena: §e1CT").hideAllFlags();
        ClickableItem createTag = ClickableItem.of(crateTagItem.build(), e -> {
            Main.getTagManager().startTagCreation(player);
            player.closeInventory();
        });
        contents.set(3, 4, createTag);

        ItemStack blueGlassItem = new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).setName("").hideAllFlags().build();
        ClickableItem blueGlass = ClickableItem.empty(blueGlassItem);
        contents.fillBorders(blueGlass);
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
