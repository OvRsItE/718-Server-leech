package com.rs.game.world.entity.updating.impl;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.item.Item;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Equipment;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.social.clanchat.ClansManager;
import com.rs.network.io.OutputStream;
import com.rs.utility.Utils;

import java.io.Serializable;
import java.util.Arrays;

public class Appearence implements Serializable {

	private static final long serialVersionUID = 7655608569741626586L;

	private transient int renderEmote;
	private int title;
	private int[] lookI;
	public int cr, cg, cb, ca, ci;
	public boolean ce;
	private byte[] colour;
	private boolean male;
	private transient boolean glowRed;
	private transient byte[] appeareanceData;
	private transient byte[] md5AppeareanceDataHash;
	private transient short transformedNpcId;
	private transient boolean hidePlayer;

	private transient Player player;

	public Appearence() {
		male = true;
		renderEmote = -1;
		title = -1;
		cr = 0; cg = 0; cb = 0;
		ce = false;
		ca = 0;
		ci = 0;
		resetAppearence();
	}

	public void setGlowRed(boolean glowRed) {
		this.glowRed = glowRed;
		generateAppearenceData();
	}

	public void setPlayer(Player player) {
		this.player = player;
		transformedNpcId = -1;
		renderEmote = -1;
		if(lookI == null)
			resetAppearence();
	}

	public void transformIntoNPC(int id) {
		transformedNpcId = (short) id;
		generateAppearenceData();
	}

	public void switchHidden() {
		hidePlayer = !hidePlayer;
		generateAppearenceData();
	}
	
	public void setHidden(boolean value) {
		hidePlayer = value;
		generateAppearenceData();
	}
	
	public boolean isHidden() {
		return hidePlayer;
	}
	
	public boolean isGlowRed() {
		return glowRed;
	}
	
	public int getSkullId() {
		return player.getGameMode().getSkullId();
	}
	
    public void setBootsColor(int color) {
	colour[3] = (byte) color;
    }
    
    public void setBootsStyle(int i) {
	lookI[6] = i;
    }

