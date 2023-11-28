package cz.craftmania.craftchat.objects;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ChatGroup {

    @Getter int priority;
    @Getter String name;
    @Getter String prefixFormat;
    @Getter List<String> prefixTooltip;
    @Getter String prefixClickCommand;
    @Getter String nameFormat;
    @Getter List<String> nameTooltip;
    @Getter String nameClickCommand;
    @Getter String suffixFormat;
    @Getter String messageFormat;
    @Getter boolean allowTagChange;
    @Getter String tagFormat;
    @Getter String customPermission;

    public ChatGroup(
            int priority,
            String name,
            String prefixFormat,
            List<String> prefixTooltip,
            String prefixClickCommand,
            String nameFormat,
            List<String> nameTooltip,
            String nameClickCommand,
            String messageFormat,
            boolean allowTagChange,
            String tagFormat,
            String customPermission
    ) {
        this.priority = priority;
        this.name = name;
        this.prefixFormat = prefixFormat;
        this.prefixClickCommand = prefixClickCommand;
        this.nameFormat = nameFormat;
        this.messageFormat = messageFormat;
        List<String> tempList = new ArrayList<>();
        for (String line : prefixTooltip) { //todo: adventure + minimessage + placeholderpai
            tempList.add(line);
        }
        this.prefixTooltip = tempList;

        tempList = new ArrayList<>();
        for (String line : nameTooltip) { //todo: adventure + minimessage + placeholderpai
            tempList.add(line);
        }
        this.nameTooltip = tempList;
        this.nameClickCommand = nameClickCommand;
        this.allowTagChange = allowTagChange;
        this.tagFormat = tagFormat;
        this.customPermission = customPermission;
    }

    public String getDefaultPrefix() {
        return "DefaultPrefix";
    }
}
