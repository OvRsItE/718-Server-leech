package com.rs.game.world.entity.player.pets;

import com.rs.utility.Utils;

public enum BossPets {

	KING_BLACK_DRAGON(29853, 15152, 50, 1_000),
	CORPOREAL_BEAST(29862, 16391, 8133, 1_000),
	GENERAL_GRAARDOR(29851, 16390, 6260, 1_000),
	KREE_ARRA(29850, 16392, 6222, 1_000),
	BORK(29846, 16003, 7134, 1_000),
	WILDYWYRM(29854, 16004, 3334, 1_000),
	SCORPIAS_OFFSPRING(29842, 16017, 16009, 1_000),
	VENENATIS_SPIDERLING(43177, 25557, 26504, 1_000),
	TENTACLE(29844, 16019, 16010, 1_000),
	VETION_JR(43179, 25536, 26612, 1_000),
	SMOKE_DEVIL(29843, 16021, 16015, 1_000),
	SKOTOS(29845, 16022, 16016, 1_000),
	ACIDIC_WYRMLING(29857, 16031, 16025, 1_000),
	COMMANDER_ZILYANA(29849, 16043, 6247, 1_000),
	SUNFREET(29847, 16039, 15222, 1_000),
	TOKHAAR_JAD(29839, 16036, 15211, 1_000),
	AVATAR_OF_CREATION(29860, 16038, 8597, 1_000),
	KALPHITE_QUEEN(29848, 16037, 1160, 1_000),
	DAGANNOTH_PRIME(29837, 16041, 2882, 1_000),
	DAGANNOTH_REX(29836, 16042, 2883, 1_000),
	DAGANNOTH_SUPREME(29838, 16040, 2881, 1_000),
	NEX(29861, 16044, 13450, 1_000),
	KRIL_TSUTSAROTH(29852, 16045, 6203, 1_000),
	GIANT_MOLE(29614, 26635, 3340, 1_000),
	JAD(29598, 25892, 2745, 10),
	VORKATH(29597, 28025, 28060, 1_000),
	CORPOREAL_BEAST1(29607, 20318, 8133, 1_000),
	ZULRAH(29601, 22128, 16641, 1_000),
	ZULRAH1(29601, 22128, 16642, 1_000),
	ZULRAH2(29601, 22128, 16643, 1_000),
	CALLISTO_CUB(43178, 25558, 26503, 1_000),
	PET_LORD_OF_LIGHTNING(28948, 16088, 16087, 300),
	PET_DARK_DEMON(28947, 16084, 16089, 300),
	PET_AMEYTHST_DRAGON(28950, 16104, 16103, 100),
	PET_BLOODIED_DRAGON(28836, 16100, 16097, 100),
	CURSED_CALLISTO_CUB(29613, 16070, 16007, 300),
	CURSED_VENENATIS_SPIDERLING(29856, 16018, 16008, 300),
	CURSED_VETION_JR(29855, 16011, 16020, 300),
	IKKLE_HYDRA(52746, 28492, 28615, 1000),
	BABY_BRUCE(28746, 16137, 16136, 500),
	PRINCE_LAVA_DRAGON(28747, 16114, 16113, 500),
	ICICLE_BEAST(28920, 16095, 16096, 500),
	ENRAGED_OLMLET(28813, 16115, 16083, 500),
	ETHER_HYDRA(28678, 16141, 16140, 1000),
	TOXIC_SMOKE_DEVIL(28677, 16143, 16143, 1000),
	SEA_KRAKEN(28675, 16145, 16144, 1000),
	UNDERWORLD_CERBERUS(28674, 16147, 16146, 1000),
	DARK_SIRE(28676, 16149, 16148, 1000),
	;
	
	private int itemId, npcId;
	private int[] bossId;
	private int rate;
	
	private BossPets(int itemId, int npcId, int[] bossId, int rate) {
		this.itemId = itemId;
		this.npcId = npcId;
		this.bossId = bossId;
		this.rate = rate;
	}
	
	private BossPets(int itemId, int npcId, int bossId, int rate) {
		this(itemId, npcId, new int[] { bossId }, rate);
	}
	
	public int getItemId() {
		return itemId;
	}

	public int getNpcId() {
		return npcId;
	}

	public int[] getBossId() {
		return bossId;
	}
	
	public String getName() {
		return Utils.getFormattedEnumName(name());
	}

	public int getRate() {
		return rate;
	}
	
	public static BossPets forBossId(int bossId) {
		for (BossPets pet : BossPets.values()) {
			for (int boss : pet.bossId) {
				if (boss == bossId)
					return pet;
			}
		}
		return null;
	}
	
	public static BossPets forItemId(int itemId) {
		for (BossPets pet : BossPets.values()) {
			if (itemId == pet.getItemId())
				return pet;
		}
		return null;
	}
	
}
