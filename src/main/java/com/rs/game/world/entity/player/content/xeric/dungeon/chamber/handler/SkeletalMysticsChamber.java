package com.rs.game.world.entity.player.content.xeric.dungeon.chamber.handler;

import com.rs.game.map.Direction;
import com.rs.game.map.WorldObject;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.xeric.dungeon.chamber.Chamber;

/**
 * @author ReverendDread
 * Created 4/6/2021 at 11:50 PM
 * @project 718---Server
 */
public class SkeletalMysticsChamber extends Chamber {

    private static final int[][] blocks = {
            {6, 16},
            {13, 24},
            {20, 20},
    };

    private static final int[][][] mysticSpawns = {
            {// l 0
                    {14, 14},
                    {19, 17},
                    {17, 21},
                    {9, 21},
                    {25, 19},
            },
            { //l 1
                    {16, 10},
                    {10, 8},
                    {4, 8},
                    {10, 15},
                    {17, 18},
            },
            { //l 2
                    {15, 14},
                    {11, 20},
                    {10, 11},
                    {6, 14},
                    {10, 25},
            }
    };

    private int mystics;

    @Override
    public void onRaidStart() {
        WorldObject block = spawnObject(WorldObject.asOSRS(29796), blocks[getLayout()], 10, 0);
        mystics = getMysticsCount(getDungeon().getGroup().getSize());
        for (int i = 0; i < mystics; i++) {
            NPC mystic = spawnNPC(NPC.asOSRS(7604 + (i % 3)), mysticSpawns[getLayout()][i], Direction.SOUTH, 3);
            mystic.deathEndListener = ((entity, killer) -> {
               if (--mystics <= 0)
                   block.delete();
            });
        }
    }

    private static int getMysticsCount(int partySize) {
        if (partySize <= 3)
            return 3;
        else if (partySize < 7)
            return 4;
        else
            return 5;
    }

}
