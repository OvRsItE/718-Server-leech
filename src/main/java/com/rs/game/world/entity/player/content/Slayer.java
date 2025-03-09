package com.rs.game.world.entity.player.content;

import java.io.Serializable;
import java.util.List;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.ShopsHandler;
import com.rs.utility.Utils;

public class Slayer {

	public enum SlayerMaster implements Serializable {

		SPRIA(8462, 85, 1, new int[] { 0, 0, 0 }, new int[] { 15, 50 }, SlayerTask.BANSHEE, SlayerTask.BAT,
				SlayerTask.BEAR, SlayerTask.COW, SlayerTask.BIRD, SlayerTask.CAVE_BUG, SlayerTask.CAVE_SLIME,
				SlayerTask.DWARF, SlayerTask.CRAWLING_HAND, SlayerTask.DESERT_LIZARD, SlayerTask.DWARF,
				SlayerTask.GHOST, SlayerTask.GOBLIN, SlayerTask.MINOTAUR, SlayerTask.MONKEY, SlayerTask.SCORPION,
				SlayerTask.SKELETON, SlayerTask.SPIDER, SlayerTask.WOLF, SlayerTask.ZOMBIE),

		TURAEL(8480, 3, 1, new int[] { 0, 0, 0 }, new int[] { 15, 50 }, SlayerTask.BANSHEE, SlayerTask.BAT,
				SlayerTask.BEAR, SlayerTask.COW, SlayerTask.BIRD, SlayerTask.CAVE_BUG, SlayerTask.CAVE_SLIME,
				SlayerTask.DWARF, SlayerTask.CRAWLING_HAND, SlayerTask.DESERT_LIZARD, SlayerTask.DWARF,
				SlayerTask.GHOST, SlayerTask.GOBLIN, SlayerTask.MINOTAUR, SlayerTask.MONKEY, SlayerTask.SCORPION,
				SlayerTask.SKELETON, SlayerTask.SPIDER, SlayerTask.WOLF, SlayerTask.ZOMBIE),

		MAZCHNA(8481, 20, 1, new int[] { 2, 5, 15 }, new int[] { 20, 60 }, SlayerTask.BANSHEE, SlayerTask.BAT,
				SlayerTask.CATABLEPON, SlayerTask.CAVE_CRAWLER, SlayerTask.COCKATRICE, SlayerTask.CRAWLING_HAND,
				SlayerTask.CYCLOPS, SlayerTask.DESERT_LIZARD, SlayerTask.DOG, SlayerTask.FLESH_CRAWLER,
				SlayerTask.GHOUL, SlayerTask.GHOST, SlayerTask.GROTWORM, SlayerTask.HILL_GIANT, SlayerTask.HOBGOBLIN,
				SlayerTask.ICE_WARRIOR, SlayerTask.KALPHITE, SlayerTask.PYREFIEND, SlayerTask.ROCKSLUG,
				SlayerTask.SKELETON, SlayerTask.VAMPYRE, SlayerTask.WOLF, SlayerTask.ZOMBIE),

		ACHTRYN(8465, 20, 1, new int[] { 2, 5, 15 }, new int[] { 20, 60 }, SlayerTask.BANSHEE, SlayerTask.BAT,
				SlayerTask.CATABLEPON, SlayerTask.CAVE_CRAWLER, SlayerTask.COCKATRICE, SlayerTask.CRAWLING_HAND,
				SlayerTask.CYCLOPS, SlayerTask.DESERT_LIZARD, SlayerTask.DOG, SlayerTask.FLESH_CRAWLER,
				SlayerTask.GHOUL, SlayerTask.GHOST, SlayerTask.GROTWORM, SlayerTask.HILL_GIANT, SlayerTask.HOBGOBLIN,
				SlayerTask.ICE_WARRIOR, SlayerTask.KALPHITE, SlayerTask.PYREFIEND, SlayerTask.ROCKSLUG,
				SlayerTask.SKELETON, SlayerTask.VAMPYRE, SlayerTask.WOLF, SlayerTask.ZOMBIE),

		VANNAKA(1597, 40, 1, new int[] { 4, 20, 60 }, new int[] { 30, 70 }, SlayerTask.ABERRANT_SPECTRE,
				SlayerTask.ANKOU, SlayerTask.BANSHEE, SlayerTask.BASILISK, SlayerTask.BLOODVELD, SlayerTask.BRINE_RAT,
				SlayerTask.COCKATRICE, SlayerTask.CROCODILE, SlayerTask.CYCLOPS, SlayerTask.DUST_DEVIL,
				SlayerTask.EARTH_WARRIOR, SlayerTask.GHOUL, SlayerTask.GREEN_DRAGON, SlayerTask.GROTWORM,
				SlayerTask.HARPIE_BUG_SWARM, SlayerTask.HILL_GIANT, SlayerTask.ICE_GIANT, SlayerTask.ICE_WARRIOR,
				SlayerTask.INFERNAL_MAGE, SlayerTask.JELLY, SlayerTask.JUNGLE_HORROR, SlayerTask.LESSER_DEMON,
				SlayerTask.MOSS_GIANT, SlayerTask.OGRE, SlayerTask.OTHERWORLDLY_BEING, SlayerTask.PYREFIEND,
				SlayerTask.SHADE, SlayerTask.SHADOW_WARRIOR, SlayerTask.TUROTH, SlayerTask.VAMPYRE,
				SlayerTask.WEREWOLF),

		CHAELDAR(1598, 70, 1, new int[] { 10, 50, 100 }, new int[] { 40, 80 }, SlayerTask.ABERRANT_SPECTRE,
				SlayerTask.BANSHEE, SlayerTask.BASILISK, SlayerTask.BLOODVELD, SlayerTask.BLUE_DRAGON,
				SlayerTask.BRINE_RAT, SlayerTask.BRONZE_DRAGON, SlayerTask.CAVE_CRAWLER, SlayerTask.CAVE_HORROR,
				SlayerTask.CRAWLING_HAND, SlayerTask.DAGANNOTH, SlayerTask.DUST_DEVIL, SlayerTask.ELF_WARRIOR,
				SlayerTask.FEVER_SPIDER, SlayerTask.FIRE_GIANT, SlayerTask.FUNGAL_MAGE, SlayerTask.GARGOYLE,
				SlayerTask.GRIFOLAPINE, SlayerTask.GRIFOLAROO, SlayerTask.GROTWORM, SlayerTask.HARPIE_BUG_SWARM,
				SlayerTask.JUNGLE_STRYKEWYRM, SlayerTask.INFERNAL_MAGE, SlayerTask.JELLY, SlayerTask.JUNGLE_HORROR,
				SlayerTask.KALPHITE, SlayerTask.KALPHITE, SlayerTask.KURASK, SlayerTask.LESSER_DEMON,
				SlayerTask.ZYGOMITE, SlayerTask.SHADOW_WARRIOR, SlayerTask.TUROTH, SlayerTask.VYREWATCH,
				SlayerTask.WARPED_TORTOISE),

		SUMONA(7780, 85, 35, new int[] { 12, 60, 180 }, new int[] { 50, 100 }, SlayerTask.ABERRANT_SPECTRE,
				SlayerTask.ABYSSAL_DEMON, SlayerTask.AQUANITE, SlayerTask.BANSHEE, SlayerTask.BASILISK,
				SlayerTask.BLACK_DEMON, SlayerTask.BLOODVELD, SlayerTask.BLUE_DRAGON, SlayerTask.CAVE_CRAWLER,
				SlayerTask.CAVE_HORROR, SlayerTask.DAGANNOTH, SlayerTask.DESERT_STRYKEWYRM, SlayerTask.DUST_DEVIL,
				SlayerTask.ELF_WARRIOR, SlayerTask.FUNGAL_MAGE, SlayerTask.GARGOYLE, SlayerTask.GREATER_DEMON,
				SlayerTask.GRIFOLAPINE, SlayerTask.GRIFOLAROO, SlayerTask.GROTWORM, SlayerTask.HELLHOUND,
				SlayerTask.IRON_DRAGON, SlayerTask.JUNGLE_STRYKEWYRM, SlayerTask.KALPHITE, SlayerTask.KURASK,
				SlayerTask.JADINKO_BABY, SlayerTask.NECHRYAEL, SlayerTask.RED_DRAGON, SlayerTask.SCABARITE,
				SlayerTask.SPIRITUAL_MAGE, SlayerTask.SPIRITUAL_WARRIOR, SlayerTask.TERROR_DOG, SlayerTask.TROLL,
				SlayerTask.TUROTH, SlayerTask.VYREWATCH
				),

