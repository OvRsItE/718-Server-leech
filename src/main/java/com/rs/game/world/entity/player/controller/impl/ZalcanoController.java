package com.rs.game.world.entity.player.controller.impl;

import com.rs.game.map.Position;
import com.rs.game.map.WorldObject;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.activities.zalcano.ZalcanoGame;
import com.rs.game.world.entity.player.controller.Controller;

/**
 * @author ReverendDread
 * Created 3/12/2021 at 6:35 AM
 * @project 718---Server
 */
public class ZalcanoController extends Controller {

    @Override
    public void start() {

    }

    @Override
    public boolean processItemTeleport(Position destination) {
        leaveSafely();
        return true;
    }

    @Override
    public boolean processObjectTeleport(Position destination) {
        leaveSafely();
        return true;
    }

    @Override
    public boolean processMagicTeleport(Position destination) {
        leaveSafely();
        return true;
    }

    @Override
    public boolean processObjectClick1(WorldObject object) {
        return true;
    }

    @Override
    public boolean sendDeath() {
        leaveSafely();
        return true;
    }

    private void leaveSafely() {
        removeControler();
    }

}
