package cz.nerdy.craftchat.objects;

import cz.nerdy.craftchat.Main;
import org.bukkit.entity.Player;

public class CraftChatPlayer {

    private ChatGroup chatGroup;
    private Tag selectedTag;
    private Tag[] playersTags;


    public CraftChatPlayer(Player player){
        this.selectedTag = Main.getInstance().getTagManager().getPlayersSelectedTag(player);
        this.chatGroup = Main.getInstance().getChatGroupManager().getChatGroup(player);
    }

    public ChatGroup getChatGroup() {
        return chatGroup;
    }

    public Tag getSelectedTag() {
        return selectedTag;
    }

}
