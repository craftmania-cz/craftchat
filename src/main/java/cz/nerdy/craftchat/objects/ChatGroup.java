package cz.nerdy.craftchat.objects;

import cz.nerdy.craftchat.Main;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class ChatGroup {

    private int priority;
    private String name;
    private String prefix;
    private String suffix;
    private ChatColor suffixColor;
    private ChatColor prefixColor;
    private ChatColor nameColor;
    private ChatColor chatColor;
    private List<String> prefixTooltip;
    private List<String> nameTooltip;
    private String nameClickCommand;
    private boolean allowTagChange;
    private String customPermission;

    public ChatGroup(
            int priority,
            String name,
            String prefix,
            String suffix,
            String prefixColor,
            String nameColor,
            String suffixColor,
            String chatColor,
            List<String> prefixTooltip,
            List<String> nameTooltip,
            String nameClickCommand,
            boolean allowTagChange,
            String customPermission
    ) {
        this.priority = priority;
        this.name = name;
        this.prefix = prefix;
        this.suffix = Main.getInstance().getPluginCompatibility().translateChatColor(suffix);
        this.suffixColor = Main.getInstance().getPluginCompatibility().resolveChatColor(suffixColor);
        this.prefixColor = Main.getInstance().getPluginCompatibility().resolveChatColor(prefixColor);
        this.nameColor = Main.getInstance().getPluginCompatibility().resolveChatColor(nameColor);
        this.chatColor = Main.getInstance().getPluginCompatibility().resolveChatColor(chatColor);
        List<String> tempList = new ArrayList<>();
        for (String line : prefixTooltip) {
            tempList.add(Main.getInstance().getPluginCompatibility().translateChatColor(line));
        }
        this.prefixTooltip = tempList;

        tempList = new ArrayList<>();
        for (String line : nameTooltip) {
            tempList.add(Main.getInstance().getPluginCompatibility().translateChatColor(line));
        }
        this.nameTooltip = tempList;
        this.nameClickCommand = nameClickCommand;
        this.allowTagChange = allowTagChange;
        this.customPermission = customPermission;
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

    public ChatColor getSuffixColor() {
        return suffixColor;
    }

    public String getCustomPermission() {
        return customPermission;
    }

    public String getDefaultPrefix() {
        return prefix;
    }
}
