package com.rs.game.world.entity.player.content;

import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.dialogue.impl.TheOverseer;
import com.rs.utility.Misc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ReverendDread
 * Created 3/10/2021 at 7:33 PM
 * @project 718---Server
 */
public class Unsired {

    public static int roll(Player player) {
        int roll = Misc.get(127);
        if (roll < 5)
            return 13262; // abyssal orphan (pet)
        if (roll < 15)
            return 7979; // abyssal head
        if (roll < 41)
            return 13265; // abyssal dagger
        if (roll < 53)
            return 4151; // whip
        if (roll < 66)
            return 13277; // jar of miasma
        return getNextBludgeonPiece(player); // 66-127 -> bludgeon piece
    }

    private static int getNextBludgeonPiece(Player player) {
        int claws = 0, spines = 0, axons = 0;
        for (Item item : player.getBank().getContainerCopy()) {
            if (item == null)
                continue;
            if (item.getId() == TheOverseer.BLUDGEON_SPINE.getId())
                spines += item.getAmount();
            else if (item.getId() == TheOverseer.BLUDGEON_CLAW.getId())
                claws += item.getAmount();
            else if (item.getId() == TheOverseer.BLUDGEON_AXON.getId())
                axons += item.getAmount();
        }
        for (Item item : player.getInventory().getItems().getItems()) {
            if (item == null)
                continue;
            if (item.getId() == TheOverseer.BLUDGEON_SPINE.getId())
                spines++;
            else if (item.getId() == TheOverseer.BLUDGEON_CLAW.getId())
                claws++;
            else if (item.getId() == TheOverseer.BLUDGEON_AXON.getId())
                axons++;
        }
        int lowest = Math.min(Math.min(claws, axons), spines);
        List<Integer> possible = new ArrayList<>();
        if (lowest == spines)
            possible.add(13274);
        if (lowest == claws)
            possible.add(13275);
        if (lowest == axons)
            possible.add(13276);
        return Misc.get(possible);
    }

}
