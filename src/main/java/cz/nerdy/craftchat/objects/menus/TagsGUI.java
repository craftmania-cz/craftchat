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

public class TagsGUI implements InventoryProvider {

    private List<Tag> tags;
    private String type;

    public TagsGUI(List<Tag> tags, String type) {
        this.tags = tags;
        this.type = type;
    }

    @Override
    public void init(Player player, InventoryContents contents) {

        final Pagination pagination = contents.pagination();
        final List<ClickableItem> items = new ArrayList<>();

        CraftChatPlayer craftChatPlayer = Main.getCraftChatPlayer(player);

        for (Tag tag : this.tags) {
            boolean hasTag = craftChatPlayer.hasTag(tag);
            String[] lore = hasTag ?
                    new String[]{"", "§7Klikni pro nastavení"} : new String[]{"", "§7Cena: §e" + tag.getPrice() + "CC"};

            ItemStack item = new ItemBuilder(Material.NAME_TAG).setName(tag.getPrefix())
                    .setLore(lore).hideAllFlags().build();
            items.add(ClickableItem.of(item, e -> {
                if (hasTag) {
                    craftChatPlayer.setSelectedTag(tag);
                    ChatInfo.info(player, "Nastavil jsi si tag: " + tag.getPrefix());
                } else {
                    if (Main.getTagManager().buyTag(player, tag)) {
                        ChatInfo.success(player, "Tag " + tag.getPrefix() + " byl úspěšně zakoupen");
                    } else {
                        ChatInfo.error(player, "Nemáš dostatek CC k nákupu tohoto tagu");
                    }
                }
                player.closeInventory();
            }));
        }

        ClickableItem[] c = new ClickableItem[items.size()];
        c = items.toArray(c);
        pagination.setItems(c);
        pagination.setItemsPerPage(36);

        ItemStack blueGlassItem = new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).setName("").hideAllFlags().build();
        ClickableItem blueGlass = ClickableItem.empty(blueGlassItem);
        contents.fillRow(0, blueGlass);
        contents.fillRow(5, blueGlass);

        ItemStack bookInfoItem = new ItemBuilder(Material.BOOK).setName("MOJE TAGY //TODO UPRAVIT").hideAllFlags().build();
        ClickableItem bookInfo = ClickableItem.empty(bookInfoItem);
        contents.set(0, 4, bookInfo);

        if (items.size() > 0 && !pagination.isLast()) {
            contents.set(5, 7, ClickableItem.of(new ItemBuilder(Material.ARROW).setName("§f§lDalsi stranka").build(), e -> {
                contents.inventory().open(player, pagination.next().getPage());
            }));
        }
        if (!pagination.isFirst()) {
            contents.set(5, 1, ClickableItem.of(new ItemBuilder(Material.ARROW).setName("§f§lPredchozi stranka").build(), e -> {
                contents.inventory().open(player, pagination.previous().getPage());
            }));
        }

        SlotIterator slotIterator = contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 0);
        slotIterator = slotIterator.allowOverride(false);
        pagination.addToIterator(slotIterator);
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
