package cz.nerdy.craftchat.objects;

public class Tag {

    private String prefix;
    private int price;

    public Tag(String prefix, int price){
        this.prefix = prefix;
        this.price = price;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getPrice() {
        return price;
    }
}
