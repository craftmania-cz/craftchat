package cz.nerdy.craftchat.objects;

import cz.craftmania.craftcore.spigot.builders.items.ItemBuilder;
import cz.craftmania.craftcore.spigot.inventory.builder.ClickableItem;
import cz.craftmania.craftcore.spigot.inventory.builder.content.InventoryContents;
import cz.craftmania.craftcore.spigot.inventory.builder.content.InventoryProvider;
import cz.craftmania.craftcore.spigot.inventory.builder.content.Pagination;
import cz.craftmania.craftcore.spigot.inventory.builder.content.SlotIterator;
import cz.nerdy.craftchat.Main;
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

        for (Tag tag : this.tags) {
            ItemStack item = new ItemBuilder(Material.NAME_TAG).setName(tag.getPrefix())
                    .setLore("", "§7Cena: §e" + tag.getPrice() + "CC").hideAllFlags().build();
            items.add(ClickableItem.of(item, e -> {
            }));
        }

        ClickableItem[] c = new ClickableItem[items.size()];
        c = items.toArray(c);
        pagination.setItems(c);
        pagination.setItemsPerPage(36);


        ItemBuilder allTagsItem = new ItemBuilder(Material.NAME_TAG).setName("§dVšechny Tagy").hideAllFlags();
        if(type.equals("all")) allTagsItem.setGlowing();

        ClickableItem allTags = ClickableItem.of(allTagsItem.build(), e -> {
            Main.getInstance().getTagManager().openMenu((Player)e.getWhoClicked(), "all");
        });
        contents.set(5, 3, allTags);


        ItemBuilder mineTagsItem = new ItemBuilder(Material.PLAYER_HEAD).setName("§dMoje Tagy").hideAllFlags();
        if(type.equals("mine")) mineTagsItem.setGlowing();

        ClickableItem mineTags = ClickableItem.of(mineTagsItem.build(), e -> {
            Main.getInstance().getTagManager().openMenu((Player)e.getWhoClicked(), "mine");
        });
        contents.set(5, 5, mineTags);


        if (items.size() > 0 && !pagination.isLast()) {
            contents.set(5, 7, ClickableItem.of(new ItemBuilder(Material.PAPER).setName("§f§lDalsi stranka").build(), e -> {
                contents.inventory().open(player, pagination.next().getPage());
            }));
        }
        if (!pagination.isFirst()) {
            contents.set(5, 1, ClickableItem.of(new ItemBuilder(Material.PAPER).setName("§f§lPredchozi stranka").build(), e -> {
                contents.inventory().open(player, pagination.previous().getPage());
            }));
        }

        SlotIterator slotIterator = contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0);
        slotIterator = slotIterator.allowOverride(false);
        pagination.addToIterator(slotIterator);
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}