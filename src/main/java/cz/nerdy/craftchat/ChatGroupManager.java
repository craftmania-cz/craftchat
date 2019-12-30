package cz.nerdy.craftchat;

import cz.nerdy.craftchat.objects.ChatGroup;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class ChatGroupManager {

    private List<ChatGroup> chatGroups;

    public ChatGroupManager() {

        this.chatGroups = new ArrayList<>();

        ConfigurationSection configurationSection = Main.getInstance().getConfig().getConfigurationSection("formats");
        for (String key : configurationSection.getKeys(false)) {
            ConfigurationSection groupSection = configurationSection.getConfigurationSection(key);
            ChatGroup chatGroup = new ChatGroup(
                    groupSection.getInt("priority"),
                    key,
                    groupSection.getString("suffix"),
                    groupSection.getString("prefix_color"),
                    groupSection.getString("name_color"),
                    groupSection.getString("chat_color"),
                    groupSection.getStringList("prefix_tooltip"),
                    groupSection.getStringList("name_tooltip"),
                    groupSection.getString("name_click_command")
            );
            this.chatGroups.add(chatGroup);
            System.out.println("Group " + groupSection.getName() + " loaded (prefix=" + chatGroup.getPrefix() + ",suffix=" + chatGroup.getSuffix() + ")");
        }

    }

    public List<ChatGroup> getChatGroups() {
        return chatGroups;
    }

    public ChatGroup getChatGroup(Player player) {
        HashMap<Integer, ChatGroup> groupMap = new HashMap<>();

        for (ChatGroup chatGroup : this.getChatGroups()){
            groupMap.put(chatGroup.getPriority(), chatGroup);
        }

        SortedSet<Integer> groupPriorities = new TreeSet<>(groupMap.keySet());
        for(int priority : groupPriorities){
            ChatGroup group = groupMap.get(priority);
            if(player.hasPermission("craftchat.format." + group.getName())){
                return group;
            }
        }

        return chatGroups.get(0);
    }

}
