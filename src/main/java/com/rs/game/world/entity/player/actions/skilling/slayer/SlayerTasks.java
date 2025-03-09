package com.rs.game.world.entity.player.actions.skilling.slayer;

import com.rs.game.map.Position;

/**
 * This is a list of assignable tasks
 * 
 * @author Emperial
 * 
 */
public enum SlayerTasks {

	BANSHEES1("Banshees", TaskSet.TURAEL, new Position(3447, 3535, 0),
			"Banshees are Slayer monsters that require level 15 Slayer and earmuffs to kill."
					+ "<br>Additionally, banshees tend to frequently drop many different types of herbs"
					+ "<br>Mighty banshees are a higher-leveled alternative, if this is given as your Slayer assignment."
					+ "<br>Banshees can be located at the Slayer Tower.",
			1, 20, 40, "Banshee"),

	CAVEBUGS("Cave Bugs", TaskSet.TURAEL, new Position(3147, 9577, 0),
			"Cave bugs are monsters that require level 7 Slayer to kill. They are located in"
					+ "<br>The Lumbridge Swamp Caves, and stronger variants can be located in the"
					+ "<br>Dorgesh-Kaan South Dungeon. They are a good source of herbs which can"
					+ "<br>Be usefull for when training the Herblorer skill.",
			1, 20, 40, "Cave bug"),

	CHICKENS("Chickens", TaskSet.TURAEL, new Position(3237, 3296, 0), "No describtion yet.", 1, 20, 40, "Chicken"), // Awating
																														// suggestion
																														// answer

	BEARS("Bears", TaskSet.TURAEL, new Position(3147, 9577, 0), "No describtion yet.", 1, 20, 40, "Grizzly bear cub",
			"Grizzly bear", "Grizzly Bear", "Black Bear", "Bear Cub"),

	COWS("Cows", TaskSet.TURAEL, new Position(3176, 3316, 0),
			"Cows are one of the training places on RealityX, and can be found easily"
					+ "<br>Through the training teleport tab, you can pickup their hides"
					+ "<br>And sell to the general store for basic money.",
			1, 20, 40, "Cow calf", "Cow"),

	GOBLINS("Goblins", TaskSet.TURAEL, new Position(2955, 3503, 0),
			"Goblins can be found in Lumbridge, requires nothing to kill." + "<br>Their rare drop is the Crystal key.",
			1, 20, 40, "Goblin", "Cave goblin guard"),

	ICEFIENDS("Icefiends", TaskSet.TURAEL, new Position(3003, 3485, 0),
			"Icefiends are located on top of the Ice Mountain, near the Dwarven Mine"
					+ "<br>At home. They are also found in the Zamorakian region of the God Wars Dungeon"
					+ "<br>But take care, they are stronger and higher leveled down there.",
			1, 10, 20, "Icefiend"),

	MINOTAURS("Minotaurs", TaskSet.TURAEL, new Position(1873, 5240, 0),
			"Minotaurs, are located in the Stronghold Of Security just near Home."
					+ "<br>They are a low leveled monster, so you should not worry."
					+ "<br>It has the chance to drop the Crystal key as a rare.",
			1, 20, 40, "Minotaur"),

	ZOMBIES("Zombies", TaskSet.TURAEL, new Position(3241, 10000, 0),
			"Zombies can be located, down the Edgeville Dungeon or"
					+ "<br>In the Wilderness, Armoured Zombies counts your task and"
					+ "<br>Can be found in the Training teleports tab, they are stronger"
					+ "<br>Than normal Zombies, so take care." + "<br>"
					+ "<br>NOTE: Drops Armour Sets, so picking up the loots is viable.",
			1, 20, 40, "Zombie", "Armoured zombie"),

	CRAWLING_HANDS("Crawling hands", TaskSet.TURAEL, new Position(3410, 3537, 0),
			"Crawling Hands, can be located in the Slayer Tower at the first floor."
					+ "<br>They are no big deal, and you should not worry about this creature."
					+ "<br>They drop Runes frequently, so you should not neglect picking them up.",
			5, 5, 15, "Zombie hand", "Skeletal hand", "Crawling Hand"),

	STS("Ghosts", TaskSet.TURAEL, new Position(2321, 5238, 0),
			"Ghosts is located out in the Stronghold of Security dungeon and"
					+ "<br>Deep in the Wilderness, you can access the Stronghold dungeon by" + "<br>Walking from home.",
			1, 20, 40, "Ghost", "ghost"),

	BATS("Bats", TaskSet.TURAEL, new Position(2913, 9832, 0),
			"Giant bats, can be located in the Taverly Dungeon. You can"
					+ "<br>Access the Dungeon by going to the Slayer Teleports",
			1, 20, 40, "Bat", "Giant bat"),

	DWARF("Dwarves", TaskSet.TURAEL, new Position(3017, 9832, 0),
			"You can find low level Dwarfs, in the Dwarven mines. They do"
					+ "<br>no harm, so you should not be worried." + "<br>"
					+ "<br>They drop the Crystal key as a rare drop.",
			1, 20, 40, "Dwarf", "Drunken dwarf", "Black Guard crossbowdwarf", "Black Guard berserker",
			"Chaos dwarf hand cannoneer"),

	SCORPIONS("Scorpions", TaskSet.TURAEL, new Position(3298, 3302, 0),
			"Scorpions are low level creatures located in the Al-Kharid Mine."
					+ "<br>Aswell in the Dwarven mines, they are a very weak creature"
					+ "<br>And you should have no problem killing these.",
			1, 20, 40, "Scorpion", "King scorpion", "Poison Scorpion", "Pit Scorpion", "Khazard scorpion",
			"Grave scorpion"),

