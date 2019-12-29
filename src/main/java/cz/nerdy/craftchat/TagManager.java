package cz.nerdy.craftchat;

import cz.craftmania.craftcore.spigot.inventory.builder.SmartInventory;
import cz.craftmania.crafteconomy.api.CraftCoinsAPI;
import cz.craftmania.craftlibs.CraftLibs;
import cz.craftmania.craftlibs.sql.DBRow;
import cz.nerdy.craftchat.objects.CraftChatPlayer;
import cz.nerdy.craftchat.objects.Tag;
import cz.nerdy.craftchat.objects.TagsGUI;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TagManager {

    private List<Tag> tagList;

    public TagManager() {
        this.tagList = new ArrayList<>();

        ArrayList<DBRow> tagRows = CraftLibs.getSqlManager().query("SELECT * FROM craftchat_tags WHERE server=?", Main.SERVER);
        for (DBRow tagRow : tagRows) {
            this.tagList.add(new Tag(tagRow.getInt("id"), tagRow.getString("name"), tagRow.getInt("price")));
        }
    }

    public List<Tag> getAllTags() {
        return this.tagList;
    }

    public List<Tag> getAllTags(Player player) {
        ArrayList<DBRow> tagRows = CraftLibs.getSqlManager().query("SELECT t.* FROM craftchat_player_tags pt INNER JOIN craftchat_tags t ON t.id=pt.tag_id WHERE pt.player_uuid=?" /*TODO server?*/, player.getUniqueId().toString());
        for (DBRow tagRow : tagRows) {
            this.tagList.add(new Tag(tagRow.getInt("id"), tagRow.getString("name"), tagRow.getInt("price")));
        }
        return this.tagList;
    }

    public Tag getPlayersSelectedTag(Player player) {
        //todo pokud nema nastaveny custom tag, tak nastavit podle groupky .. u AT nastavovat vždy podle groupky
        return null;
    }

    public void openMenu(Player player, String type) {
        List<Tag> tags = null;
        switch (type) {
            case "mine":
                tags = getAllTags();
            default:
                tags = getAllTags();
        }

        SmartInventory.builder().size(6, 9).title("Tagy").provider(new TagsGUI(tags, type)).build().open(player);
    }

    public boolean buyTag(Player player, Tag tag) {
        if (CraftCoinsAPI.getCoins(player) < tag.getPrice()) {
            return false;
        }

        CraftCoinsAPI.takeCoins(player, tag.getPrice());
        this.giveTag(tag, player);

        return true;
    }

    public void giveTag(Tag tag, Player player) {
        CraftChatPlayer craftChatPlayer = Main.getCraftChatPlayer(player);
        craftChatPlayer.giveTag(tag);
    }
}
