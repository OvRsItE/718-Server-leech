package com.rs.game.world.entity.npc.instances.EliteDungeon;

import com.google.common.collect.Lists;
import com.rs.Constants;
import com.rs.game.map.instance.RegionInstance;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.npc.dungeonnering.DungeonBoss;
import com.rs.game.world.entity.player.CombatDefinitions;
import com.rs.game.world.entity.player.DominionTower;
import com.rs.game.world.entity.player.Player;
import com.rs.utility.NPCCombatDefinitionsL;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class EliteDungeonOne extends RegionInstance {

    private static final long serialVersionUID = -7763874210826198964L;

    private final Player owner;
    @Setter
    private boolean isStarted;

    private final List<Player> players = Lists.newArrayList();
    
    public EliteDungeonOne(Player owner) {
        super(8, 152, 784);
        this.owner = owner;
        addPlayer(owner);
        constructRegion(e -> {
            spawnMonsters();
            return false;
        });
    }
    
    private final List<MinionNpc> npcs = Lists.newArrayList();

    private final List<MinionNpc> minions_2nd_room = Lists.newArrayList();

    private final List<MinionNpc> minions_2nd_floor = Lists.newArrayList();

    private BossNpc vasa;

    private BossNpc tekton;

    private OlmClaws rightclaw;

    private OlmClaws leftclaw;

    private Olm olm;


    public void enter(Player player) {

        if (players.size() >= 6) {
            player.sm("There is already 5 people in the dungeon.");
            return;
        }

        player.setEliteDungeonOne(this);
        player.setForceMultiArea(true);
        player.setAtMultiArea(true);
        addPlayer(player);
        player.setNextPosition(getLocation(1227, 6308, 0));
        player.getControlerManager().startControler("EliteDungeonController");
    }

    public void leave(Player player) {
        if (getOwner() == player) {
            setStarted(false);
        }
        removePlayer(player);
        checkDungeon();
        player.setEliteDungeonOne(null);
        player.setForceMultiArea(false);
        player.setNextPosition(Constants.START_PLAYER_LOCATION);
    }

    public void hpscale () {
        vasa.setMaxHitpoints(vasa.getMaxHitpoints() / 2 * players.size());
        tekton.setMaxHitpoints(tekton.getMaxHitpoints() / 2 * players.size());
        olm.setMaxHitpoints(olm.getMaxHitpoints() / 2 * players.size());
        vasa.heal(10000);
        olm.heal(10000);
        tekton.heal(10000);
    }



    private void checkDungeon() {
        if (players.isEmpty()) {
            npcs.forEach(NPC::finish);
            minions_2nd_room.forEach(NPC::finish);
            minions_2nd_floor.forEach(NPC::finish);
            if (vasa != null)
                vasa.finish();
            if (tekton != null)
                tekton.finish();
            if (olm != null)
                olm.finish();
            destroyRegion();
        }
    }

    private void addPlayer(Player player) {
        players.add(player);
    }

    private void removePlayer(Player player) {
        players.removeIf(other -> other.index() == player.index());
    }

    private void spawnMonsters() {
        npcs.add(new MinionNpc(16157, getLocation(1241, 6319, 0)));
        npcs.add(new MinionNpc(16157, getLocation(1246, 6318, 0)));
        npcs.add(new MinionNpc(16157, getLocation(1240, 6316, 0)));
        npcs.add(new MinionNpc(16157, getLocation(1246, 6315, 0)));
        npcs.add(new MinionNpc(16157, getLocation(1241, 6312, 0)));
        npcs.add(new MinionNpc(16157, getLocation(1247, 6309, 0)));
        npcs.add(new MinionNpc(16157, getLocation(1242, 6305, 0)));
        npcs.add(new MinionNpc(16157, getLocation(1247, 6302, 0)));
        npcs.add(new MinionNpc(16157, getLocation(1241, 6300, 0)));
        vasa = new BossNpc(NPC.asOSRS(7566), getLocation(1240, 6282, 0));
        tekton = new BossNpc(NPC.asOSRS(7541), getLocation(1259, 6293, 1));
        olm = new Olm(16160, getLocation(1227, 6276, 1));
        //3rd room
        minions_2nd_room.add(new MinionNpc(16157, getLocation(1262, 6322, 0)));
        minions_2nd_room.add(new MinionNpc(16157, getLocation(1267, 6322, 0)));
        minions_2nd_room.add(new MinionNpc(16157, getLocation(1263, 6319, 0)));
        minions_2nd_room.add(new MinionNpc(16157, getLocation(1268, 6317, 0)));
        minions_2nd_room.add(new MinionNpc(16157, getLocation(1262, 6314, 0)));
        minions_2nd_room.add(new MinionNpc(16157, getLocation(1267, 6312, 0)));
        minions_2nd_room.add(new MinionNpc(16157, getLocation(1262, 6309, 0)));
        minions_2nd_room.add(new MinionNpc(16157, getLocation(1270, 6309, 0)));
        minions_2nd_room.add(new MinionNpc(16157, getLocation(1267, 6304, 0)));
        //2nd floor first room
        //2nd room
        rightclaw = new OlmClaws(16162, getLocation(1232, 6275, 1));
        leftclaw = new OlmClaws(16161, getLocation(1223, 6276, 1));
        minions_2nd_floor.add(new MinionNpc(16158, getLocation(1226, 6325, 1)));
        minions_2nd_floor.add(new MinionNpc(16158, getLocation(1223, 6323, 1)));
        minions_2nd_floor.add(new MinionNpc(16158, getLocation(1228, 6321, 1)));
        minions_2nd_floor.add(new MinionNpc(16158, getLocation(1226, 6325, 1)));
        minions_2nd_floor.add(new MinionNpc(16158, getLocation(1224, 6319, 1)));
        minions_2nd_floor.add(new MinionNpc(16158, getLocation(1226, 6325, 1)));
        minions_2nd_floor.add(new MinionNpc(16158, getLocation(1227, 6317, 1)));
        minions_2nd_floor.add(new MinionNpc(16158, getLocation(1223, 6315, 1)));
        minions_2nd_floor.add(new MinionNpc(16158, getLocation(1226, 6313, 1)));
    }


}


