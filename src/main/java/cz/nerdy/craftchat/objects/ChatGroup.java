package cz.nerdy.craftchat.objects;

import java.util.List;

public class ChatGroup {

    private int priority;
    private String prefix;
    private String suffix;
    private String nameFormat;
    private String chatColor;
    private List<String> prefixTooltip;
    private List<String> nameTooltip;
    private String nameClickCommand;

    public ChatGroup(int priority, String prefix, String suffix, String nameFormat, String chatColor, List<String> prefixTooltip, List<String> nameTooltip, String nameClickCommand) {
        this.priority = priority;
        this.prefix = prefix;
        this.suffix = suffix;
        this.nameFormat = nameFormat;
        this.chatColor = chatColor;
        this.prefixTooltip = prefixTooltip;
        this.nameTooltip = nameTooltip;
        this.nameClickCommand = nameClickCommand;
    }

    public int getPriority() {
        return priority;
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

    public String getChatColor() {
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
