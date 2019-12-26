package cz.nerdy.craftchat.objects;

public class Tag {

    private long id;
    private String prefix;
    private int price;

    public Tag(long id, String prefix, int price){
        this.id = id;
        this.prefix = prefix;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getPrice() {
        return price;
    }
}
