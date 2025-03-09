package com.rs.game.world.entity.npc.dungeonnering;

import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.Drop;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.player.content.skills.dungeoneering.RoomReference;
import com.rs.game.world.entity.player.controller.impl.DungeonController;


@SuppressWarnings("serial")
public class DungeonBoss extends DungeonNPC {

	private RoomReference reference;

	public DungeonBoss(int id, Position tile, DungeonManager manager, RoomReference reference) {
		this(id, tile, manager, reference, 1);
	}

	public DungeonBoss(int id, Position tile, DungeonManager manager, RoomReference reference, double multiplier) {
		super(id, tile, manager, multiplier);
		this.setReference(reference);
		setForceAgressive(true);
		//setIntelligentRouteFinder(true);
		setLureDelay(0);
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		for (Player player : getManager().getParty().getTeam()) {
			if (player.getHitpoints() <= 10)
				((DungeonController) player.getControlerManager().getControler()).setKilledBossWithLessThan10Hp();
		}
		getManager().openStairs(getReference());
	}

	@Override
	public void drop() {
		//TODO dungeon boss drops
		/*Drop drops = NPCDrops.getDrops(getRealId(this));
		if (drops == null)
			return;
		List<Player> players = getManager().getParty().getTeam();
		if (players.size() == 0)
			return;
		Player luckyPlayer = players.get(com.rs.utility.Utils.random(players.size()));
		handleJournalDrops(luckyPlayer);
		List<Drop> dropL = drops.generateDrops(1);
		 for(Drop d : dropL){
			 sendDrop(luckyPlayer, d);
		luckyPlayer.getPackets().sendGameMessage("You received: " + d.getMinAmount() + " " + ItemDefinitions.getItemDefinitions(d.getItemId()).getName() + ".");
		for (Player p2 : players) {
			if (p2 == luckyPlayer)
				continue;
			p2.getPackets().sendGameMessage("" + luckyPlayer.getDisplayName() + " received: " + d.getMinAmount() + " " +  ItemDefinitions.getItemDefinitions(d.getItemId()).getName() + ".");
			}
		 }             */
	}
	
