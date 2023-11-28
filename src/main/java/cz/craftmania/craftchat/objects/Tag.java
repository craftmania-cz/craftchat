package cz.craftmania.craftchat.objects;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.NotNull;

public class Tag {

    private int id;
    private String prefix;
    private int price;
    private int type; // 1 = Cshop, 2 = custom

    public Tag(int id, String prefix, int price, int type) {
        this.id = id;
        this.prefix = prefix;
        this.price = price;
        this.type = type;
    }

    public Tag(int id, String prefix, int type) {
        this.id = id;
        this.prefix = prefix;
        this.price = 1;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getPrefixAsSTring() {
        return prefix;
    }

    public ComponentLike getPrefixAsComponent() {
        HoverEvent<Component> hoverEvent = null;
        if (this.type == 1) {
            hoverEvent = HoverEvent.showText(Component.text("§7Tento prefix byl zakoupen v §fCshop\n§7K zakoupení použij §b/cshop"));
        }
        if (this.type == 2) {
            hoverEvent = HoverEvent.showText(Component.text("§7Tento prefix byl vytvořen hráčem."));
        }
        return Component.text(prefix).hoverEvent(hoverEvent);
    }

    public int getPrice() {
        return price;
    }

    public int getType() {
        return type;
    }
}
