package com.rs.game.world.entity.npc.instances.TheHive;

import com.google.common.collect.Lists;
import com.rs.Constants;
import com.rs.game.map.WorldObject;
import com.rs.game.map.instance.RegionInstance;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.impl.TheHive.MassiveBeeHiveCombat;
import com.rs.game.world.entity.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class TheHive extends RegionInstance {


    private static final long serialVersionUID = 6537961569889692125L;

    private final Player owner;
    @Setter
    private boolean isStarted;

    private final List<Player> players = Lists.newArrayList();

    public TheHive(Player owner) {
        super(8, 368, 737);
        this.owner = owner;
        addPlayer(owner);
        constructRegion(e -> {
            spawnMonsters();
            return false;
        });
    }
    
    private final List<MinionNpc> npcs = Lists.newArrayList();

    private final List<NestNpc> nests = Lists.newArrayList();

    private BossNpc Queen_Bee;




    public void enter(Player player) {

        if (players.size() >= 11) {
            player.sm("There is already 5 people in the dungeon.");
            return;
        }

        player.setThehive(this);
        player.setForceMultiArea(true);
        player.setAtMultiArea(true);
        addPlayer(player);
        player.setNextPosition(getLocation(2954, 5910, 0));
        player.getControlerManager().startControler("TheHiveController");
    }

    public void leave(Player player) {
        if (getOwner() == player) {
            setStarted(false);
        }
        removePlayer(player);
        checkDungeon();
        player.setThehive(null);
        player.setForceMultiArea(false);
        player.setNextPosition(Constants.START_PLAYER_LOCATION);
    }

    public void hpscale () {
        Queen_Bee.setMaxHitpoints(Queen_Bee.getMaxHitpoints() / 2 * players.size());
        Queen_Bee.heal(10000);
    }



    private void checkDungeon() {
        if (players.isEmpty()) {
            npcs.forEach(NPC::finish);
            nests.forEach(NPC::finish);
            if (Queen_Bee != null)
                Queen_Bee.finish();
            destroyRegion();
        }
    }

    public void checkqueen() {
        if (getQueen_Bee().isFinished()) {
            npcs.forEach(NPC::finish);
            for (NestNpc nest: nests) {
                nest.sendDeath(owner);
            }

        }
    }

    private void addPlayer(Player player) {
        players.add(player);
    }

    private void removePlayer(Player player) {
        players.removeIf(other -> other.index() == player.index());
    }

    private void spawnMonsters() {
        nests.add(new NestNpc(16173, getLocation(2967, 5936, 0)));
        npcs.add(new MinionNpc(16167, getLocation(2963, 5937, 0)));
        npcs.add(new MinionNpc(16168, getLocation(2964, 5935, 0)));
        npcs.add(new MinionNpc(16169, getLocation(2962, 5937, 0)));

        nests.add(new NestNpc(16173, getLocation(2980, 5936, 0)));
        nests.add(new NestNpc(16173, getLocation(2989, 5933, 0)));
        npcs.add(new MinionNpc(16167, getLocation(2985, 5939, 0)));
        npcs.add(new MinionNpc(16168, getLocation(2985, 5934, 0)));
        npcs.add(new MinionNpc(16169, getLocation(2980, 5933, 0)));
        npcs.add(new MinionNpc(16167, getLocation(2993, 5925, 0)));

        nests.add(new NestNpc(16174, getLocation(3000, 5916, 0)));
        nests.add(new NestNpc(16174, getLocation(3000, 5903, 0)));
        nests.add(new NestNpc(16176, getLocation(2984, 5903, 0)));
        nests.add(new NestNpc(16176, getLocation(2984, 5916, 0)));

        Queen_Bee = new BossNpc(16135, getLocation(2991, 5909, 0));


    }


}


