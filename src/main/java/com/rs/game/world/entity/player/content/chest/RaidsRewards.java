package com.rs.game.world.entity.player.content.chest;

import com.rs.game.item.Item;
import com.rs.game.item.LootTable;
import com.rs.game.item.WeightedItem;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.utility.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ReverendDread
 * Created 3/18/2021 at 7:43 PM
 * @project 718---Server
 */
public class RaidsRewards {

    private static final int KEY_ID = 53083; //brimstone key

    private static LootTable table = new LootTable().addItems(
            //common
            new WeightedItem(565, Utils.random(500, 1000), 100),
            new WeightedItem(560, Utils.random(500, 1000), 100),
            new WeightedItem(566, Utils.random(500, 1000), 100),
            new WeightedItem(892, Utils.random(1000, 3000), 100),
            new WeightedItem(11212, Utils.random(100, 648), 100),
            new WeightedItem(208, Utils.random(100, 163), 100),
            new WeightedItem(3050, Utils.random(100, 248), 75),
            new WeightedItem(210, Utils.random(100, 400), 75),
            new WeightedItem(212, Utils.random(100, 404), 75),
            new WeightedItem(214, Utils.random(1, 338), 75),
            new WeightedItem(3052, Utils.random(50, 97), 75),
            new WeightedItem(216, Utils.random(100, 394), 75),
            new WeightedItem(2486, Utils.random(100, 394), 75),
            new WeightedItem(218, Utils.random(100, 600), 75),
            new WeightedItem(220, Utils.random(100, 161), 75),
            //uncommon
            new WeightedItem(453, Utils.random(500, 2000), 10),
            new WeightedItem(445, Utils.random(500, 2000), 10),
            new WeightedItem(448, Utils.random(500, 2000), 10),
            new WeightedItem(450, Utils.random(100, 400), 10),
            new WeightedItem(452, Utils.random(30, 65), 10),
            new WeightedItem(1624, Utils.random(100, 400), 10),
            new WeightedItem(1622, Utils.random(100, 700), 10),
            new WeightedItem(1620, Utils.random(100, 300), 10),
            new WeightedItem(1618, Utils.random(100, 255), 10),
            new WeightedItem(8781, Utils.random(100, 600), 10),
            new WeightedItem(8783, Utils.random(100, 500), 10),
            //rare
            new WeightedItem(29745, 1, 3),
            new WeightedItem(14484, 1, 3),
            new WeightedItem(29747, 1, 3),
            new WeightedItem(29897, 1, 3),
            new WeightedItem(29743, 1, 3),
            new WeightedItem(29864, 1, 3),
            new WeightedItem(29491, 1, 2),
            new WeightedItem(29493, 1, 2),
            new WeightedItem(29489, 1, 2),
            //very rare
            new WeightedItem(29943, 1, 1),
            new WeightedItem(51003, 1, 1),
            new WeightedItem(28644, 1, 1),
            new WeightedItem(29608, 1, 1),
            new WeightedItem(28825, 1, 1),
            new WeightedItem(28831, 1, 1),
            new WeightedItem(29868, 1, 1)
    ).addGuaranteed(new WeightedItem(6969, 10));

    public static void open(Player player) {
            List<WeightedItem> items = table.getRandomItems(false);
            items.forEach(item -> {
                player.getItemCollectionManager().handleCollection(item);
                player.getInventory().addItemOrBank(item);

                if (item.getWeight() <= 4) {
                    World.sendWorldMessage(
                            "<img=23><col=F39407>" + player.getDisplayName() + "<col=F39407> Received<col=F39407> Rare "
                                    + item.getAmount() + "x " + item.getName() + "<col=F39407> from a Elite dungeon completed: "+ player.elitedungeonone,
                            false);
                }
            });
            player.elitedungeonone++;
    }

}
