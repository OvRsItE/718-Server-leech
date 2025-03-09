package com.rs.game.world.entity.player.content.chest;

import com.rs.game.item.Item;
import com.rs.game.item.LootTable;
import com.rs.game.item.WeightedItem;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;

import java.util.List;

/**
 * @author ReverendDread
 * Created 3/18/2021 at 7:43 PM
 * @project 718---Server
 */
public class ToxicChest {

    private static final int KEY_ID = 29477; //infernal key

    private static LootTable table = new LootTable().addItems(
            //common
            new WeightedItem(7937, 1000, 50),
            new WeightedItem(1514, 100, 50),
            new WeightedItem(2354, 50, 50),
            //uncommon
            new WeightedItem(15273, 100, 10),
            new WeightedItem(384, 100, 10),
            new WeightedItem(3145, 100, 10),
            new WeightedItem(1620, 50, 10),
            new WeightedItem(1616, 50, 10),
            new WeightedItem(1632, 25, 10),
            new WeightedItem(6694, 50, 10),
            new WeightedItem(565, 2000, 10),
            new WeightedItem(560, 2000, 10),
            new WeightedItem(5289, 10, 10),
            new WeightedItem(5316, 10, 10),
            new WeightedItem(5315, 10, 10),
            //rare
            new WeightedItem(29622, 150, 2),
            new WeightedItem(11212, 150, 2),
            new WeightedItem(11230, 150, 2),
            new WeightedItem(23352, 150, 2),
            new WeightedItem(537, 150, 2),
            new WeightedItem(18831, 150, 2),
            new WeightedItem(1382, 150, 2),
            new WeightedItem(8783, 150, 2),
            new WeightedItem(2, 1500, 2),
            new WeightedItem(9144, 150, 2),
            new WeightedItem(1514, 150, 2),
            //legendary
            new WeightedItem(995, 10000000, 1),
            new WeightedItem(29248, 1, 1),
            new WeightedItem(29244, 1, 1),
            new WeightedItem(42922, 1, 1),
            new WeightedItem(29484, 1, 1),
            new WeightedItem(29246, 1, 1)
    ).addGuaranteed(new WeightedItem(6969, 10));

    public static void open(Player player) {
        if (player.getInventory().hasItem(KEY_ID)) {
            player.getInventory().removeItems(new Item(KEY_ID, 1));
            player.toxicchest++;
            List<WeightedItem> items = table.getRandomItems(false);
            items.forEach(player.getInventory()::addItemDrop);
            for (WeightedItem item : items) {
                player.getItemCollectionManager().handleCollection(item);
                if (item.getWeight() == 1) {
                    World.sendWorldMessage(
                            "<img=23><col=F39407>" + player.getDisplayName() + "<col=F39407> Received<col=F39407> Very rare "
                                    + item.getAmount() + "x " + item.getName() + "<col=F39407> from the toxic chest amount opened: " + player.toxicchest,
                            false);
                }

            }
        } else {
            player.sendMessage("You don't have the key to open this chest.");
        }
    }

}
