package com.rs.game.world.entity.player.content.xeric.dungeon.chamber.handler;

import com.rs.game.map.Direction;
import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.xeric.dungeon.chamber.Chamber;
import com.rs.utility.Misc;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

/**
 * @author ReverendDread
 * Created 4/6/2021 at 11:40 PM
 * @project 718---Server
 */
public class ScavengerChamber extends Chamber {

    private static final int[][][] scavengerSpawns = {
            {
                    {6, 16},
                    {10, 18},
                    {13, 20},
                    {19, 18},
                    {19, 15},
                    {16, 13},
            },
            {
                    {15, 5},
                    {13, 9},
                    {8, 13},
                    {5, 20},
                    {8, 20},
                    {14, 25},
            },
            {
                    {13, 7},
                    {11, 10},
                    {9, 16},
                    {11, 19},
                    {15, 16},
                    {19, 13},
            },
    };

    @Override
    public void onRaidStart() {
        LinkedList<int[]> spawns = new LinkedList<>(Arrays.asList(scavengerSpawns[getLayout()]));
        Collections.shuffle(spawns);
        int spawnCount = getSpawnCount();
        for (int i = 0; i < spawnCount; i++) {
            int[] spawnPoint = spawns.pop();
            int id = Misc.rollDie(50) ? NPC.asOSRS(7548) : NPC.asOSRS(7549);
            spawnNPC(id, spawnPoint[0], spawnPoint[1], Direction.SOUTH, 4);
        }
    }

    private int getSpawnCount() {
        if (getDungeon().getGroup().getSize() > 9)
            return 4;
        else if (getDungeon().getGroup().getSize() > 4)
            return 3;
        return 2;
    }

}