	SKELETON("Skeletons", TaskSet.TURAEL, new Position(2884, 9827, 0),
			"Skeletons can be located in the Taverly Dungeon, just at the entrance."
					+ "<br>Or in the deep in the Wilderness at the Grave Yard.",
			1, 20, 40, "Skeleton", "Skeleton Mage", "Giant Skeleton"),

	TROLLS("Trolls", TaskSet.TURAEL, new Position(2911, 3629, 0),
			"Trolls, can be found in Trollheim with different combat levels."
					+ "<br>Their drops differs, but the higher the level the better" + "<br>the drop." + "<br>"
					+ "<br>Note : Picking up the loots & selling it to the General store"
					+ "<br>Will result in some good money in the long run.",
			1, 20, 40, "Mountain troll", "Ice troll", "River troll", "Sea troll", "Thrower Troll", "Stick", "Rock",
			"Pee Hat", "Kraka", "Troll general", "Troll spectator"),

	WOLVES("Wolves", TaskSet.TURAEL, new Position(2850, 3500, 0),
			"Wolfs can be found with different levels at the White Wolf Mountain."
					+ "<br>You can get there by running from Camelot or Catherby.",
			1, 20, 40, "Ice wolf", "Wolf", "Big wolf", "Big Wolf", "White Wolf"),

	BIRDS("Birds", TaskSet.TURAEL, new Position(2541, 3217, 0),
			"Terrorbirds, can be found at the Tree Gnome Village Battlefield."
					+ "<br>You can teleport there by going to your teleportal tab, and"
					+ "<br>Clicking on the Training teleports.",
			1, 20, 40, "Chicken", "Duck", "Terrorbird", "Chompy Bird", "Jubbly Bird"), MEN("Men and woman",
					TaskSet.TURAEL, new Position(3147, 9577, 0), "Describtion/location Comming soon.", 1, 10, 20,
					"Woman", "Man"),

	ABOM("Gelatinous Abominations", TaskSet.TURAEL, new Position(2216, 4515, 0),
			"Gelatinous Abominations can be found in the Taverly Slayer Dungeon."
					+ "<br>This monster, drops many herbs and is ideal for collecting herbs.",
			1, 5, 15, "Gelatinous Abomination", "Magic Stick"),

	/*
	 * Mazchina / Achtryn Tasks
	 */
	BANSHEES2("Banshees", TaskSet.TURAEL, new Position(3447, 3535, 0),
			"Banshees are Slayer monsters that require level 15 Slayer and earmuffs to kill."
					+ "<br>Additionally, banshees tend to frequently drop many different types of herbs"
					+ "<br>Mighty banshees are a higher-leveled alternative, if this is given as your Slayer assignment."
					+ "<br>Banshees can be located at the Slayer Tower.",
			1, 20, 40, "Banshee"),

	BANSHEES("Banshees", TaskSet.TURAEL, new Position(3447, 3535, 0),
			"Banshees are Slayer monsters that require level 15 Slayer and earmuffs to kill."
					+ "<br>Additionally, banshees tend to frequently drop many different types of herbs"
					+ "<br>Mighty banshees are a higher-leveled alternative, if this is given as your Slayer assignment."
					+ "<br>Banshees can be located at the Slayer Tower.",
			1, 20, 40, "Banshee"),

	BATS2("Bats", TaskSet.MAZ, new Position(2913, 9832, 0),
			"Giant bats, can be located in the Taverly Dungeon. You can"
					+ "<br>Access the Dungeon by going to the Slayer Teleports",
			1, 20, 40, "Bat", "Giant bat"),

	CAVECRAWLER("Cave Crawlers", TaskSet.MAZ, new Position(2802, 9999, 0),
			"Cave Crawlers is located in the Fremmenik Slayer Dungeon."
					+ "<br>They are no threath, and you should not be worried.",
			1, 20, 40, "Cave crawler"),

	COCKATRICE("Cockatrices", TaskSet.MAZ, new Position(2006, 5201, 0), "Comming Soon", 1, 20, 40, "Cockatrice"),

	FC("Flesh Crawlers", TaskSet.MAZ, new Position(2006, 5201, 0),
			"Flesh Crawlers can be located in the Stronghold of Security Dungeon."
					+ "<br>These creatures does no harm, and drops runes & gold.",
			1, 20, 40, "Flesh Crawler"),

	Ghoul("Ghouls", TaskSet.MAZ, new Position(3431, 3466, 0),
			"Ghouls is can be found through the Training teleport menu."
					+ "<br>They drop decent items, that can be sold to the general store.",
			1, 20, 40, "Ghoul"),

	Ghost("Ghosts", TaskSet.MAZ, new Position(2321, 5238, 0),
			"Ghosts is located out in the Stronghold of Security dungeon and"
					+ "<br>Deep in the Wilderness, you can access the Stronghold dungeon by" + "<br>Walking from home.",
			1, 20, 40, "Ghost", "ghost"),

	Grots("Grotworms", TaskSet.TURAEL, new Position(1158, 6499, 0),
			"Grotworms, are located at Queen black dragons lair they" + "<br>are near the entrance to fighting QBD."
					+ "<br>" + "<br>Note : This creature drops valueable items.",
			1, 20, 40, "Young grotworm", "Grotworm", "Mature grotworm"),

	Hill("Hill Giants", TaskSet.MAZ, new Position(3117, 9847, 0),
			"Hill Giants, are located in the Edgeville Dungeon that is"
					+ "<br>Connected to the Varrock Dungeon. Hill Giants drops"
					+ "<br>Many kind of runes, and it is suggested to loot the drops.",
			1, 20, 40, "Hill Giant"),

	Ghosts("Hobgoblins", TaskSet.MAZ, new Position(3147, 9577, 0), "Comming soon!", 1, 20, 40, "Hobgoblin"),

