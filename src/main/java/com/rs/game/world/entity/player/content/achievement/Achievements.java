package com.rs.game.world.entity.player.content.achievement;

import com.google.common.collect.Lists;
import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Player;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class Achievements implements Serializable {

    private static final long serialVersionUID = -7223995394249088551L;

    public static final int INTERFACE_ID = 3026;
    public static final int COLLECT_REWARD = 31;
    public static final int ACHIEVEMENTNAME = 28;
    public static final int PROGRESSTEXT = 49;
    public static final int COMPLETTIONTEXT = 16;
    public static final int TASKINFORMATION = 52;
    public static final int BONUSREWARD = 56;
    public static final int NEXT_PAGE = 189;
    public static final int REWARDS = 57;

    private final Player player;
    private transient int page = 0;

    private final List<AchievementList> achievementlist = Lists.newArrayListWithCapacity(ACHIEVEMENT_NAME_COMPONENTS.length);
    private transient AchievementList selectedAchievement;
    private final List<AchievementList> completed = Lists.newArrayList();


    private static final int[] ACHIEVEMENT_NAME_COMPONENTS = {
            61, 68, 72, 76, 80, 84, 88, 92, 96, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140,
            144, 148, 152, 156, 160, 164, 168, 172, 176, 180, 184, 188
    };

    public void open() {
        player.getInterfaceManager().sendInterface(INTERFACE_ID);
        String title = "Completed (" + completed.size() + "/" + AchievementList.VALUES.length + ")";
        player.getPackets().sendIComponentText(INTERFACE_ID,COMPLETTIONTEXT, title);
        selectRecipe(AchievementList.VALUES[0]);
        populateachievement();
    }

    private int getMaxPage() {
        return 2;
    }



    private void populateachievement() {
        achievementlist.clear();
        int startOrdinal = page * ACHIEVEMENT_NAME_COMPONENTS.length;
        int idx = 0;
        for (int index = startOrdinal; index < ((startOrdinal == 0 ? ACHIEVEMENT_NAME_COMPONENTS.length : startOrdinal * 2)); index++, idx++) {
            if (index < AchievementList.VALUES.length) {
                AchievementList achievement = AchievementList.VALUES[index];
                achievementlist.add(achievement);
                player.getPackets().sendIComponentText(INTERFACE_ID, ACHIEVEMENT_NAME_COMPONENTS[idx], achievement.achievementName);
            } else
                player.getPackets().sendIComponentText(INTERFACE_ID, ACHIEVEMENT_NAME_COMPONENTS[idx], "");
        }
    }

    private void selectRecipe(AchievementList achievement) {
        selectedAchievement = achievement;

        String prefix = (selectedAchievement.getSuccess().test(player) ? "<col=00ff00>" : "<col=F6EB0A>") + "Progress: ";
        String progress = prefix + selectedAchievement.getProcessText().apply(player);

        player.getPackets().sendIComponentText(INTERFACE_ID, ACHIEVEMENTNAME, achievement.achievementName);
        player.getPackets().sendIComponentText(INTERFACE_ID, PROGRESSTEXT, progress);
        player.getPackets().sendIComponentText(INTERFACE_ID, BONUSREWARD, achievement.bonusreward);
        player.getPackets().sendIComponentText(INTERFACE_ID, TASKINFORMATION, achievement.taskinformation);
        player.getPackets().sendItems(6969, achievement.reward);
        player.getPackets().sendInterSetItemsOptionsScript(INTERFACE_ID, REWARDS, 6969, 3, 10, "Examine");
        player.getPackets().sendUnlockIComponentOptionSlots(INTERFACE_ID, REWARDS, 0, 160, 0);

    }

    public boolean handleButtonClick(int interfaceId, int buttonId) {

        if (interfaceId != INTERFACE_ID)
            return false;



        for (int index = 0; index < ACHIEVEMENT_NAME_COMPONENTS.length; index++) {
            if (buttonId == ACHIEVEMENT_NAME_COMPONENTS[index]) {
                selectRecipe(achievementlist.get(index));
            }
        }

        if (buttonId == NEXT_PAGE) {
            if (page + 1 < getMaxPage()) {
                page++;
            } else page = 0;
            populateachievement();
            return true;
        }

        if (buttonId == COLLECT_REWARD) {

            if (completed.contains(selectedAchievement)) {
                player.sendMessage("You've already claimed your rewards.");
                return false;
            }

            if (selectedAchievement.success.test(player)) {
                Stream.of(selectedAchievement.getReward()).forEach(item -> player.getInventory().addItem(item));
                completed.add(selectedAchievement);
            } else {
                player.sendMessage("You haven't completed this achievement yet.");
            }
        }

        // player.sendMessage("Unhandled button: " + buttonId);

        return true;
    }

    @Getter
    @RequiredArgsConstructor
    public enum AchievementList {

        NORMAL_LOGS("<img=30> Woodcutting 1", "100 achievement tokens", (player) -> player.logscut + "/100", (player) -> player.logscut >= 100, "Cut 100 logs", new Item[] { new Item(995, 500000), new Item(23713, 1),  new Item(28619, 100) }),
        NORAML_BONES("<img=30> Prayer 1", "100 achievement tokens", (player) -> player.bonessacrificed + "/100", (player) -> player.bonessacrificed >= 100, "Use 100 of any bone on a altar", new Item[] { new Item(995, 500000), new Item(23713, 1),  new Item(28619, 100) }),
        RUNECRAFT("<img=30> Runecrafting 1", "100 achievement tokens", (player) -> player.craftrune + "/20", (player) -> player.craftrune >= 20, "Do 20 runecrafting runs", new Item[] { new Item(995, 500000), new Item(23713, 1),  new Item(28619, 100) }),
        AGILITY("<img=30> Agility 1", "100 achievement tokens", (player) -> player.gnomescourse + "/10", (player) -> player.gnomescourse >= 10, "Run 10 gnome agility courses", new Item[] { new Item(995, 500000), new Item(23713, 1),  new Item(28619, 100) }),
        HERBLORE("<img=30> Heblore 1", "100 achievement tokens", (player) -> player.guams + "/50", (player) -> player.guams >= 50, "Clean 50 guams", new Item[] { new Item(995, 500000), new Item(23713, 1),  new Item(28619, 100) }),
        THIEVING("<img=30> Thieving 1", "100 achievement tokens", (player) -> player.menwoman + "/50", (player) -> player.menwoman >= 50, "Pickpocket 50 men/women", new Item[] { new Item(995, 500000), new Item(23713, 1),  new Item(28619, 100) }),
        CRATING("<img=30> Crafting 1", "100 achievement tokens", (player) -> player.sapphirescut + "/50", (player) -> player.sapphirescut >= 50, "Cut 50 sapphires", new Item[] { new Item(995, 500000), new Item(23713, 1),  new Item(28619, 100) }),
        FLETCHING("<img=30> Fletching 1", "100 achievement tokens", (player) -> player.bowsmade + "/100", (player) -> player.bowsmade >= 100, "Cut 100 of any type of bow", new Item[] { new Item(995, 500000), new Item(23713, 1),  new Item(28619, 100) }),
        HUNTER("<img=30> Fletching 1", "100 achievement tokens", (player) -> player.bowsmade + "/100", (player) -> player.bowsmade >= 100, "Cut 100 of any type of bow", new Item[] { new Item(995, 500000), new Item(23713, 1),  new Item(28619, 100) }),
        YEW_LOGS( "<img=29> Woodcutting 2", "200 achievement tokens", (player) -> player.yewscut + "/250", (player) -> player.yewscut >= 250, "Cut 250 yew logs", new Item[] { new Item(995, 1000000), new Item(23714, 1) }),

        MAGIC_LOGS( "<img=28> Woodcutting 3", "400 achievement tokens", (player) -> player.magicscut + "/500", (player) -> player.magicscut >= 500, "Cut 500 magic logs", new Item[] { new Item(995, 5000000), new Item(23716, 1), new Item(28641, 1) });

        private final String achievementName;
        private final String bonusreward;
        //pass in player, outputs string
        private final Function<Player, String> processText;
        //pass in player, output boolean
        private final Predicate<Player> success;
        private final String taskinformation;
        private final Item[] reward;
        public static final AchievementList[] VALUES = values();


    }
}
