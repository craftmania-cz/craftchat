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
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TagsMainMenuGUI implements InventoryProvider {

    @Override
    public void init(Player player, InventoryContents contents) {

        //final List<ClickableItem> items = new ArrayList<>();

        ItemBuilder allTagsItem = new ItemBuilder(Material.NAME_TAG).setName("§dVšechny Tagy").hideAllFlags();
        ClickableItem allTags = ClickableItem.of(allTagsItem.build(), e -> {
            Main.getTagManager().openMenu((Player) e.getWhoClicked(), "all");
        });
        contents.set(2, 3, allTags);

        ItemBuilder achievementTagsItem = new ItemBuilder(Material.DIAMOND).setName("§dTagy za achievementy").hideAllFlags();
        ClickableItem achievementTags = ClickableItem.of(achievementTagsItem.build(), e -> {
            Main.getTagManager().openMenu((Player) e.getWhoClicked(), "mine");
        });
        contents.set(2, 4, achievementTags);

        ItemBuilder ctTagsItem = new ItemBuilder(Material.BOOK).setName("§dVytvořené tagy za CT").hideAllFlags();
        ClickableItem ctTags = ClickableItem.of(ctTagsItem.build(), e -> {
            Main.getTagManager().openMenu((Player) e.getWhoClicked(), "ct");
        });
        contents.set(2, 5, ctTags);

        ItemBuilder crateTagItem = new ItemBuilder(Material.ANVIL).setName("§aVytvořit vlastní tag").setLore("", "§7Cena: §e1CT").hideAllFlags();
        ClickableItem createTag = ClickableItem.of(crateTagItem.build(), e -> {
            Main.getTagManager().startTagCreation(player);
            player.closeInventory();
        });
        contents.set(3, 4, createTag);


//        ItemBuilder mineTagsItem = new ItemBuilder(Material.BOOK).setName("§dMoje zakoupené tagy").hideAllFlags();
//        ClickableItem mineTags = ClickableItem.of(mineTagsItem.build(), e -> {
//            Main.getTagManager().openMenu((Player) e.getWhoClicked(), "mine");
//        });
//        contents.set(5, 5, mineTags);

        ItemStack blueGlassItem = new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).setName("").hideAllFlags().build();
        ClickableItem blueGlass = ClickableItem.empty(blueGlassItem);
        contents.fillBorders(blueGlass);


//        SlotIterator slotIterator = contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0);
//        slotIterator = slotIterator.allowOverride(false);
        //pagination.addToIterator(slotIterator);
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