	IceW("Ice warriors", TaskSet.MAZ, new Position(3046, 9579, 0),
			"Ice Warriors, can be found in the Asgarnian Ice Dungeon that is located"
					+ "<br>In Port Sarim. These creatures drops many kind of herbs and junk items"
					+ "<br>That can be sol to the general store for money.",
			1, 20, 40, "Ice warrior"),

	Kalphite("Kalphites", TaskSet.MAZ, new Position(3402, 9488, 0),
			"Kalphite monsters can be found in the Desert Dungeon."
					+ "<br>The Dungeon that contains the Kalphite Queen aswell." + "<br>"
					+ "<br>Note : These monsters, drops the Crystal key as a common drop.",
			1, 20, 40, "Kalphite queen", "Kalphite larva", "Kalphite worker", "Kalphite soldier", "Kalphite guardian"),

	Pyrefiend("Pyrefiends", TaskSet.MAZ, new Position(3239, 9365, 0),
			"Pyrefiends is located, in the Smoke Dungeon and can also"
					+ "<br>Be found in the Godwars Dungeon, in the Zammorak chamber.",
			30, 20, 40, "Pyrefiend"),

	Skeleton2("Skeletons", TaskSet.MAZ, new Position(2884, 9827, 0),
			"Skeletons can be located in the Taverly Dungeon, just at the entrance."
					+ "<br>Or in the deep in the Wilderness at the Grave Yard.",
			1, 20, 40, "Skeleton"),

	Zombie2("Zombies", TaskSet.MAZ, new Position(3241, 10000, 0),
			"Zombies can be located, down the Edgeville Dungeon or"
					+ "<br>In the Wilderness, Armoured Zombies counts your task and"
					+ "<br>Can be found in the Training teleports tab, they are stronger"
					+ "<br>Than normal Zombies, so take care." + "<br>"
					+ "<br>NOTE: Drops Armour Sets, so picking up the loots is viable.",
			1, 20, 40, "Zombie"),

	/*
	 * Chaeldar Tasks
	 */
	ABBYSPEC("Aberrant spectres", TaskSet.CHAELDAR, new Position(3439, 3550, 1),
			"Aberrant Spectre can be located in the Slayer Tower on the first floor."
					+ "<br>The Aberrant spectre drops a good amount of runes.",
			60, 20, 40, "Aberrant spectre"),

	BANSHEES3("Banshees", TaskSet.CHAELDAR, new Position(3447, 3535, 0),
			"Banshees are Slayer monsters that require level 15 Slayer and earmuffs to kill."
					+ "<br>Additionally, banshees tend to frequently drop many different types of herbs"
					+ "<br>Mighty banshees are a higher-leveled alternative, if this is given as your Slayer assignment."
					+ "<br>Banshees can be located at the Slayer Tower.",
			15, 20, 40, "Banshee", "banshee", "Mighty banshee", "Mighty_banshee"), Basilisks("Basilisks",
					TaskSet.CHAELDAR, new Position(2736, 10008, 0), "Basilisk's is located in the Fremmenik Dungeon."
							+ "<br>These drops runes frequently, and some herbs.",
					40, 20, 40, "Basilisk"),

	BloodVelds("Bloodvelds", TaskSet.CHAELDAR, new Position(3423, 3565, 1),
			"Bloodvelds can be located in the Slayer Tower on the Third floor."
					+ "<br>Blood runes, is a very common drop from these creatures." + "<br>"
					+ "<br>Note : This creature also drop the Crystal key.",
			50, 20, 40, "Bloodveld"),

	Blue_Dragon("Blue Dragons", TaskSet.CHAELDAR, new Position(2906, 9810, 0),
			"Blue Dragons, is located in the Taverley Dungeon and its recommended"
					+ "<br>To bring your Anti-fireshield. The dragon is weak to ranged attacks."
					+ "<br>It drops the Dragonic Visage, and many other useable items." + "<br>"
					+ "<br>Note : Higher level Dragons, have a higher rate at drops.",
			1, 20, 40, "Blue dragon"),

	Bronze_Dragon("Bronze Dragons", TaskSet.CHAELDAR, new Position(2726, 9481, 0),
			"Bronze Dragons, is located in the Taverley Dungeon and its recommended"
					+ "<br>To bring your Anti-fireshield. The dragon is weak to ranged attacks."
					+ "<br>It drops the Dragonic Visage, and many other useable items." + "<br>"
					+ "<br>Note : Higher level Dragons, have a higher rate at drops.",
			11, 20, 40, "Bronze dragon"),

	CC("Cave Crawlers", TaskSet.CHAELDAR, new Position(2795, 9998, 0),
			"Cave Crawlers, can be found in the Fremmenik Slayer dungeon."
					+ "<br>They are found just by the entrance, and do no treath.",
			10, 20, 40, "Cave crawler"),

	CH("Crawling Hands", TaskSet.CHAELDAR, new Position(3410, 3537, 0),
			"Crawling Hands, can be located in the Slayer Tower at the first floor."
					+ "<br>They are no big deal, and you should not worry about this creature."
					+ "<br>They drop Runes frequently, so you should not neglect picking them up.",
			5, 20, 40, "Crawling hand", "Zombie hand", "Skeletal hand", "Skeletal hand", "Crawling Hand"),

	Dags("Dagganoths", TaskSet.CHAELDAR, new Position(1805, 4405, 3),
			"The Dagannoths, can be found in their dungeon. It can be accessed" + "<br>Through the boss teleports"
					+ "<br>" + "<br>Note : Dagannoths, are a dangerous creature. You should"
					+ "<br>Be wearing some decent gear.",
			1, 20, 40, "Dagannoth", "Dagannoth Mother", "Dagannoth guardian", "Dagannoth spawn", "Dagannoth Prime",
			"Dagannoth Supreme", "Dagannoth Rex"), FG("Fire giants", TaskSet.CHAELDAR, new Position(3234, 9368, 0),
					"Fire Giants, is located in the Smoke Dungeon. The Fire giants"
							+ "<br>Drops many Rune items frequently, and its recommended to"
							+ "<br>Pick up the items, and sell to the General store.",
					1, 20, 40, "Fire giant"),

