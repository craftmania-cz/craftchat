package cz.nerdy.craftchat.objects;

import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class ChatGroup {
    /*

    TODO default tag
     */

    private int priority;
    private String name;
    private String prefix;
    private String suffix;
    private String nameFormat;
    private ChatColor chatColor;
    private List<String> prefixTooltip;
    private List<String> nameTooltip;
    private String nameClickCommand;

    public ChatGroup(int priority, String name, String prefix, String suffix, String nameFormat, String chatColor, List<String> prefixTooltip, List<String> nameTooltip, String nameClickCommand) {
        this.priority = priority;
        this.name = name;
        this.prefix = ChatColor.translateAlternateColorCodes('&', prefix);
        this.suffix = ChatColor.translateAlternateColorCodes('&', suffix);
        this.nameFormat = ChatColor.translateAlternateColorCodes('&', nameFormat);
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
    }

    public int getPriority() {
        return priority;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getNameFormat() {
        return nameFormat;
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
}
