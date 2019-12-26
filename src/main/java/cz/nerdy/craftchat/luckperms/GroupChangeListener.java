package cz.nerdy.craftchat.luckperms;

import cz.nerdy.craftchat.Main;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.user.track.UserTrackEvent;

public class GroupChangeListener {
    private final Main plugin;

    public GroupChangeListener(Main plugin, LuckPerms luckPerms) {
        this.plugin = plugin;

        // get the LuckPerms event bus
        EventBus eventBus = luckPerms.getEventBus();

        // subscribe to an event using a lambda
        eventBus.subscribe(UserTrackEvent.class, e -> {
            Main.getInstance().updatePlayer(e.getUser().getUniqueId());
        });

    }
}
