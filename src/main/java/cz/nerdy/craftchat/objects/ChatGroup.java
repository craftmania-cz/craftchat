package cz.nerdy.craftchat.objects;

import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class ChatGroup {

    private int priority;
    private String name;
    private String suffix;
    private ChatColor prefixColor;
    private ChatColor nameColor;
    private ChatColor chatColor;
    private List<String> prefixTooltip;
    private List<String> nameTooltip;
    private String nameClickCommand;
    private boolean allowTagChange;

    public ChatGroup(int priority, String name, String suffix, String prefixColor, String nameColor, String chatColor, List<String> prefixTooltip, List<String> nameTooltip, String nameClickCommand, boolean allowTagChange) {
        this.priority = priority;
        this.name = name;
        this.suffix = ChatColor.translateAlternateColorCodes('&', suffix);
        this.prefixColor = ChatColor.valueOf(prefixColor);
        this.nameColor = ChatColor.valueOf(nameColor);
        this.chatColor = ChatColor.valueOf(chatColor);
        List<String> tempList = new ArrayList<>();
        for (String line : prefixTooltip) {
            tempList.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        this.prefixTooltip = tempList;

        tempList = new ArrayList<>();
        for (String line : nameTooltip) {
            tempList.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        this.nameTooltip = tempList;
        this.nameClickCommand = nameClickCommand;
        this.allowTagChange = allowTagChange;
    }

    public int getPriority() {
        return priority;
    }

    public String getName() {
        return name;
    }

    public String getSuffix() {
        return suffix;
    }

    public ChatColor getPrefixColor() {
        return prefixColor;
    }

    public ChatColor getNameColor() {
        return nameColor;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public List<String> getPrefixTooltip() {
        return prefixTooltip;
    }

    public List<String> getNameTooltip() {
        return nameTooltip;
    }

    public String getNameClickCommand() {
        return nameClickCommand;
    }

    public boolean isAllowTagChange() {
        return allowTagChange;
    }
}
