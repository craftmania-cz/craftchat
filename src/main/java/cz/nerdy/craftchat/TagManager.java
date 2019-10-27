package cz.nerdy.craftchat;

import cz.craftmania.craftcore.spigot.inventory.builder.SmartInventory;
import cz.craftmania.craftlibs.CraftLibs;
import cz.craftmania.craftlibs.sql.DBRow;
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
            this.tagList.add(new Tag(tagRow.getString("name"), tagRow.getInt("price")));
        }

    }

    public List<Tag> getAllTags() {
        return this.tagList;
    }

    public Tag getPlayersSelectedTag(Player player) {
        //todo
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

}