	FUNG("Fungal Mages", TaskSet.CHAELDAR, new Position(4657, 5433, 3),
			"Fungal mage, can be found in the Polypore Dungeon. You can"
					+ "<br>Access the teleport to the Dungeon in the Slayer Dungeon" + "<br>Teleports.",
			1, 20, 40, "Fungal mage", "Fungal magi"),

	Garg("Gargoyles", TaskSet.CHAELDAR, new Position(3439, 3547, 2),
			"Gargoyles are Slayer monsters located in the Slayer Tower's top floor"
					+ "<br>And basement requiring 75 Slayer in order to be harmed."
					+ "<br>Gargoyles have decent drops that can be sold to make money of." + "<br>"
					+ "<br>Note : You must bring a rock hammer to kill these.",
			75, 20, 40, "Gargoyle"),

	Grots2("Grotworms", TaskSet.CHAELDAR, new Position(1158, 6499, 0),
			"Grotworms, are located at Queen black dragons lair they" + "<br>are near the entrance to fighting QBD."
					+ "<br>" + "<br>Note : This creature drops valueable items.",
			1, 20, 40, "Young grotworm", "Grotworm", "Mature grotworm"),

	JS("Jungle Strykewyrms", TaskSet.CHAELDAR, new Position(2458, 2903, 0),
			"Strykewyrms, is a mini version of the boss Wildywyrm."
					+ "<br>Killing them requires level 93 Slayer, and they have" + "<br>A very good drop table.",
			73, 20, 40, "Jungle strykewyrm"),

	IMages("Infernal mages", TaskSet.CHAELDAR, new Position(3440, 3561, 1),
			"Infernal Mages, is located in the Slayer tower on the second floor."
					+ "They drop a good amount of Herbs & Runes. You can sell these for"
					+ "<br>Good amounts of money, or train Herblore & Magic.",
			45, 20, 40, "Infernal Mage", "Infernal_Mage"),

	JELLY("Jellies", TaskSet.CHAELDAR, new Position(2701, 10020, 0),
			"Jelly's, can be found in the Fremmenik Dungeon." + "<br>They drop decent amounts of herbs.", 52, 20, 40,
			"Jelly"),

	Kalphite2("Kalphites", TaskSet.CHAELDAR, new Position(3402, 9488, 0),
			"Kalphite monsters can be found in the Desert Dungeon."
					+ "<br>The Dungeon that contains the Kalphite Queen aswell." + "<br>"
					+ "<br>Note : These monsters, drops the Crystal key as a common drop.",
			1, 20, 40, "Kalphite queen", "Kalphite larva", "Kalphite worker", "Kalphite soldier",
			"Kalphite guardian"), LDemon("Lesser Demons", TaskSet.CHAELDAR, new Position(2936, 9790, 0),
					"Lesser Demons, is located in the Taverley Dungeon and drops"
							+ "<br>Rune items and many kind of runes. You can access the"
							+ "<br>Dungeon in the Slayer Teleports",
					1, 20, 40, "Lesser demon"),

	Turoth("Turoths", TaskSet.CHAELDAR, new Position(2725, 10008, 0),
			"The Turoths can be found in the Fremmeniks Dungeon."
					+ "<br>Turoths drops mediocre items, sellable in the store.",
			55, 20, 40, "Turoth"),

	ANKOU("Ankou", TaskSet.CHAELDAR, new Position(2319, 5227, 0),
			"Ankous, is located in the Stronghold of Secutiry dungeon and"
					+ "<br>Drops decent items, that can be sold to the general store" + "<br>For some decent money",
			1, 30, 40, "Ankou"),

	OGRES("Ogres", TaskSet.CHAELDAR, new Position(2505, 3121, 0),
			"Ogres is located South of the Gnome Maze, they drop"
					+ "<br>Many random items, that is worth picking up and selling.",
			1, 20, 30, "Ogre", "Zogre", "Skogre", "Jogre"),

	COCKROACHES("Cockroaches", TaskSet.CHAELDAR, new Position(3151, 4277, 3),
			"Cockroaches, can be found in the Stronghold Of Player Safety"
					+ "<br>Dungeon. They are no threath, and you should not be aware of anything.",
			1, 20, 30, "Cockroach drone", "Cockroach worker", "Cockroach soldier", "Cockroach_soldier",
			"Cockroach_worker", "Cockroach_drone"),

	BRINERATS("Brine Rats", TaskSet.CHAELDAR, new Position(2706, 10132, 0),
			"Brine rats, is found in the Brine Rat Cave that is located"
					+ "<br>At the Rock Crabs in the Fremennik Province.",
			47, 25, 50, "brine rat", "Brine rat", "Brine_rat", "brine_rat"),
	/*
	 * Duradel Tasks
	 */
	ABBYSPEC2("Aberrant spectres", TaskSet.DURADEL, new Position(3439, 3550, 1),
			"Aberrant Spectre can be located in the Slayer Tower on the first floor."
					+ "<br>The Aberrant spectre drops a good amount of runes.",
			60, 20, 40, "Aberrant spectre"),

	ABYDEMON("Abyssal demons", TaskSet.DURADEL, new Position(3410, 3574, 2),
			"Abyssal Demons, can be found in the Slayer tower at the last floor."
					+ "<br>They drop the Abyssal whip in all colors, and various of other viable items.",
			85, 20, 40, "Ayssal demon"),

