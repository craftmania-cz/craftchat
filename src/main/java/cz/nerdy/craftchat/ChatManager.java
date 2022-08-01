package cz.nerdy.craftchat;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

public class ChatManager {

    private final HashMap<String, String> premiumReplacements;
    private final HashMap<String, String> freeReplacements;
    private final List<String> blockedTexts;
    private final List<Player> activeLandChat;

    public ChatManager() {
        this.activeLandChat = new ArrayList<>();
        this.premiumReplacements = new HashMap<>();
        this.freeReplacements = new HashMap<>();
        this.blockedTexts = Arrays.asList(
                "⻒", "⻓", "⻔", "⻕", "⻖", "⻗", "⻘", "⻙", "⻚", "⻛", "⻜", "⻝", "⻞", "⻟", "⻪", "⻫", "⻬", "⻯", "⻮", "⻭", "⻰", "⻱",
                "⻲", "⻳", "⼀", "⼢", "⼣", "夑", "夐", "⼧", "夔", "솄", "솶");

        this.loadPremiumReplacements();
        this.loadFreeReplacements();
    }

    private void loadFreeReplacements() {
        this.freeReplacements.put("o/", "( ﾟ◡ﾟ)/");
        this.freeReplacements.put(":star:", "§6✮");
        this.freeReplacements.put(":shrug:", Matcher.quoteReplacement("¯\\_(ツ)_/¯"));
        this.freeReplacements.put(":tableflip:", "(╯°□°）╯︵ ┻━┻");
        this.freeReplacements.put(":unflip:", "┬─┬ ノ( ゜-゜ノ)");
        this.freeReplacements.put(":fight:", "(ง'̀-'́)ง");
        this.freeReplacements.put(":lenny:", "( ͡° ͜ʖ ͡°)");
        this.freeReplacements.put(":moneypls:", "(づ｡◕‿‿◕｡)づ");
        this.freeReplacements.put(":*", "(づ￣ ³￣)づ");
        this.freeReplacements.put(":hype:", "ヾ(⌐■_■)ノ♪");
    }

    /*
        Replacementy pro VIP
     */
    private void loadPremiumReplacements() {

        this.premiumReplacements.put(":her:", "§f⻠");
        this.premiumReplacements.put(":herold:", "§f⻠");
        this.premiumReplacements.put(":har:", "§f⻠");
        this.premiumReplacements.put(":harold:", "§f⻠");
        this.premiumReplacements.put(":dog:", "§f⻡");
        this.premiumReplacements.put(":sml:", "§f⻢");
        this.premiumReplacements.put(":smile:", "§f⻢");
        this.premiumReplacements.put(":aaa:", "§f⻣");
        this.premiumReplacements.put(":pog:", "§f⻤");
        this.premiumReplacements.put(":kek:", "§f⻥");
        this.premiumReplacements.put(":5h:", "§f⻦");
        this.premiumReplacements.put(":5head:", "§f⻦");
        this.premiumReplacements.put(":shb:", "§f⻧");
        this.premiumReplacements.put(":shiba:", "§f⻧");
        this.premiumReplacements.put(":fp:", "§f⻩");
        this.premiumReplacements.put(":facepalm:", "§f⻩");
        this.premiumReplacements.put(":job:", "§f⻑");
        this.premiumReplacements.put(":pag:", "§f⻐");
        this.premiumReplacements.put(":pagman:", "§f⻐");
        this.premiumReplacements.put(":pugman:", "§f⻐");
        this.premiumReplacements.put(":pogo:", "§f⻏");
        this.premiumReplacements.put(":cry:", "§f⼰");
        this.premiumReplacements.put(":blush:", "§f⼭");
        this.premiumReplacements.put(":rage:", "§f⼮");
        this.premiumReplacements.put(":bsmart:", "§f⼯");
        this.premiumReplacements.put(":bst:", "§f⼯");
        this.premiumReplacements.put(":lol:", "§f⼱");
        this.premiumReplacements.put(":love:", "§f⼲");
        this.premiumReplacements.put(":hug:", "§f⼳");
        this.premiumReplacements.put(":jam:", "§f⼴");
        this.premiumReplacements.put(":party:", "§f⼵");
        this.premiumReplacements.put(":sus:", "§f쇒");
        this.premiumReplacements.put(":mw:", "§f쇑");
        this.premiumReplacements.put(":monkaw:", "§f쇑");
        this.premiumReplacements.put(":old:", "§f쇐");
        this.premiumReplacements.put(":yep:", "§f쇏");
        this.premiumReplacements.put(":bedge:", "§f쇎");
        this.premiumReplacements.put(":pray:", "§f쇍");
        this.premiumReplacements.put(":fs:", "§f쇌");
        this.premiumReplacements.put(":clueless:", "§f쇋");
        this.premiumReplacements.put(":clue:", "§f쇋");
    }

    public HashMap<String, String> getPremiumReplacements() {
        return premiumReplacements;
    }

    public HashMap<String, String> getFreeReplacements() {
        return freeReplacements;
    }

    public List<String> getBlockedTexts() {
        return blockedTexts;
    }

    public List<Player> getActiveLandChat() {
        return activeLandChat;
    }
}
