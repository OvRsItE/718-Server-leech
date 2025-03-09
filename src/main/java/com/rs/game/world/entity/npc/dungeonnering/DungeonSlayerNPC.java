package com.rs.game.world.entity.npc.dungeonnering;

import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;

import java.util.ArrayList;
@SuppressWarnings("serial")
public class DungeonSlayerNPC extends DungeonNPC {

	private final DungeonManager manager;
	
	public DungeonSlayerNPC(int id, Position tile, DungeonManager manager, double multiplier) {
		super(id, tile, manager, multiplier);
		this.manager = manager;
	}

	@Override
	public void drop() {
		super.drop();
		int size = getSize();
		ArrayList<Item> drops = new ArrayList<Item>();
		if (getId() == 10694) {
			if (com.rs.utility.Utils.random(2) == 0)
				drops.add(new Item(17261));
			else if (com.rs.utility.Utils.random(10) == 0)
				drops.add(new Item(17263));
		} else if (getId() == 10695) {
			if (com.rs.utility.Utils.random(2) == 0)
				drops.add(new Item(17265));
			else if (com.rs.utility.Utils.random(10) == 0)
				drops.add(new Item(17267));
		} else if (getId() == 10696) {
			if (com.rs.utility.Utils.random(2) == 0)
				drops.add(new Item(17269));
			else if (com.rs.utility.Utils.random(10) == 0)
				drops.add(new Item(17271));
		} else if (getId() == 10697) {
			if (com.rs.utility.Utils.random(2) == 0)
				drops.add(new Item(17273));
		} else if (getId() == 10698) {
			if (com.rs.utility.Utils.random(10) == 0)
				drops.add(new Item(17279));
		} else if (getId() == 10699) {
			if (com.rs.utility.Utils.random(2) == 0)
				drops.add(new Item(17281));
			else if (com.rs.utility.Utils.random(10) == 0)
				drops.add(new Item(17283));
		} else if (getId() == 10700) {
			if (com.rs.utility.Utils.random(2) == 0)
				drops.add(new Item(17285));
			else if (com.rs.utility.Utils.random(10) == 0)
				drops.add(new Item(17287));
		} else if (getId() == 10701) {
			if (com.rs.utility.Utils.random(10) == 0)
				drops.add(new Item(17289));
		} else if (getId() == 10702) {
			if (com.rs.utility.Utils.random(10) == 0)
				drops.add(new Item(17293));
		} else if (getId() == 10704) {
			if (com.rs.utility.Utils.random(10) == 0)
				drops.add(new Item(17291));
		} else if (getId() == 10705) {
			if (com.rs.utility.Utils.random(10) == 0)
				drops.add(new Item(17295));
		}

		for (Item item : drops)
			World.addDungeoneeringGroundItem(manager.getParty(), item, new Position(getCoordFaceX(size), getCoordFaceY(size), getZ()), false);
	}

}