	BDemon("Black demons", TaskSet.DURADEL, new Position(2717, 9480, 0),
			"Black Demons, is located in Brimhaven Dungeon, and the Edgeville"
					+ "<br>Dungeon aswell. These monsters are Dangerous and drops decent items.",
			1, 20, 40, "Black demon"),

	BDragon("Black dragons", TaskSet.DURADEL, new Position(2835, 9818, 0),
			"Black Dragons, is located in the Taverley Dungeon and its recommended"
					+ "<br>To bring your Anti-fireshield. The dragon is weak to ranged attacks."
					+ "<br>It drops the Dragonic Visage, and many other useable items." + "<br>"
					+ "<br>Note : Higher level Dragons, have a higher rate at drops.",
			1, 20, 40, "Black dragon"),

	BVElds("Bloodvelds", TaskSet.DURADEL, new Position(3423, 3565, 1),
			"Bloodvelds can be located in the Slayer Tower on the Third floor."
					+ "<br>Blood runes, is a very common drop from these creatures.",
			50, 20, 40, "Bloodveld"),

	Dags2("Dagganoths", TaskSet.DURADEL, new Position(1805, 4405, 3),
			"The Dagannoths, can be found in their dungeon. It can be accessed" + "<br>Through the boss teleports"
					+ "<br>" + "<br>Note : Dagannoths, are a dangerous creature. You should"
					+ "<br>Be wearing some decent gear.",
			1, 20, 40, "Dagannoth", "Dagannoth Mother", "Dagannoth guardian", "Dagannoth spawn", "Dagannoth Prime",
			"Dagannoth Supreme", "Dagannoth Rex"),

	DBeast("Dark beasts", TaskSet.DURADEL, new Position(3419, 3550, 2),
			"Dark Beasts, is located in the Slayer tower at the last floor."
					+ "<br>And drops the Dark bow in different colors.",
			90, 20, 40, "Dark beast"),

	DStryke("Desert strykewyrms", TaskSet.DURADEL, new Position(3369, 3159, 0),
			"Strykewyrms, is a mini version of the boss Wildywyrm."
					+ "<br>Killing them requires level 93 Slayer, and they have" + "<br>A very good drop table.",
			77, 20, 40, "Desert strykewyrms"),

	DDevil("Dust devil", TaskSet.DURADEL, new Position(3215, 9357, 0),
			"Dust Devils is located, in the Smoke Dungeon that you can accessed"
					+ "<br>in the Slayer Dungeon teleports.",
			65, 20, 40, "Dust devil"),

	FGiant("Fire giants", TaskSet.DURADEL, new Position(3234, 9368, 0),
			"Fire Giants, is located in the Smoke Dungeon. The Fire giants"
					+ "<br>Drops many Rune items frequently, and its recommended to"
					+ "<br>Pick up the items, and sell to the General store.",
			1, 20, 40, "Fire giant"),

	FUNG2("Fungal Mages", TaskSet.DURADEL, new Position(4657, 5433, 3),
			"Fungal mage, can be found in the Polypore Dungeon. You can"
					+ "<br>Access the teleport to the Dungeon in the Slayer Dungeon" + "<br>Teleports.",
			1, 20, 40, "Fungal mage", "Fungal magi"),

	GCreatures("Ganodermic beasts", TaskSet.DURADEL, new Position(4697, 5474, 0),
			"Ganodermic beasts can be found in the Polypore dungeon"
					+ "<br>They deal magic damage, and have decent drops.",
			95, 20, 40, "Ganodermic beast"),

	garg2("Gargoyles", TaskSet.DURADEL, new Position(3439, 3547, 2),
			"Gargoyles are Slayer monsters located in the Slayer Tower's top floor"
					+ "<br>And basement requiring 75 Slayer in order to be harmed."
					+ "<br>Gargoyles have decent drops that can be sold to make money of.",
			75, 20, 40, "Gargoyle"),

	GDEmons("Greater demons", TaskSet.DURADEL, new Position(2642, 9504, 2),
			"Greater Demons, can be found in the Brimhaven Dungeon"
					+ "<br>They attack with magic, and drop many kind of runes.",
			1, 20, 40, "Greater demon"),

	Grif("Grifalopines", TaskSet.DURADEL, new Position(3147, 9577, 0), "Describtion/location Comming soon.", 88, 20,
			40, "Grifalopine"),

	Grif2("Grifaloroo", TaskSet.DURADEL, new Position(4650, 5392, 3),
			"Grifaloroos is located in the Polypore Dungeon at the end."
					+ "<br>It drops decent items, and it would be wise to pick them up.",
			82, 20, 40, "Grifaloroo"),

	Grots3("Grotworms", TaskSet.DURADEL, new Position(1158, 6499, 0),
			"Grotworms, are located at Queen black dragons lair they" + "<br>are near the entrance to fighting QBD."
					+ "<br>" + "<br>Note : This creature drops valueable items.",
			1, 20, 40, "Young grotworm", "Grotworm", "Mature grotworm"),

	HH("Hellhounds", TaskSet.DURADEL, new Position(2856, 9847, 0),
			"Hellhounds are located in the Taverley Dungeon, and can"
					+ "<br>Also be found in the Godwars dungeon. They drop many"
					+ "<br>Types of armor including some Dragon pieces.",
			1, 20, 40, "Hellhound"),

	IStryke("Ice strykewyrm", TaskSet.DURADEL, new Position(3413, 5673, 0),
			"Strykewyrms, is a mini version of the boss Wildywyrm."
					+ "<br>Killing them requires level 93 Slayer, and they have" + "<br>A very good drop table.",
			93, 20, 40, "Ice strykewyrm"),

