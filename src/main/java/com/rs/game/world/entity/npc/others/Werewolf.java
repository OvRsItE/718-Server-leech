package com.rs.game.world.entity.npc.others;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Utils;

@SuppressWarnings("serial")
public class Werewolf extends NPC {

    private int realId;

    public Werewolf(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
	super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
	realId = id;
    }

    public boolean hasWolfbane(Entity target) {
	if(target instanceof NPC)
	    return false;
	return ((Player) target).getEquipment().getWeaponId() == 2952;
    }
    @Override
    public void processNPC() {
	if (isDead() || isCantInteract())
	    return;
	if(isUnderCombat() && getId() == realId && Utils.random(5) == 0) {
	    final Entity target = getCombat().getTarget();
	    if(!hasWolfbane(target)) {
		    animate(new Animation(6554));
		    setCantInteract(true);
		    WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
			    setNextNPCTransformation(realId-20);
			    animate(new Animation(-1));
			    setCantInteract(false);
			    setTarget(target);
			}
		    }, 1);
		    return;
	    }
	}
	super.processNPC();
    }

    @Override
    public void reset() {
	setNPC(realId);
	super.reset();
    }

}
