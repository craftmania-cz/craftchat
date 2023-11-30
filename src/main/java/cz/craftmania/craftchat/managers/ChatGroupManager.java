package cz.craftmania.craftchat.managers;

import cz.craftmania.craftchat.Main;
import cz.craftmania.craftchat.objects.ChatGroup;
import cz.craftmania.craftchat.utils.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class ChatGroupManager {

    @lombok.Getter
    private List<ChatGroup> chatGroups;

    public ChatGroupManager() {

        this.chatGroups = new ArrayList<>();

        ConfigurationSection configurationSection = Main.getInstance().getConfig().getConfigurationSection("format-groups");
        for (String key : configurationSection.getKeys(false)) {
            ConfigurationSection groupSection = configurationSection.getConfigurationSection(key);
            ChatGroup chatGroup = new ChatGroup(
                    groupSection.getInt("priority"),
                    key,
                    groupSection.getString("prefix.format"),
                    groupSection.getStringList("prefix.tooltip"),
                    groupSection.getString("prefix.click_action"),
                    groupSection.getString("name.format"),
                    groupSection.getStringList("name.tooltip"),
                    groupSection.getString("name.click_action"),
                    groupSection.getString("message_format"),
                    groupSection.getBoolean("prefix.tags.allowed"),
                    groupSection.getString("prefix.tags.format"),
                    groupSection.getString("custom_permission")
            );
            this.chatGroups.add(chatGroup);
        }

        Logger.info("Nacteno celkem " + this.chatGroups.size() + " chat skupin.");
    }

    public ChatGroup getDefaultChatGroup() {
        return chatGroups.get(0);
    }

    public ChatGroup getChatGroup(Player player) {
        HashMap<Integer, ChatGroup> groupMap = new HashMap<>();

        for (ChatGroup chatGroup : this.getChatGroups()) {
            groupMap.put(chatGroup.getPriority(), chatGroup);
        }

        List<Integer> groupPrioritiesList = new ArrayList<>(groupMap.keySet());
        Collections.sort(groupPrioritiesList);
        Collections.reverse(groupPrioritiesList);
        for (int priority : groupPrioritiesList) {
            ChatGroup group = groupMap.get(priority);
            if (group.getCustomPermission() != null && player.hasPermission(group.getCustomPermission())) {
                return group;
            }
            if (player.hasPermission("craftchat.format." + group.getName())) {
                Logger.info("Detekovana role: " + group.getName());
                return group;
            }
        }

        return chatGroups.get(0);
    }

}
