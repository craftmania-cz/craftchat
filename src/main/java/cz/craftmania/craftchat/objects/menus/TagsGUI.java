package cz.craftmania.craftchat.objects.menus;

import cz.craftmania.craftchat.Main;
import cz.craftmania.craftchat.TagManager;
import cz.craftmania.craftchat.objects.CraftChatPlayer;
import cz.craftmania.craftchat.objects.Tag;
import cz.craftmania.craftchat.objects.TagMenuType;
import cz.craftmania.craftcore.builders.items.ItemBuilder;
import cz.craftmania.craftcore.inventory.builder.ClickableItem;
import cz.craftmania.craftcore.inventory.builder.content.InventoryContents;
import cz.craftmania.craftcore.inventory.builder.content.InventoryProvider;
import cz.craftmania.craftcore.inventory.builder.content.Pagination;
import cz.craftmania.craftcore.inventory.builder.content.SlotIterator;
import cz.craftmania.craftcore.messages.chat.ChatInfo;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TagsGUI implements InventoryProvider {

    private List<Tag> tags;
    private TagMenuType type;
    private ItemStack menuMainItem;
    private TagManager tagManager = new TagManager();
    private boolean showOnlyOwned;

    public TagsGUI(List<Tag> tags, TagMenuType type, boolean showOnlyOwned) {
        this.tags = tags;
        this.type = type;
        this.menuMainItem = resolveTitle(type);
        this.showOnlyOwned = showOnlyOwned;
    }

    @Override
    public void init(Player player, InventoryContents contents) {

        final Pagination pagination = contents.pagination();
        final List<ClickableItem> items = new ArrayList<>();

        CraftChatPlayer craftChatPlayer = Main.getCraftChatPlayer(player);

        for (Tag tag : this.tags) {
            if (tag == null) {
                continue;
            }
            boolean hasTag = craftChatPlayer.hasTag(tag);
            if (showOnlyOwned && hasTag) {
                String[] lore = new String[]{"§7Klikni pro nastavení"};
                ItemStack item = new ItemBuilder(Material.NAME_TAG).setName(tag.getPrefixAsSTring()).setLore(lore).hideAllFlags().build();
                items.add(ClickableItem.of(item, e -> {
                    craftChatPlayer.setSelectedTag(tag);
                    ChatInfo.info(player, "Nastavil jsi si tag: " + tag.getPrefixAsSTring());
                    player.closeInventory();
                }));
            } else {
                if (hasTag) {
                    continue;
                }
                String[] lore = new String[]{"§7Cena: §e" + tag.getPrice() + "CC"};
                ItemStack item = new ItemBuilder(Material.NAME_TAG).setName(tag.getPrefixAsSTring()).setLore(lore).hideAllFlags().build();
                items.add(ClickableItem.of(item, e -> {
                    if (Main.getTagManager().buyTag(player, tag)) {
                        ChatInfo.success(player, "Tag " + tag.getPrefixAsSTring() + " byl úspěšně zakoupen");
                    } else {
                        ChatInfo.error(player, "Nemáš dostatek CC k nákupu tohoto tagu");
                    }
                    player.closeInventory();
                }));
            }
        }

        ClickableItem[] c = new ClickableItem[items.size()];
        c = items.toArray(c);
        pagination.setItems(c);
        pagination.setItemsPerPage(36);

        ItemStack blueGlassItem = new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).setName("§7").hideAllFlags().build();
        ClickableItem blueGlass = ClickableItem.empty(blueGlassItem);
        contents.fillRow(0, blueGlass);
        contents.fillRow(5, blueGlass);

        ItemStack bookInfoItem = this.menuMainItem;
        ClickableItem bookInfo = ClickableItem.empty(bookInfoItem);
        contents.set(0, 4, bookInfo);

        if (items.size() > 0 && !pagination.isLast()) {
            contents.set(5, 7, ClickableItem.of(new ItemBuilder(Material.ARROW).setName("§f§lDalší stránka").build(), e -> {
                contents.inventory().open(player, pagination.next().getPage());
            }));
        }
        if (!pagination.isFirst()) {
            contents.set(5, 1, ClickableItem.of(new ItemBuilder(Material.ARROW).setName("§f§lPředchozí stránka").build(), e -> {
                contents.inventory().open(player, pagination.previous().getPage());
            }));
        }

        ItemStack backItem = new ItemBuilder(Material.SPECTRAL_ARROW).setName("§eZpět do menu").hideAllFlags().build();
        contents.set(5, 4, ClickableItem.of(backItem, e -> {
            tagManager.openMainMenu((Player) e.getWhoClicked());
        }));

        SlotIterator slotIterator = contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 0);
        slotIterator = slotIterator.allowOverride(false);
        pagination.addToIterator(slotIterator);
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    private ItemStack resolveTitle(TagMenuType type){
        switch (type) {
            case BUY:
                return new ItemBuilder(Material.BOOK).setName("§d§lZakoupit tag").setLore("§7Zde je seznam tagů", "§7které si můžeš zakoupit!").hideAllFlags().build();
            case OWNED:
                return new ItemBuilder(Material.BOOK).setName("§e§lZakoupené tagy").setLore("§7Zde je seznam tagů", "§7které jsi si zakoupil!").hideAllFlags().build();
            case ACHIEVEMENT:
                return new ItemBuilder(Material.BOOK).setName("§b§lAchievement tagy").setLore("§7Zde je seznam tagů", "§7které se dají získat", "§7plněním achievementů.").hideAllFlags().build();
            case SELF_CREATED:
                return new ItemBuilder(Material.BOOK).setName("§e§lVytvořené tagy").setLore("§7Zde je seznam tagů", "§7jsi si vytvořil(a).").hideAllFlags().build();
            case SPECIAL:
                return new ItemBuilder(Material.BOOK).setName("§c§lSpeciální tagy").setLore("§7Zde je seznam tagů", "§7které jsou speciální nebo limitované!").hideAllFlags().build();
            default:
                return new ItemBuilder(Material.BOOK).setName("§7Default book").build();
        }
    }
}
