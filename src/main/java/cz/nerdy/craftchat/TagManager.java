package cz.nerdy.craftchat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cz.craftmania.craftcore.spigot.inventory.builder.SmartInventory;
import cz.craftmania.craftcore.spigot.messages.chat.ChatInfo;
import cz.craftmania.crafteconomy.api.CraftCoinsAPI;
import cz.craftmania.crafteconomy.api.CraftTokensAPI;
import cz.craftmania.craftlibs.CraftLibs;
import cz.craftmania.craftlibs.sql.DBRow;
import cz.nerdy.craftchat.objects.CraftChatPlayer;
import cz.nerdy.craftchat.objects.Tag;
import cz.nerdy.craftchat.objects.menus.TagsGUI;
import cz.nerdy.craftchat.objects.menus.TagsMainMenuGUI;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagManager {

    private List<Tag> tagList;
    private List<Player> playersCreatingTag;
    private List<Pattern> blockedTagsPatterns;

    public TagManager() {
        this.tagList = new ArrayList<>();
        this.playersCreatingTag = new ArrayList<>();
        this.blockedTagsPatterns = new ArrayList<>();

        // Nastaveni blokovanych tagu
        for (String s : Main.getInstance().getConfig().getStringList("blocked-tags")) {
            Pattern p = Pattern.compile(s);
            blockedTagsPatterns.add(p);
        }

        CraftLibs.getSqlManager().query("SELECT * FROM craftchat_tags WHERE server IS NULL OR server=?", Main.SERVER).thenAccept(res -> {
            for (DBRow tagRow : res) {
                this.tagList.add(new Tag(tagRow.getInt("id"), tagRow.getString("name"), tagRow.getInt("price")));
            }
        });

    }

    public List<Tag> getAllTags() {
        return this.tagList;
    }

    public CompletableFuture<List<Tag>> getAllTags(Player player) {
        CompletableFuture<List<Tag>> completableFuture = new CompletableFuture<>();

        List<Tag> tags = new ArrayList<>();
        for (Tag tag : this.getAllTags()) {
            if (player.hasPermission("deluxetags.tag." + tag.getPrefix().toLowerCase())) {
                tags.add(tag);
            }
        }

        CraftLibs.getSqlManager().query("SELECT t.id FROM craftchat_player_tags pt INNER JOIN craftchat_tags t ON t.id=pt.tag_id " +
                "WHERE pt.uuid=? AND t.server IS NULL OR t.server=?", player.getUniqueId().toString(), Main.SERVER)
                .thenAccept(res -> {
                    List<Tag> dbTags = new ArrayList<>();
                    for (DBRow tagRow : res) {
                        Tag tag = getTagById(tagRow.getInt("id"));
                        if (!tags.contains(tag)) {
                            dbTags.add(tag);
                        }
                    }
                    tags.addAll(dbTags);
                    completableFuture.complete(tags);
                });

        return completableFuture;
    }

    public Tag getTagById(int id) {
        for (Tag tag : tagList) {
            if (tag.getId() == id) {
                return tag;
            }
        }
        return null;
    }

    public void fetchSelectedTag(CraftChatPlayer craftChatPlayer, Player player) {
        //ChatInfo.info(player, "Načítání předchozího tagu..");
        //CraftLibs.getSqlManager().query("SELECT JSON_EXTRACT(`tags`, CONCAT('$.','survival')) as tag_id FROM player_profile WHERE uuid=?", TODO pomocí SQL jsonu
        //CraftLibs.getSqlManager().query("SELECT JSON_EXTRACT(`tags`, CONCAT('$.','survival')) AS tags FROM player_profile WHERE nick='Nerdy42'"
        CraftLibs.getSqlManager().query("SELECT tags FROM player_profile WHERE uuid=?", player.getUniqueId().toString()
        ).thenAccept(res -> {
            if (res.size() > 0) {
                JsonObject jsonObject = new JsonParser().parse(res.get(0).getString("tags")).getAsJsonObject();
                int tagId = jsonObject.get(Main.SERVER).getAsInt();

                if (tagId == 0) {
                    return;
                }
                Tag tag = this.getTagById(tagId);
                ChatInfo.success(player, "Předchozí tag načten §7(" + tag.getPrefix() + ")");

                craftChatPlayer.setSelectedTagWithoutSavingIntoDatabase(tag);
            } else {
                System.out.println("res size je NULA");
            }
        });
    }

    public void openMenu(Player player, String type) {
        List<Tag> tags = null;
        CraftChatPlayer craftChatPlayer = Main.getCraftChatPlayer(player);

        switch (type) {
            case "mine": // veškeré zakoupené tagy
                tags = craftChatPlayer.getTags();
                break;
            case "ct": // tagy vytvořené za CT
                tags = craftChatPlayer.getTags();  // TODO tagy za CT
                break;
            default:
                tags = getAllTags();
        }

        SmartInventory.builder().size(6, 9).title("Seznam tagů").provider(new TagsGUI(tags, type)).build().open(player);
    }

    public void openMainMenu(Player player) {
        SmartInventory.builder().size(6, 9).title("Tagy").provider(new TagsMainMenuGUI()).build().open(player);
    }

    public boolean buyTag(Player player, Tag tag) {
        if (CraftCoinsAPI.getCoins(player) < tag.getPrice()) {
            return false;
        }

        CraftCoinsAPI.takeCoins(player, tag.getPrice());
        this.giveTag(tag, player);

        return true;
    }

    public void giveTag(Tag tag, Player player) {
        CraftChatPlayer craftChatPlayer = Main.getCraftChatPlayer(player);
        craftChatPlayer.giveTag(tag);
        craftChatPlayer.setSelectedTag(tag);
    }

    public boolean isCreatingTag(Player player) {
        return this.playersCreatingTag.contains(player);
    }

    public void startTagCreation(Player player) {
        if (CraftTokensAPI.getTokens(player) < 1) {
            ChatInfo.error(player, "Nemáš dostatečný počet CT");
            return;
        }

        this.playersCreatingTag.add(player);

        player.sendMessage("");
        player.sendMessage("§e§lEditor pro vytváreni vlastních tagů");
        player.sendMessage("§7Nyní napiš do chatu, jaký tag chceš vytvořit.");
        player.sendMessage("");
        player.sendMessage("§fMaximálni délka tagu je 12 znaků. Cena za tag je 1 CraftToken!");
        player.sendMessage("");
        player.sendMessage("§cPokud chceš opustit editor napiš \"stop\" do chatu");
        player.sendMessage("");
        player.playSound(player.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
    }

    public void stopTagCreation(Player player) {
        this.playersCreatingTag.remove(player);
        player.sendMessage("");
        player.sendMessage("§cVytváření tagu zastaveno");
        player.sendMessage("");
    }

    private boolean validateTag(Player player, String tag) {
        if (tag.length() > 12) {
            player.sendMessage("");
            player.sendMessage("§cTag nemuze byt delsi nez 12 znaku!");
            player.sendMessage("");
            return false;
        }
        if (tag.contains(" ")) {
            player.sendMessage("");
            player.sendMessage("§cNelze vytvorit tag, ktery obsahuje mezeru!");
            player.sendMessage("");
            return false;
        }
        if (tag.contains("&") || tag.contains("§")) {
            player.sendMessage("");
            player.sendMessage("§cNelze vytvorit tag, ktery obsahuje prefix pro barvy!");
            player.sendMessage("");
            return false;
        }
        for (Pattern pattern : blockedTagsPatterns) {
            String editedMessage = tag.toLowerCase();
            Matcher matcher = pattern.matcher(editedMessage);
            if (matcher.find()) {
                player.sendMessage("");
                player.sendMessage("§cTento tag je blokovany, nelze ho vytvorit!");
                player.sendMessage("");
                return false;
            }
        }
        if (!tag.matches("[a-zA-Z]+")) {
            player.sendMessage("");
            player.sendMessage("§cNelze vytvorit tag, ktery obsahuje specialni znaky!");
            player.sendMessage("");
            return false;
        }

        return true;
    }

    public void createTag(Player player, String tag) {
        if (!validateTag(player, tag)) {
            return;
        }
        this.playersCreatingTag.remove(player);

        CraftTokensAPI.takeTokens(player, 1);

        CraftChatPlayer craftChatPlayer = Main.getCraftChatPlayer(player);

        ChatInfo.info(player, "Vytváření tagu...");

        CraftLibs.getSqlManager().insertAndReturnLastInsertedId("INSERT INTO craftchat_tags (server,name,price,description) VALUE(?,?,?,?)", Main.SERVER, tag, 0, "Tag koupený za CraftToken")
                .thenAccept(res -> {
                    Tag createdTag = new Tag(res, tag);
                    craftChatPlayer.giveTag(createdTag);

                    player.sendMessage("");
                    player.sendMessage("§aTvuj tag §f" + tag + " §abyl uspesne vytvoren! Nyni si ho aktivuj v §e/tags");
                    player.sendMessage("");
                });
    }
}
