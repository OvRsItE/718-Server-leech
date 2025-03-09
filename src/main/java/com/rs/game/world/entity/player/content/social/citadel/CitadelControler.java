package com.rs.game.world.entity.player.content.social.citadel;

import java.util.Calendar;
import java.util.TimeZone;

import com.rs.Constants;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.CitadelActions.CitadelCookFish;
import com.rs.game.world.entity.player.actions.CitadelActions.CitadelGrabFish;
import com.rs.game.world.entity.player.actions.CitadelActions.CitadelGrabOre;
import com.rs.game.world.entity.player.actions.CitadelActions.CitadelMining;
import com.rs.game.world.entity.player.actions.CitadelActions.CitadelSmeltOre;
import com.rs.game.world.entity.player.actions.CitadelActions.CitadelSummoning;
import com.rs.game.world.entity.player.actions.CitadelActions.CitadelWoodCutting;
import com.rs.game.world.entity.player.actions.CitadelActions.CitadelMining.RockDefinitions;
import com.rs.game.world.entity.player.actions.CitadelActions.CitadelSummoning.PouchDefinitions;
import com.rs.game.world.entity.player.actions.CitadelActions.CitadelWoodCutting.RootDefinitions;
import com.rs.game.world.entity.player.actions.skilling.summoning.Summoning.Pouches;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.game.world.entity.updating.impl.Animation;

public class CitadelControler extends Controller {

	private boolean night;

	public static Position getWorldTile(int mapX, int mapY, Player player) {
		return new Position((player.boundChunks[0] << 3) + mapX,
				(player.boundChunks[1] << 3) + mapY, 0);
	}

	@Override
	public void start() {
		player.sm("Welcome to your Citadel!");
		player.citadelOpen = true;
		Citadel.createBasicCitadel(player);
	}

	@Override
	public void process() {
		final Calendar c = Calendar.getInstance();
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("US/Central"));
		c.setTimeZone(now.getTimeZone());

