package com.rs.game.world.entity.player.content.toxin;

import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.Maps;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

import lombok.Getter;
import lombok.Setter;

/**
 * Handles all forms of toxin.
 * @author ReverendDread
 * Jul 14, 2018
 */
public final class ToxinManager implements Serializable {

	/** The serial UID */
	private static final long serialVersionUID = -6324477860776313690L;
	
	/** The entity */
	@Getter private transient Entity entity;
	
	/** The toxin type currently effecting the entity */
	@Getter @Setter private ToxinType toxin;
	
	/** A list of immunities the player has from toxins */
	@Getter private ConcurrentHashMap<ToxinType, Integer> immunities = new ConcurrentHashMap<>();
	
	/** Current damage of the active toxin */
	private int damage;
	
	/** The cap damage */
	private int cap;
	
	/** Time in ticks until damage tick */
	private int frequencey;
	
	/** If the toxin increases in damage */
	private boolean increases;

	/**
	 * Applies a toxin to the entity.
	 * @param type
	 * 			the {@code ToxinType}.
	 */
	public void applyToxin(ToxinType type) {
		if (isImmune(type) || toxin != null)
			return;
		applyToxin(type, type.getStart(), type.getCap(), type.getFrequencey(), type.isIncrease());
	}
	
	/**
	 * Applies a toxin to the entity.
	 * @param type
	 * 			the {@code ToxinType}.
	 * @param damage
	 * 			the starting damage.
	 */
	public void applyToxin(ToxinType type, int damage) {
		if (isImmune(type) || toxin != null)
			return;
		applyToxin(type, damage, damage, type.getFrequencey(), type.isIncrease());
	}
	
	/**
	 * Applies a toxin to the entity.
	 * @param type
	 * 			the {@code ToxinType}.
	 * @param damage
	 * 			the starting damage.
	 * @param frequencey
	 * 			the frequencey.
	 */
	public void applyToxin(ToxinType type, int damage, int cap, int frequencey, boolean increases) {
		if (isImmune(type) || toxin != null)
			return;
		setToxin(type);
		this.damage = damage;
		this.frequencey = frequencey;
		this.increases = increases;
		this.cap = cap;
		//Apply the damage
		applyDamage();
	}
	
	/**
	 * Checks for an immunity to the given toxin.
	 * @param type
	 * 			the {@code ToxinType}.
	 */
	public boolean isImmune(ToxinType type) {
		if (immunities.containsKey(type))
			return true;
		if (entity.isPlayer()) {
			Player player = entity.getAsPlayer();
			switch (type) {
				case POISON:
					if (player.getEquipment().getShieldId() == 18340)
						return true;
					if (player.getSerpentineHelm().wearing())
						return true;
					if (player.healthBath)
						return true;
					break;
				case VENOM:
					if (player.getSerpentineHelm().wearing())
						return true;
					break;
			}
		}
		if (entity.isNPC()) {
			NPC npc = entity.getAsNPC();
			switch (type) {
				case POISON:				
					break;
				case VENOM:
					break;		
			}
		}
		return false;
	}

	/**
	 * Processes effects of toxins every tick.
	 */
	public void processToxins() {
		
		//Process the immunities from toxins.
		processImmunities();
		
		//Check if entity is dead or toxin doesnt exist.
		if (entity.isDead() || getToxin() == null)
			return;
		
		//Decrease time till next hit.
		if (frequencey > 0) {
			frequencey--;
			return;
		}
		
		//Apply the damage
		applyDamage();
		
		//Decrease/Increase toxin damage.
		if (increases && damage < cap)
			damage += 20;
		else 
			damage -= 20;
		
		//If it deals no damage, reset toxin.
		if (damage <= 0)
			reset();
		
		//Reset next damage tick.
		if (poisoned()) {
			frequencey = toxin.getFrequencey();
			return;
		}
		
	}
	
	/**
	 * Applys an immunity to the entity for the desired {@code ToxinType} and duration.
	 * @param type
	 * 				the toxin.
	 * @param duration
	 * 				the duration.
	 */
	public void applyImmunity(ToxinType type, int duration) {
		int time = immunities.get(type) == null ? duration : //Make higher duration take over from lower duration one.
			immunities.get(type) < duration ? duration : immunities.get(type);
		immunities.put(type, time);
		if (toxin != null) {
			if (toxin == type)
				toxin = null;
		}
		refresh();
	}
	
	/**
	 * Processes immunities.
	 */
	private void processImmunities() {
		if (immunities == null)
			return;
		for (ToxinType toxin : immunities.keySet()) {
			//Reduse the time remaining on immunity.
			immunities.put(toxin, immunities.get(toxin) - 1);
			//Remove expired immunities.
			if (immunities.get(toxin) <= 0) {	
				immunities.remove(toxin);
			}
		}
	}
	
	/**
	 * Apply damage from active toxin.
	 */
	private void applyDamage() {
		
		//Check immunity to the toxin.
		if (isImmune(toxin))
			return;
		
		boolean heal = false;
		//Check if poison purge will heal damage.
		if (entity.isPlayer()) {
			if (entity.getAsPlayer().getAuraManager().hasPoisonPurge() && (toxin == ToxinType.POISON)) {
				heal = true;
			}
		}
		//Send toxin damage
		if (!isImmune(toxin))
			entity.applyHit(new Hit(entity, damage, heal ? HitLook.HEALED_DAMAGE : HitLook.POISON_DAMAGE));
		
		//Refresh orb
		refresh();
	}

	/**
	 * Check if entity is poisoned.
	 * @return
	 */
	public boolean poisoned() {
		return toxin != null && !isImmune(toxin);
	}

	/**
	 * Reset the toxin.
	 */
	public void reset() {
		damage = 0;
		toxin = null;
		frequencey = 0;
		immunities.clear();
		refresh();
	}

	/**
	 * Refresh healing orb conifg.
	 */
	public void refresh() {
		if (entity instanceof Player) {
			Player player = ((Player) entity);
			player.getPackets().sendConfig(102, poisoned() ? 1 : 0);
		}
	}
	
	/**
	 * 
	 */
	public void setEntity(Entity entity) {
		if (this.immunities == null)
			this.immunities = new ConcurrentHashMap<ToxinType, Integer>();
		this.entity = entity;
	}
}