	private void handleJournalDrops(Player luckyPlayer) {
		/*if (Utils.random(3) == 0) {
			int tier = Utils.random(2) == 0 ? getManager().getParty().getFloor() / 2 : getManager().getParty().getFloor() / 2 - 1;
			if (tier < 0)
				tier = 0;
			if (tier > 29)
				tier = 29;
			Chronicles chronicle = Chronicles.values()[tier];
			dropChronicle(luckyPlayer, chronicle);
		}
		boolean bookDrop = Utils.random(3) == 0;
		if (bookDrop) {
			if (this instanceof ToKashBloodChiller) {
				if (this.getManager().getParty().getFloor() >= 9 && this.getManager().getParty().getFloor() <= 11) {
					Chronicles chronicle = Chronicles.KALPART1;
					dropChronicle(luckyPlayer, chronicle);
				}
			} else if (this instanceof LakkTheRiftSplitter) {
				if (this.getManager().getParty().getFloor() >= 18 && this.getManager().getParty().getFloor() <= 29) {
					Chronicles chronicle = Chronicles.KALPART2;
					dropChronicle(luckyPlayer, chronicle);
				}
			} else if (this instanceof BalLakThePummeler) {
				if (this.getManager().getParty().getFloor() >= 33 && this.getManager().getParty().getFloor() <= 35) {
					Chronicles chronicle = Chronicles.KALPART3;
					dropChronicle(luckyPlayer, chronicle);
				}
			} else if (this instanceof YkLagorThunderous) {
				if (this.getManager().getParty().getFloor() >= 45 && this.getManager().getParty().getFloor() <= 47) {
					Chronicles chronicle = Chronicles.KALPART4;
					dropChronicle(luckyPlayer, chronicle);
				}
			} else if (this instanceof KalGerWarmonger) {
				if (this.getManager().getParty().getFloor() >= 57 && this.getManager().getParty().getFloor() <= 60) {
					Chronicles chronicle = Chronicles.KALPART5;
					dropChronicle(luckyPlayer, chronicle);
				}
			} else if (this.getName().equals("Plane-freezer Lakhrahnaz")) {
				if (this.getManager().getParty().getFloor() >= 6 && this.getManager().getParty().getFloor() <= 11) {
					Chronicles chronicle = Chronicles.STALKPART1;
					dropChronicle(luckyPlayer, chronicle);
				}
			} else if (this instanceof NightGazerKhighorahk) {
				if (this.getManager().getParty().getFloor() >= 26 && this.getManager().getParty().getFloor() <= 29) {
					Chronicles chronicle = Chronicles.STALKPART2;
					dropChronicle(luckyPlayer, chronicle);
				}
			} else if (this instanceof ShadowForgerIhlakhizan) {
				if (this.getManager().getParty().getFloor() >= 30 && this.getManager().getParty().getFloor() <= 35) {
					Chronicles chronicle = Chronicles.STALKPART3;
					dropChronicle(luckyPlayer, chronicle);
				}
			} else if (this instanceof FleshspoilerHaasghenahk) {
				if (this.getManager().getParty().getFloor() >= 42 && this.getManager().getParty().getFloor() <= 47) {
					Chronicles chronicle = Chronicles.STALKPART4;
					dropChronicle(luckyPlayer, chronicle);
				}
			} else if (this instanceof WorldGorgerShukarhazh) {
				if (this.getManager().getParty().getFloor() >= 54 && this.getManager().getParty().getFloor() <= 60) {
					Chronicles chronicle = Chronicles.STALKPART5;
					dropChronicle(luckyPlayer, chronicle);
				}
			} else if (this instanceof GluttonousBehemoth) {
				if (this.getManager().getParty().getFloor() >= 1 && this.getManager().getParty().getFloor() <= 11) {
					Chronicles chronicle = Chronicles.BEHPART1;
					dropChronicle(luckyPlayer, chronicle);
				}
			} else if (this instanceof BulwarkBeast) {
				if (this.getManager().getParty().getFloor() >= 12 && this.getManager().getParty().getFloor() <= 17 || this.getManager().getParty().getFloor() >= 30 && this.getManager().getParty().getFloor() <= 35) {
					Chronicles chronicle = Chronicles.BEHPART2;
					dropChronicle(luckyPlayer, chronicle);
				}
			} else if (this instanceof Stomp) {
				if (this.getManager().getParty().getFloor() >= 18 && this.getManager().getParty().getFloor() <= 29) {
					Chronicles chronicle = Chronicles.BEHPART3;
					dropChronicle(luckyPlayer, chronicle);
				}
			} else if (this instanceof RuneboundBehemoth) {
				if (this.getManager().getParty().getFloor() >= 36 && this.getManager().getParty().getFloor() <= 47) {
					Chronicles chronicle = Chronicles.BEHPART4;
					dropChronicle(luckyPlayer, chronicle);
				}
			} else if (this instanceof HopeDevourer) {
				if (this.getManager().getParty().getFloor() >= 51 && this.getManager().getParty().getFloor() <= 60) {
					Chronicles chronicle = Chronicles.BEHPART4;
					dropChronicle(luckyPlayer, chronicle);
				}
			} else if (this instanceof AsteaFrostweb) {
				Chronicles chronicle = Chronicles.ASTEA_FROSTWEB;
				dropChronicle(luckyPlayer, chronicle);
			} else if (this instanceof IcyBones) {
				Chronicles chronicle = Chronicles.TROLL_SCRAWLINGS;
				dropChronicle(luckyPlayer, chronicle);
			} else if (this instanceof LuminscentIcefiend) {
				Chronicles chronicle = Chronicles.ENVIRONMENTAL_EFFECTS;
				dropChronicle(luckyPlayer, chronicle);
			} else if (this instanceof DivineSkinweaver) {
				Chronicles chronicle = Chronicles.DIVINE_SKINWEAVERS;
				dropChronicle(luckyPlayer, chronicle);
			} else if (this instanceof HobgoblinGeomancer) {
				Chronicles chronicle = Chronicles.HOBGOBLIN_SCRAWLINGS;
				dropChronicle(luckyPlayer, chronicle);
			} else if (this.getName().equals("Unholy cursebearer")) {
				Chronicles chronicle = Chronicles.THE_PRICE_OF_BETRAYAL;
				dropChronicle(luckyPlayer, chronicle);
			} else if (this instanceof Rammernaut) {
				Chronicles chronicle = Chronicles.EQUIPMENT_REQUISITION_RECEIPTS;
				dropChronicle(luckyPlayer, chronicle);
			} else if (this instanceof LexicusRunewright) {
				Chronicles chronicle = Chronicles.LEXICUS_RUNEWRIGHT;
				dropChronicle(luckyPlayer, chronicle);
			} else if (this instanceof Sagittare) {
				Chronicles chronicle = Chronicles.AMMUNITION_REQUISITION_ORDERS;
				dropChronicle(luckyPlayer, chronicle);
			} else if (this instanceof Gravecreeper) {
				Chronicles chronicle = Chronicles.TOMBSTONE_TRANSCRIPTION;
				dropChronicle(luckyPlayer, chronicle);
			} else if (this instanceof NecroLord) {
				Chronicles chronicle = Chronicles.ARCH_NECROLORD_REQUEST;
				dropChronicle(luckyPlayer, chronicle);
			} else if (this instanceof Blink) {
				Chronicles chronicle = Chronicles.BLINKS_SCRIBBLINGS;
				dropChronicle(luckyPlayer, chronicle);
			} else if (this instanceof WarpedGulega) {
				Chronicles chronicle = Chronicles.GULEGA_MISSIVE;
				dropChronicle(luckyPlayer, chronicle);
			} else if (this instanceof Dreadnaut) {
				Chronicles chronicle = Chronicles.RESOURCE_REQUISITION_ORDERS;
				dropChronicle(luckyPlayer, chronicle);
			}
		}*/
	}
	
