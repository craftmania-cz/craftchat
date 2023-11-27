package cz.nerdy.craftchat.objects;

import cz.craftmania.craftlibs.CraftLibs;
import cz.craftmania.craftlibs.sql.DBRow;
import cz.craftmania.craftlibs.utils.ChatInfo;
import cz.nerdy.craftchat.Main;
import cz.nerdy.craftchat.utils.Colors;
import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class CraftChatPlayer {

    private final @Getter String uuid;
    private final @Getter Player player;
    private final @Getter ChatGroup chatGroup;
    private @Getter Tag selectedTag;
    private @Getter List<Tag> tags = new ArrayList<>();
    private HashMap<String, String> ignoredPlayers;
    private @Getter @Setter boolean checkForSlashMistake;
    private TextColor chatColor = null;

    public CraftChatPlayer(Player player) {
        this.uuid = player.getUniqueId().toString();
        this.player = player;
        this.chatGroup = Main.getChatGroupManager().getChatGroup(player);
        this.ignoredPlayers = Main.getIgnoreManager().getIgnoredPlayers(uuid);
        this.loadChatColor();
        if (!Main.getInstance().isDisabledTags()) {
            Main.getTagManager().getAllTags(player).thenAccept(res -> {
                this.tags = res;
                Main.getTagManager().fetchSelectedTag(this, player);
            });
        }
        this.checkForSlashMistake = true;
    }

    public CraftChatPlayer(Player player, ChatGroup chatGroup) {
        this.uuid = player.getUniqueId().toString();
        this.player = player;
        this.chatGroup = chatGroup;
        this.ignoredPlayers = new HashMap<>();
        this.chatColor = NamedTextColor.WHITE;
        this.checkForSlashMistake = true;
    }

    /**
     * Returns active chat color. Supports all colors MC + hex
     * @return {@link TextColor}
     */
    public @Nullable TextColor getSelectedChatColor() {
        return this.chatColor;
    }

    /**
     * This method resolves active prefixes for a player. <br>
     * - If the player doesn't have an active prefix, returns a basic prefix.<br>
     * - If the player has an active prefix, return prefix from the database.
     * @return {@link net.kyori.adventure.text.ComponentLike}
     */
    public @NotNull Component getPrefix() {
        if (Main.getInstance().isDisabledTags() || this.selectedTag == null) {
            Component hoverText = MiniMessage.miniMessage().deserialize(String.join("\n", this.chatGroup.prefixTooltip));
            HoverEvent<Component> hoverEvent = HoverEvent.showText(hoverText);
            return MiniMessage.miniMessage().deserialize(this.chatGroup.getPrefixFormat()).hoverEvent(hoverEvent);
        }
        return MiniMessage.miniMessage().deserialize(PlaceholderAPI.setPlaceholders(
                this.player,
                this.chatGroup.getTagFormat()),
                Placeholder.component("selected_tag", this.selectedTag.getPrefixAsComponent())
        );
    }

    public @NotNull Component getNameWithHover() {
        Component hoverText = MiniMessage.miniMessage().deserialize(String.join("\n", PlaceholderAPI.setPlaceholders(this.player, this.chatGroup.nameTooltip)));
        HoverEvent<Component> hoverEvent = HoverEvent.showText(hoverText);
        return MiniMessage.miniMessage().deserialize(PlaceholderAPI.setPlaceholders(
                this.player,
                this.chatGroup.getNameFormat()),
                Placeholder.component("player", Component.text(this.player.getName()).hoverEvent(hoverEvent))
        );
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

    /**
     * Load saved chatcolor from database.
     * If player is member of admin team, sets color to null
     */
    public void loadChatColor() {
        if (!this.player.hasPermission("craftchat.chatcolor")) {
            this.chatColor = NamedTextColor.WHITE;
            return;
        }
        if (this.player.hasPermission("craftchat.chatcolor.at")) {
            this.chatColor = null;
            return;
        }
        CraftLibs.getSqlManager().query("SELECT chatcolor FROM player_settings WHERE nick = ?", this.player.getName()).thenAcceptAsync(res -> {
            for (DBRow row : res) {
                String dbColor = row.getString("chatcolor");
                // Detekce rozdílu mezi custom barvou a normální VIP barvou
                if (dbColor.length() == 6) {
                    if (!this.player.hasPermission("craftchat.chatcolor.custom")) {
                        this.chatColor = NamedTextColor.WHITE;
                        return;
                    }
                    this.chatColor = TextColor.fromHexString("#" + dbColor);
                } else {
                    int colorId = Integer.valueOf(dbColor);
                    this.chatColor = Colors.resolveColorById(colorId);
                }
            }
        });
    }

    /**
     * Sets color from Minecraft and set as active color
     * @param color Color to set
     * @param id Numerical ID based Minecraft colors
     */
    public void setChatColor(TextColor color, int id) {
        if (this.player.hasPermission("craftchat.chatcolor.at")) {
            this.chatColor = null;
            return;
        }
        this.chatColor = color;
        CraftLibs.getSqlManager().query("UPDATE player_settings SET chatcolor = ? WHERE nick = ?", id, this.player.getName());
    }

    /**
     * Sets hex format color to database and set as active color
     * @param colorHex Hex string e.x. #12345
     */
    public void setCustomChatColor(String colorHex) {
        if (this.player.hasPermission("craftchat.chatcolor.at")) {
            this.chatColor = null;
            return;
        }
        this.chatColor = TextColor.fromHexString("#" + colorHex);
        CraftLibs.getSqlManager().query("UPDATE player_settings SET chatcolor = ? WHERE nick = ?", colorHex, this.player.getName());
    }
}
