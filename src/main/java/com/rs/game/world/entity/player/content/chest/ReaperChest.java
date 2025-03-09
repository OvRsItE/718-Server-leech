package com.rs.game.world.entity.player.content.chest;

import com.rs.game.item.Item;
import com.rs.game.item.LootTable;
import com.rs.game.item.WeightedItem;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.utility.Utils;

import java.util.List;

/**
 * @author ReverendDread
 * Created 3/18/2021 at 7:43 PM
 * @project 718---Server
 */
public class ReaperChest {

    private static final int KEY_ID = 28651; //brimstone key

    private static LootTable table = new LootTable().addItems(
            //common
            new WeightedItem(1618, Utils.random(25, 35), 100),
            new WeightedItem(1620, Utils.random(25, 35), 100),
            new WeightedItem(454, Utils.random(300, 500), 100),
            new WeightedItem(995, Utils.random(50000, 150000), 100),
            new WeightedItem(1618, Utils.random(25, 35), 100),
            new WeightedItem(445, Utils.random(100, 200), 100),
            new WeightedItem(11237, Utils.random(50, 200), 75),
            new WeightedItem(441, Utils.random(350, 500), 75),
            new WeightedItem(1164, Utils.random(2, 4), 75),
            new WeightedItem(1128, Utils.random(1, 2), 75),
            new WeightedItem(1080, Utils.random(1, 2), 75),
            new WeightedItem(390, Utils.random(80, 160), 75),
            //uncommon
            new WeightedItem(452, Utils.random(10, 15), 10),
            new WeightedItem(2354, Utils.random(300, 500), 10),
            new WeightedItem(1514, Utils.random(120, 160), 10),
            new WeightedItem(11232, Utils.random(40, 160), 10),
            new WeightedItem(5289, Utils.random(2, 4), 10),
            new WeightedItem(5316, Utils.random(2, 4), 10),
            new WeightedItem(5304, Utils.random(3, 5), 10),
            new WeightedItem(5300, Utils.random(3, 5), 10),
            new WeightedItem(5295, Utils.random(3, 5), 10),
            new WeightedItem(6571, Utils.random(1, 1), 10),
            new WeightedItem(29110, Utils.random(1, 1), 10),
            new WeightedItem(29111, Utils.random(1, 1), 10),
            new WeightedItem(29112, Utils.random(1, 1), 10),
            new WeightedItem(29113, Utils.random(1, 1), 10),
            new WeightedItem(7937, Utils.random(3000, 6000), 10),
            //rare
            //very rare
            new WeightedItem(28654, 1, 1),
            new WeightedItem(28648, 1, 1)
    ).addGuaranteed(new WeightedItem(6969, 10));

    public static void open(Player player) {
        if (player.getInventory().hasItem(KEY_ID)) {
            player.getInventory().removeItems(new Item(KEY_ID, 1));
            player.reaperchests++;
            List<WeightedItem> items = table.getRandomItems(false);
            items.forEach(player.getInventory()::addItemDrop);
            for (WeightedItem item : items) {
                player.getItemCollectionManager().handleCollection(item);
                if (item.getWeight() == 1) {
                    World.sendWorldMessage(
                            "<img=23><col=F39407>" + player.getDisplayName() + "<col=F39407> Received<col=F39407> Very rare "
                                    + item.getAmount() + "x " + item.getName() + "<col=F39407> from the reaper chest amount opened: " + player.reaperchests,
                            false);
                }

            }
        } else {
            player.sendMessage("You don't have the key to open this chest.");
        }
    }

}
