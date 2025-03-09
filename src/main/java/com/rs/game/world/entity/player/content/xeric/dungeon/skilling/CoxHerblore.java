package com.rs.game.world.entity.player.content.xeric.dungeon.skilling;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;
import lombok.RequiredArgsConstructor;

public class CoxHerblore {

    public static final int EMPTY_GOURD_VIAL = 20800;
    public static final int WATER_FILLED_GOURD_VIAL = 20801;

    /**
     * Potion brewing
     */
    public enum Potions {

        /* combat potions */
        ELDER(20905, 20910, new int[]{70, 59, 47}, new int[]{20924, 20920, 20916}),
        TWISTED(20905, 20912, new int[]{70, 59, 47}, new int[]{20936, 20932, 20928}),
        KODAI(20905, 20911, new int[]{70, 59, 47}, new int[]{20948, 20944, 20940}),

        /* restore potions */
        REVITALISATION(20908, 20910, new int[]{78, 65, 52}, new int[]{20960, 20956, 20952}),
        PRAYER_ENHANCE(20908, 20912, new int[]{78, 65, 52}, new int[]{20972, 20968, 20964}),
        XERIC_ACID(20908, 20911, new int[]{78, 65, 52}, new int[]{20984, 20980, 20976}),

        /* overload */
        OVERLOAD(new int[][]{
                {20924, 20936, 20948},
                {20920, 20932, 20944},
                {20916, 20928, 20940}}, 20902, new int[]{90, 75, 60}, new int[]{20996, 20992, 20988});

        public int herbId, secondaryId;
        public int[] levelReqs, potionIds;
        public int[][] secondaryPotions;

        Potions(int herbId, int secondaryId, int[] levelReqs, int[] potionIds) {
            this.herbId = herbId;
            this.secondaryId = secondaryId;
            this.levelReqs = levelReqs;
            this.potionIds = potionIds;
        }

        Potions(int[][] secondaryPotions, int herbId, int[] levelReqs, int[] potionIds) {
            this.secondaryPotions = secondaryPotions;
            this.herbId = herbId;
            this.levelReqs = levelReqs;
            this.potionIds = potionIds;;
        }

    }

    @RequiredArgsConstructor
    public enum Herbs {

        NOXIFER(50901, 50902),
        GOLPAR(50904, 50905),
        BUCHU_LEAF(50907, 50908);

        private final int grimyId, cleanId;

        private static final Herbs[] VALUES = values();

        public static Herbs forItemId(int itemId) {
            for (Herbs herb : VALUES) {
                if (herb.grimyId == itemId)
                    return herb;
            }
            return null;
        }

        public void clean(Player player) {
            player.getInventory().deleteItem(Item.asOSRS(grimyId), 1);
            player.getInventory().addItem(Item.asOSRS(cleanId), 1);
            player.getSkills().addXp(Skills.HERBLORE, 2);
            player.getPackets().sendGameMessage("You clean the " + ItemDefinitions.getItemDefinitions(cleanId).getName(), true);
        }

    }

}
