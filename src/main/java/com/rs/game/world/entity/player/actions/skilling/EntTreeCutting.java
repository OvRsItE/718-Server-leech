package com.rs.game.world.entity.player.actions.skilling;

import java.util.Random;
import com.rs.game.item.Item;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.player.actions.Action;
import com.rs.game.world.entity.player.actions.objects.EvilTree;

public class EntTreeCutting extends Action {

	static Item[] listLogs = { new Item(1512, 1), new Item(1522, 1), new Item(1520, 1), new Item(1518, 1), new Item(1516, 1), new Item(1514, 1)};
	// TODO Item[] listRares  = mysterious emblems , slayer enchantment
		
	private static void addLog(Player player) {
		final int xp = 25;
		Random r = new Random();
		Item log = listLogs[r.nextInt(listLogs.length)];
		String logName = log.getName();
		player.getSkills().addXp(11, xp);
		player.getInventory().addItem(log);
		player.getPackets().sendGameMessage("You get some " + logName + ".", true);
	}
	
	private static boolean hasAxe(Player player) {
		if (player.getInventory().containsOneItem(1351, 1349, 1353, 1355, 1357, 1361, 1359, 6739, 13661))
			return true;
		int weaponId = player.getEquipment().getWeaponId();
		if (weaponId == -1)
			return false;
		switch (weaponId) {
		case 1351:// Bronze Axe
		case 1349:// Iron Axe
		case 1353:// Steel Axe
		case 1361:// Black Axe
		case 1355:// Mithril Axe
		case 1357:// Adamant Axe
		case 1359:// Rune Axe
		case 6739:// Dragon Axe
		case 13661: // Inferno adze
			return true;
		default:
			return false;
		}
	}
	
	NPC tree;
	
	public EntTreeCutting(NPC tree) {
		this.tree = tree;
	}

	@Override
	public boolean start(Player player) {
		if (!hasAxe(player)) {
			return false;
		}
		player.sm("You swing your axe at the Ent trunk.");
		setActionDelay(player, 10);
		return true;
	}

	@Override
	public boolean process(Player player) {
		player.animate(new Animation(EvilTree.emoteId(player)));
		return !tree.isFinished();
	}

	@Override
	public int processWithDelay(Player player) {
		addLog(player);
		if (tree.isFinished()) {
			player.animate(new Animation(-1));
			player.getActionManager().setAction(null);
			return -1;
		}
		if (!player.getInventory().hasFreeSlots()) {
			player.animate(new Animation(-1));
			player.getPackets().sendGameMessage(
					"Not enough space in your inventory.");
			return -1;
		}
		return 10;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
	}
	
}
