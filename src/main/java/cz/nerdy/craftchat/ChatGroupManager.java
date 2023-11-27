package cz.nerdy.craftchat;

import cz.nerdy.craftchat.objects.ChatGroup;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class ChatGroupManager {

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

        System.out.println("Nacteno celkem " + this.chatGroups.size() + " groupek."); //todo: log
    }

    public List<ChatGroup> getChatGroups() {
        return chatGroups;
    }

    public ChatGroup getChatGroup(Player player) {
        HashMap<Integer, ChatGroup> groupMap = new HashMap<>();

        for (ChatGroup chatGroup : this.getChatGroups()) {
            System.out.println("chatGroup: " + chatGroup.getName() + ", priority: " + chatGroup.getPriority());
            groupMap.put(chatGroup.getPriority(), chatGroup);
        }

        List<Integer> groupPrioritiesList = new ArrayList<>(groupMap.keySet());
        Collections.sort(groupPrioritiesList);
        Collections.reverse(groupPrioritiesList);
        System.out.println(Arrays.toString(groupPrioritiesList.toArray()));
        for (int priority : groupPrioritiesList) {
            System.out.println("checking: " + groupPrioritiesList.get(priority));
            ChatGroup group = groupMap.get(priority);
            if (group.getCustomPermission() != null && player.hasPermission(group.getCustomPermission())) {
                return group;
            }
            if (player.hasPermission("craftchat.format." + group.getName())) {
                System.out.println("ma pravo na: " + group.getName());
                return group;
            }
        }

        return chatGroups.get(0);
    }

}
