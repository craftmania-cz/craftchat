package cz.nerdy.craftchat.objects;

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

    public String getPrefix() {
        return prefix;
    }

    public int getPrice() {
        return price;
    }

    public int getType() {
        return type;
    }
}
