package com.rs.game.world.entity.player.content;

import com.rs.game.world.entity.player.Player;

public class AchievementSystem {

	private Player player;

	public AchievementSystem(Player player) {
		setPlayer(player);
	}

	public static int EASY = 0;
	public static int MEDIUM = 1;
	public static int HARD = 2;
	static boolean started;
	static boolean finished;

	public enum Ahchievements {
		
		DEALDAMAGE(5, EASY, "Deal 500 damage to any monster.", false, false),
		EATFOOD(5, EASY, "Eat 5 types of any food.", false, false),
		DONATETOWELL(5, EASY, "Donate 1M to the XP Well.", false, false),
		THIEVINGSTALLS(5, EASY, "Thieve 30 Cowhides.", false, false),
		PLAYTIME(5, EASY, "Reach a Play-time of 4 hours.", false, false),
		BURYBONES(5, EASY, "Bury 50 of any bones.", false, false),
		FISH(5, EASY, "Fish 100 of any fish.", false, false),
		FLETCH(5, EASY, "Fletch 100 of any logs.", false, false),
		TRIVIA(5, EASY, "Answer the trivia 10 times.", false, false),
		COOK(5, EASY, "Cook 100 of any fishes.", false, false),
		SLAYERTASK(5, EASY, "Complete 3 Slayer task's.", false, false),
		AGILITYLAP(5, EASY, "Run a gnome agility lap.", false, false),
		CRAFTING(5, EASY, "Craft any gems 5 times..", false, false),
		FIREMAKING(5, EASY, "Light a fire 50 times.", false, false),
		MINING(5, EASY, "Mine 25 of any ores.", false, false),
		WCLOGS(5, EASY, "Cut 100 logs of any type.", false, false),
		CRYSTALCHEST(5, EASY, "Open the Crystal Chest 3 times.", false, false),
		VOTE4SERVER(5, EASY, "Vote and claim your reward.", false, false),
		ADDAFRIEND(5, EASY, "Add a person to your friendslist.", false, false),
		// MED
		TRIVIAMED(10, MEDIUM, "Answer the trivia 50 times.", false, false),
		DRAGONS(10, MEDIUM, "Kill 75 Black dragons.", false, false),
		HERBLORE(10, MEDIUM, "Clean 50 Ranarr.", false, false),
		PVMSTORE(10, MEDIUM, "Spend 3000 RealityX Points.", false, false),
		PLAYTIMEMED(10, MEDIUM, "Reach a play-time of 20 hours.", false, false),
		BONESONALTAR(10, MEDIUM, "Use 100 Dragon bones on the altar.", false, false),
		EXPWELL(10, MEDIUM, "Contribute 10M to the XP Well.", false, false),
		EATSHARKS(10, MEDIUM, "Eat 200 Sharks.", false, false),
		SLAYERTASKMED(10, MEDIUM, "Finish 20 Slayer tasks.", false, false),
		RESSOURCEBOX(10, MEDIUM, "Find the Resource box.", false, false),
		GLACORS(10, MEDIUM, "Kill 20 Glacors..", false, false),
		YEWLOGS(10, MEDIUM, "Cut 200 Yew logs.", false, false),
		FISHSHARKS(10, MEDIUM, "Fish 150 Sharks.", false, false),
		AGILITYBARB(10, MEDIUM, "Finish 10 laps of the Barbarian course.", false, false),
		FIREYEW(10, MEDIUM, "Light 200 Yew logs.", false, false),
		KALPHITEQUEEN(10, MEDIUM, "Kill the Kalphite Queen 5 times.", false, false),
		ICESTRYKE(10, MEDIUM, "Kill 20 Ice strykewyrms.", false, false),
		// Hard
		WILDYLAP(30, HARD, "Run 30 Wildy Agility Laps.", false, false),
		FIRECAPE(30, HARD, "Finish the Fight Caves minigame.", false, false),
		KRAKEN(30, HARD, "Kill Kraken 20 times.", false, false),
		SCIMSTALL(30, HARD, "Thieve 300 times from the Scimitar stall.", false, false),
		CATCHROCKS(30, HARD, "Catch 300 Rock tails.", false, false),
		CUTMAGICS(30, HARD, "Cut 300 Magic logs.", false, false),
		RUNITEORE(30, HARD, "Mine 250 Runite ores.", false, false),
		PLAYTIMEHARD(30, HARD, "Reach a play-time of 50 hours.", false, false),
		CRYSTALCHESTHARD(30, HARD, "Use the Crystal Chest 25 times.", false, false),
		VOTE(30, HARD, "Vote a total of 25 times.", false, false),
		ACIDICSTRYKE(30, HARD, "Beat the Acidic Strykewyrm.", false, false),
		SHARKSCOOK(30, HARD, "Cook 400 Sharks.", false, false),
		RFDMINIGAME(30, HARD, "Complete the Recipe for Diaster minigame.", false, false);

		String description;
		int reward;
		int difficulties;

		private Ahchievements(int Reward, int Difficulties, String Description, boolean Started, boolean Finished) {
			reward = Reward;
			difficulties = Difficulties;
			description = Description;
			started = Started;
			finished = Finished;
		}

		public String getDescription() {
			return description;
		}

		public int getDifficulty() {
			return difficulties;
		}

		public boolean isStarted() {

			return started = true;
		}

		public boolean isFinished() {
			return finished = true;
		}

	};

	public static void displayEasyAchievments(Player player) {
		player.getInterfaceManager().sendAchievments();
		//Achivements interface is 1362
	}

	public static void displayMedAchievments(Player player) {
		player.getInterfaceManager().sendAchievments();
		//Achivements interface is 1362
	}

	public static void displayHardAchievments(Player player) {
		player.getInterfaceManager().sendAchievments();
		//Achivements interface is 1362
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}


}


