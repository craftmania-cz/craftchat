package cz.nerdy.craftchat;

import cz.craftmania.craftlibs.CraftLibs;
import cz.craftmania.craftlibs.sql.DBRow;
import cz.nerdy.craftchat.objects.CraftChatPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class IgnoreManager {

    public HashMap<String, String> getIgnoredPlayers(String uuid) {
        HashMap<String, String> ignores = new HashMap<>();
        CraftLibs.getSqlManager().query("SELECT ig.ignored_uuid, pp.nick " +
                "FROM craftchat_ignores ig INNER JOIN player_profile pp ON pp.uuid=ig.ignored_uuid WHERE ig.uuid=?", uuid).thenAccept(res -> {
            for (DBRow tagRow : res) {
                ignores.put(tagRow.getString("nick"), tagRow.getString("ignored_uuid"));
            }
        });

        return ignores;
    }

    public boolean hasIgnored(Player player, Player ignoredPlayer) {
        CraftChatPlayer craftChatPlayer = Main.getCraftChatPlayer(player);
        return craftChatPlayer.hasIgnored(ignoredPlayer);
    }
}