	IDrag("Dragons", TaskSet.DURADEL, new Position(3147, 9577, 0),
			"Iron Dragons, is located in the Taverley Dungeon and its recommended"
					+ "<br>To bring your Anti-fireshield. The dragon is weak to ranged attacks."
					+ "<br>It drops the Dragonic Visage, and many other useable items." + "<br>"
					+ "<br>Note : Higher level Dragons, have a higher rate at drops.",
			1, 20, 40, "Iron dragon"),

	MDragon("Mithril dragons", TaskSet.DURADEL, new Position(1761, 5340, 1),
			"Mithril Dragons, is located in the Taverley Dungeon and its recommended"
					+ "<br>To bring your Anti-fireshield. The dragon is weak to ranged attacks."
					+ "<br>It drops the Dragonic Visage, and many other useable items." + "<br>"
					+ "<br>Note : Higher level Dragons, have a higher rate at drops.",
			1, 20, 40, "Mithril dragon"),

	MJadinkos("Mutated jadinkos", TaskSet.DURADEL, new Position(3065, 9238, 0),
			"Mutated Jadinkos, is located in the Jadinko Lair."
					+ "<br>They drop many different items, and are high lvled.",
			80, 20, 40, "Mutated jadinko baby", "Mutated jadinko guard", "Mutated jadinko male"),

	Nech("Nechryaels", TaskSet.DURADEL, new Position(3440, 3563, 2),
			"Nechryaels, is located in the Slayer tower on the third floor."
					+ "<br>They require a Slayer level of 80 and they drop some cash" + "<br>On every kill.",
			80, 20, 40, "Nechryael"),

	SWyvern("Skeletal Wyverns", TaskSet.DURADEL, new Position(3026, 9543, 0),
			"Skeletal Wyvern can be found in the Asgarnian Ice Dungeon."
					+ "<br>They require a Slayer level of 72 to kill, and have decent drops.",
			72, 20, 40, "Skeletal Wyvern"),

	SMage("Spiritual Mages", TaskSet.DURADEL, new Position(2920, 5354, 0),
			"Spiritual Mages can be located in the Godwars Dungeon"
					+ "<br>At the Saradomin side. They drop decent amount of runes.",
			83, 20, 40, "Spiritual mage"),

	SDragon("Steel Dragons", TaskSet.DURADEL, new Position(2725, 9466, 0),
			"Steel Dragons, is located in the Taverley Dungeon and its recommended"
					+ "<br>To bring your Anti-fireshield. The dragon is weak to ranged attacks."
					+ "<br>It drops the Dragonic Visage, and many other useable items." + "<br>"
					+ "<br>Note : Higher level Dragons, have a higher rate at drops.",
			1, 20, 40, "Steel dragon"),

	WFiends("Waterfiends", TaskSet.DURADEL, new Position(1738, 5349, 0),
			"Waterfiends, is located in the Ancient caveren. They have a ok"
					+ "<br>Drop table. They use Ranged attacks so be prepared.",
			1, 20, 40, "Waterfiend"),

	OGRES2("Ogres", TaskSet.DURADEL, new Position(2505, 3121, 0),
			"Ogres is located South of the Gnome Maze, they drop"
					+ "<br>Many random items, that is worth picking up and selling.",
			1, 25, 30, "Ogre", "Zogre", "Skogre", "Jogre"),

	ANKOU2("Ankou", TaskSet.DURADEL, new Position(2319, 5227, 0),
			"Ankous, is located in the Stronghold of Secutiry dungeon and"
					+ "<br>Drops decent items, that can be sold to the general store" + "<br>For some decent money.",
			1, 35, 45, "Ankou"),

	COCKROACHES2("Cockroaches", TaskSet.DURADEL, new Position(3151, 4277, 3),
			"Cockroaches, can be found in the Stronghold Of Player Safety"
					+ "<br>Dungeon. They are no threath, and you should not be aware of anything.",
			1, 20, 30, "Cockroach drone", "Cockroach worker", "Cockroach soldier", "Cockroach_soldier",
			"Cockroach_worker", "Cockroach_drone"),
	/*
	 * Kuradel
	 */
	ABBYSPEC3("Aberrant spectres", TaskSet.KURADEL, new Position(3439, 3550, 1),
			"Aberrant Spectre can be located in the Slayer Tower on the first floor."
					+ "<br>The Aberrant spectre drops a good amount of runes.",
			60, 20, 40, "Aberrant spectre"),

	ABYDEMON2("Abyssal demons", TaskSet.KURADEL, new Position(3410, 3574, 2),
			"Abyssal Demons, can be found in the Slayer tower at the last floor."
					+ "<br>They drop the Abyssal whip in all colors, and various of other viable items.",
			85, 20, 40, "Ayssal demon"),

	BDragon2("Black dragons", TaskSet.KURADEL, new Position(2835, 9818, 0),
			"Black Dragons, is located in the Taverley Dungeon and its recommended"
					+ "<br>To bring your Anti-fireshield. The dragon is weak to ranged attacks."
					+ "<br>It drops the Dragonic Visage, and many other useable items.",
			1, 20, 40, "Black dragon"),

	Blue_Dragon2("Blue Dragons", TaskSet.KURADEL, new Position(2906, 9810, 0),
			"Blue Dragons, is located in the Taverley Dungeon and its recommended"
					+ "<br>To bring your Anti-fireshield. The dragon is weak to ranged attacks."
					+ "<br>It drops the Dragonic Visage, and many other useable items." + "<br>"
					+ "<br>Note : Higher level Dragons, have a higher rate at drops.",
			1, 20, 40, "Blue dragon"),

	BVElds2("Bloodvelds", TaskSet.KURADEL, new Position(3423, 3565, 1),
			"Bloodvelds can be located in the Slayer Tower on the Third floor."
					+ "<br>Blood runes, is a very common drop from these creatures." + "<br>"
					+ "<br>Note : This creature also drops the Crystal key.",
			50, 20, 40, "Bloodveld"),

