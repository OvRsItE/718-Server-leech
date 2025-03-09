package com.rs.game.world.entity.npc.dungeonnering;

import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonConstants;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonUtils;
import com.rs.game.world.entity.player.content.skills.dungeoneering.RoomReference;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class ForgottenWarrior extends Guardian {

	private final DungeonManager manager;
	
	public ForgottenWarrior(int id, Position tile, DungeonManager manager, RoomReference reference, double multiplier) {
		super(id, tile, manager, reference, multiplier);
		this.manager = manager;
	}

	@Override
	public void drop() {
		super.drop();
		int size = getSize();
		ArrayList<Item> drops = new ArrayList<Item>();
		//just 1 for now
		for (int type = 0; type < DungeonConstants.FORGOTTEN_WARRIORS.length; type++) {
			for (int id : DungeonConstants.FORGOTTEN_WARRIORS[type])
				if (id == getId()) {
					int tier = getManager().getParty().getAverageCombatLevel() / 10;
					if (tier > 10)
						tier = 10;
					else if (tier < 1)
						tier = 1;
					//melee mage range
					if (type == 0)
						drops.add(new Item(DungeonUtils.getRandomMeleeGear(com.rs.utility.Utils.random(tier) + 1)));
					else if (type == 1)
						drops.add(new Item(DungeonUtils.getRandomMagicGear(com.rs.utility.Utils.random(tier) + 1)));
					else
						drops.add(new Item(DungeonUtils.getRandomRangeGear(com.rs.utility.Utils.random(tier) + 1)));
					break;
				}
		}
		for (Item item : drops)
			World.addDungeoneeringGroundItem(manager.getParty(), item, new Position(getCoordFaceX(size), getCoordFaceY(size), getZ()), false);
	}

}
