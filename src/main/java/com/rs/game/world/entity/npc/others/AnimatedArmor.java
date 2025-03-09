package com.rs.game.world.entity.npc.others;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.activities.WarriorsGuild;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Utils;

@SuppressWarnings("serial")
public class AnimatedArmor extends NPC {

    private transient Player player;
    
    public AnimatedArmor(Player player, int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
	super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	this.player = player;
    }

    @Override
    public void processNPC() {
	super.processNPC();
	if (!getCombat().underCombat())
	    finish();
    }

    @Override
    public void sendDeath(final Entity source) {
	final NPCCombatDefinitions defs = getCombatDefinitions();
	resetWalkSteps();
	getCombat().removeTarget();
	animate(null);
	WorldTasksManager.schedule(new WorldTask() {
	    int loop;

	    @Override
	    public void run() {
		if (loop == 0) {
		    animate(new Animation(defs.getDeathEmote()));
		} else if (loop >= defs.getDeathDelay()) {
		    if (source instanceof Player) {
			Player player = (Player) source;
			for (Integer items : getDroppedItems()) {
			    if (items == -1)
				continue;
			    World.addGroundItem(new Item(items), new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), player, true, 60);
			}
			player.setWarriorPoints(3, WarriorsGuild.ARMOR_POINTS[getId() - 4278]);
		    }
		    finish();
		    stop();
		}
		loop++;
	    }
	}, 0, 1);
    }

    public int[] getDroppedItems() {
	int index = getId() - 4278;
	int[] droppedItems = WarriorsGuild.ARMOUR_SETS[index];
	if (Utils.getRandom(15) == 0) // 1/15 chance of losing
	    droppedItems[Utils.random(0, 2)] = -1;
	return droppedItems;
    }

    @Override
    public void finish() {
	if (isFinished())
	    return;
	super.finish();
	if (player != null) {
	    player.getTemporaryAttributtes().remove("animator_spawned");
	    if (!isDead()) {
		for (int item : getDroppedItems()) {
		    if (item == -1)
			continue;
		    player.getInventory().addItem(item, 1);
		}
	    }
	}
    }
}
