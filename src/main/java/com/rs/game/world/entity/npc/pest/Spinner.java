package com.rs.game.world.entity.npc.pest;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.activities.PestControl;
import com.rs.game.world.entity.player.content.toxin.ToxinType;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;

@SuppressWarnings("serial")
public class Spinner extends PestMonsters {

    private byte healTicks;

    public Spinner(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned, int index, PestControl manager) {
	super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned, index, manager);
    }

    @Override
    public void processNPC() {
	PestPortal portal = manager.getPortals()[portalIndex];
	if (portal.isDead()) {
	    explode();
	    return;
	}
	if (!portal.isLocked) {
	    healTicks++;
	    if (!withinDistance(portal, 1))
		this.addWalkSteps(portal.getX(), portal.getY());
	    else if (healTicks % 6 == 0)
		healPortal(portal);
	}
    }

    private void healPortal(final PestPortal portal) {
	setNextFaceEntity(portal);
	WorldTasksManager.schedule(new WorldTask() {

	    @Override
	    public void run() {
		animate(new Animation(3911));
		setNextGraphics(new Graphics(658, 0, 96 << 16));
		if (portal.getHitpoints() != 0)
		    portal.heal((portal.getMaxHitpoints() / portal.getHitpoints()) * 45);
		healTicks = 0; /* Saves memory in the long run. Meh */
	    }
	});
    }

    private void explode() {
	final NPC npc = this;
	WorldTasksManager.schedule(new WorldTask() {

	    @Override
	    public void run() {
		for (Player player : manager.getPlayers()) {
		    if (!withinDistance(player, 7))
			continue;
		    player.getToxin().applyToxin(ToxinType.POISON, 50);
		    player.applyHit(new Hit(npc, 50, HitLook.REGULAR_DAMAGE));
		    npc.reset();
		    npc.finish();
		}
	    }
	}, 1);
    }
}
