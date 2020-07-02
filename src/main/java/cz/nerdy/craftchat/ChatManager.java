package cz.nerdy.craftchat;

import java.util.HashMap;
import java.util.regex.Matcher;

public class ChatManager {

    private HashMap<String, String> replacements;

    public ChatManager() {
        this.replacements = new HashMap<>();

        this.loadReplacements();
    }

    private void loadReplacements() {
        this.replacements.put("o/", "( ﾟ◡ﾟ)/");
        this.replacements.put("<3", "§c❤");
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
    }

    public HashMap<String, String> getReplacements() {
        return replacements;
    }
}
