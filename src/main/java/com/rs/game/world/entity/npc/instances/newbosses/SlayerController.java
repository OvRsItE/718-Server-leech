package com.rs.game.world.entity.npc.instances.newbosses;

import com.google.common.collect.Lists;
import com.rs.Constants;
import com.rs.game.map.Position;
import com.rs.game.map.WorldObject;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.instances.EliteDungeon.MinionNpc;
import com.rs.game.world.entity.player.controller.Controller;

import java.util.List;

public class SlayerController extends Controller {

    SlayerInstance instance;
    BossNpc npc;
    private final List<BossNpc> npcs = Lists.newArrayList();

    @Override
    public void start() {
        instance = (SlayerInstance) getArguments()[0];
        npcs.add(new BossNpc(player.getSlayerManager().getCurrentTask().getId(), instance.getLocation(2016, 1188, 0)));
        npcs.add(new BossNpc(player.getSlayerManager().getCurrentTask().getId(), instance.getLocation(2018, 1188, 0)));
        npcs.add(new BossNpc(player.getSlayerManager().getCurrentTask().getId(), instance.getLocation(2020, 1188, 0)));
        npcs.add(new BossNpc(player.getSlayerManager().getCurrentTask().getId(), instance.getLocation(2022, 1188, 0)));
        npcs.add(new BossNpc(player.getSlayerManager().getCurrentTask().getId(), instance.getLocation(2016, 1186, 0)));
        npcs.add(new BossNpc(player.getSlayerManager().getCurrentTask().getId(), instance.getLocation(2018, 1186, 0)));
        npcs.add(new BossNpc(player.getSlayerManager().getCurrentTask().getId(), instance.getLocation(2020, 1186, 0)));
        npcs.add(new BossNpc(player.getSlayerManager().getCurrentTask().getId(), instance.getLocation(2022, 1186, 0)));
        npcs.add(new BossNpc(player.getSlayerManager().getCurrentTask().getId(), instance.getLocation(2016, 1184, 0)));
        npcs.add(new BossNpc(player.getSlayerManager().getCurrentTask().getId(), instance.getLocation(2018, 1184, 0)));
        npcs.add(new BossNpc(player.getSlayerManager().getCurrentTask().getId(), instance.getLocation(2020, 1184, 0)));
        npcs.add(new BossNpc(player.getSlayerManager().getCurrentTask().getId(), instance.getLocation(2022, 1184, 0)));
        npcs.add(new BossNpc(player.getSlayerManager().getCurrentTask().getId(), instance.getLocation(2016, 1182, 0)));
        npcs.add(new BossNpc(player.getSlayerManager().getCurrentTask().getId(), instance.getLocation(2018, 1182, 0)));
        npcs.add(new BossNpc(player.getSlayerManager().getCurrentTask().getId(), instance.getLocation(2020, 1182, 0)));
        npcs.add(new BossNpc(player.getSlayerManager().getCurrentTask().getId(), instance.getLocation(2022, 1182, 0)));
        player.setForceMultiArea(true);
        player.setAtMultiArea(true);
    }

    @Override
    public void process() {
        if (player.getSlayerManager().getCurrentTask() == null) {
            for (BossNpc npc : npcs) {
                npc.setSpawned(true);
            }
        }
        super.process();
    }

    @Override
    public boolean processItemTeleport(Position destination) {
        finish();
        return true;
    }

    @Override
    public boolean processObjectTeleport(Position destination) {
        finish();
        return true;
    }

    @Override
    public boolean processMagicTeleport(Position destination) {
        finish();
        return true;
    }

    @Override
    public boolean logout() {
        player.setNextPosition(Constants.START_PLAYER_LOCATION);
        finish();
        return true;
    }

    @Override
    public boolean login() {
        player.setNextPosition(Constants.START_PLAYER_LOCATION);
        return true;
    }

    @Override
    public boolean sendDeath() {
        finish();
        return true;
    }

    @Override
    public boolean processObjectClick1(WorldObject object) {
        return false;
    }

    private void finish() {
        instance.destroyRegion();
        player.setForceMultiArea(false);
        npcs.forEach(NPC::finish);
        instance = null;
        removeControler();
    }

}
