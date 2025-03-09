package com.rs.game.world.entity.player.content;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.utility.Utils;

import lombok.Getter;

/**
 * Handles boss slayer content.
 * @author ReverendDread
 * Jul 22, 2018
 * 
 * -NOTES-
 * Death's id - 14386
 * 
 */
public class BossSlayer implements Serializable {
	
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -8035331642599855251L;
	
	/**
	 * The player.
	 */
	private transient Player player;
	
	/**
	 * The task.
	 */
	@Getter private Tasks task;
	
	/**
	 * Kills remaining on task.
	 */
	@Getter private int killsRemaining;
	
	/**
	 * Amount of reaper points.
	 */
	@Getter private int reaperPoints;
	
	/**
	 * Time until another ask is obtainable.
	 */
	private long task_cooldown;
	
	/**
	 * How many tasks have been skipped so far.
	 */
	private int skipped_tasks;
	
	/**
	 * Contains static task data for boss creatures.
	 * @author ReverendDread
	 * Jul 22, 2018
	 */
	public enum Tasks {
		
		KING_BLACK_DRAGON(50, 15, 40, 5),
		CORPOREAL_BEAST(8133, 1, 10, 20),
		NEX(13450, 1, 5, 30),
		GENERAL_GRAARDOR(6260, 10, 25, 10),
		COMMANDER_ZILYANA(6247, 10, 25, 10),
		KRIL_TSUTSAROTH(6203, 10, 25, 10),
		KREE_ARRA(6222, 10, 25, 10),
        KALPHITE_QUEEN(new int[] {1158, 1160}, 5, 10,  5),
		DAGANNOTH_REX(2883, 20, 40, 5),
		DAGANNOTH_PRIME(2882, 20, 40,  5),
		DAGANNOTH_SUPREME(2881, 20, 40, 5),
		SUNFREET(15222, 5, 10, 15),
		AVATAR_OF_CREATION(8597, 5, 10, 15),
		WILDY_WYRM(3334, 5, 10, 15),
		ACIDIC_STRYKEWYRM(new int[] {16023, 16024, 16025}, 5, 10, 20),
		KRAKEN(16027, 10, 20, 15),
		CERBERUS(25862, 10, 20, 15),
		ABYSSAL_SIRE(25887, 10, 20, 15),
		ALCHEMICAL_HYDRA(28622, 10, 20, 15),
		THE_ALCHEMIST(new int[] { 16071, 16080, 16081 }, 5, 10, 30),
		GLACOR(14301, 5, 10, 5),
		QUEEN_BLACK_DRAGON(15454, 5, 10, 10);
	
		@Getter private int ids[];
		@Getter private int min, max, reaperPoints;
		
		private Tasks(int id[], int min, int max, int reaperPoints) {
			this.ids = id;
			this.min = min;
			this.max = max;
			this.reaperPoints = reaperPoints;
		}
		
		private Tasks(int id, int min, int max, int reaperPoints) {
			this(new int[] {id}, min, max, reaperPoints);
		}
		
		/**
		 * Checks if the specified id is a monster of a boss.
		 * @param id
		 * 			the id to check for.
		 * @return true
		 * 				if the id is a monster.
		 */
		public boolean isMonster(int id) {
			for (int mob : ids) {
				if (mob == id)
					return true;
			}
			return false;
		}
		
		/**
		 * Checks if the specified id is a boss in this enum.
		 * @param id
		 * 			the id to check for.
		 * @return true
		 * 				if the id is a boss, false otherwise.
		 */
		public static boolean isBoss(int id) {
			for (Tasks task : VALUES) {
				if (task.isMonster(id))
					return true;
			}
			return false;
		}
		
		/**
		 * Gets the formatted enum name.
		 * @return
		 */
		public String getName() {
			return Utils.getFormattedEnumName(name());
		}
		
		/**
		 * Static tasks values, so new object isn't created every reference.
		 */
		public static Tasks[] VALUES = Tasks.values();
	
	}
	