	Dags3("Dagganoths", TaskSet.KURADEL, new Position(1805, 4405, 3),
			"The Dagannoths, can be found in their dungeon. It can be accessed" + "<br>Through the boss teleports"
					+ "<br>" + "<br>Note : Dagannoths, are a dangerous creature. You should"
					+ "<br>Be wearing some decent gear.",
			1, 20, 40, "Dagannoth", "Dagannoth Mother", "Dagannoth guardian", "Dagannoth spawn", "Dagannoth Prime",
			"Dagannoth Supreme", "Dagannoth Rex"), DBeast2("Dark beasts", TaskSet.KURADEL, new Position(3419, 3550, 2),
					"Dark Beasts, is located in the Slayer tower at the last floor."
							+ "<br>And drops the Dark bow in different colors.",
					90, 20, 40, "Dark beast"),

	DStryke2("Desert strykewyrms", TaskSet.KURADEL, new Position(3369, 3159, 0),
			"Strykewyrms, is a mini version of the boss Wildywyrm."
					+ "<br>Killing them requires level 93 Slayer, and they have" + "<br>A very good drop table.",
			77, 20, 40, "Desert strykewyrms"),

	DDevil2("Dust devil", TaskSet.KURADEL, new Position(3215, 9357, 0),
			"Dust Devils is located, in the Smoke Dungeon that you can accessed"
					+ "<br>in the Slayer Dungeon teleports.",
			65, 20, 40, "Dust devil"),

	FGiant2("Fire giants", TaskSet.KURADEL, new Position(3234, 9368, 0),
			"Fire Giants, is located in the Smoke Dungeon. The Fire giants"
					+ "<br>Drops many Rune items frequently, and its recommended to"
					+ "<br>Pick up the items, and sell to the General store.",
			1, 20, 40, "Fire giant"),

	GCreatures2("Ganodermic beasts", TaskSet.KURADEL, new Position(4697, 5474, 0),
			"Ganodermic beasts can be found in the Polypore dungeon"
					+ "<br>They deal magic damage, and have decent drops.",
			95, 20, 40, "Ganodermic beast"),

	garg3("Gargoyles", TaskSet.KURADEL, new Position(3439, 3547, 2),
			"Gargoyles are Slayer monsters located in the Slayer Tower's top floor"
					+ "<br>And basement requiring 75 Slayer in order to be harmed."
					+ "<br>Gargoyles have decent drops that can be sold to make money of." + "<br>"
					+ "<br>Note : You must bring a rock hammer to kill these.",
			75, 20, 40, "Gargoyle"),

	GDEmons2("Greater demons", TaskSet.KURADEL, new Position(2642, 9504, 2),
			"Greater Demons, can be found in the Brimhaven Dungeon"
					+ "<br>They attack with magic, and drop many kind of runes.",
			1, 20, 40, "Greater demon"),

	Grif4("Grifalopines", TaskSet.KURADEL, new Position(3147, 9577, 0), "Describtion/location", 88, 20, 40,
			"Grifalopine"), Grif3("Grifaloroo", TaskSet.KURADEL, new Position(4650, 5392, 3),
					"Grifaloroos is located in the Polypore Dungeon at the end."
							+ "<br>It drops decent items, and it would be wise to pick them up.",
					82, 20, 40, "Grifaloroo"),

	Grots4("Grotworms", TaskSet.KURADEL, new Position(1158, 6499, 0),
			"Grotworms, are located at Queen black dragons lair they" + "<br>are near the entrance to fighting QBD."
					+ "<br>" + "<br>Note : This creature drops valueable items.",
			1, 20, 40, "Young grotworm", "Grotworm", "Mature grotworm"),

	HH2("Hellhounds", TaskSet.KURADEL, new Position(2856, 9847, 0),
			"Hellhounds are located in the Taverley Dungeon, and can"
					+ "<br>Also be found in the Godwars dungeon. They drop many"
					+ "<br>Types of armor including some Dragon pieces.",
			1, 20, 40, "Hellhound"),

	IStryke3("Ice strykewyrm", TaskSet.KURADEL, new Position(3413, 5673, 0),
			"Strykewyrms, is a mini version of the boss Wildywyrm."
					+ "<br>Killing them requires level 93 Slayer, and they have" + "<br>A very good drop table.",
			93, 20, 40, "Ice strykewyrm"),

	IDrag2("Iron dragons", TaskSet.KURADEL, new Position(3147, 9577, 0),
			"Iron Dragons, is located in the Taverley Dungeon and its recommended"
					+ "<br>To bring your Anti-fireshield. The dragon is weak to ranged attacks."
					+ "<br>It drops the Dragonic Visage, and many other useable items." + "<br>"
					+ "<br>Note : Higher level Dragons, have a higher rate at drops.",
			1, 20, 40, "Iron dragon"),

	LRC("Living rock creatures", TaskSet.KURADEL, new Position(3147, 9577, 0), "Describtion/location", 1, 20, 40,
			"Living rock protector", "Living rock striker", "Living rock patriarch"),

	MDragon2("Mithril dragons", TaskSet.KURADEL, new Position(1761, 5340, 1),
			"Iron Dragons, is located in the Taverley Dungeon and its recommended"
					+ "<br>To bring your Anti-fireshield. The dragon is weak to ranged attacks."
					+ "<br>It drops the Dragonic Visage, and many other useable items." + "<br>"
					+ "<br>Note : Higher level Dragons, have a higher rate at drops.",
			1, 20, 40, "Mithril dragon"),

