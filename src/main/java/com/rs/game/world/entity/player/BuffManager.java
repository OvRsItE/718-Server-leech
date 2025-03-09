/**
 * 
 */
package com.rs.game.world.entity.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Graphics;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Handles buffs & buff timers.
 * @author ReverendDread
 * Jan 11, 2019
 */
public class BuffManager implements Serializable {

	/** Serial UID */
	private static final long serialVersionUID = 1695895154663614500L;

	private static final int ITEM = 0, SPRITE = 1;
	
	/** Map of active buffs */
	@Getter private List<Buff> buffs = new CopyOnWriteArrayList<Buff>();
	
	/** The player */
	@Setter private transient Player player;
	
	/**
	 * Applys a buff to a player if allowed.
	 * @param buff
	 * 			the buff to apply, this will replace the old buff with the new one.
	 * @return
	 * 			true if the buff was applied, false otherwise.
	 */
	public boolean applyBuff(Buff buff) {
		BuffType type = buff.type;
		if (!type.apply(buff, player))
			return false;
		Buff currentBuff = getBuffForType(type);
		if (currentBuff != null)
			buffs.set(buffs.indexOf(currentBuff), buff);
		else
			buffs.add(buff);
		return true;
	}
	
	/**
	 * Removes a buff and calls the @see {@link BuffType#onRemove(Player)}.
	 * @param type
	 * 			the buff type to remove.
	 * @return
	 * 			true if the buff was removed, false otherwise.
	 */
	public boolean removeBuff(BuffType type) {
		Buff buff = getBuffForType(type);
		if (buff == null)
			return false;
		if (buff.getDuration() > 0)
			buff.setDuration(0);
		type.onRemove(buff, player);
		boolean removedBuff = buffs.remove(buff);
		return removedBuff;
	}
	
	/**
	 * Processes applied buffs each game tick.
	 */
	public void processBuffs() {
		if (player.isFinished() || buffs.isEmpty()) 
			return;
		boolean dead = player.isDead();
		for (Buff buff : buffs) { //Loop through each active buff.
			if (buff.duration != -1) { //Used for unlimited buffs.
				BuffType type = buff.type;
				type.process(buff, player); //Process the buff
				buff.duration--; //Deincrement the buff duration.
				if (buff.duration == 0 || (dead && buff.removeOnDeath)) {
					removeBuff(buff.type); //Buff expires, remove it.
				}
				if (type.iconType != -1)
					player.getPackets().sendRunScript(-7, type.iconType, type.icon, buff.duration * 600);
			}
		}
	}
	
	/**
	 * Checks if the player has the specified {@code BuffType}.
	 * @param type
	 * 			the type to check for.
	 * @return
	 * 			true if the player has the buff type, false otherwise.
	 */
	public boolean hasBuff(BuffType type) {
		Buff buff = getBuffForType(type);
		if (buff == null)
			return false;
		return buffs.contains(buff);
	}
	
	/**
	 * Gets the first buff with the specified {@code BuffType}.
	 * @param type
	 * 			the type.
	 * @return
	 * 			the buff with the type, null otherwise.
	 */
	public Buff getBuffForType(BuffType type) {
		for (Buff buff : buffs) {
			if (buff.type == type)
				return buff;
		}
		return null;
	}
	
	/**
	 * Gets the time remaining for the desired {@code BuffType}.
	 * @param type
	 * 			the buff type.
	 * @return
	 * 			time remaining in ticks.
	 */
	public int getTimeRemaining(BuffType type) {
		Buff buff = getBuffForType(type);
		if (buff == null)
			return 0;
		return buff.getDuration();
	}
	
	/**
	 * Removes all active buffs.
	 */
	public void reset() {
		for (Buff buff : buffs) {
			buffs.remove(buff);
		}
	}
	
	/**
	 * Contains buffs & their methods.
	 * @author ReverendDread
	 * Jan 11, 2019
	 */
	@RequiredArgsConstructor
	public enum BuffType {
		