		DURADEL(8466, 100, 50, new int[] { 15, 75, 225 }, new int[] { 50, 100 }, SlayerTask.ABERRANT_SPECTRE,
				SlayerTask.ABYSSAL_DEMON, SlayerTask.AQUANITE, SlayerTask.BLACK_DEMON, SlayerTask.BLACK_DRAGON,
				SlayerTask.BLOODVELD, SlayerTask.DAGANNOTH, SlayerTask.DARK_BEAST, SlayerTask.DESERT_STRYKEWYRM,
				SlayerTask.DUST_DEVIL, SlayerTask.FIRE_GIANT, SlayerTask.FUNGAL_MAGE, SlayerTask.GANODERMIC,
				SlayerTask.GARGOYLE, SlayerTask.GORAK, SlayerTask.GREATER_DEMON, SlayerTask.GRIFALOPINE,
				SlayerTask.GRIFALOROO, SlayerTask.GROTWORM, SlayerTask.HELLHOUND, SlayerTask.ICE_STRYKEWYRM,
				SlayerTask.IRON_DRAGON, SlayerTask.JUNGLE_STRYKEWYRM, SlayerTask.KALPHITE, SlayerTask.MITHRIL_DRAGON,
				SlayerTask.JADINKO, SlayerTask.JADINKO_BABY, SlayerTask.JADINKO_GUARD, SlayerTask.NECHRYAEL, SlayerTask.SCABARITE, SlayerTask.SKELETAL_WYVERN,
				SlayerTask.SPIRITUAL_MAGE, SlayerTask.STEEL_DRAGON, SlayerTask.SUQAH, SlayerTask.VYREWATCH,
				SlayerTask.WARPED_TERRORBIRD, SlayerTask.WATERFIEND),

		LAPALOK(8467, 100, 50, new int[] { 15, 75, 225 }, new int[] { 50, 100 }, SlayerTask.ABERRANT_SPECTRE,
				SlayerTask.ABYSSAL_DEMON, SlayerTask.AQUANITE, SlayerTask.BLACK_DEMON, SlayerTask.BLACK_DRAGON,
				SlayerTask.BLOODVELD, SlayerTask.DAGANNOTH, SlayerTask.DARK_BEAST, SlayerTask.DESERT_STRYKEWYRM,
				SlayerTask.DUST_DEVIL, SlayerTask.FIRE_GIANT, SlayerTask.FUNGAL_MAGE, SlayerTask.GANODERMIC,
				SlayerTask.GARGOYLE, SlayerTask.GORAK, SlayerTask.GREATER_DEMON, SlayerTask.GRIFALOPINE,
				SlayerTask.GRIFALOROO, SlayerTask.GROTWORM, SlayerTask.HELLHOUND, SlayerTask.ICE_STRYKEWYRM,
				SlayerTask.IRON_DRAGON, SlayerTask.JUNGLE_STRYKEWYRM, SlayerTask.KALPHITE, SlayerTask.MITHRIL_DRAGON,
				SlayerTask.JADINKO, SlayerTask.JADINKO_BABY, SlayerTask.JADINKO_GUARD, SlayerTask.NECHRYAEL, SlayerTask.SCABARITE, SlayerTask.SKELETAL_WYVERN,
				SlayerTask.SPIRITUAL_MAGE, SlayerTask.STEEL_DRAGON, SlayerTask.SUQAH, SlayerTask.VYREWATCH,
				SlayerTask.WARPED_TERRORBIRD, SlayerTask.WATERFIEND),

		KURADAL(9085, 110, 75, new int[] { 18, 90, 270 }, new int[] { 25, 100 }, SlayerTask.ABERRANT_SPECTRE,
				SlayerTask.ABYSSAL_DEMON, SlayerTask.BLACK_DEMON, SlayerTask.BLACK_DRAGON, SlayerTask.BLOODVELD,
				SlayerTask.BLUE_DRAGON, SlayerTask.DAGANNOTH, SlayerTask.DARK_BEAST, SlayerTask.DESERT_STRYKEWYRM,
				SlayerTask.DUST_DEVIL, SlayerTask.FIRE_GIANT, SlayerTask.GANODERMIC, SlayerTask.GARGOYLE,
				SlayerTask.GRIFOLAPINE, SlayerTask.GRIFOLAROO, SlayerTask.GROTWORM, SlayerTask.HELLHOUND,
				SlayerTask.IRON_DRAGON, SlayerTask.JUNGLE_STRYKEWYRM, SlayerTask.KALPHITE, SlayerTask.LIVING_ROCK,
				SlayerTask.MITHRIL_DRAGON, SlayerTask.JADINKO, SlayerTask.JADINKO_BABY, SlayerTask.JADINKO_GUARD, SlayerTask.NECHRYAEL, SlayerTask.SKELETAL_WYVERN,
				SlayerTask.SPIRITUAL_MAGE, SlayerTask.STEEL_DRAGON, SlayerTask.SUQAH, SlayerTask.TERROR_DOG,
				SlayerTask.TZHAAR, SlayerTask.TZHAAR, SlayerTask.VYREWATCH, SlayerTask.WARPED_TORTOISE,
				SlayerTask.WATERFIEND, SlayerTask.TOXIC_TIGER, SlayerTask.TOXIC_WORM, SlayerTask.TOXIC_TOAD, SlayerTask.DARK_TIGER, SlayerTask.DARK_TOAD, SlayerTask.DARK_WORM, SlayerTask.ETHER_TIGER, SlayerTask.ETHER_TOAD, SlayerTask.ETHER_WORM,
				SlayerTask.CAVE_KRAKEN, SlayerTask.SMOKE_DEVIL, SlayerTask.HYDRA, SlayerTask.WYRM, SlayerTask.DRAKE, SlayerTask.SAND_MUMMY, SlayerTask.ICE_ABYSSAL_DEMON, SlayerTask.ICE_DEMON),
		KRYSTILLIA(27663, 1, 1, new int[] { 25, 125, 375 }, new int[] { 25, 100 },
				SlayerTask.ABYSSAL_DEMON,SlayerTask.GREEN_DRAGON,SlayerTask.LESSER_DEMON,
			    SlayerTask.DARK_BEAST,
			    SlayerTask.GREATER_DEMON,
				SlayerTask.GANODERMIC,SlayerTask.GARGOYLE,
				SlayerTask.HELLHOUND,
				SlayerTask.NECHRYAEL,
				SlayerTask.ANKOU,
				SlayerTask.BAT,
				SlayerTask.REVENANT,
				SlayerTask.SKELETON,
				SlayerTask.SCORPION,
				SlayerTask.HOBGOBLIN,
				SlayerTask.DWARF,
				SlayerTask.SPIDER,
				SlayerTask.CALLISTO,
				SlayerTask.SCORPIA,
				SlayerTask.VENENATIS,
				SlayerTask.ZOMBIE,
				SlayerTask.WATERFIEND, SlayerTask.LAVA_STRYKEWYRM);

		private SlayerTask[] task;
		private int[] tasksRange, pointsRange;
		private int requriedCombatLevel, requiredSlayerLevel, npcId;

		private SlayerMaster(int npcId, int requriedCombatLevel, int requiredSlayerLevel, int[] pointsRange,
				int[] tasksRange, SlayerTask... task) {
			this.npcId = npcId;
			this.requriedCombatLevel = requriedCombatLevel;
			this.requiredSlayerLevel = requiredSlayerLevel;
			this.pointsRange = pointsRange;
			this.tasksRange = tasksRange;
			this.task = task;
		}