		if ((c.get(Calendar.HOUR) >= 8 && c.get(Calendar.AM_PM) == Calendar.PM || c
				.get(Calendar.HOUR) >= 1
				&& c.get(Calendar.HOUR) < 6
				&& c.get(Calendar.AM_PM) == Calendar.AM)) {
			if (night == false) {
				Citadel.createNightCitadel(player);
				night = true;
			}
		} else {
			if (night == true) {
				night = false;
				Citadel.createDayCitadel(player);
			}
		}
	}

	private void leave(boolean logout) {
		removeControler();
		if (logout)
			player.setLocation(Constants.HOME_PLAYER_LOCATION1);
		else
			player.setNextPosition(Constants.HOME_PLAYER_LOCATION1);
		Citadel.removeMap(player);
		player.setCanPvp(false);
	}

	@Override
	public boolean processMagicTeleport(Position toTile) {
		removeControler();
		Citadel.removeMap(player);
		player.setCanPvp(false);
		return true;
	}

	/**
	 * return can teleport
	 */
	@Override
	public boolean processItemTeleport(Position toTile) {
		removeControler();
		Citadel.removeMap(player);
		player.setCanPvp(false);
		return true;
	}

	/**
	 * return can teleport
	 */
	@Override
	public boolean processObjectTeleport(Position toTile) {
		removeControler();
		Citadel.removeMap(player);
		player.setCanPvp(false);
		return true;
	}

	@Override
	public boolean sendDeath() {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.animate(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage(
							"Oh dear, you have died.");
				} else if (loop == 3) {
					player.reset();
					player.animate(new Animation(-1));
					player.setNextPosition(getWorldTile(14, 12, player));
					player.setCanPvp(false);
					player.inBattleField = false;
				} else if (loop == 4) {
					player.getPackets().sendMusicEffect(90);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	@Override
	public void forceClose() {
		leave(false);
	}

	@Override
	public boolean logout() {
		leave(true);
		return false;
	}

	@Override
	public boolean processNPCClick1(final NPC npc) {
		if (npc.getId() == 13932)
			player.getDialogueManager().startDialogue("citadelbattle", player);
		return true;
	}

	@Override
	public boolean handleItemOnObject(WorldObject object, Item item) {
		if (object.getId() == 20628) {
			for (Pouches pouch : Pouches.values())
				if (item.getId() == pouch.getPouchId())
					player.getActionManager().setAction(
							new CitadelSummoning(object, item.getId(),
									PouchDefinitions.Golden_Object));
		} else if (object.getId() == 20110) {
			for (Pouches pouch : Pouches.values())
				if (item.getId() == pouch.getPouchId())
					player.getActionManager().setAction(
							new CitadelSummoning(object, item.getId(),
									PouchDefinitions.Iron_Object));
		} else if (object.getId() == 28041|| object.getId() == 27088) {
			if (item.getId() == 24517 || item.getId() == 24515) {
				player.getActionManager().setAction(new CitadelSmeltOre(item));
			}
		}

		return false;
	}

	@Override
	public boolean processObjectClick1(final WorldObject object) {
		if (object.getId() == 31297)
			player.getDialogueManager().startDialogue("plotControl", object);
		else if (object.getId() == 42425) {
			player.setNextPosition(getWorldTile(14, 12, player));
			player.setCanPvp(false);
			player.inBattleField = false;
		} else if (object.getId() == 59462) {
			leave(false);
		}
		switch (object.getDefinitions().name.toLowerCase()) {
		case "large chopping board":
			player.getActionManager().setAction(new CitadelGrabFish());
			break;
		case "grill":
			player.getActionManager().setAction(new CitadelCookFish());
			break;
		}
		switch (object.getId()) {
		case 20585:
			player.getActionManager().setAction(
					new CitadelSummoning(player.obelisk, player.savePouchId,
							PouchDefinitions.Golden_Object));
			break;
		case 27273:
		case 27201:
		case 28073:
			player.getActionManager().setAction(new CitadelGrabOre(object));
			break;
		case 24978:
			player.getActionManager().setAction(
					new CitadelMining(object, RockDefinitions.Stone_Ore2));
			break;
		case 24977:
			player.getActionManager().setAction(
					new CitadelMining(object, RockDefinitions.Stone_Ore3));
			break;
		case 24965:
			player.getActionManager().setAction(
					new CitadelMining(object, RockDefinitions.Stone_Ore1));
			break;
		case 24962:
			player.getActionManager().setAction(
					new CitadelMining(object, RockDefinitions.Stone_Ore));
			break;
		case 25062:
			player.getActionManager().setAction(
					new CitadelMining(object, RockDefinitions.Mini_Golden_Ore));
			break;
		case 25053:
			player.getActionManager()
					.setAction(
							new CitadelMining(object,
									RockDefinitions.Mini_Diamond_Ore2));
			break;
		case 25052:
			player.getActionManager()
					.setAction(
							new CitadelMining(object,
									RockDefinitions.Mini_Diamond_Ore));
			break;
		case 25063:
			player.getActionManager()
					.setAction(
							new CitadelMining(object,
									RockDefinitions.Mini_Golden_Ore2));
			break;
		case 25064:
			player.getActionManager().setAction(
					new CitadelMining(object, RockDefinitions.Golden_Ore));
			break;
		case 25054:
			player.getActionManager().setAction(
					new CitadelMining(object, RockDefinitions.Diamond_Ore));
			break;
		case 18406:
			player.getActionManager()
					.setAction(
							new CitadelWoodCutting(object,
									RootDefinitions.GOLDEN_ROOT1));
			break;
		case 24317:
			player.getActionManager().setAction(
					new CitadelMining(object, RockDefinitions.IRON_Stone_Ore));
			break;
		case 24276:
			player.getActionManager().setAction(
					new CitadelMining(object, RockDefinitions.IRON_Stone_Ore1));
			break;
		case 18920:
			player.getActionManager().setAction(
					new CitadelWoodCutting(object, RootDefinitions.IRON_ROOT2));
			break;
		case 24406:
			player.getActionManager().setAction(
					new CitadelMining(object, RockDefinitions.IRON_Stone_Ore2));
			break;
		case 18907:
			player.getActionManager().setAction(
					new CitadelWoodCutting(object, RootDefinitions.IRON_ROOT3));
			break;
		case 24398:
			player.getActionManager().setAction(
					new CitadelMining(object, RockDefinitions.IRON_Stone_Ore3));
			break;
		case 18901:
			player.getActionManager().setAction(
					new CitadelWoodCutting(object, RootDefinitions.IRON_ROOT4));
			break;
		case 18883:
			player.getActionManager().setAction(
					new CitadelWoodCutting(object, RootDefinitions.IRON_ROOT5));
			break;
		case 18874:
			player.getActionManager().setAction(
					new CitadelWoodCutting(object, RootDefinitions.IRON_ROOT6));
			break;
		case 18496:
			player.getActionManager()
					.setAction(
							new CitadelWoodCutting(object,
									RootDefinitions.GOLDEN_ROOT2));
			break;
		case 18503:
			player.getActionManager()
					.setAction(
							new CitadelWoodCutting(object,
									RootDefinitions.GOLDEN_ROOT3));
			break;
		case 18662:
			player.getActionManager()
					.setAction(
							new CitadelWoodCutting(object,
									RootDefinitions.GOLDEN_ROOT4));
			break;
		case 18856:
			player.getActionManager()
					.setAction(
							new CitadelWoodCutting(object,
									RootDefinitions.GOLDEN_ROOT5));
			break;
		}
		return false;
	}

}