		ANTI_FIRE(2452, ITEM) {
			
			@Override
			public boolean apply(Buff buff, Player player) {
				player.getPackets().sendGameMessage("You are now immune to dragonfire.");
				return true;
			}
			
			@Override
			public void process(Buff buff, Player player) {
				if (buff.getDuration() == 100)
				player.getPackets().sendGameMessage("<col=480000>Your antifire potion is about to run out...</col>", false);
			}
			
			@Override
			public void onRemove(Buff buff, Player player) {
				player.getPackets().sendGameMessage("<col=480000>Your antifire potion has ran out...</col>", false);
			}
			
		},	
		SUPER_ANTI_FIRE(15304, ITEM) {
			
			@Override
			public boolean apply(Buff buff, Player player) {
				player.getPackets().sendGameMessage("You are now completely immune to dragonfire.");
				return true;
			}
			
			@Override
			public void process(Buff buff, Player player) {
				if (buff.getDuration() == 100)
				player.getPackets().sendGameMessage("<col=480000>Your super antifire potion is about to run out...</col>", false);
			}
			
			@Override
			public void onRemove(Buff buff, Player player) {
				player.getPackets().sendGameMessage("<col=480000>Your super antifire potion has ran out...</col>", false);
			}
			
		},		
		PRAYER_RENEWAL(21630, ITEM) {
			
			@Override
			public void process(Buff buff, Player player) {
				if (buff.getDuration() == 50) {
					player.getPackets().sendGameMessage("<col=8B0000>Your prayer renewal will wear off in 30 seconds.</col>");
				}
				if (!player.getPrayer().hasFullPrayerpoints()) {
					player.getPrayer().restorePrayer(1);
					if ((buff.getDuration() - 1) % 25 == 0) {
						player.setNextGraphics(new Graphics(1295));
					}
				}
			}
			
			@Override
			public void onRemove(Buff buff, Player player) {
				player.getPackets().sendGameMessage("<col=8B0000>Your prayer renewal has ended.</col>");			
			}
			
		},		
		BONFIRE(0, -1) {
			
			@Override
			public void process(Buff buff, Player player) {
				if (buff.getDuration() == 500) {
					player.getPackets().sendGameMessage("<col=8B0000>The health boost you received from stoking a bonfire will run out in 5 minutes.");
				}
			}
			
			@Override
			public void onRemove(Buff buff, Player player) {
				player.getPackets().sendGameMessage("<col=8B0000>The health boost you received from stoking a bonfire has run out.");
				player.getEquipment().refreshConfigs(false);
			}
			
		}, 
		IMBUED_HEART(29698, ITEM) {
			
			@Override
			public boolean apply(Buff buff, Player player) {
				if (player.getBuffManager().hasBuff(BuffType.IMBUED_HEART)) {					
					int time = player.getBuffManager().getTimeRemaining(BuffType.IMBUED_HEART);
					player.getPackets().sendGameMessage("The heart is still drained of its power. "
							+ "Judging by how it feels, it will be ready in around " + TimeUnit.MILLISECONDS.toMinutes((time * 600)) + " minutes.");
					return false;
					
				}
				player.getSkills().set(Skills.MAGIC, player.getSkills().getLevel(Skills.MAGIC) + (int) (1 + Math.ceil((player.getSkills().getLevelForXp(Skills.MAGIC) * 0.10))));
				player.setNextGraphics(Graphics.createOSRS(1316));
				return true;
			}
			
		},
		UNDERWATERPOTION(28788, ITEM) {

			@Override
			public boolean apply(Buff buff, Player player) {
				if (player.getBuffManager().hasBuff(BuffType.UNDERWATERPOTION)) {
					int time = player.getBuffManager().getTimeRemaining(BuffType.UNDERWATERPOTION);
					player.getPackets().sendGameMessage("Your underwater potion is still active. "
							+ "it still has " + TimeUnit.MILLISECONDS.toMinutes((time * 600)) + " minutes left.");
					return false;

				}
				return true;
			}
			@Override
			public void onRemove(Buff buff, Player player) {
				player.getPackets().sendGameMessage("<col=8B0000>Your underwater potion has run out.");
			}

		},
		AGGRESSION_POTION(28660, ITEM) {

			@Override
			public boolean apply(Buff buff, Player player) {
				if (player.getBuffManager().hasBuff(BuffType.AGGRESSION_POTION)) {
					int time = player.getBuffManager().getTimeRemaining(BuffType.AGGRESSION_POTION);
					player.getPackets().sendGameMessage("Your aggression potion is still active. "
							+ "it still has " + TimeUnit.MILLISECONDS.toMinutes((time * 600)) + " minutes left.");
					return false;

				}
				return true;
			}
			@Override
			public void onRemove(Buff buff, Player player) {
				player.getPackets().sendGameMessage("<col=8B0000>Your aggression potion has run out.");
			}

		},
		XPPOTION(28780, ITEM) {

			@Override
			public boolean apply(Buff buff, Player player) {
				if (player.getBuffManager().hasBuff(BuffType.XPPOTION)) {
					int time = player.getBuffManager().getTimeRemaining(BuffType.XPPOTION);
					player.getPackets().sendGameMessage("Your xp potion is still active. "
							+ "it still has " + TimeUnit.MILLISECONDS.toMinutes((time * 600)) + " minutes left.");
					return false;

				}
				return true;
			}
			@Override
			public void onRemove(Buff buff, Player player) {
				player.getPackets().sendGameMessage("<col=8B0000>Your xp potion has run out.");
			}


		},
		XP_BOOST(23716, ITEM) {

			@Override
			public boolean apply(Buff buff, Player player) {
				if (player.getBuffManager().hasBuff(BuffType.XP_BOOST)) {
					int time = player.getBuffManager().getTimeRemaining(BuffType.XP_BOOST);
					player.getPackets().sendGameMessage("Your xp boost is still active. "
							+ "it still has " + TimeUnit.MILLISECONDS.toMinutes((time * 600)) + " minutes left.");
					return false;

				}
				return true;
			}
			@Override
			public void onRemove(Buff buff, Player player) {
				player.getPackets().sendGameMessage("<col=8B0000>Your xp boost has run out.");
			}


		},
		DARK_AURA(8373, ITEM) {

			@Override
			public boolean apply(Buff buff, Player player) {
				if (player.getBuffManager().hasBuff(BuffType.DARK_AURA)) {
					int time = player.getBuffManager().getTimeRemaining(BuffType.DARK_AURA);
					player.getPackets().sendGameMessage("Your dark aura is still active. "
							+ "it still has " + TimeUnit.MILLISECONDS.toMinutes((time * 240)) + " minutes left.");
					return false;

				}
				return true;
			}
			@Override
			public void onRemove(Buff buff, Player player) {
				player.getPackets().sendGameMessage("<col=8B0000>Your dark aura has run out.");
			}


		},
		CRYSTAL_SHIELD(29743, ITEM) {

			@Override
			public boolean apply(Buff buff, Player player) {
				if (player.getBuffManager().hasBuff(BuffType.CRYSTAL_SHIELD)) {
					int time = player.getBuffManager().getTimeRemaining(BuffType.CRYSTAL_SHIELD);
					player.getPackets().sendGameMessage("Your crystal shield effect is still active. "
							+ "it still has " + TimeUnit.MILLISECONDS.toMinutes((time * 30)) + " minutes left.");
					return false;

				}
				return true;
			}
			@Override
			public void onRemove(Buff buff, Player player) {
				player.getPackets().sendGameMessage("<col=2697D3>Your crystal shield effect has faded.");
			}


		},
		DROPRATE_POTION(28784, ITEM) {

			@Override
			public boolean apply(Buff buff, Player player) {
				if (player.getBuffManager().hasBuff(BuffType.DROPRATE_POTION)) {
					int time = player.getBuffManager().getTimeRemaining(BuffType.DROPRATE_POTION);
					player.getPackets().sendGameMessage("Your droprate potion is still active. "
							+ "it still has " + TimeUnit.MILLISECONDS.toMinutes((time * 600)) + " minutes left.");
					return false;

				}
				return true;
			}
			@Override
			public void onRemove(Buff buff, Player player) {
				player.getPackets().sendGameMessage("<col=8B0000>Your Droprate potion has run out.");
			}


		},
		DROPRATE_BUFF(2572, ITEM) {

			@Override
			public boolean apply(Buff buff, Player player) {
				if (player.getBuffManager().hasBuff(BuffType.DROPRATE_BUFF)) {
					int time = player.getBuffManager().getTimeRemaining(BuffType.DROPRATE_BUFF);
					player.getPackets().sendGameMessage("Your drop rate bonus  is still active. "
							+ "it still has " + TimeUnit.MILLISECONDS.toMinutes((time * 600)) + " minutes left.");
					return false;

				}
				return true;
			}
			@Override
			public void onRemove(Buff buff, Player player) {
				player.getPackets().sendGameMessage("<col=8B0000>Your bonus droprate has run out.");

			}


		},
		GREED_POTION(28786, ITEM) {

			@Override
			public boolean apply(Buff buff, Player player) {
				if (player.getBuffManager().hasBuff(BuffType.GREED_POTION)) {
					int time = player.getBuffManager().getTimeRemaining(BuffType.GREED_POTION);
					player.getPackets().sendGameMessage("Your greed potion is still active. "
							+ "it still has " + TimeUnit.MILLISECONDS.toMinutes((time * 600)) + " minutes left.");
					return false;

				}
				return true;
			}
			@Override
			public void onRemove(Buff buff, Player player) {
				player.getPackets().sendGameMessage("<col=8B0000>Your Greed potion has run out.");
			}


		},
		HEALTH_BOOST_POTION(28782, ITEM) {

			@Override
			public boolean apply(Buff buff, Player player) {
				if (player.getBuffManager().hasBuff(BuffType.HEALTH_BOOST_POTION)) {
					int time = player.getBuffManager().getTimeRemaining(BuffType.HEALTH_BOOST_POTION);
					player.getPackets().sendGameMessage("Your health boost potion is still active. "
							+ "it still has " + TimeUnit.MILLISECONDS.toMinutes((time * 600)) + " minutes left.");
					return false;

				}
				return true;
			}
			@Override
			public void onRemove(Buff buff, Player player) {
				player.getPackets().sendGameMessage("<col=8B0000>Your health boost potion has run out.");
			}


		},
		OVERLOADED_HEART(28882, ITEM) {

			@Override
			public boolean apply(Buff buff, Player player) {
				if (player.getBuffManager().hasBuff(BuffType.OVERLOADED_HEART)) {
					int time = player.getBuffManager().getTimeRemaining(BuffType.OVERLOADED_HEART);
					player.getPackets().sendGameMessage("The heart is still drained of its power. "
							+ "Judging by how it feels, it will be ready in around " + TimeUnit.MILLISECONDS.toMinutes((time * 500)) + " minutes.");
					return false;

				}
				player.setOverloadDelay(501);
				player.setNextGraphics(new Graphics(4019));
				return true;
			}

		};

