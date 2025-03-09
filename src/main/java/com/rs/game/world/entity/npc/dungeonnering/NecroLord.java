package com.rs.game.world.entity.npc.dungeonnering;

import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.Drop;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.player.content.skills.dungeoneering.RoomReference;
import com.rs.game.world.entity.updating.impl.Graphics;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("serial")
public class NecroLord extends DungeonBoss {

	private int resetTicks;
	private List<SkeletalMinion> skeletons;

	public NecroLord(int id, Position tile, DungeonManager manager, RoomReference reference) {
		super(id, tile, manager, reference, 0.6D);
		setCantFollowUnderCombat(true); //force can't walk
		setLureDelay(Integer.MAX_VALUE);//doesn't stop focusing on target
		skeletons = new CopyOnWriteArrayList<SkeletalMinion>();
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (!isUnderCombat() && skeletons != null && skeletons.size() > 0) {
			resetTicks++;
			if (resetTicks == 50) {
				resetSkeletons();
				resetTicks = 0;
				return;
			}
		}
	}

	@Override
	public double getMagePrayerMultiplier() {
		return 0.6;
	}

	public void addSkeleton(Position tile) {
		SkeletalMinion npc = new SkeletalMinion(this, 11722, tile, getManager(), getMultiplier() / 2);
		npc.setForceAgressive(true);
		skeletons.add(npc);
		World.sendGraphics(npc, new Graphics(2399), tile);
	}

	public void resetSkeletons() {
		for (SkeletalMinion skeleton : skeletons)
			skeleton.sendDeath(this);
		skeletons.clear();
	}

	public void removeSkeleton(DungeonNPC sk) {
		skeletons.remove(sk);
	}

	/*
	 * because necrolord room has a safespot which shouldnt
	 */
	@Override
	public boolean clipedProjectile(Position tile, boolean checkClose, int size) {
		//because npc is under cliped data
		return getManager().isAtBossRoom(tile);
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		resetSkeletons();
	}

	@Override
	public void sendDrop(Player player, Drop drop) {
		Item item = new Item(drop.getItemId());
		player.getInventory().addItemDrop(item.getId(), item.getAmount());
		
	}
}
