package cz.nerdy.craftchat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

public class ChatManager {

    private HashMap<String, String> replacements;
    private List<String> blockedTexts;

    public ChatManager() {
        this.replacements = new HashMap<>();
        this.blockedTexts = Arrays.asList(
                "⻒", "⻓", "⻔", "⻕", "⻖", "⻗", "⻘", "⻙", "⻚", "⻛", "⻜", "⻝", "⻞", "⻟", "⻪", "⻫", "⻬", "⻯", "⻮", "⻭", "⻰", "⻱",
                "⻲", "⻳", "⼀", "⼢", "⼣");

        this.loadReplacements();
    }

    private void loadReplacements() {
        this.replacements.put("o/", "( ﾟ◡ﾟ)/");
        //this.replacements.put("<3", "§c❤");
        this.replacements.put(":star:", "§6✮");
        this.replacements.put(":shrug:", Matcher.quoteReplacement("¯\\_(ツ)_/¯"));
        this.replacements.put(":tableflip:", "(╯°□°）╯︵ ┻━┻");
        this.replacements.put(":unflip:", "┬─┬ ノ( ゜-゜ノ)");
        this.replacements.put(":fight:", "(ง'̀-'́)ง");
        this.replacements.put(":lenny:", "( ͡° ͜ʖ ͡°)");
        this.replacements.put(":moneypls:", "(づ｡◕‿‿◕｡)づ");
        this.replacements.put(":*", "(づ￣ ³￣)づ");
        this.replacements.put(":hype:", "ヾ(⌐■_■)ノ♪");
        this.replacements.put(":cry:", "(ಥ﹏ಥ)");

        this.replacements.put(":her:", "§f⻠");
        this.replacements.put(":har:", "§f⻠");
        this.replacements.put(":dog:", "§f⻡");
        this.replacements.put(":sml:", "§f⻢");
        this.replacements.put(":aaa:", "§f⻣");
        this.replacements.put(":pog:", "§f⻤");
        this.replacements.put(":kek:", "§f⻥");
        this.replacements.put(":5h:", "§f⻦");
        this.replacements.put(":shb:", "§f⻧");
        this.replacements.put(":fp:", "§f⻩");
        this.replacements.put(":job:", "§f⻑");
        this.replacements.put(":pag:", "§f⻐");
        this.replacements.put(":pogo:", "§f⻏");
    }

    public HashMap<String, String> getReplacements() {
        return replacements;
    }

    public List<String> getBlockedTexts() {
        return blockedTexts;
    }
}