	/**
	 * Checking object instance rather than ids because the bosses often transform to diff npcs,
	 * cbf listing down all npcs.
	 * @return drop npc id.
	 */
	public int getRealId(DungeonBoss boss) {
		if (boss instanceof AsteaFrostweb)
			return 9965;
		if (boss instanceof GluttonousBehemoth)
			return 9948;
		if (boss instanceof LuminscentIcefiend)
			return 9912;
		if (boss instanceof HobgoblinGeomancer)
			return 10059;
		if (boss instanceof IcyBones)
			return 10040;
		if (boss instanceof ToKashBloodChiller)
			return 10024;
		if (boss instanceof DivineSkinweaver)
			return 10058;
		if (boss instanceof BulwarkBeast)
			return 10073;
		if (boss instanceof Rammernaut)
			return 9767;
		if (boss instanceof Stomp)
			return 9782;
		if (boss instanceof LakkTheRiftSplitter)
			return 9898;
		if (boss instanceof Sagittare)
			return 9753;
		if (boss instanceof NightGazerKhighorahk)
			return 9725;
		if (boss instanceof LexicusRunewright)
			return 9842;
		if (boss instanceof BalLakThePummeler)
			return 10128;
		if (boss instanceof ShadowForgerIhlakhizan)
			return 10143;
		if (boss instanceof SkeletalAdventurer)
			return 11985;
		if (boss instanceof RuneboundBehemoth)
			return 11752;
		if (boss instanceof Gravecreeper)
			return 11708;
		if (boss instanceof NecroLord)
			return 11737;
		if (boss instanceof FleshspoilerHaasghenahk)
			return 11895;
		if (boss instanceof YkLagorThunderous)
			return 11872;
		if (boss instanceof WarpedGulega)
			return 11737;
		if (boss instanceof Dreadnaut)
			return 12848;
		if (boss instanceof HopeDevourer)
			return 12886;
		if (boss instanceof WorldGorgerShukarhazh)
			return 12478;
		if (boss instanceof Blink)
			return 12865;
		if (boss instanceof KalGerWarmonger)
			return 12752;
		if (boss.getName().equals("Unholy cursebearer"))
			return 10111;
		if (boss.getName().equals("Plane-freezer Lakhrahnaz"))
			return 9929; 
		return -1;
	}

	
	@Override
	public  void sendDrop(Player player, Drop drop) {
		Item item = new Item(drop.getItemId());
		player.getInventory().addItemDrop(item.getId(), item.getAmount());
	}
	
	/*@Override
	public boolean isPoisonImmune() {
		return true;
	}*/

	public RoomReference getReference() {
		return reference;
	}

	public void setReference(RoomReference reference) {
		this.reference = reference;
	}
}