		public int getNPCId() {
			return npcId;
		}

		public int getRequiredCombatLevel() {
			return requriedCombatLevel;
		}

		public int getRequiredSlayerLevel() {
			return requiredSlayerLevel;
		}

		public SlayerTask[] getTask() {
			return task;
		}
		
		public int[] getTasksRange() {
			return tasksRange;
		}

		public int[] getPointsRange() {
			return pointsRange;
		}

		public static boolean startInteractionForId(Player player, int npcId, int option) {
			for (SlayerMaster master : SlayerMaster.values()) {
				if (master.getNPCId() == npcId) {
					if (option == 1) {
						player.getDialogueManager().startDialogue("SlayerMasterD", npcId, master);
					} else if (option == 2) {
						player.getDialogueManager().startDialogue("QuickTaskD", master);
					} else if (option == 3) {
						ShopsHandler.openShop(player, 9);
					} else if (option == 4) {
						player.getSlayerManager().sendSlayerInterface(SlayerManager.BUY_INTERFACE);
					}
					return true;
				}
			}
			return false;
		}
	}

	public enum SlayerTask implements Serializable {// 79 matches out of 117

		MIGHTY_BANSHEE(37, new Position(3366, 9384, 0),
				new String[] { "Banshees is located at the Slayer tower. You will"
						+ "Need a ear mask, to kill these creatures."
						+  "<br>"
						+ "You get 5 Slayer points for finishing this task." }, 7786),

		// finished
		MIGHTY_BANSEE(37, new Position(3366, 9384, 0),
				new String[] { "Banshee's is located at the Slayer tower. You will"
						+ "<br>Need a ear mask, to kill these creatures."
						+ "<br>"
						+ "<br>You get 5 Slayer points for finishing this task." }, 7768, MIGHTY_BANSHEE),

		// finished
		BANSHEE(15, new Position(3436, 3562, 0),
				new String[] { "Banshee's is located at the Slayer tower. You will"
						+ "<br>Need a ear mask, to kill these creatures."
						+ "<br>"
						+ "<br>You get 5 Slayer points for finishing this task." }, 1612,  MIGHTY_BANSEE),

		// finished
		BAT(1, new Position(2913, 9832, 0), new String[] {"Bat's, can be found with different combat levels around"
				+ "<br>Valius, one of the places is the Taverley Dungeon."
				+ "<br>"
				+ "<br>You get 5 Slayer points for finishing this task."}, 412),

		ROCKCRAB(1, new Position(2669, 3715, 0),
				new String[] { "Bat's, can be found with different combat levels around"
						+ "<br>Valius, one of the places is the Taverley Dungeon." + "<br>"
						+ "<br>You get 5 Slayer points for finishing this task." }, 1267),

		AVIANSIE(1, new Position(2870, 5286, 0), new String[] {"Aviansie's can be found in the Godwars dungeon, in the"
				+ "<br>Southeren section, they are immune to meele attacks."
				+ "<br>"
				+ "<br>You get 5 Slayer points for finishing this task."}, 6232),

		// finished
		CHICKEN(1, new Position(3237, 3296, 0), new String[] {"Chickens, is a very simple and easy task that requires"
				+ "<br>No special information, they can be found in many"
				+ "<br>Farming gardens. One is in Lumbridge."
				+ "<br>"
				+ "<br>You get 5 Slayer points for finishing this task"}, 41),

		// not needed

		// finished
		DUCK(1, new Position(2990, 3378, 0), new String[] {"TODO"}, 46),
	


		// finished
		BIRD(1, new Position(3237, 3296, 0),
				new String[] { "Birds are a type of species found throughout Valius in different forms."
						+  "<br>It's recommended that you bring range weapons to fight these monsters."
						+  "<br>Avansies are the strongest and most widely known type of bird."
						+  "<br>Chickens are great for a fast task."
						+ "<br>"
						+ "<br>You get 5 Slayer points for finishing this task." }, 41,
				AVIANSIE, CHICKEN,  DUCK),

		// finished
		BEAR(1, new Position(3231, 3503, 0), new String[] {"To do"}, 105),

		// finished
		CAVE_BUG(7, new Position(3147, 9577, 0), new String[] {"Cave bugs are located in the Lumbridge Swamp dungeon, stronger"
				+	"<br>Variants can be located in the Dorgesh-Kaan South Dungeon."
				+	"<br>"
				+	"<br>You get 5 Slayer points for finishing this task."}, 1832),

		// finished
		CAVE_SLIME(17, new Position(3158, 9555, 0), new String[] {"Cave Slimes are located in the Lumbridge Swamp dungeon, stronger"
				+	"<br>Variants can be located in the Dorgesh-Kaan South Dungeon."
				+	"<br>"
				+	"<br>You get 5 Slayer points for finishing this task."}, 1831),

		// finished
		COW(1, new Position(3176, 3316, 0), new String[] {"Cows, is one of Valius's training places. You can"
				+  "<br>Access the dungeon by going to your navigation tab"
				+  "<br>And clicking on training teleports."
				+  "<br>"
				+  "<br>You get 5 Slayer points for finishing this task."}, 81),
		// finished
		ZOMBIE_HAND(5, new Position(1, 1, 0), new String[] {"To Do"}, 7641),

		// finished
		SKELETAL_HAND(5, new Position(1, 1, 0), new String[] {"To Do"}, 7640),

		// finished
		CRAWLING_HAND(5, new Position(3410, 3537, 0), new String[] {"Crawling hands, is located in the Slayer Tower near"
				+  "<br>The entrance, they drop herbs so picking up the"
				+  "<br>Drops will benefit in the long run."
				+  "<br>"
				+  "<br>You get 5 Slayer points for finishing this task."}, 1648,  ZOMBIE_HAND, SKELETAL_HAND),

		// finished
		DWARF(1, new Position(3017, 9832, 0), new String[] {"Dwarves can be found, Dwarven mines located in Edgeville."
				+ "<br>They are no harm, and they drop the Crystal key as a rare."
				+ "<br>"
				+ "<br>You get 5 Slayer points for finishing this task."}, 118),

		// finished
		LIZARD(22, new Position(3412, 3031, 0), new String[] { "Lizards, can be found in the Kharidian Desert possibly"
				+  "<br>Possibly somewhere near the River. They drop a good"
				+  "<br>Amount of cash on each kill, so its worth picking up."
				+  "<br>"
				+  "<br>You get 5 Slayer points for finishing this Task." }, 2803),

		// finished
		DESERT_LIZARD(22, new Position(3412, 3031, 0), new String[] {"Lizards, can be found in the Kharidian Desert possibly"
				+  "<br>Possibly somewhere near the River. They drop a good"
				+  "<br>Amount of cash on each kill, so its worth picking up."
				+  "<br>"
				+  "<br>You get 5 Slayer points for finishing this Task."}, 2804, LIZARD),

		// finishe
		REVENANT(1, new Position(3112, 10152, 0), new String[] {"Ravenants is located in the Wilderness, and you"
				+	 "<br>Can teleport there from the Training teleports menu."
				+	 "<br>They drop PvP armor, and are quite strong."
				+	 "<br>"
				+	 "<br>You get 10 Slayer points for finishing this task."}, 13474),

		// finished
		GHOST(1, new Position(1663, 10024, 0), new String[] {"Ghost's can be found in many dungeons one of them is"
				+ "<br>The Taverley dungeon. Ravenants counts as Ghost's to."
				+ "<br>"
				+ "<br>You get 5 Slayer points for finishing this task."}, 103, REVENANT),

		// finished
		GOBLIN(1, new Position(2955, 3503, 0), new String[] {"Goblins can be found, at the Lumbridge bridge"
				+   "<br>They are nothing special, and only drops"
				+   "<br>The Crystal key."
				+   "<br>"
				+   "<br>You get 5 Slayer points for finishing this task."}, 3264),

