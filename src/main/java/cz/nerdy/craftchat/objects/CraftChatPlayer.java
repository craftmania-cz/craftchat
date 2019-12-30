package cz.nerdy.craftchat.objects;

import cz.craftmania.craftlibs.CraftLibs;
import cz.nerdy.craftchat.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class CraftChatPlayer {

    private String uuid;
    private ChatGroup chatGroup;
    private Tag selectedTag;
    private List<Tag> tags;
    private HashMap<String, String> ignoredPlayers; //nick, uuid

    public CraftChatPlayer(Player player) {
        this.uuid = player.getUniqueId().toString();
        this.selectedTag = Main.getTagManager().getPlayersSelectedTag(player);
        this.chatGroup = Main.getChatGroupManager().getChatGroup(player);
        this.tags = Main.getTagManager().getAllTags(player);
        this.ignoredPlayers = Main.getIgnoreManager().getIgnoredPlayers(uuid);
    }

    public ChatGroup getChatGroup() {
        return chatGroup;
    }

    public Tag getSelectedTag() {
        return selectedTag;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public String getPrefix() {
        return (this.selectedTag == null ? "%luckperms_prefix%" : this.selectedTag.getPrefix() + " ");
    }

    public boolean hasTag(Tag tag) {
        return this.tags.contains(tag);
    }

    public void setSelectedTag(Tag tag) {
        this.selectedTag = tag;
    }

    public void giveTag(Tag tag) {
        this.tags.add(tag);
        CraftLibs.getSqlManager().query("INSERT INTO craftchat_player_tags(uuid,tag_id) VALUES(?,?)", this.uuid, tag.getId());
    }

    public boolean hasIgnored(Player player) {
        return this.ignoredPlayers.containsValue(player.getUniqueId().toString());
    }

    public Set<String> getIgnoredPlayers() {
        return this.ignoredPlayers.keySet();
    }

    public void removeIgnoredPlayer(Player player) {
        this.ignoredPlayers.remove(player.getName());
        CraftLibs.getSqlManager().query("DELETE FROM craftchat_ignores WHERE uuid=? AND ignored_uuid=?", this.uuid, player.getUniqueId().toString()).thenAccept(r -> System.out.println("deleted"));
    }

    public void addIgnoredPlayer(Player player) {
        this.ignoredPlayers.put(player.getName(), player.getUniqueId().toString());
        CraftLibs.getSqlManager().query("INSERT INTO craftchat_ignores(uuid,ignored_uuid) VALUES(?,?)", this.uuid, player.getUniqueId().toString()).thenAccept(r -> System.out.println("Inserted"));
    }
}