	public void generateAppearenceData() {
		OutputStream stream = new OutputStream();
		int flag = 0;
		if (!male)
			flag |= 0x1;
		if (transformedNpcId >= 0
				&& NPCDefinitions.getNPCDefinitions(transformedNpcId).aBoolean6180)
			flag |= 0x2;
		if(title != 0) 
			flag |=  player.getTitleManager().goesAfterName(title) ? 0x80 : 0x40;
			stream.writeByte(flag);
		if(title != 0) {
			stream.writeGJString(""/* + player.getTitleManager().getFullTitle(male, title, player)*/);
		}
		stream.writeByte(player.hasSkull() ? player.getSkullId() : getSkullId());
		//stream.writeByte(player.hasSkull() ? player.getSkullId() : -1); // pk// icon
		stream.writeByte(player.getPrayer().getPrayerHeadIcon()); // prayer icon
		stream.writeByte(hidePlayer ? 1 : 0);
		// npc
		if (transformedNpcId >= 0) {
			stream.writeInt(-1); // 65535 tells it a npc
			stream.writeShort(transformedNpcId);
			stream.writeByte(0);
		} else {
			for (int index = 0; index < 4; index++) {
				Item item = player.getEquipment().getItems().get(index);
				if (glowRed) {
					if (index == 0) {
						stream.writeInt(16384 + 2910);
						continue;
					}
					if (index == 1) {
						stream.writeInt(16384 + 14641);
						continue;
					}
				}
				if (item == null)
					stream.writeInt(0);
				else
					stream.writeInt(16384 + item.getId());
			}
			
			Item item = player.getEquipment().getItems().get(Equipment.SLOT_CHEST);
			stream.writeInt(item == null ? 0x100 + lookI[2] : 16384 + item.getId());

			item = player.getEquipment().getItems().get(Equipment.SLOT_SHIELD);
			stream.writeInt(item == null ? 0 : 16384 + item.getId());
			
			item = player.getEquipment().getItems().get(Equipment.SLOT_CHEST);
			stream.writeInt(item == null || !Equipment.hideArms(item) ? 0x100 + lookI[3] : 0);
			
			item = player.getEquipment().getItems().get(Equipment.SLOT_LEGS);
			stream.writeInt(glowRed ? 16384 + 2908 : item == null ? 0x100 + lookI[5] : 16384 + item.getId());
			
			item = player.getEquipment().getItems().get(Equipment.SLOT_HAT);
			stream.writeInt(!glowRed && (item == null || !Equipment.hideHair(item)) ? 0x100 + lookI[0] : 0);
			
			item = player.getEquipment().getItems().get(Equipment.SLOT_HANDS);
			stream.writeInt(glowRed ? 16384 + 2912 : item == null ? 0x100 + lookI[4] : 16384 + item.getId());
			
			item = player.getEquipment().getItems().get(Equipment.SLOT_FEET);
			stream.writeInt(glowRed ? 16384 + 2904 : item == null ? 0x100 + lookI[6] : 16384 + item.getId());
			
			item = player.getEquipment().getItems().get(male ? Equipment.SLOT_HAT : Equipment.SLOT_CHEST);
			stream.writeInt(item == null || (male && Equipment.showBear(item)) ? 0x100 + lookI[1] : 0);
			
			item = player.getEquipment().getItems().get(Equipment.SLOT_AURA);
			stream.writeInt(item == null ? 0 : 16384 + item.getId()); //Fixes the winged auras lookIing fucked.
			
			int pos = stream.getOffset();
			stream.writeShort(0);
			int hash = 0;
			int slotFlag = -1;
			for (int slotId = 0; slotId < player.getEquipment().getItems().getSize(); slotId++) {
				if (Equipment.DISABLED_SLOTS[slotId] != 0)
					continue;
				slotFlag++;
				if (slotId == Equipment.SLOT_HAT) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == 20768 || hatId == 20770 || hatId == 20772) {
						ItemDefinitions defs = ItemDefinitions.getItemDefinitions(hatId-1);
						if ((hatId == 20768
								&& Arrays.equals(
										player.getMaxedCapeCustomized(),
										defs.originalModelColors) || ((hatId == 20770 || hatId == 20772) && Arrays
								.equals(player.getCompletionistCapeCustomized(),
										defs.originalModelColors))))
							continue;
						hash |= 1 << slotFlag;
						stream.writeByte(0x4); // modify 4 model colors
						int[] hat = hatId == 20768 ? player
								.getMaxedCapeCustomized() : player
								.getCompletionistCapeCustomized();
						int slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
						stream.writeShort(slots);
						for (int i = 0; i < 4; i++)
							stream.writeShort(hat[i]);
					}
				} else if (slotId == Equipment.SLOT_WEAPON) {
					int weaponId = player.getEquipment().getWeaponId();
				    if (weaponId == 20709) {
						ClansManager manager = player.getClanManager();
						if (manager == null)
						    continue;
						int[] colors = manager.getClan().getMottifColors();
						ItemDefinitions defs = ItemDefinitions.getItemDefinitions(20709);
						boolean modifyColor = !Arrays.equals(colors, defs.originalModelColors);
						int bottom = manager.getClan().getMottifBottom();
						int top = manager.getClan().getMottifTop();
						if (bottom == 0 && top == 0 && !modifyColor)
						    continue;
						hash |= 1 << slotFlag;
						stream.writeByte((modifyColor ? 0x4 : 0) | (bottom != 0 || top != 0 ? 0x8 : 0));
						if (modifyColor) {
						    int slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
						    stream.writeShort(slots);
						    for (int i = 0; i < 4; i++)
							stream.writeShort(colors[i]);
						}
						if (bottom != 0 || top != 0) {
						    int slots = 0 | 1 << 4;
						    stream.writeByte(slots);
						    stream.writeShort(ClansManager.getMottifTexture(top));
						    stream.writeShort(ClansManager.getMottifTexture(bottom));
						}
				    }
				} else if (slotId == Equipment.SLOT_CAPE) {
					int capeId = player.getEquipment().getCapeId();
					if (capeId == 20767 || capeId == 20769 || capeId == 20771) {
						ItemDefinitions defs = ItemDefinitions
								.getItemDefinitions(capeId);
						if ((capeId == 20767
								&& Arrays.equals(
										player.getMaxedCapeCustomized(),
										defs.originalModelColors) || ((capeId == 20769 || capeId == 20771) && Arrays
								.equals(player.getCompletionistCapeCustomized(),
										defs.originalModelColors))))
							continue;
						hash |= 1 << slotFlag;
						stream.writeByte(0x4); // modify 4 model colors
						int[] cape = capeId == 20767 ? player
								.getMaxedCapeCustomized() : player
								.getCompletionistCapeCustomized();
						int slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
						stream.writeShort(slots);
						for (int i = 0; i < 4; i++)
							stream.writeShort(cape[i]);
				    } else if (capeId == 20708) {
						ClansManager manager = player.getClanManager();
						if (manager == null)
						    continue;
						int[] colors = manager.getClan().getMottifColors();
						ItemDefinitions defs = ItemDefinitions.getItemDefinitions(20709);
						boolean modifyColor = !Arrays.equals(colors, defs.originalModelColors);
						int bottom = manager.getClan().getMottifBottom();
						int top = manager.getClan().getMottifTop();
						if (bottom == 0 && top == 0 && !modifyColor)
						    continue;
						hash |= 1 << slotFlag;
						stream.writeByte((modifyColor ? 0x4 : 0) | (bottom != 0 || top != 0 ? 0x8 : 0));
						if (modifyColor) {
						    int slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
						    stream.writeShort(slots);
						    for (int i = 0; i < 4; i++)
							stream.writeShort(colors[i]);
						}
						if (bottom != 0 || top != 0) {
						    int slots = 0 | 1 << 4;
						    stream.writeByte(slots);
						    stream.writeShort(ClansManager.getMottifTexture(top));
						    stream.writeShort(ClansManager.getMottifTexture(bottom));
						}

					}
				} else if (slotId == Equipment.SLOT_AURA) {
					int auraId = player.getEquipment().getAuraId();
					if (auraId == -1 || !player.getAuraManager().isActivated())
						continue;
					ItemDefinitions auraDefs = ItemDefinitions.getItemDefinitions(auraId);
					if(auraDefs.getMaleWornModelId1() == -1 || auraDefs.getFemaleWornModelId1() == -1)
						continue;
					hash |= 1 << slotFlag;
					stream.writeByte(0x1); // modify model ids
					int modelId = player.getAuraManager().getAuraModelId();
					stream.writeBigSmart(modelId); // male modelid1
					stream.writeBigSmart(modelId); // female modelid1
					if(auraDefs.getMaleWornModelId2() != -1 || auraDefs.getFemaleWornModelId2() != -1) {
						int modelId2 = player.getAuraManager().getAuraModelId2();
						stream.writeBigSmart(modelId2); 
						stream.writeBigSmart(modelId2); 
					}
				}
			}
			int pos2 = stream.getOffset();
			stream.setOffset(pos);
			stream.writeShort(hash);
			stream.setOffset(pos2);
		}

		for (int index = 0; index < colour.length; index++)
			// colour length 10
			stream.writeByte(colour[index]);

		stream.writeShort(getRenderEmote());
		stream.writeString(player.getDisplayName());
		boolean pvpArea = World.isPvpArea(player);
		stream.writeByte(pvpArea ? player.getSkills().getCombatLevel() : player
				.getSkills().getCombatLevelWithSummoning());
		stream.writeByte(pvpArea ? player.getSkills()
				.getCombatLevelWithSummoning() : 0);
		stream.writeByte(-1); // higher level acc name appears in front :P

		stream.writeByte(transformedNpcId >= 0 ? 1 : 0); // to end here else id
															// need to send more
															// data
		if (transformedNpcId >= 0) {
			NPCDefinitions defs = NPCDefinitions.getNPCDefinitions(transformedNpcId);
			stream.writeShort(defs.anInt6151);
			stream.writeShort(defs.anInt6174);
			stream.writeShort(defs.anInt6169);
			stream.writeShort(defs.anInt6140);
			stream.writeByte(defs.anInt6140);
		}
		if (ce) {
			stream.writeByte(1);
			stream.writeByte(cr);
			stream.writeByte(cg);
			stream.writeByte(cb);
			stream.writeByte(ci);
			stream.writeByte(ca);
		} else
			stream.writeByte(0);
		// done separated for safe because of synchronization
		byte[] appeareanceData = new byte[stream.getOffset()];
		System.arraycopy(stream.getBuffer(), 0, appeareanceData, 0, appeareanceData.length);
		byte[] md5Hash = Utils.encryptUsingMD5(appeareanceData);
		this.appeareanceData = appeareanceData;
		md5AppeareanceDataHash = md5Hash;
	}

	/**
	 * Writes the player's default flags to the stream
	 *
	 * @param stream
	 *            The stream to write data to
	 */
	private void writeFlags(OutputStream stream) {
		int flag = 0;
		if (!male)
			/**
			 * Female flag
			 */
			flag |= 0x1;
		if (asNPC >= 0 && NPCDefinitions.getNPCDefinitions(asNPC).aBoolean6180)
			/**
			 * Is NPC flag
			 */
			flag |= 0x2;
		if (showSkillLevel)
			flag |= 0x4;
		if (title != 0)
			/**
			 * Has title flag
			 */
			flag |= title >= 32 && title <= 37 ? 0x80 : 0x40; // after/before
		stream.writeByte(flag);
	}

	/**
	 * Writes the player's NPC data to the stream
	 *
	 * @param stream
	 *            The stream to write data to
	 */
	private void writeNPCData(OutputStream stream) {
		stream.writeShort(-1);
		stream.writeShort(asNPC);
		stream.writeByte(0);
	}

	/**
	 * Writes the player's skull to the stream
	 *
	 * @param stream
	 *            The stream to write data to
	 */
	private void writeSkull(OutputStream stream) {
		stream.writeByte(player.hasSkull() ? player.getSkullId() : -1);
		stream.writeByte(player.getPrayer().getPrayerHeadIcon());
		stream.writeByte(hidePlayer ? 1 : 0);
	}
	
	/**
	 * The player's body looks.
	 */
	private int[] bodyStyle;
	/**
	 * The cosmetic items
	 */
	private Item[] cosmeticItems;
	/**
	 * The player's body color
	 */
	private byte[] bodyColors;
	/**
	 * The appearance block
	 */
	private transient byte[] appearanceBlock;
	/**
	 * The encyrpted appearance block
	 */
	private transient byte[] encyrptedAppearanceBlock;
	/**
	 * The NPC the player is transformed into
	 */
	private transient short asNPC;
	/**
	 * If we should show the player's skill level rather then combat level
	 */
	private boolean showSkillLevel;
	
	/**
	 * Resets the appearance flags
	 */
	public void resetAppearance() {
		bodyStyle = new int[7];
		bodyColors = new byte[10];
		if (cosmeticItems == null)
			cosmeticItems = new Item[14];
		setMale();
	}

	public int getSize() {
		if (transformedNpcId >= 0)
			return NPCDefinitions.getNPCDefinitions(transformedNpcId).size;
		return 1;
	}

	public void setRenderEmote(int id) {
		this.renderEmote = id;
		generateAppearenceData();
	}

	public int getRenderEmote() {
		if (renderEmote >= 0)
			return renderEmote;
		if (transformedNpcId >= 0)
			return NPCDefinitions.getNPCDefinitions(transformedNpcId).renderEmote;
		return player.getEquipment().getWeaponRenderEmote();
	}

	public void resetAppearence() {
		lookI = new int[7];
		colour = new byte[10];
		male();
	}

	public void male() {
		lookI[0] = 3; // Hair
		lookI[1] = 14; // Beard
		lookI[2] = 18; // Torso
		lookI[3] = 26; // Arms
		lookI[4] = 34; // Bracelets
		lookI[5] = 38; // Legs
		lookI[6] = 42; // Shoes~

		colour[2] = 16;
		colour[1] = 16;
		colour[0] = 3;
		male = true;
	}

	public void female() {
		lookI[0] = 48; // Hair
		lookI[1] = 57; // Beard
		lookI[2] = 57; // Torso
		lookI[3] = 65; // Arms
		lookI[4] = 68; // Bracelets
		lookI[5] = 77; // Legs
		lookI[6] = 80; // Shoes

		colour[2] = 16;
		colour[1] = 16;
		colour[0] = 3;
		male = false;
	}

	public byte[] getAppeareanceData() {
		return appeareanceData;
	}

	public byte[] getMD5AppeareanceDataHash() {
		return md5AppeareanceDataHash;
	}

	public boolean isMale() {
		return male;
	}

	public void setLook(int i, int i2) {
		lookI[i] = i2;
	}

	public void setColor(int i, int i2) {
		colour[i] = (byte) i2;
	}

	public void setMale(boolean male) {
		this.male = male;
	}

	public void setHairStyle(int i) {
		lookI[0] = i;
	}
	
	public void setTopStyle(int i) {
		lookI[2] = i;
	}
	
	public int getTopStyle() {
		return lookI[2];
	}
	
	
	public void setArmsStyle(int i) {
		lookI[3] = i;
	}
	
	public void setWristsStyle(int i) {
		lookI[4] = i;
	}

	public void setLegsStyle(int i) {
		lookI[5] = i;
	}

	public int getHairStyle() {
		return lookI[0];
	}

	public void setBeardStyle(int i) {
		lookI[1] = i;
	}

	public int getBeardStyle() {
		return lookI[1];
	}

	public void setFacialHair(int i) {
		lookI[1] = i;
	}

	public int getFacialHair() {
		return lookI[1];
	}

	public void setSkinColor(int color) {
		colour[4] = (byte) color;
	}

	public int getSkinColor() {
		return colour[4];
	}

	public void setHairColor(int color) {
		colour[0] = (byte) color;
	}
	
	public void setTopColor(int color) {
		colour[1] = (byte) color;
	}
	
	public void setLegsColor(int color) {
		colour[2] = (byte) color;
	}

	public int getHairColor() {
		return colour[0];
	}

	public void setTitle(int title) {
		this.title = title;
		generateAppearenceData();
	}

	/**
	 * Sets the player to a male
	 */
	public void setMale() {
		bodyStyle[0] = 3;
		bodyStyle[1] = 14;
		bodyStyle[2] = 18;
		bodyStyle[3] = 26;
		bodyStyle[4] = 34;
		bodyStyle[5] = 38;
		bodyStyle[6] = 42;
		bodyColors[2] = 16;
		bodyColors[1] = 16;
		bodyColors[0] = 3;
		male = true;
	}

	/**
	 * Retruns the title
	 * 
	 * @return The title
	 */
	public int getTitle() {
		return title;
	}
	
	public void writeClanVexilliumData(OutputStream stream) {
		int flag = 0;
		flag |= 0x4; // modify colors
		flag |= 0x8; // modify textures
		stream.writeByte(flag);

		int slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
		stream.writeShort(slots);

		for (int i = 0; i < 4; i++) {
			stream.writeShort(player.getClanCapeCustomized()[i]);
		}

		slots = 0 | 1 << 4;
		stream.writeByte(slots);

		for (int i = 0; i < 2; i++) {
			stream.writeShort(player.getClanCapeSymbols()[i]);
		}
	}

	public void writeClanCapeData(OutputStream stream) {
		int flag = 0;
		flag |= 0x4; // modify colors
		flag |= 0x8; // modify textures
		stream.writeByte(flag);

		int slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
		stream.writeShort(slots);

		for (int i = 0; i < 4; i++) {
			stream.writeShort(player.getClanCapeCustomized()[i]);
		}

		slots = 0 | 1 << 4;
		stream.writeByte(slots);

		for (int i = 0; i < 2; i++) {
			stream.writeShort(player.getClanCapeSymbols()[i]);
		}
	}

	public boolean isNPC() {
		return transformedNpcId != -1;
	}
}
