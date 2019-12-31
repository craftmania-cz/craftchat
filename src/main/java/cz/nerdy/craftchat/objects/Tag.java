package cz.nerdy.craftchat.objects;

import cz.craftmania.craftlibs.CraftLibs;
import cz.nerdy.craftchat.Main;

public class Tag {

    private int id;
    private String prefix;
    private int price;

    public Tag(int id, String prefix, int price) {
        this.id = id;
        this.prefix = prefix;
        this.price = price;
    }

    public Tag(int id, String prefix) {
        this.id = id;
        this.prefix = prefix;
        this.price = 1;
    }

    public int getId() {
        return id;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getPrice() {
        return price;
    }
}
