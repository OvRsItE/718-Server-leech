package com.rs.game.world.entity.player.content;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.utility.Misc;
import com.rs.utility.Utils;

public class MysteryBox {

    private static final int COMMON[][] = {{995, 25000}, {685, 1}, {1623, 1}, {559, 1}, {1925, 1}, {1965, 1}, {956, 1}, {1061, 1}, {1957, 1}, {592, 1}, {359, 1}};
    private static final int UNCOMMON[][] = {{2802, 1}, {1621, 1}, {7937, 30}, {1119, 1}, {1442, 1}, {1444, 1}, {9666, 1}, {9672, 1}, {9674, 1}, {9676, 1}, {9194, Misc.random(1, 73)}, {2639, 1},{2641, 1},{2653,1 },{2655,1}, {2657, 1},{2659, },{2661, 1},{2663,1},{ 2665, 1},{2667, 1},{2669,1},{ 2671, 1},{2673, 1},{2675,1 },{2643,1},{ 6857, 1},{6859, 1},{6861, 1},{6863,1},{9470, 1},{10400, 1},{Misc.random(554, 565), 1000} ,{10402, 1},{10404, 1},{10406, },{10408, 1},{10410, 1},{10412, },{10414,1 },{10416,1},{10418, 1},{10420, },{10422, },{10424, 1},{10426, 1}};
    private static final int RARE[][] = {{2724, 1}, {563, 10}, {561, 20}, {1329, 1}, {1315, 1}, {1123, 1}, {1454, 1}, {15511, 1}, {15509, 1}, {15505, 1}, {15507, 1}, {15503, 1}, {11212, Misc.random(40, 144)}, {11237, Misc.random(64, 148)}, {9193, Misc.random(62, 70)}, {11230, Misc.random(182, 319)}, {11232, Misc.random(70, 214)}, {1306, Misc.random(1, 2)}, {1249, 1}, {1632, 5}, {1616, 6}, {9341, Misc.random(40, 70)}, {9342, Misc.random(50, 57)}, {2364, 10}};
    private static final int VERY_RARE[][] = {{19039, 1}, {985, 1}, {987, 1}, {995, 3500000}, {7158, 1}, {2366, 1}, {6571, 1}, {7776, 1}, {990, 3}, {4151, 1}, {11235, 1}, {4708, 1}, {4710, 1}, {4712, 1}, {4714, 1}, {4716, 1}, {4718, 1}, {4720, 1}, {4722, 1}, {4724, 1}, {4726, 1}, {4728, 1}, {4730, 1}, {4732, 1}, {4734, 1}, {4736, 1}, {4738, 1}, {4740, Misc.random(100, 1000)}, {4745, 1}, {4747, 1}, {4749, 1}, {4751, 1}, {4753, 1}, {4755, 1}, {4757, 1}, {4759, 1}, {13858, 1}, {13861, 1}, {13864, 1}, {13867, 1}, {13884, 1}, {13887, 1}, {13890, 1}, {13893, 1}, {13896, 1}, {13899, 1}, {10887, 1}, {11732, 1}, };
    private static int reward, r, c, q;

    private static void handle(Player p) {
        r = Utils.random(10);
        p.getInventory().deleteItem(18768, 1);
        p.getPackets().sendSound(98, 0, 1);

        switch (r) {
            case 1:
                r = Utils.random(VERY_RARE.length);
                q = VERY_RARE[r][1];
                p.getInventory().addItem(VERY_RARE[r][0], q);

                reward = VERY_RARE[r][0];
                p.sm("You open the box and find " + getGrammar() + " " + getNameForItem() + ", awesome!");
                break;
            case 2:
            case 3:
                r = Utils.random(RARE.length);
                q = RARE[r][1];
                p.getInventory().addItem(RARE[r][0], q);
                reward = RARE[r][0];
                p.sm("You open the box and find " + getGrammar() + " " + getNameForItem() + ", excellent.");
                break;
            case 4:
            case 5:
            case 6:
                r = Utils.random(UNCOMMON.length);
                q = UNCOMMON[r][1];
                p.getInventory().addItem(UNCOMMON[r][0], q);
                reward = UNCOMMON[r][0];
                p.sm("You open the box and find " + getGrammar() + " " + getNameForItem() + ", not bad.");
                break;
            default:
                c = Utils.random(COMMON[0][1]);
                r = Utils.random(COMMON.length);
                q = COMMON[r][1];
                p.getInventory().addItem(COMMON[r][0], COMMON[r][0] == (995) ? c : q);
                reward = COMMON[r][0];
                p.sm((String) "You open the box and find " + (COMMON[r][0] == (995) ? c : getGrammar()) + " "
                        + getNameForItem() + ", " + (COMMON[r][0] == (995) ? "sweet!" : "better luck next time."));
                break;
        }
    }

    private static String getGrammar() {
        if (q == 1) {
            return sw("a") || sw("u") || sw("o") ? "an" : "a";
        }
        return q + "";
    }

    private static boolean sw(String n) {
        return getNameForItem().startsWith(n);
    }

    private static String getNameForItem() {
        switch (reward) {
            case 995:
                return COMMON[r][1] == (1) ? "coin" : "coins";
            case 1061:
                return "pair of leather boots";
            case 592:
                return "ash";
            case 563:
                return "law runes";
            case 561:
                return "nature runes";
            case 1329:
                return "mithril scimitar";
            case 1315:
                return "mithril two handed sword";
        }
        return ItemDefinitions.getItemDefinitions(reward).getName().toLowerCase();
    }

    public static boolean isBox(int itemId, Player p) {
        switch (itemId) {
            case 18768:
                handle(p);
                return true;
        }
        return false;
    }
}