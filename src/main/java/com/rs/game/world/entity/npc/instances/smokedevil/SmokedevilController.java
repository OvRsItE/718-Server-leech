package com.rs.game.world.entity.npc.instances.smokedevil;

import com.rs.Constants;
import com.rs.game.map.Position;
import com.rs.game.map.WorldObject;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.instances.newbosses.BossNpc;
import com.rs.game.world.entity.npc.randomspawns.SuperiorBosses;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.utility.Utils;

public class SmokedevilController extends Controller {

    SmokedevilInstance instance;
    BossNpc smokedevil;

    @Override
    public void start() {
        instance = (SmokedevilInstance) getArguments()[0];
        smokedevil = new BossNpc(20499, instance.getLocation(2876, 2538, 0));
        player.setForceMultiArea(true);
        player.setAtMultiArea(true);
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

    @Override
    public void processNPCDeath(NPC npc) {
        if (npc.getId() == 20499 && player.UnlockedSuperiorBosses == 1) {
            SuperiorBosses.SpawnSmokeDevil(player, npc);
            World.sendWorldMessage("[<col=ff0000>Boss Spawn</col>] - <col=ff0000>" + Utils.formatPlayerNameForDisplay(player.getUsername()) +
                    "<col=ff0000> has spawned a superior smoke devil boss they have 5 minutes to kill it. <col=ff0000>", false);
            smokedevil.setSpawned(true);
        }
        if (npc.getId() == 16142) {
            smokedevil.setSpawned(false);
            smokedevil.setRespawnTask();
        }

        super.processNPCDeath(npc);
    }

    private void finish() {
        instance.destroyRegion();
        player.setForceMultiArea(false);
        smokedevil.finish();
        instance = null;
        removeControler();
    }

}
