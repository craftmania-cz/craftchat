package cz.craftmania.craftchat.objects;

import lombok.Getter;
import lombok.Setter;

public class Emote {

    @Getter private String id;
    @Getter @Setter private String name;
    @Getter @Setter private EmoteType type;
    @Getter @Setter private String toReplace;
    @Getter @Setter private String replaceWith;
    @Getter @Setter private String permission;

    public Emote(String id) {
        this.id = id;
    }
}
