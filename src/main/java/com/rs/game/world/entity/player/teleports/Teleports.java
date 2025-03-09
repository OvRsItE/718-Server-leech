/**
 * 
 */
package com.rs.game.world.entity.player.teleports;

import com.rs.cores.CoresManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.combat.impl.AmethystDragonCombat;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.Magic;
import com.rs.utility.Utils;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static com.rs.game.world.entity.player.teleports.TeleportCatagory.*;

/**
 * Holds data about teleport destinations.
 * @author ReverendDread
 * Aug 4, 2018
 */
public enum Teleports {

	TRAINING(MONSTERS, (player) -> {
		//use this for doing special stuff like dialogues etc
		player.getDialogueManager().startDialogue("TrainingTeleports");
	}),
	SLAYER(MONSTERS, (player) -> {
		//use this for doing special stuff like dialogues etc
		player.getDialogueManager().startDialogue("MTSlayerDungeons");
	}),

	SMOKE_DEVILS(MONSTERS, (player) -> {
		if (player.getEquipment().wearingFaceMask()) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2916, 2503, 0));
			CoresManager.slowExecutor.schedule(() -> {
				player.getControlerManager().startControler("SmokeDungeonController");
			}, 5, TimeUnit.SECONDS);
		} else {
			player.getDialogueManager().startDialogue("SmokeDungeonConfirm");
		}

	}),
	//skiling
	FARMING(SKILLING, (player) -> {
		player.getDialogueManager().startDialogue("IrishToolTeleD");
	}),
	FISHING(SKILLING, (player) -> {
		player.getDialogueManager().startDialogue("Fishing");
	}),
	AGILITY(SKILLING, (player) -> {
		player.getDialogueManager().startDialogue("Agility");
	}),
	MINING(SKILLING, (player) -> {
		player.getDialogueManager().startDialogue("Mining");
	}),
	WOODCUTTING(SKILLING, (player) -> {
		player.getDialogueManager().startDialogue("Woodcutting");
	}),
	RUNESPAN(SKILLING, (player) -> {
		player.getDialogueManager().startDialogue("RunespanPortalD");
	}),
	RUNECRAFTING(SKILLING, (player) -> {
		player.getDialogueManager().startDialogue("RC");
	}),
	//BOSSES
	BORK(BOSSES, (player) -> {
		player.getControlerManager().startControler("BorkControler", 0, null);
	}),
	WILDYWYRM(BOSSES, (player) -> {
		player.getDialogueManager().startDialogue("WildyWyrmTP");
	}),
	//use this for just normal teleports under normal conditions. wont let you tele in wild so dw about that
	JADINKO_LAIR(MONSTERS, new Position(3025, 9224, 0)),
	LITHKREN_VAULT(MONSTERS, new Position(1568, 5062, 0)),
	KOUREND_DUNGEON(MONSTERS, new Position(1640, 3673, 0)),
	GLACOR_CAVE(MONSTERS, new Position(4180, 5731, 0)),
	FROST_DRAGONS(MONSTERS, new Position(3033, 9597, 0)),
	SHADOW_WARRIOR(MONSTERS, new Position(2701, 9773, 0)),
	KARLUUM_DUNGEON(MONSTERS, player ->  {
		player.addEvent(event -> {
			Magic.sendNormalTeleportSpell(player, 0, 0, Position.of(1311, 10188));
			event.delay(4);
			player.getControlerManager().startControler("KaruulmController");
		});
	}),
	ICE_DUNGEON(MONSTERS, new Position(1161, 2630)),
	SAND_DUNGEON(MONSTERS, new Position(3393, 9246, 1)),
	ELVE_CAMP(MONSTERS, new Position(2204, 3253, 0)),
	POLYPORE_DUNGEON(MONSTERS, new Position(4622, 5457, 3)),
    //skilling
	SUMMONING(SKILLING, new Position(2209, 5343, 0)),
	HUNTER(SKILLING, new Position(2526, 2916, 0)),
	PUROPURO(SKILLING, new Position(2419, 4446, 0)),
	GEMSTONE_ROCKS(SKILLING, new Position(2825, 2997, 0)),
	DUNGEONEERING(SKILLING, new Position(3451, 3725, 0)),
	//wilderness
	MAGE_BANK(WILDERNESS, new Position(2539, 4716, 0)),
	REVENANTS(WILDERNESS, new Position(3071, 3649, 0)),
	EDGEVILLE(WILDERNESS, new Position(3087, 3496, 0)),
	EASTS(WILDERNESS, new Position(3364, 3688, 0)),
	WESTS(WILDERNESS, new Position(2985, 3596, 0)),
	GRAVEYARD_OF_SHADOWS(WILDERNESS, new Position(3229, 3700, 0)),
	THE_FORGOTTEN_CEMETERY(WILDERNESS, new Position(2978, 3750, 0)),
	KING_BLACK_DRAGON_LAIR(WILDERNESS, new Position(2999, 3848, 0)),
	DEMONIC_RUINS(WILDERNESS, new Position(3290, 3886, 0)),
	LAVA_STRYKWYRM(WILDERNESS, new Position(3061, 3768, 0)),
	//bosses
	GOD_WARS(BOSSES, (player) -> {
		player.getDialogueManager().startDialogue("GodwarsTeleports");
	}),
	QUEEN_BLACK_DRAGON(BOSSES, new Position(1197, 6499, 0)),
	KING_BLACK_DRAGON(BOSSES, new Position(3067, 10254, 0)),
	KALPHITE_QUEEN(BOSSES, new Position(3228, 3108, 0)),
	CORPOREAL_BEAST(BOSSES, new Position(2969, 4383, 2)),
	DAGANNOTH_KINGS(BOSSES, new Position(1896, 4409, 0)),
	TORMENTED_DEMONS(BOSSES, new Position(2564, 5739, 0)),
	DEMONIC_GORILLAS(BOSSES, new Position(2435, 3516, 0)),
	VORKATH(BOSSES, new Position(2641, 3697, 0)),
	ZULRAH(BOSSES, new Position(2200, 3056, 0)),
	CERBERUS(BOSSES, new Position(1310, 1251, 0)),
	KRAKEN_COVE(BOSSES, new Position(2275, 9991, 0)),
	THE_ALCHEMIST(BOSSES, new Position(3825, 4767, 0)),
	ABYSSAL_SIRE(BOSSES, new Position(3037, 4763, 0)),
	DARK_DEMON(BOSSES, new Position(3872, 2550, 0)),
	LORD_OF_LIGHTNIHNG(BOSSES, new Position(3759, 2527, 0)),
	AMETHYST_DRAGON(BOSSES, player -> {
		if (AmethystDragonCombat.isSpawned)
			Magic.sendNormalTeleportSpell(player, 0, 0, Position.of(3798, 2520));
		else
			player.sendMessage("You can't teleport to that right now.");
	}),
	BURNT_ARENA(BOSSES, new Position(3685, 4454, 0)),
	HWEEN_BOSS(BOSSES, new Position(3544, 3238, 0)),
	SUNFREET(BOSSES, new Position(2020, 1180, 0)),
	ACIDIC_STRYKEWYRM(BOSSES, new Position(2020, 1255, 0)),
	AVATAR_OF_CREATION(BOSSES, new Position(2085, 1245, 0)),
	INSTANCES(BOSSES, (player) -> {
		player.getDialogueManager().startDialogue("InstanceManager");
	}),
	//minigames
	DOMINION_TOWER(MINIGAMES, new Position(3373, 3090, 0)),
	FIGHT_CAVES(MINIGAMES, new Position(4613, 5129, 0)),
	BARROWS(MINIGAMES, new Position(3563, 3288, 0)),
	DUEL_ARENA(MINIGAMES, new Position(3365, 3275, 0)),
	FIGHT_KILN(MINIGAMES, new Position(4743, 5170, 0)),
	PEST_CONTROL(MINIGAMES, new Position(2658, 2660, 0)),
	ELITE_DUNGEON_ONE(MINIGAMES, (player) -> {
		player.getDialogueManager().startDialogue("EliteDungeonEnter");
	})

	;

	public static final Teleports[] VALUES = values();

	@Getter private TeleportCatagory catagory;
	@Getter private Position destination;
	@Getter private Consumer<Player> function;

	Teleports(TeleportCatagory monsters, Consumer<Player> function) {
		this.catagory = monsters;
		this.function = function;
	}

	Teleports(TeleportCatagory catagory, Position destination) {
		this.catagory = catagory;
		this.destination = destination;
	}

	public static List<Teleports> getTeleportsForCatagory(TeleportCatagory catagory) {
		List<Teleports> teles = new ArrayList<>();
		for (Teleports teleport : VALUES) {
			if (teleport.catagory == catagory)
				teles.add(teleport);
		}
		return teles;
	}

    /**
	 * Gets formatted name of the element.
	 * @return
	 */
	public String getName() {
		return Utils.getFormattedEnumName(name());
	}
	
}
