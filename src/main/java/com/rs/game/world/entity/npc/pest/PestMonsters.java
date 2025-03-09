package com.rs.game.world.entity.npc.pest;

import java.util.ArrayList;
import java.util.List;

import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.activities.PestControl;
import com.rs.utility.Utils;

@SuppressWarnings("serial")
public class PestMonsters extends NPC {

    protected PestControl manager;
    protected int portalIndex;

    public PestMonsters(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned, int index, PestControl manager) {
	super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
	this.manager = manager;
	this.portalIndex = index;
	setForceMultiArea(true);
	setForceAgressive(true);
	setForceTargetDistance(70);
    }

    @Override
    public void processNPC() {
	super.processNPC();
	if (!getCombat().underCombat())
	    checkAgressivity();
    }

    @Override
    public ArrayList<Entity> getPossibleTargets() {
	ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
	List<Integer> playerIndexes = World.getRegion(getRegionId()).getPlayerIndexes();
	if (playerIndexes != null) {
	    for (int playerIndex : playerIndexes) {
		Player player = World.getPlayers().get(playerIndex);
		if (player == null || player.isDead() || player.isFinished() || !player.isRunning() || !player.withinDistance(this, 10))
		    continue;
		possibleTarget.add(player);
	    }
	}
	if (possibleTarget.isEmpty() || Utils.random(3) == 0) {
	    possibleTarget.clear();
	    possibleTarget.add(manager.getKnight());
	}
	return possibleTarget;
    }

    @Override
    public void sendDeath(Entity source) {
	super.sendDeath(source);
	manager.getPestCounts()[portalIndex]--;
    }
}
