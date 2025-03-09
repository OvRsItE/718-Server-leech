package com.rs.game.world.entity.npc.others;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.godwars.bandos.GodwarsBandosFaction;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.activities.WarriorsGuild;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.utility.Utils;

@SuppressWarnings("serial")
public class Cyclopse extends GodwarsBandosFaction {

    public Cyclopse(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
	super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, false);
    }

    @Override
    public void sendDeath(Entity source) {
	super.sendDeath(source);
	if (source instanceof Player) {
	    WarriorsGuild.killedCyclopses++;
	    final NPC npc = this;
	    final Player player = (Player) source;
	    Controller controler = player.getControlerManager().getControler();
	    if (controler == null || !(controler instanceof WarriorsGuild) || Utils.random(15) != 0) 
		return;
	    WorldTasksManager.schedule(new WorldTask() {

		@Override
		public void run() {
		    World.addGroundItem(new Item(WarriorsGuild.getBestDefender(player)), new Position(getCoordFaceX(npc.getSize()), getCoordFaceY(npc.getSize()), getZ()), player, true, 60);
		}
	    }, getCombatDefinitions().getDeathDelay());
	}
    }
}