	/**
	 * Assigns the player a new reaper task.
	 */
	public void assignTask() {
		int random = Utils.getRandom(Tasks.VALUES.length - 1);
		task = Tasks.VALUES[random];
		int randomAmount = Utils.random(task.getMin(), task.getMax());
		killsRemaining = randomAmount;
		if (player.getEquipment().wearingSkillCape(Skills.SLAYER)) {
			int extra = (int) (randomAmount * 0.10D);
			killsRemaining += extra;
			player.sendMessage("Your Slayer cape adds another " + extra + " kills to your task.");
		}
	}

	public boolean isValidTask(String name) {
		if (getTask() == null) {
			return false;
		}
		List<BossSlayer.Tasks> tasks = new LinkedList<BossSlayer.Tasks>(Arrays.asList());
		tasks.add(getTask());
		for (BossSlayer.Tasks currentTask : tasks) {
			if (name.toLowerCase().contains(currentTask.toString().replace("_", " ").toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Starts the skip task dialogue.
	 */
	public void skipTask() {
		player.getDialogueManager().startDialogue(new Dialogue() {

			@Override
			public void start() {
				if (skipped_tasks >= 4) {
					sendNPCDialogue(14386, NORMAL, "I will not allow you to skip another task until tomarrow.");
					stage = 3;
					return;
				}
				sendNPCDialogue(14386, NORMAL, "Are you sure you would like to skip your current task? You will forfit " + (skipped_tasks * 25 + 25) + "% of your rewards.");
				stage = 1;
			}

			@Override
			public void run(int interfaceId, int componentId) {
				
				if (stage == 1) {
					sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Yes, I'm sure.", "No thanks.");
					stage = 2;
				} else if (stage == 2) {
					if (componentId == OPTION_1) {
						assignTask();
						skipped_tasks++;
						if (skipped_tasks >= 1) {
							task_cooldown = System.currentTimeMillis();
						}
						sendNPCDialogue(14386, NORMAL, "Your new assignment is to bring me the souls of " + killsRemaining + " " + task.getName() + ".");
						stage = 3;
					} else if (componentId == OPTION_2) {
						end();
					}
				} else if (stage == 3) {
					end();
				}		
			}

			@Override
			public void finish() {
				
			}
	
		});
	}
	
	/**
	 * Returns a formatted task message.
	 * @return
	 */
	public String getTaskMessage() {
		return "You still need to bring me the souls of x" + getKillsRemaining() + " " + task.getName() + ".";
	}
	
	/**
	 * Gets formatted task information for a task message.
	 * @return
	 */
	public String getTaskInformation() {
		return (getTask() == null ? "None" : getKillsRemaining() + " " + getTask().getName());
	}
	
	/**
	 * Reduces the kills left on the current task.
	 * @param npc
	 * 			the npc that was killed.
	 */
	public void deincrementKills(NPC npc) {
		if (getTask() == null || !task.isMonster(npc.getId()))
			return;
		if (getKillsRemaining() > 0) {
			killsRemaining--;	
			World.sendProjectile(npc, player, 2263, 36, 36, 15, 0, 0, 0);
			if (getKillsRemaining() % 5 == 0 && getKillsRemaining() != 0)
				player.sendMessage("Well done, you only have " + getKillsRemaining() + " " + task.getName() + " left.");
			player.getSkills().addXp(Skills.SLAYER, npc.getMaxHitpoints() / 7);
		}
		if (getKillsRemaining() <= 0 && task != null) {
			int reward = (int) (task.getReaperPoints() / (skipped_tasks > 0 ? (skipped_tasks * 1.25) : 1));
			reaperPoints += reward;
			player.sendMessage("Well done, you've completed your task. You've been awarded " + reward + " Reaper points.");
			task = null;
		}	
	}
	
	/**
	 * Removes the desired amount of reaper points.
	 * @param amount
	 * 			the amount of points to remove.
	 */
	public void removeReaperPoints(int amount) {
		reaperPoints -= amount;
	}
	
	/**
	 * Sets the player.
	 * @param player
	 * 			the player to set.
	 */
	public void setPlayer(Player player) {
		this.player = player;
		if (task_cooldown + TimeUnit.DAYS.toMillis(1) > System.currentTimeMillis()) {
			skipped_tasks = 0;
		}
	}
	
}
