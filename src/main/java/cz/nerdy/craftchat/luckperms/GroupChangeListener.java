package cz.nerdy.craftchat.luckperms;

import cz.nerdy.craftchat.Main;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.actionlog.Action;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.log.LogBroadcastEvent;
import net.luckperms.api.event.log.LogReceiveEvent;

public class GroupChangeListener {

    public GroupChangeListener(Main plugin, LuckPerms luckPerms) {

        // get the LuckPerms event bus
        EventBus eventBus = luckPerms.getEventBus();

        // subscribe to an event using a lambda
        eventBus.subscribe(plugin, LogReceiveEvent.class, event -> {
            System.out.println("LogReceiveEvent");
            Action action = event.getEntry();
            if (action.getTarget().getType() != Action.Target.Type.USER) return;
            Action.Target target = action.getTarget();
            if (!target.getUniqueId().isPresent()) return;
            if (!action.getDescription().startsWith("parent")) return;

            // Update target player
            Main.getInstance().updatePlayer(target.getUniqueId().get());
        });

        eventBus.subscribe(plugin, LogBroadcastEvent.class, event -> {
            System.out.println("LogBroadcastEvent");
            Action action = event.getEntry();
            if (action.getTarget().getType() != Action.Target.Type.USER) return;
            Action.Target target = action.getTarget();
            if (!target.getUniqueId().isPresent()) return;
            if (!action.getDescription().startsWith("parent")) return;

            // Update target player
            Main.getInstance().updatePlayer(target.getUniqueId().get());
        });
    }
}
