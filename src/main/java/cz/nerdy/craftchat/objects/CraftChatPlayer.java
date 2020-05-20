package cz.nerdy.craftchat.objects;

import cz.craftmania.craftlibs.CraftLibs;
import cz.craftmania.craftlibs.sql.DBRow;
import cz.nerdy.craftchat.Main;
import cz.nerdy.craftchat.utils.Colors;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class CraftChatPlayer {

    private String uuid;
    private Player player;
    private ChatGroup chatGroup;
    private Tag selectedTag;
    private List<Tag> tags = new ArrayList<>();; // Default
    private HashMap<String, String> ignoredPlayers; //nick, uuid
    private boolean checkForSlashMistake;
    private ChatColor chatColor = ChatColor.WHITE; // Pro VIP

    public CraftChatPlayer(Player player) {
        this.uuid = player.getUniqueId().toString();
        this.player = player;
        this.chatGroup = Main.getChatGroupManager().getChatGroup(player);
        this.ignoredPlayers = Main.getIgnoreManager().getIgnoredPlayers(uuid);
        this.loadChatColor();

        if (Main.sqlEnabled) {
            Main.getTagManager().getAllTags(player).thenAccept(res -> {
                this.tags = res;
                Main.getTagManager().fetchSelectedTag(this, player);
            });
        }

        this.checkForSlashMistake = true;
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

    public ChatColor getSelectedChatColor() {
        return this.chatColor;
    }

    public String getPrefix() {
        return (this.selectedTag == null ? "%luckperms_prefix%" : this.selectedTag.getPrefix() + " ");
    }

    public boolean hasTag(Tag tag) {
        return this.tags.contains(tag);
    }

    public void setSelectedTag(Tag tag) {
        this.selectedTag = tag;
        CraftLibs.getSqlManager().query("UPDATE player_profile SET tags = JSON_REPLACE(tags, '$." + Main.SERVER + "', ?) WHERE uuid=?", tag.getId(), this.uuid);
    }

    public void removeTag() {
        this.selectedTag = null;
        CraftLibs.getSqlManager().query("UPDATE player_profile SET tags = JSON_REPLACE(tags, '$." + Main.SERVER + "', ?) WHERE uuid=?", 0, this.uuid);
    }

    public void setSelectedTagWithoutSavingIntoDatabase(Tag tag) {
        this.selectedTag = tag;
    }

    public void giveTag(Tag tag) {
        this.tags.add(tag);
        Main.getTagManager().getAllTags().add(tag);
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
        CraftLibs.getSqlManager().query("DELETE FROM craftchat_ignores WHERE uuid=? AND ignored_uuid=?", this.uuid, player.getUniqueId().toString()).thenAcceptAsync((data) -> {});
    }

    public void addIgnoredPlayer(Player player) {
        this.ignoredPlayers.put(player.getName(), player.getUniqueId().toString());
        CraftLibs.getSqlManager().query("INSERT INTO craftchat_ignores(uuid,ignored_uuid) VALUES(?,?)", this.uuid, player.getUniqueId().toString()).thenAcceptAsync(r -> {});
    }

    public boolean isCheckForSlashMistake() {
        return checkForSlashMistake;
    }

    public void setCheckForSlashMistake(boolean checkForSlashMistake) {
        this.checkForSlashMistake = checkForSlashMistake;
    }

    public void loadChatColor() {
        if (!this.player.hasPermission("craftchat.chatcolor")) {
            this.chatColor = ChatColor.WHITE;
            return;
        }
        if (this.player.hasPermission("craftchat.chatcolor.at")) {
            this.chatColor = ChatColor.YELLOW;
            return;
        }
        if (!Main.sqlEnabled) return;
        CraftLibs.getSqlManager().query("SELECT chatcolor FROM player_settings WHERE nick = ?", this.player.getName()).thenAcceptAsync(res -> {
            for (DBRow row : res) {
                this.chatColor = Colors.resolveColorById(row.getInt("chatcolor"));
            }
        });
    }

    public void setChatColor(ChatColor color, int id) {
        if (this.player.hasPermission("craftchat.chatcolor.at")) {
            this.chatColor = ChatColor.YELLOW;
            return;
        }
        this.chatColor = color;
        CraftLibs.getSqlManager().query("UPDATE player_settings SET chatcolor = " + id + " WHERE nick = ?", this.player.getName());
    }

    public Player getPlayer() {
        return player;
    }
}