		// finished
		ICEFIEND(1, new Position(3003, 3485, 0), new String[] {"Icefiends, is located at the top of the Ice Mountain near home."
				+	"<br>They are also found in the Godwars Dungeon but take care"
				+	"<br>They are stronger down there."
				+	"<br>"
				+	"<br>You get 5 Points for finishing this task."}, 3406),

		// finished
		MINOTAUR(4, new Position(1873, 5240, 0), new String[] { "Minotaurs, are located in the Stronghold Of Security just near Home."
				+	"<br>They are a low leveled monster, so you should not worry."
				+	"<br>It has the chance to drop the Crystal key as a rare."
				+	"<br>"
				+	"<br>You get 5 Slayer points for finishing this task."}, 4404),

		// finished
		MONKEY(1, new Position(2898, 3075, 0), new String[] {"Comming soon."}, 132),

		// finished
		SCORPION(1, new Position(3298, 3302, 0), new String[] {"Scorpions, can be at the Al-kharid mining place or"
				+	"<br>In the Taverley Dungeon. They are low levled"
				+	"<br>So you should not worry."
				+	"<br>"
				+	"<br>You get 5 Slayer points for finishing this task."}, 107),

		// finishe
		SKELETON(1, new Position(2884, 9827, 0), new String[] {"Skeletons can be found in the Taverley Dungeon and many"
				+	"<br>Other dungeons aswell, their main item drop is Herbs."
				+	"<br>"
				+	"<br>You get 5 Slayer points for finishing this task."}, 90, SKELETAL_HAND),

		// finished
		SPIDER(1, new Position(2837, 9581, 0), new String[] {"Spiders, can be found in the Lair where you pull the"
				+	"<br>Lever to go into KBD, they drop many kind of raw fishes."
				+	"<br>"
				+	"<br>You get 5 Slayer points for finishing this task."}, 59),

		// finished
		WOLVE(1, new Position(2850, 3500, 0), new String[] {"Wolfs, can be found at the top of the Ice Mountain near home."
				+	     "<br>They drop many kinds of arrows, and also the Crystal Key."
				+	     "<br>"
				+            "<br>You get 5 Slayer points for finishing this task."}, 95),

		// finished
		WOLF(1, new Position(2850, 3500, 0), new String[] {"TODO"}, 95, WOLVE),

		// finished
		ZOMBIE(1, new Position(3241, 10000, 0), new String[] {"Zombies, can be found in many Dungeons on Valius"
				+       "<br>They can be found with various levels, and drops"
				+      "<br>Basic drops that can be sold to the General store"
				+     "<br>For some money."
				+    "<br>"
				+     "<br>You get 5 Slayer points for finishing this task."}, 73),

		// finished
		CATABLEPON(1, new Position(2152, 5251, 0), new String[] {"TODO"}, 4397),

		// finished
		CAVE_CRAWLER(10, new Position(2802, 9999, 0), new String[] {"Cave Crawlers, can be found in the Fremmenik Slayer Dungeon"
				+   "<br>They have a low combat level, so you shouldnt worry."
				+   "<br>"
				+   "<br>"}, 1600),

		// finished
		DOG(1, new Position(2668, 9523, 0), new String[] {"Cmon..."}, 1593),

		// done
		FLESH_CRAWLER(1, new Position(2006, 5201, 0), new String[] {"Flesh Crawlers, is located in the Stronghold of Security"
				+ "<br>Dungeon. They drop gold & Runes so it's adviseable to"
				+ "<br>Pick them up."
				+ "<br>"
				+ "<br>You get 5 Slayer points for finishing this task."}, 4389),

		// finished
		HOBGOBLIN(1, new Position(1, 1, 0), new String[] {"Hobgoblins, can be found in the Stronghold of Security Dungeon."
				+ "<br>They are a bit stronger than the lvl 5 Goblins, and drops"
				+ "<br>The Crystal key as a rare drop."
				+"<br>"
				+ "<br>You get 5 Slayer points for finishing this task."}, 2685),

		// finished
		KALPHITE(1, new Position(3402, 9488, 0), new String[] {"Kalphites, is located in the Al-kharid Desert Dungeon. Its"
				+ "<br>Required that you bring a rope with you. They drop"
				+"<br>Different kind of Scimitars, and many types of seeds."
				+"<br>"
				+"<br>You get 5 Slayer points for finishing this task."}, 1154),

		// finished
		ROCKSLUG(20, new Position(2795, 10018, 0), new String[] {"TO DO"}, 1631),

		// finished
		ROCK_SLUG(20, new Position(2795, 10018, 0), new String[] {"TO DO"}, 1631,  ROCKSLUG),

		// finished
		ABERRANT_SPECTRE(60, new Position(3439, 3550, 1), new String[] {"Aberrant Spectre's, can be found in the Slayer Tower, on"
				+ "<br>The first floor. They drop a decent amount of runes"
				+ "<br>And it's adviseable to pick them up."
				+ "<br>"
				+ "<br>You get 5 Slayer points for finishing this task."}, 1604),

		// finished
		ANKOU(1, new Position(3024, 10144, 0), new String[] {"Ankou's is found in the Stronghold Security Dungeon. They"
				+ "<br>Have a good drop table compared to other same leveled npcs."
				+ "<br>"
				+ "<br>You get 10 Slayer points for finishing this task."}, 4381),

		// finished
		BASILISK(40, new Position(2736, 10008, 0), new String[] {"Basilsk's, is located in the Fremmenik Slayer Dungeon, and"
				+ "<br>They drop Runes & Cash frequently, so picking them up"
				+ "<br>Would only do good in the long run."
				+ "<br>"
				+ "<br>You get 5 Slayer points for finishing this task." }, 1616),

		// finished
		BLOODVELD(50, new Position(3423, 3565, 1), new String[] {"Bloodvields, can be found in the Slayer tower. They drop decent"
				+ "<br>Items, and Blood runes is a very common drop."
				+ "<br>"
				+ "<br>You get 10 Slayer points for finishing this task. "}, 1618),

		// finished
		BRINE_RAT(47, new Position(2706, 10132, 0), new String[] {"Brine Rats, is found in the Brine rat cave, wich is located"
				+  "<br>In the the Fremmenik province near the Rock crabs. They drop"
				+  "<br>The Brine sabre and the Crystal key."
				+  "<br>"
				+  "<br>You get 5 Slayer points for finishing this task."}, 3707),

		// finished
		COCKATRICE(25, new Position(2798, 10033, 0), new String[] {"Cockatrice's, is located in the Fremmenik slayer dungeon, they"
				+ "<br>Drop all arrows, so it's adviseable to stack them."
				+ "<br>"
				+ "<br>You get 5 Slayer points for finishing this task."}, 1620),

		// finished
		CROCODILE(1, new Position(3329, 2911, 0), new String[] {"TO DO"}, 1993),

		// finished
		CYCLOPS(1, new Position(1647, 10013, 0), new String[] {"Cyclops's can be found in the Warriors guild, with different"
				+  "<br>Combat levels. They drop noted herbs so it's adviseable"
				+  "<br>That you pick up the loots."
				+  "<br>"
				+  "<br>You get 10 Slayer points for finishing this task."}, 116),

		// finished
		CYCLOPSE(1, new Position(1647, 10013, 0), new String[] {"Cyclops's can be found in the Warriors guild, with different"
				+  "<br>Combat levels. They drop noted herbs so it's adviseable"
				+  "<br>That you pick up the loots."
				+  "<br>"
				+  "<br>You get 10 Slayer points for finishing this task."},116, CYCLOPS),

		// finished
		DUST_DEVIL(65, new Position(3234, 9368, 0), new String[] {"Dust Devil's can be found in the Smoke dungeon, you can"
				+  "<br>Access the dungeon, in the Slayer dungeons teleports."
				+  "<br>They drop Blood runes frequently."
				+  "<br>"
				+  "<br>You get 5 Slayer points for finishing this task."}, 1624),

