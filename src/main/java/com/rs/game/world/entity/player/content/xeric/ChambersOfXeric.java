package com.rs.game.world.entity.player.content.xeric;

import com.google.common.collect.Lists;
import com.rs.Constants;
import com.rs.cache.loaders.ClientScriptMap;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.Summoning;
import com.rs.game.world.entity.player.content.activities.group.PlayerGroup;
import com.rs.game.world.entity.player.content.xeric.dungeon.XericDungeon;
import lombok.Getter;

import java.util.List;

/**
 * @author ReverendDread
 * Created 4/5/2021 at 7:11 AM
 * @project 718---Server
 */
public class ChambersOfXeric extends PlayerGroup {

    public static final String STARTED = "#XericStarted";
    public static final String POINTS = "#XericPoints";
    public static final String COMPLETE = "#XericComplete";
    public static final Position LOBBY_AREA = Position.of(1246, 3558);

    /**
     * The dungeon instance.
     */
    @Getter private final XericDungeon dungeon;

    /**
     * Constructs a new player group, with the desired player as its leader.
     * @param leader
     *          the leader.
     */
    public ChambersOfXeric(Player leader) {
        super(leader);
        setListener(new ChambersGroupListener(this));
        this.dungeon = new XericDungeon(this);
    }

    /**
     * Entering the dungeon via clicking on the dungeon doors.
     * @param player
     *      the player entering.
     */
    public void enterDungeon(Player player) {
        //Don't let players join if the dungeon has been started.
        if (!isLeader(player) && !join(player)) {
            return;
        }
        //Dont let players join if the raid started.
        if (getBoolean(STARTED)) {
            player.sendMessage("The raid has already begun. You can no longer enter.");
            return;
        }
        //Check for a familiar.
        if (player.getFamiliar() != null) {
            player.sendMessage("You can't take a familiar into " + name() + ".");
            return;
        }
        //TODO check private raid storage
        List<Item> blacklisted = hasBlacklistedItems(player);
        if (blacklisted.isEmpty()) {
            //Generate the dungeon layout
            if (isLeader(player) && dungeon.getMap() == null) {
                dungeon.generate();
                notifyAll("<col=ef20ff>Your group has entered the dungeons! Come join them now.", member -> !isLeader(member));
                player.sendMessage("<col=ef20ff>Inviting party...");
            }
            //If the dungeon has been generated, move the player into the lobby.
            if (dungeon.getMap() != null) {
                dungeon.join(player);
            } else { //otherwise tell them the leader has to join first.
                player.sendMessage("<col=ff0000>Only the leader of your group can begin the raid.");
            }
        } else {
            player.sendMessage("<col=ff0000>Your inventory contains the following items, that are blacklisted.");
            player.sendMessage("--------------------------------");
            for (Item item : blacklisted) {
                player.sendMessage("-- " + item.getAmount() + " " + item.getName() + ".");
            }
            player.sendMessage("--------------------------------");
        }
    }

    /**
     * Checks if a player has any blacklisted items.
     * @param player
     *          the player to check.
     * @return
     *          a list of items found that are blacklisted.
     */
    private List<Item> hasBlacklistedItems(Player player) {
        final List<Item> items = Lists.newArrayList();
        for (Item item : CoxConstants.blacklistedItems) {
            int amount = player.getInventory().contains(item.getId());
            if (amount > 0)
            items.add(new Item(item.getId(), amount));
        }
        for (Summoning.Pouches pouch : Summoning.Pouches.VALUES) {
            int amount = player.getInventory().contains(pouch.getPouchId());
            if (amount > 0)
            items.add(new Item(pouch.getPouchId(), amount));
        }
        return items;
    }

    @Override
    public String name() {
        return "Chambers of Xeric";
    }

    @Override
    public String notifyPrefix() {
        return "<col=00ff00>[" + name() + "]</col>: ";
    }

    static {
        ClientScriptMap.getMap(1666).ints().forEach((k, v) -> {
            ItemDefinitions.getItemDefinitions(v + Constants.OSRS_ITEMS_OFFSET).cox = true;
        });
    }

}
