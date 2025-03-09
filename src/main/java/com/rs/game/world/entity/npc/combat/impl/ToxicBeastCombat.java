package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;

/**
 * @author ReverendDread
 * Created 3/18/2021 at 7:37 PM
 * @project 718---Server
 */
public class ToxicBeastCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[] { "Toxic beast" };
    }

    @Override
    public int attack(NPC npc, Entity target) {
        return 0;
    }

}