		// finished
		EARTH_WARRIOR(1, new Position(3120, 9971, 0), new String[] {"Earth Warrior's can be found in the Edgeville dungeon."
				+   "<br>They drop meele armours, all the way up to Rune."
				+    "<br>"
				+    "<br>You get 5 Slayer points for finishing this task."}, 124),

		// finished
		GHOUL(1, new Position(3431, 3466, 0), new String[] {"Ghoul's is one of Valius's training places, you can get"
				+   "<br>To them by going to your Training teleports. They drop"
				+   "<br>many form of raw fishes."
				+  "<br>You get 5 Slayer points for finishing this task."}, 1218),

		// finished
		GREEN_DRAGON(1, new Position(3343, 3671, 0), new String[] {"Green Dragons can be found several places in the Wilderness"
				+    "<br>If you wish not to enter the Wilderness, you can kill"
				+    "<br>The Brutal Green Dragons, located in the Ancient Cavern."
				+    "<br>Brutal Dragons, have a better drop table."
				+    "<br>"
				+   "<br>You get 10 Slayer points for finishing this task."}, 941),

		// finished
		GROTWORM(1, new Position(1158, 6499, 0), new String[] {"Grotworms, can be found at the QBD lair. They are high in"
				+	"<br>Combat level, and you should care fighting them."
				+	"<br>Grotworms have a decent drop table."
				+	"<br>"
				+	"<br>You get 10 Points for finishing this task."}, 15462),

		HARPIE_BUG_SWARM(33, new Position(1, 1, 0), new String[] {"TO DO"}, 3153),

		// finished
		HILL_GIANT(1, new Position(3117, 9847, 0), new String[] {"Hill giants, can be found in the Varrock dungeon the"
				+	  "<br>Dungeon that is connected with the Edgeville Dungeon."
				+	  "<br>These creatures are suitable to kill for herblore items."
				+	  "<br>"
				+	  "<br>You get 5 Slayer points for finishing this task."}, 117),

		// finished
		ICE_GIANT(1, new Position(3061, 9575, 0), new String[] {"Ice Giant's can be found in the Asgarian ice dungeon."
				+  "<br>They drop many types of Runes, and also the Crystal key."
				+  "<br>"
				+  "<br>You get 5 Slayer points for finishing this task."}, 111),

		// finished
		ICE_WARRIOR(1, new Position(3046, 9579, 0), new String[] {"Ice Warriors, can be found in the Asgarian ice dungeon, they"
				+  "<br>Drop the Crystal key as a uncommon drop!"
				+  "<br>"
				+  "<br>You get 5 Slayer points for finishing this task."}, 125),

		// finished
		INFERNAL_MAGE(45, new Position(3440, 3561, 1), new String[] {"Infernal Mages, is located in the Slayer tower on the"
				+  "<br>Second floor. They drop a good amount of Herbs and runes."
				+  "<br>"
				+  "<br>You get 10 Slayer points for finishing this task."},1643 ),

		// finished
		JELLY(52, new Position(2701, 10020, 0), new String[] {"Jelly's can be found in the Fremmenik Slayer dungeon."
				+   "<br>They frequently drop Herbs, so it's adviseable to pick"
				+   "<br>Them up."
				+   ""
				+   "You get 5 Slayer points for finishing this task."}, 1637),

		// finished
		JUNGLE_HORROR(1, new Position(3729, 2975, 0), new String[] {"TO DO"}, 4348),

		// finished
		LESSER_DEMON(1, new Position(2936, 9790, 0), new String[] {"Lesser Demons, is located in the Taverley dungeon. They drop"
				+   "Rune items and many kind of Runes."
				+   ""
				+  "You get 10 Slayer points for finishing this task."}, 82),

		// finished
		MOLANISK(39, new Position(1, 1, 0), new String[] {"TO DO"}, 5751),

		// finished
		MOSS_GIANT(1, new Position(3159, 9904, 0), new String[] {"Moss Giant's can be found in the Edgeville Dungeon. They"
				+  "Drop runes frequently, and also the Crystal key as a uncommon."
				+  "drop"
				+  ""
				+ "You get 5 Slayer points for finishing this task."}, 112),

		// finished
		OGRE(1, new Position(2505, 3121, 0), new String[] {"Ogre's are located south of the Gnome maze. They drop"
				+  "Many form of items, some of them are worth picking up."
				+  ""
				+  "You get 5 Slayer points for finishing this task."}, 115),

		// finished
		OTHERWORLDLY_BEING(1, new Position(2385, 4428, 0), new String[] {"Otherworldly beings Are located in Zanaris, they drop"
				+   "Caskets, and Crystal keys and other viable items."
				+   ""
				+  "You get 5 Slayer points for finishing this Task."}, 126),

		// finished
		PYREFIEND(30, new Position(3239, 9365, 0), new String[] {"Pyrefiends, is located in the Smoke Dungeon, they can also"
				+  "Be found in the Godwars dungeon, at the Zammorak section."
				+  "Their drop table is pretty good compared to their level."
				+  ""
				+  "You get 5 Slayer points for finshing this task."}, 1633),

		// finished
		SHADE(1, new Position(3487, 3276, 0), new String[] {"TO DO"}, 1241),

		// finished
		SHADOW_WARRIOR(1, new Position(2701, 9776, 0), new String[] {"TO DO"}, 158),

		// finished
		TUROTH(55, new Position(2725, 10008, 0), new String[] {"Turoth's can be found in the Fremmenik Slayer Dungeon."
				+ "They frequently drop Yew logs, and some other useable items."
				+ "You get 5 Slayer points for finishing this task."}, 1623),

		// finished
		VAMPYRE(1, new Position(1, 1, 0), new String[] {"Vampyres can be found in Morytania near the abandoned"
				+ "Mine. They drop many kind of runes and double charms."
				+ ""
				+ "You get 5 Slayer points for finishing this task."}, 1120),

		// finished
		WEREWOLF(1, new Position(3496, 3486, 0), new String[] {"Werewolves, can be found at Canafis and also in the Godwars"
				+  "Dungeon. They drop different kind if useable items so its"
				+  "Suggested that you pick them up."
				+  ""
				+  "You get 5 Slayer points for finishing this task."}, 1665),

		// finished
		BLUE_DRAGON(1, new Position(2906, 9810, 0), new String[] {"Blue Dragons can be found in the Taverley Dungeon and also in the"
				+	   "<br>Ancient cavern. They drop many Dragon items. It is recommended that"
				+	   "<br>You bring an Antifire-shield to absorb their fire breath damage."
				+	   "<br>"
				+	   "<br>You get 10 Slayer points for finishing this task."}, 55),

		// finished
		BRONZE_DRAGON(1, new Position(2726, 9481, 0), new String[] {"Bronze Dragons, can be found in the Brimhaven Dungeon and also"
				+	     "<br>In the Chaos tunnels. They drop many kind of Dragon items and"
				+	     "<br>The Dragonic visage. an Antifire-shield is recommended."
				+	     "<br>"
				+	     "<br>You get 15 Slayer points for finishing this task."}, 1590),

		// finished
		CAVE_HORROR(58, new Position(3746, 9373, 0), new String[] {"Cave Horrors, can be found in the Mos Le'Harmless Caves."
				+	    "<br>They drop many items, one of them is the Black mask."
				+	    "<br>"
				+	    "<br>You get 5 Slayer points for finishing this task."}, 4353),

		DAGANNOTH(1, new Position(1813, 4405, 2), new String[] {"Dagannoths can be found through the Bossteleports."
				+	 "<br>They drop many kind of items, and also the Crystal key."
				+	 "<br>"
				+	 "<br>You get 10 Slayer points for finishing this task."}, 1338),

		ELF_WARRIOR(1, new Position(2204, 3253, 0), new String[] {"Comming soon."}, 1183),

		// completed
		FEVER_SPIDER(42, new Position(2151, 5094, 0), new String[] {"Comming soon."}, 2850),

		// completed
		FIRE_GIANT(1, new Position(3245, 9364, 0), new String[] {"Fire giants, can be found in the Smoke dungeon. They drop"
				+  "<br>All kind of runes & rune items, it's suggested picking them up."
				+  "<br>"
				+  "<br>You get 5 Slayer points for finishing this task."}, 110),

