package com.rs.game.world.entity.npc.randomspawns;

import com.google.common.collect.Lists;
import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.utility.Misc;
import com.rs.utility.Utils;

import java.util.List;

public class RandomSpawns {
    public static void SpawnRev() {

        List<Position> locations = Lists.newArrayList();
        //lava maze
        locations.add(Position.of(3008, 3869));
        locations.add(Position.of(3005, 3868));
        locations.add(Position.of(2996, 3854));
        locations.add(Position.of(2997, 3840));
        locations.add(Position.of(3016, 3830));
        locations.add(Position.of(2982, 3824));
        locations.add(Position.of(3030, 3831));
        locations.add(Position.of(3052, 3814));
        locations.add(Position.of(3056, 3805));
        locations.add(Position.of(3079, 3808));
        locations.add(Position.of(3083, 3804));
        locations.add(Position.of(3107, 3808));
        locations.add(Position.of(3119, 3813));
        locations.add(Position.of(3135, 3843));
        locations.add(Position.of(3135, 3869));
        locations.add(Position.of(3134, 3889));

        for (int index = 0; index < 1; index++) {

            int id = Utils.random(16106, 16111);

            Position pos = Misc.get(locations);

            NPC npc = new NPC(id, pos, -1, true);

            npc.setSpawned(true);
        }

    }
}
