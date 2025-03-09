package com.rs.game.world.entity.npc.instances.newbosses;

import com.rs.Constants;
import com.rs.game.map.Position;
import com.rs.game.map.WorldObject;
import com.rs.game.world.entity.npc.instances.Corp.CorpInstance;
import com.rs.game.world.entity.npc.instances.Corp.CorpNpc;
import com.rs.game.world.entity.player.controller.Controller;

public class SunfreetController extends Controller {

    SunfreetInstance instance;
    BossNpc npc;

    @Override
    public void start() {
        instance = (SunfreetInstance) getArguments()[0];
        npc = new BossNpc(15222, instance.getLocation(2018, 1185, 0));
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

    private void finish() {
        instance.destroyRegion();
        player.setForceMultiArea(false);
        npc.finish();
        instance = null;
        removeControler();
    }

}