		FUNGAL_MAGE(1, new Position(4657, 5433, 3), new String[] {"Fungal mage's can be found in the Polypore Dungeon. They"
				+   "<br>Drop the Crystal key as a common drop, and also alot of"
				+   "<br>Other viable items."
				+  "<br>"
				+   "<br>You get 10 Slayer points for finishing this task."}, 14690),

		// finished
		GARGOYLE(75, new Position(3439, 3547, 2), new String[] {"Gargoyles are Slayer monsters located at the top floor in"
				+ "<br>The slayer tower. Gargoyles have a decent drop table."
				+ "<br>"
				+ "<br>You get 10 Slayer points for finishing this task."}, 1610),

		// finished
		GRIFOLAPINE(88, new Position(4627, 5410, 0), new String[] {"Comming soon."}, 14688),

		// finished
		GRIFOLAROO(82, new Position(4650, 5392, 3), new String[] {"Grifaloroos, is located in the Polypore dungeon. They drop"
				+   "<br>Many useable items from Raw sharks to noted magic logs."
				+   "<br>"
				+   "<br>You get 10 Slayer points for finishing this task."}, 14700),

		GRIFALOPINE(88, new Position(4627, 5410, 0), new String[] {"Comming soon."}, 14688, GRIFOLAPINE),

		GRIFALOROO(82, new Position(4650, 5392, 3), new String[] {"Grifaloroos, is located in the Polypore dungeon. They drop"
				+  "<br>Many useable items from Raw sharks to noted magic logs."
				+  "<br>"
				+  "<br>You get 10 Slayer points for finishing this task."}, 14700, GRIFOLAROO),

		// finished
		JUNGLE_STRYKEWYRM(73, new Position(2458, 2903, 0), new String[] {"Jungle strykewyrm, is a mini version of the boss 'Wildywyrm'."
				+   "<br>Killing them requires level 93 Slayer, and they have"
				+   "<br>A very decent drop table. You can find the teleports"
				+   "<br>in the Slayer dungeons teleports."
				+   "<br>"
				+   "<br>You get 15 Slayer points for finishing this Task."}, 9467),

		// finished
		KURASK(70, new Position(2699, 9994, 0), new String[] {"Kurask's can be found in the Fremennik Slayer Dungeon. They drop"
				+ "<br>Many kind of herbs, and also drops the Crystal key as a rare drop."
				+ "<br>"
				+ "<br>You get 5 Slayer points for finishing this task."}, 1608),

		// finished
		FUNGI(57, new Position(2387, 4430, 0), new String[] {"Fungi's, can be found in Zanaris. They drop many kind"
				+   "<br>Of runes, and also the Crystal key."
				+   "<br>"
				+   "<br>You get 5 Slayer points for finishing this task."}, 3346),

		// finished
		ZYGOMITE(57, new Position(2410, 4471, 0), new String[] {"Zygomite's can be found in Zanaris. They drop many kind"
				+  "<br>Of runes, and also the Crystal key."
				+  "<br>"
				+  "<br>You get 5 Slayer points for finishing this task."}, 3346,  FUNGI),

		VYRELORD(31, new Position(3620, 3364, 0), new String[] {"Comming soon!"}, 4822),

		VYRELADY(31, new Position(3620, 3364, 0), new String[] {"Comming soon!"}, 4810),

		VYREWATCH(31, new Position(3620, 3364, 0), new String[] {"Comming soon!"}, 4822, VYRELORD, VYRELADY),

		// finished
		WARPED_TORTOISE(56, new Position(1997, 4171, 1), new String[] {"Warped Tortoise can be found in the Poison Waste Dungeon."
				+	"<br>They drop many useable noted items."
				+	"<br>"
				+	"<br>You get 5 Slayer points for finishing this task."}, 6298),

		// finished
		ABYSSAL_SIRE(85, new Position(3040, 4765, 0), new String[] {"Abyssal Demon's, can be found at the third floor at the Slayer"
				+     "<br>Tower, they drop the Abyssal whip in all colors."
				+     "<br>"
				+     "<br>You get 10 Slayer points for finishing this task."}, 1615),

		ABYSSAL_DEMON(85, new Position(3410, 3574, 2), new String[] {"Abyssal Demon's, can be found at the third floor at the Slayer"
				+     "<br>Tower, they drop the Abyssal whip in all colors."
				+     "<br>"
				+     "<br>You get 10 Slayer points for finishing this task."}, 1615, SlayerTask.ABYSSAL_SIRE),

		// finished
		AQUANITE(78, new Position(2715, 9968, 0), new String[] {"The Aquanites, is located in the Fremennik Slayer Dungeon."
				+  "<br>They drop many useable seeds, and also herbs."
				+  "<br>"
				+  "<br>You get 5 Slayer points for finishing this task."}, 9172),

		// finished
		BLACK_DEMON(1, new Position(2717, 9480, 0), new String[] {"Black demons, can be found in the Brimhaven dungeon and"
				+   "<br>The Edgeville Dungeon. They drop many items varying from"
				+   "<br>Dragon items to Mithril."
				+   "<br>"
				+  "<br>You get 10 Slayer points for finishing this task."}, 84),

		// finished
		DESERT_STRYKEWYRM(77, new Position(3369, 3159, 0), new String[] {"Desert strykewyrm, is a mini version of the boss 'Wildywyrm'."
				+   "<br>Killing them requires level 93 Slayer, and they have"
				+   "<br>A very decent drop table. You can find the teleports"
				+   "<br>in the Slayer dungeons teleports."
				+   "<br>"
				+   "<br>You get 15 Slayer points for finishing this Task."}, 9465),

		// finished
		GREATER_DEMON(1, new Position(26142, 9504, 2), new String[] {"Greater Demons, can be found in the Brimhaven Dungeon"
				+    "<br>They attack with magic, and drops many kind of runes."
				+    "<br>"
				+    "<br>You get 5 Slayer points for finishing this task."}, 83),

		// finished
		CERBERUS(90, new Position(1318, 1240, 0), new String[] {"Hellhounds are located in the Taverley Dungeon"}, 49),

		HELLHOUND(1, new Position(2856, 9847, 0), new String[] {"Hellhounds are located in the Taverley Dungeon"}, 49, SlayerTask.CERBERUS),

		// finished
		IRON_DRAGON(1, new Position(2702, 9428, 0), new String[] { "Iron Dragons, is located in the Ancient Cavern Dungeon and its recommended"
				+	 "<br>To bring your Anti-fireshield. The dragon is weak to ranged attacks."
				+	 "<br>It drops the Dragonic Visage, and many other useable items."
				+	 "<br>"
				+	 "<br>You get 15 Slayer points for finishing this task."}, 1591),

		JADINKO(91, new Position(3065, 9238, 0), new String[] {"Mutated Jadinkos, is located in the Jadinko Lair they can be found"
				+	"<br>With different levels. Their main drop is Vine items."
				+	"<br>"
				+	"<br>You get 10 Slayer points for finishing this task."}, 13819),
		JADINKO_BABY(80, new Position(3065, 9238, 0), new String[] {"Mutated Jadinkos, is located in the Jadinko Lair they can be found"
				+	"<br>With different levels. Their main drop is Vine items."
				+	"<br>"
				+	"<br>You get 10 Slayer points for finishing this task."}, 13816),
		JADINKO_GUARD(86, new Position(3065, 9238, 0), new String[] {"Mutated Jadinkos, is located in the Jadinko Lair they can be found"
				+	"<br>With different levels. Their main drop is Vine items."
				+	"<br>"
				+	"<br>You get 10 Slayer points for finishing this task."}, 13818),
		// finished
		NECHRYAEL(80, new Position(3440, 3563, 2), new String[] {"Nechryaels, is located in the Slayer tower on the third floor."
				+	  "<br>They require a Slayer level of 80 and they drop some cash"
				+	  "<br>On every kill."
				+	  "<br>"
				+	  "<br>You get 10 Slayer points for finishing this task."}, 1613),

