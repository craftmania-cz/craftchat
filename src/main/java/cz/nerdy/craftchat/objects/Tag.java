package cz.nerdy.craftchat.objects;

public class Tag {

    private int id;
    private String prefix;
    private int price;

    public Tag(int id, String prefix, int price){
        this.id = id;
        this.prefix = prefix;
        this.price = price;
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
