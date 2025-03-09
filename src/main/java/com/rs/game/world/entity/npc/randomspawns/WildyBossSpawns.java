package com.rs.game.world.entity.npc.randomspawns;

import com.google.common.collect.Lists;
import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.actions.objects.Wildy;
import com.rs.utility.Misc;
import com.rs.utility.Utils;

import java.util.List;

public class WildyBossSpawns {
    public static boolean veneatispawned;
    public static boolean getSpawnedv() {
        return veneatispawned;
    }

    public static boolean callistospawned;
    public static boolean getSpawnedc() {
        return callistospawned;
    }

    public static boolean vetionspawned;
    public static boolean getSpawnedvv() {
        return vetionspawned;
    }

    public static void SpawnWildyBoss() {

        List<Position> locations = Lists.newArrayList();
        //lava maze
        locations.add(Position.of(3056, 3789, 0));



            int id = (16008);
            Position pos = Misc.get(locations);
            NPC npc = new NPC(id, pos, -1, true);
            npc.setSpawned(true);
            veneatispawned = true;

            if (npc.isSpawned() == true) {
                return;
            }


    }
    public static void SpawnCursedCallsito() {

        List<Position> locations = Lists.newArrayList();
        //lava maze
        locations.add(Position.of(3056, 3789, 0));



        int id = (16007);
        Position pos = Misc.get(locations);
        NPC npc = new NPC(id, pos, -1, true);
        npc.setSpawned(true);
        callistospawned = true;

        if (npc.isSpawned() == true) {
            return;
        }


    }
    public static void SpawnCursedvetion() {

        List<Position> locations = Lists.newArrayList();
        //lava maze
        locations.add(Position.of(3056, 3789, 0));



        int id = (16011);
        Position pos = Misc.get(locations);
        NPC npc = new NPC(id, pos, -1, true);
        npc.setSpawned(true);
        vetionspawned = true;

        if (npc.isSpawned() == true) {
            return;
        }


    }
}