		// finished
		RED_DRAGON(1, new Position(2682, 9506, 0), new String[] {"Red Dragons, can be found in the Brimhaven Dungeon."
				+	  "<br>They drop many items, including dragon stuff and also"
				+	  "<br>They drop the crystal key."
				+	  "<br>"
				+	  "<br>You get 10 Slayer points for finishing this Task."}, 53),

		LOCUST(1, new Position(1, 1, 0), new String[] {"Comming soon."}, 1995),

		SCABARAS(1, new Position(1, 1, 0), new String[] {"Comming soon."}, 1969),

		SCARAB(1, new Position(1, 1, 0), new String[] {"Comming soon."}, 1969),

		SCABARITE(1, new Position(1, 1, 0), new String[] {"Comming soon."}, 1969, LOCUST, SCABARAS, SCARAB),

		// finished
		SPIRITUAL_MAGE(83, new Position(2920, 5354, 0), new String[] {"Spiritual Mages can be found in the Godwars dungeon, at the"
				+       "<br>Saradomin area. They drop a decent amount of runes."
				+      "<br>"
				+      "<br>You get 10 Slayer points for finishing this." }, 6221),

		// finished
		SPIRITUAL_WARRIOR(68, new Position(2920, 5354, 0), new String[] {"Spiritual Warriors can be found in the Godwars Dungeon, at the"
				+	  "<br>Saradomin area. They drop decent Weapon gear that can be used"
				+	  "<br>Or sold to the general store for money."
				+	  "<br>"
				+	  "<br>You get 10 Slayer points for finishing this task."}, 6219),

		// finished
		TERROR_DOG(40, new Position(3149, 4649, 0), new String[] {"Comming soon"}, 5417),

		// a stupid troll at death plateau
		ROCK(1, new Position(2911, 3629, 0), new String[] {"Comming soon"}, 1265),

		TROLL(1, new Position(2911, 3629, 0), new String[] {"Trolls, can be found in Trollheim with different combat levels."
				+  "<br>Their drops differs, but the higher the level the better"
				+  "<br>The drops. Selling them to the General store will result in"
				+  "<br>Some good money in the long run."
				+  "<br>"
				+  "<br>You get 5 Slayer points for finishing this task."}, 72, ROCK),

		// finished
		BLACK_DRAGON(1, new Position(2835, 9818, 0), new String[] {"Black Dragons, can be found in the Taverley Dungeon."
				+  "<br>They drop many items, including Dragon, they also"
				+  "<br>Drop the crystal key."
				+  "<br>"
				+  "<br>You get 10 Slayer points for finishing this task."}, 50),

		// finished
		ICE_BEAST(92, new Position(1248, 2655, 0), new String[] { "ice beast are located at the ice dungeon."}, 2783),
		DARK_BEAST(90, new Position(3419, 3550, 2), new String[] {"Dark beasts can be found at the Third floor in Slayer tower"
				+ "<br>And also in the Temple of light Dungeon. They require"
				+ "<br>A Slayer level of 90 to kill, and they drop the Dark bow"
				+ "<br>in all colours."
				+  "<br>"
				+  "<br>You get 10 Slayer points for finishing this task."},2783, ICE_BEAST),

		// finished
		GANODERMIC(95, new Position(4697, 5474, 0), new String[] {"Ganodermic creatures can be found at the bottom level of"
				+  "<br>The Polypore Dungeon. They drop Ganodermic items."
				+   "<br>"
				+   "<br>You get 10 Slayer points for finishing this task."}, 14696),

		// finished
		GORAK(1, new Position(3038, 5341, 0), new String[] {"Goraks, can be acessed in the Training teleports menu."
				+  "<br>They frequently drop the Crystal key and some other useable"
				+  "<br>Items."
				+  "<br>"
				+  "<br>You get 10 Slayer points for finishing this task."}, 4418),

		ICE_STRYKEWYRM(93, new Position(3413, 5673, 0), new String[] {"Ice strykewyrm, is a mini version of the boss 'Wildywyrm'."
				+  "<br>Killing them requires level 93 Slayer, and they have"
				+  "<br>A very decent drop table. You can find the teleports"
				+  "<br>in the Slayer dungeons teleports."
				+  "<br>"
				+  "<br>You get 15 Slayer points for finishing this Task."}, 9463),

		// finished
		MITHRIL_DRAGON(1, new Position(1761, 5340, 1), new String[] {"Mithril Dragons, can be found in the Ancient Cavern."
				+    "<br>It's recommended to bring a Antifire Potion & Antifire-shield."
				+     "<br>They have a very good drop table."
				+      "<br>"
				+      "<br>You get 15 Slayer points for finishing this task."}, 5363),

		// finished
		SKELETAL_WYVERN(72, new Position(3057, 9554, 0), new String[] {"Skeletal Wyverns, can be found in the Asgarian ice dungeon."
				+	"<br>They have a decent drop table, and also pretty good stats."
				+	"<br>"
				+	"<br>You get 10 Slayer points for finishing this task."}, 3068),

		// finished
		STEEL_DRAGON(1, new Position(2725, 9466, 0), new String[] {"Steel Dragons, is located in the Ancient Cavern Dungeon. Its"
				+   "<br>Recommended to bring your Antifire-shield. The dragon is weak to"
				+   "<br>Ranged attacks, and it have a good drop table."
				+   "<br>"
				+  "<br>You get 15 Slayer points for finishing this task."}, 1592),

		// finished
		SUQAH(1, new Position(2123, 3939, 0), new String[] {"Comming soon."}, 4527),

		// finished
		WARPED_TERRORBIRD(56, new Position(2014, 4165, 1), new String[] {"Warped Terrorbird can be found in the Poison"}, 6285),

		// finished
		WATERFIEND(1, new Position(1738, 5349, 0), new String[] {"Waterfiends, is located in the Ancient caveren. They have a okay"
				+  "<br>Drop table. They use Ranged attacks so be prepared."
				+  "<br>"
				+  "<br>You get 10 Points for finishing this task."}, 5361),

		// finished
		LIVING_ROCK(1, new Position(3689, 5148, 0), new String[] {"Living Rock's, can be accessed through the Training teleports"
				+   "<br>They drop living minerals, and some other viable items."
				+   "<br>"
				+   "<br>You get 10 Slayer points for finishing this task."}, 8832),