		@Getter private final int icon;
		@Getter private final int iconType;
		
		/**
		 * Called when the buff is applied.
		 * @param buff
		 * 			the buff.
		 * @param player
		 * 			the player being effected.
		 * @return
		 */
		public boolean apply(Buff buff, Player player) {
			return true;
		}
		
		/** Called when the buff is removed */
		public void onRemove(Buff buff, Player player) {}
		
		/** Called each tick the buff is active */
		public void process(Buff buff, Player player) {}
		
		/**
		 * Called when the player dies when the buff is active.
		 * @param buff
		 * 			the buff.
		 * @param player
		 * 			the player.
		 * @param source
		 * 			the source of the players death.
		 */
		public void handleDeath(Buff buff, Player player, Entity source) {}
		
	}
	
	/**
	 * Represents a buff.
	 * @author ReverendDread
	 * Jan 11, 2019
	 */
	public static class Buff implements Serializable {
		
		/** Serial UID */
		private static final long serialVersionUID = 5695037721775075381L;
		
		/** The buff key word */
		@Getter private final BuffType type;
		
		/** Duration of the buff. */
		@Setter @Getter private int duration;
		
		private boolean removeOnDeath;
		
		/** Extra arguments */
		@Getter private Object[] args;
		
		/**
		 * Constructor
		 * @param key
		 * 			the buff key word
		 * @param duration
		 * 			the buff duration
		 * @param args
		 * 			the buff arguments
		 */
		public Buff(BuffType type, int duration, boolean removeOnDeath, Object... args) {
			this.type = type;
			this.duration = duration;
			this.args = args;
			this.removeOnDeath = removeOnDeath;
		}

		@Override
		public String toString() {
			return "BUFF - Type: " + type.toString() + ", Cycle: " + duration;
		}
		
	}
	
}
