package cz.craftmania.craftchat.managers;

import cz.craftmania.craftchat.Main;
import cz.craftmania.craftchat.objects.Emote;
import cz.craftmania.craftchat.objects.EmoteType;
import cz.craftmania.craftchat.utils.configs.Config;
import cz.craftmania.crafteconomy.utils.Logger;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class EmoteManager {

    @Getter
    private final List<Emote> emotes = new ArrayList<>();

    public void loadEmotes() {

        Logger.info("Načítání chat emotů:");
        Config emoteConfig = Main.getInstance().getConfigAPI().getConfig("emotes");
        if (emoteConfig == null) {
            Logger.danger("Emote config se nepodařilo načíst.");
            return;
        }

        ConfigurationSection emoteListSection = emoteConfig.getConfig().getConfigurationSection("emote_list");

        try {
            for (String key : emoteListSection.getKeys(false)) {
                ConfigurationSection emote = emoteListSection.getConfigurationSection(key);
                Emote emoteObject = new Emote(key);
                emoteObject.setName(emote.getString("name"));
                emoteObject.setType(EmoteType.valueOf(emote.getString("type")));
                emoteObject.setToReplace(emote.getString("toReplace"));
                emoteObject.setReplaceWith(emote.getString("replaceWith"));
                emoteObject.setPermission(emote.getString("permission"));
                emotes.add(emoteObject);

                Logger.info("Emote zaregistrovan: " + emoteObject.getName() + ", id: " + emoteObject.getId() + ", type: " + emoteObject.getType().name() + ", toReplace: "
                        + emoteObject.getToReplace() + ", replaceWith: " + emoteObject.getReplaceWith() + ", permission: " + emoteObject.getPermission());
            }
        } catch (Exception exception) {
            exception.printStackTrace();

        }
    }
}