		// finished
		TZHAAR(1, new Position(4668, 5073, 0), new String[] {"Tzhaar's can be found in the Tzhaar caves. They drop Tokkul"
				+   "<br>On every kill, and you can use them in a shop for Obsidian items."
				+  "<br>"
				+   "<br>You get 10 Slayer points for finishing this task."}, 2591),
		KRAKEN(87, new Position(2275, 9996, 0), new String[] { "Cave kraken can be found at kraken cove."}, 20492),
		CAVE_KRAKEN(87, new Position(2275, 9996, 0), new String[] { "Cave kraken can be found at kraken cove."}, 20492, SlayerTask.KRAKEN),
		THERMONUCLEAR_SMOKE_DEVIL(93, new Position(2910, 2527, 0), new String[] { "Smoke devils can be found in the smoke dungeon."}, 20499),
		SMOKE_DEVIL(93, new Position(2910, 2527, 0), new String[] { "Smoke devils can be found in the smoke dungeon."}, 20498, SlayerTask.THERMONUCLEAR_SMOKE_DEVIL),
		WYRM(62, new Position(1311, 10205, 0), new String[] { "Wyrms can be found in the Karuulm Slayer Dungeon.<br>They drop plenty of herbs & seeds."}, 28610),
		DRAKE(84, new Position(1311, 10205, 0), new String[] { "Drakes can be found in the Karuulm Slayer Dungeon.<br>They drop plenty of herbs & seeds."}, 28612),
		ALCHEMICAL_HYDRA(95, new Position(1311, 10205, 0), new String[] { "Hydras can be found in the Karuulm Slayer Dungeon.<br>They drop plenty of herbs & seeds."}, 28609),
		HYDRA(95, new Position(1311, 10205, 0), new String[] { "Hydras can be found in the Karuulm Slayer Dungeon.<br>They drop plenty of herbs & seeds."}, 28609, SlayerTask.ALCHEMICAL_HYDRA),
		ETHER_TIGER(90, new Position(3393, 9246, 1), new String[] { "Ether tigers can be found in the sand dungeon."}, 16128),
		TOXIC_TIGER(92, new Position(3393, 9246, 1), new String[] { "Toxic tigers can be found in the sand dungeon."}, 16130),
		DARK_TIGER(94, new Position(3393, 9246, 1), new String[] { "Dark tigers can be found in the sand dungeon."}, 16129),
		ETHER_WORM(90, new Position(3393, 9246, 1), new String[] { "Ether worms can be found in the sand dungeon."}, 16126),
		TOXIC_WORM(92, new Position(3393, 9246, 1), new String[] { "Toxic worms can be found in the sand dungeon."}, 16134),
		DARK_WORM(94, new Position(3393, 9246, 1), new String[] { "Dark worms can be found in the sand dungeon."}, 16133),
		ETHER_TOAD(90, new Position(3393, 9246, 1), new String[] { "Ether toads can be found in the sand dungeon."}, 16127),
		TOXIC_TOAD(92, new Position(3393, 9246, 1), new String[] { "Toxic toads can be found in the sand dungeon."}, 16132),
		SAND_MUMMY(75, new Position(3393, 9246, 1), new String[] { "Sand mummys can be found in the sand dungeon."}, 16151),
		ICE_ABYSSAL_DEMON(85, new Position(1237, 2639, 0), new String[] { "Ice abyssal demons are located at the ice dungeon."}, 16101),
		ICE_DEMON(82, new Position(1177, 2675, 0), new String[] { "Ice` demons are located at the ice dungeon."}, 9052),
		LAVA_STRYKEWYRM(93, new Position(3064, 3769, 0), new String[] { "lava strykewyrms can be found in the wilderness."}, 16153),
		VENENATIS(1, new Position(3358, 3743, 0), new String[] { "Venenatis can be found in the wilderness."}, 26504),
		CALLISTO(1, new Position(3266, 3835, 0), new String[] { "Callisto can be found in the wilderness"}, 26609),
		SCORPIA(1, new Position(3235, 3940, 0), new String[] { "Scorpia can be found in the wilderness."}, 6615),
		DARK_TOAD(94, new Position(3393, 9246, 1), new String[] { "Dark toadss can be found in the sand dungeon."}, 16131);

		;

        private String[] tips;
		private SlayerTask[] alternatives;
		private int levelRequried;
		private Position destination;
		private int id;

		private SlayerTask(int levelRequried, Position destination, String[] tips, int id, SlayerTask... alternatives) {
			this.levelRequried = levelRequried;
			this.destination = destination;
			this.tips = tips;
			this.alternatives = alternatives;
			this.id = id;
		}

		public Position getMonsterLocation() {
			return destination;
		}

		public String[] getTips() {
			return tips;
		}

		public int getId() {return id;}

		public SlayerTask[] getAlternatives() {
			return alternatives;
		}

		public int getLevelRequried() {
			return levelRequried;
		}

		public String getName() {
			return Utils.formatPlayerNameForDisplay(toString());
		}
	}

	public static boolean canAttackNPC(int slayerLevel, String name) {
		return slayerLevel >= getLevelRequirement(name);
	}

	public static int getLevelRequirement(String name) {
		for (SlayerTask task : SlayerTask.values()) {
			if (name.toLowerCase().contains(task.toString().replace("_", " ").toLowerCase())) {
				return task.getLevelRequried();
			}
		}
		return 1;
	}

	public static boolean hasNosepeg(Entity target) {
		if (!(target instanceof Player)) {
			return true;
		}
		Player targetPlayer = (Player) target;
		int hat = targetPlayer.getEquipment().getHatId();
		return hat == 4168 || hasSlayerHelmet(target);
	}

	public static boolean hasEarmuffs(Entity target) {
		if (!(target instanceof Player)) {
			return true;
		}
		Player targetPlayer = (Player) target;
		int hat = targetPlayer.getEquipment().getHatId();
		return hat == 4166 || hat == 13277 || hasSlayerHelmet(target);
	}

	public static boolean hasMask(Entity target) {
		if (!(target instanceof Player)) {
			return true;
		}
		Player targetPlayer = (Player) target;
		int hat = targetPlayer.getEquipment().getHatId();
		return hat == 1506 || hat == 4164 || hat == 13277 || hasSlayerHelmet(target);
	}

	public static boolean hasWitchWoodIcon(Entity target) {
		if (!(target instanceof Player)) {
			return true;
		}
		Player targetPlayer = (Player) target;
		int hat = targetPlayer.getEquipment().getAmuletId();
		return hat == 8923;
	}

	public static boolean hasSlayerHelmet(Entity target) {
		if (!(target instanceof Player)) {
			return true;
		}
		Player targetPlayer = (Player) target;
		int hat = targetPlayer.getEquipment().getHatId();
		return hat == 13263 || hat == 14636 || hat == 14637 || hasFullSlayerHelmet(target);
	}

	public static boolean hasFullSlayerHelmet(Entity target) {
		if (!(target instanceof Player)) {
			return true;
		}
		Player targetPlayer = (Player) target;
		int hat = targetPlayer.getEquipment().getHatId();
		return hat == 15492 || hat == 15496 || hat == 15497 || hat >= 22528 && hat <= 22550;
	}
	
	public static boolean hasDeathSlayerHelmet(Entity target) {
		if (!(target instanceof Player)) {
			return true;
		}
		Player targetPlayer = (Player) target;
		int hat = targetPlayer.getEquipment().getHatId();
		return hat == 29115;
	}

	public static boolean hasBloodSlayerHelmet(Entity target) {
		if (!(target instanceof Player)) {
			return true;
		}
		Player targetPlayer = (Player) target;
		int hat = targetPlayer.getEquipment().getHatId();
		return hat == 28835;
	}



	public static boolean hasReflectiveEquipment(Entity target) {
		if (!(target instanceof Player)) {
			return true;
		}
		Player targetPlayer = (Player) target;
		int shieldId = targetPlayer.getEquipment().getShieldId();
		return shieldId == 4156;
	}

	public static boolean hasSpinyHelmet(Entity target) {
		if (!(target instanceof Player)) {
			return true;
		}
		Player targetPlayer = (Player) target;
		int hat = targetPlayer.getEquipment().getHatId();
		return hat == 4551 || hasSlayerHelmet(target);
	}

	public static boolean isUsingBell(final Player player) {
		player.lock(3);
		player.animate(new Animation(6083));
		List<WorldObject> objects = World.getRegion(player.getRegionId()).getAllObjects();
		if (objects == null) {
			return false;
		}
		for (final WorldObject object : objects) {
			if (!object.withinDistance(player, 3) || object.getId() != 22545) {
				continue;
			}
			player.getPackets().sendGameMessage("The bell re-sounds loudly throughout the cavern.");
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					NPC npc = World.spawnNPC(5751, player, -1, 0, true);
					npc.getCombat().setTarget(player);
					WorldObject o = new WorldObject(object);
					o.setId(22544);
					World.spawnTemporaryObject(o, 30000);
				}
			}, 1);
			return true;
		}
		return false;
	}
	
	public static void teleportToTask(Player player) {
		if (player.getSlayerManager().getCurrentTask() == null) {
			player.sm("You do not have a task. Talk to a Slayer Master to get one.");
		} else {
			if (player.getSlayerManager().getCurrentTask().getMonsterLocation() != null) {
				if (player.getControlerManager().getControler() == null) { //Most likely a better way to check if he could tp 
				Magic.sendNormalTeleportSpell(player, 1, 0,
						player.getSlayerManager().getCurrentTask().getMonsterLocation());
				} else {
					player.sm("Please leave this area before teleporting to your task's monsters location.");
				}
			}
		}
	}
	
}
