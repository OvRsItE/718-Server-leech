package com.rs.game.world.entity.npc.randomspawns;

import com.google.common.collect.Lists;
import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.impl.HydraCombat;
import com.rs.game.world.entity.npc.instances.hydra.AlchemicalHydraNPC;
import com.rs.game.world.entity.npc.instances.hydra.HydraController;
import com.rs.game.world.entity.npc.slayer.SuperiorMonster;
import com.rs.game.world.entity.player.Player;
import com.rs.utility.Misc;

import java.util.List;

public class SuperiorBosses {

    public static boolean HydraSpawned;
    public static boolean getspawnedhydra() {
        return HydraSpawned;
    }

    public static void SpawnHydra(Player player, NPC npc) {

            int id = (16140);

           Position pos = npc.getPosition();


            new com.rs.game.world.entity.npc.slayer.SuperiorBosses(player, id, pos, npc.getMapAreaNameHash(),
                npc.canBeAttackFromOutOfArea(), true);



    }

    public static void SpawnCerberus(Player player, NPC npc) {

        int id = (16146);

        Position pos = npc.getPosition();

        new com.rs.game.world.entity.npc.slayer.SuperiorBosses(player, id, pos, npc.getMapAreaNameHash(),
                npc.canBeAttackFromOutOfArea(), true);



    }

    public static void SpawnSire(Player player, NPC npc) {

        int id = (16148);

        Position pos = npc.getPosition();

        new com.rs.game.world.entity.npc.slayer.SuperiorBosses(player, id, pos, npc.getMapAreaNameHash(),
                npc.canBeAttackFromOutOfArea(), true);



    }

    public static void SpawnKraken(Player player, NPC npc) {

        int id = (16144);

        Position pos = npc.getPosition();

        new com.rs.game.world.entity.npc.slayer.SuperiorBosses(player, id, pos, npc.getMapAreaNameHash(),
                npc.canBeAttackFromOutOfArea(), true);



    }

    public static void SpawnSmokeDevil(Player player, NPC npc) {

        int id = (16142);

        Position pos = npc.getPosition();

        new com.rs.game.world.entity.npc.slayer.SuperiorBosses(player, id, pos, npc.getMapAreaNameHash(),
                npc.canBeAttackFromOutOfArea(), true);



    }


    }

