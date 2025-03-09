package com.rs.game.world.entity.npc.pest;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.activities.PestControl;

@SuppressWarnings("serial")
public class Ravager extends PestMonsters {

    boolean destroyingObject = false;

    public Ravager(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned, int index, PestControl manager) {
	super(id, tile, -1, false, false, index, manager);
    }
    
    @Override
    public void processNPC() {
	super.processNPC();
	
    }
}
