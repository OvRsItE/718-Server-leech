package com.rs.game.world.entity.npc.dungeonnering;


import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.familiar.Familiar;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.player.content.skills.dungeoneering.RoomReference;
import com.rs.game.world.entity.updating.impl.Hit;

@SuppressWarnings("serial")
public class WarpedGulega extends DungeonBoss {

	public WarpedGulega(int id, Position tile, DungeonManager manager, RoomReference reference) {
		super(id, tile, manager, reference);
	}

	//thats default lol
	/* @Override
	 public double getMeleePrayerMultiplier() {
	return 0.0;//Fully block it.
	 }
	 
	 @Override
	 public double getRangePrayerMultiplier() {
	return 0.0;//Fully block it.
	 }
	 
	 @Override
	 public double getMagePrayerMultiplier() {
	return 0.0;//Fully block it.
	 }*/

	@Override
	public void handleIngoingHit(Hit hit) {
		if (!(hit.getSource() instanceof Familiar))
			hit.setDamage((int) (hit.getDamage() * 0.45D));
		super.handleIngoingHit(hit);
	}
}