	MJadinkos2("Mutated jadinkos", TaskSet.KURADEL, new Position(3065, 9238, 0),
			"Mutated Jadinkos, is located in the Jadinko Lair."
					+ "<br>They drop many different items, and are high lvled.",
			80, 20, 40, "Mutated jadinko baby", "Mutated jadinko guard", "Mutated jadinko male"),

	Nech2("Nechryaels", TaskSet.KURADEL, new Position(3440, 3563, 2),
			"Nechryaels, is located in the Slayer tower on the third floor."
					+ "<br>They require a Slayer level of 80 and they drop some cash" + "<br>On every kill.",
			80, 20, 40, "Nechryael"),

	SWyvern2("Skeletal Wyverns", TaskSet.KURADEL, new Position(3026, 9543, 0),
			"Skeletal Wyvern can be found in the Asgarnian Ice Dungeon."
					+ "<br>They require a Slayer level of 72 to kill, and have decent drops.",
			72, 20, 40, "Skeletal Wyvern"),

	SMage2("Spiritual Mages", TaskSet.KURADEL, new Position(3147, 9577, 0), "Describtion/location", 83, 20, 40,
			"Spiritual mage"), SDragon2("Steel Dragons", TaskSet.KURADEL, new Position(2725, 9466, 0),
					"Steel Dragons, is located in the Taverley Dungeon and its recommended"
							+ "<br>To bring your Anti-fireshield. The dragon is weak to ranged attacks."
							+ "<br>It drops the Dragonic Visage, and many other useable items." + "<br>"
							+ "<br>Note : Higher level Dragons, have a higher rate at drops.",
					1, 20, 40, "Steel dragon"),

	OGRES3("Ogres", TaskSet.KURADEL, new Position(2505, 3121, 0),
			"Ogres is located South of the Gnome Maze, they drop"
					+ "<br>Many random items, that is worth picking up and selling.",
			1, 30, 40, "Ogre", "Zogre", "Skogre", "Jogre"),

	ANKOU3("Ankou", TaskSet.KURADEL, new Position(2319, 5227, 0),
			"Ankous, is located in the Stronghold of Secutiry dungeon and"
					+ "<br>Drops decent items, that can be sold to the general store" + "<br>For some decent money",
			1, 40, 45, "Ankou"),

	WFiends2("Waterfiends", TaskSet.KURADEL, new Position(1738, 5349, 0),
			"Waterfiends, is located in the Ancient caveren. They have a ok"
					+ "<br>Drop table. They use Ranged attacks so be prepared.",
			1, 20, 40, "Waterfiend"), COCKROACHES3("Cockroaches", TaskSet.KURADEL, new Position(3151, 4277, 3),
					"Cockroaches, can be found in the Stronghold Of Player Safety"
							+ "<br>Dungeon. They do not threath, and you should not be aware of anything.",
					1, 30, 40, "Cockroach drone", "Cockroach worker", "Cockroach soldier", "Cockroach_soldier",
					"Cockroach_worker", "Cockroach_drone"),

	BORK("Bork", TaskSet.KURADEL, new Position(3147, 9577, 0),
			"Bork can be found through the Navigation menu under"
					+ "<br>Boss teleports, Bork is not specially strong but" + "<br>He can do some harm, so watch out.",
			95, 1, 1, "Bork", "bork"),

	KBD("King Black Dragon", TaskSet.KURADEL, new Position(3067, 10253, 0),
			"King Black Dragon, is the Boss dragon and its recommended"
					+ "<br>That you not only bring your Anti-fire shield, but also a"
					+ "<br>Antifire potion. King Black Dragon is a VERY dangerous creature." + "<br>"
					+ "<br>Note : This drops the best drops of all Dragons!",
			95, 1, 1, "King black dragon", "King_black_dragon"),

	CORPBEAST("Corporeal Beast", TaskSet.KURADEL, new Position(2970, 4348, 2),
			"Corporeal beast, is a monster with a very high Combat level."
					+ "<br>The beast, can be found in the Boss teleports menu." + "<br>"
					+ "<br>DANGER : THIS IS A ADVANCED BOSS! WATCH OUT!",
			95, 1, 1, "Corporeal Beast", "Corporeal_Beast", "Corporeal_beast", "Corporeal beast"),

	QUEEN("Kalphite Queen", TaskSet.KURADEL, new Position(3147, 9577, 0),
			"The Kalphite Queen, can be found in the Al-Kharid Desert Dungeon."
					+ "<br>You can teleport to the entrance in the Slayer Dungeon Teleports." + "<br>"
					+ "<br>Note : The Queen is using prayer protects. Veracs armor is usefull.",
			95, 1, 1, "Kalphite Queen", "Kalphite_Queen", "Kalphite queen", "Kalphite_queen");

	SlayerTasks(String simpleName, TaskSet type, Position monsterDestination, String taskLocationInfomation, int level,
                int min, int max, String... monsters) {
		this.simpleName = simpleName;
		this.type = type;
		this.monsterDestination = monsterDestination;
		this.taskLocationInfomation = taskLocationInfomation;
		this.slayable = monsters;
		this.level = level;
		this.min = min;
		this.max = max;
	}

	private Position monsterDestination;

	private String taskLocationInfomation;
	/**
	 * A simple name for the task
	 */
	public String simpleName;

	/**
	 * The task set
	 */
	public TaskSet type;
	/**
	 * The monsters that will effect this task
	 */
	public String[] slayable;
	/**
	 * The minimum amount of monsters the player may be assigned to kill
	 */
	public int min;
	/**
	 * The maximum amount of monsters the player may be assigned to kill
	 */
	public int max;

	/*
	 * Slayer level for monsters
	 */
	public int level;

	public int getLevel() {
		return level;
	}

	public Position getMonsterLocation() {
		return monsterDestination;
	}

	public String getTaskLocationInformation() {
		return taskLocationInfomation;
	}

}
