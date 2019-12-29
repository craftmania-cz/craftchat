package cz.nerdy.craftchat.objects;

import cz.craftmania.craftlibs.CraftLibs;
import cz.nerdy.craftchat.Main;
import org.bukkit.entity.Player;

import java.util.List;

public class CraftChatPlayer {

    private String uuid;
    private ChatGroup chatGroup;
    private Tag selectedTag;
    private List<Integer> tags;

    public CraftChatPlayer(Player player) {
        this.uuid = player.getUniqueId().toString();
        this.selectedTag = Main.getTagManager().getPlayersSelectedTag(player);
        this.chatGroup = Main.getChatGroupManager().getChatGroup(player);

        this.tags = Main.getTagManager().getAllTags(player);
    }

    public ChatGroup getChatGroup() {
        return chatGroup;
    }

    public Tag getSelectedTag() {
        return selectedTag;
    }

    public String getPrefix() {
        return this.chatGroup.getPrefix() + (this.selectedTag == null ? "" : this.selectedTag.getPrefix());
    }

    public boolean hasTag(Tag tag) {
        return this.tags.contains(tag.getId());
    }

    public void setSelectedTag(Tag tag) {
        this.selectedTag = tag;
    }

    public void giveTag(Tag tag) {
        this.tags.add(tag.getId());
        CraftLibs.getSqlManager().query("INSERT INTO craftchat_player_tags(uuid,tag_id) VALUES(?,?)", this.uuid, tag.getId());
    }
}
