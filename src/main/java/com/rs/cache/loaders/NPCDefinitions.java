package com.rs.cache.loaders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.rs.Constants;
import com.rs.cache.Cache;
import com.rs.network.io.InputStream;

@SuppressWarnings("unused")
public final class NPCDefinitions {

	private static final ConcurrentHashMap<Integer, NPCDefinitions> npcDefinitions = new ConcurrentHashMap<Integer, NPCDefinitions>();

	private int id;
	public HashMap<Integer, Object> params;
	public int anInt6145;
	public int anInt6167;
	public int anInt837;
	public byte respawnDirection;
	public int size = 1;
	public int[][] anIntArrayArray6166;
	public boolean clickable;
	public int anInt6174;
	public int varbit;
	public int[] morphisms;
	public int anInt6143;
	public int renderEmote;
	public boolean aBoolean6173 = false;
	public int anInt850;
	public byte aByte6168;
	public boolean aBoolean6164;
	public int rotation;
	public byte aByte6152;
	public boolean aBoolean6155;
	public boolean aBoolean6165;
	public short[] retextureToFind;
	public int combatLevel;
	public byte[] aByteArray6135;
	public short aShort6191;
	public boolean renderPriority;
	public int resizeX;
	public String name;
	public short[] recolorToReplace;
	public byte walkMask;
	public int[] models;
	public int ambient;
	public int anInt6186;
	public int anInt6176;
	public int anInt6147;
	public int anInt6128;
	public int anInt6140;
	public int anInt6151;
	public int headIcon;
	public int anInt6179;
	public short[] recolorToFind;
	public int[][] anIntArrayArray882;
	public int anInt6169;
	public int[] anIntArray6131;
	public int varp;
	public int anInt889;
	public boolean invisible;
	public int[] chatheads;
	public short aShort6153;
	public String[] options;
	public short[] retextureToReplace;
	public int contrast;
	public int resizeY;
	public int npcId;
	public int anInt6178;

	public static final NPCDefinitions getNPCDefinitions(int id) {
		NPCDefinitions def = npcDefinitions.get(id);
		if (def == null) {
			def = new NPCDefinitions(id);
			def.method694();
			byte[] data = Cache.STORE.getIndexes()[18].getFile(
					id >>> 134238215, id & 0x7f);
			if (data != null)
				def.readValueLoop(new InputStream(data));
			if (id >= Constants.OSRS_NPCS_OFFSET)
				def.renderEmote = (id + Constants.OSRS_BAS_OFFSET);
			npcDefinitions.put(id, def);
		}
		return def;
	}


	public void method694() {
		if (models == null) {
			models = new int[0];
		}
	}

	private void readValueLoop(InputStream stream) {
		while (true) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0) {
				break;
			}
			if (this.id >= Constants.OSRS_NPCS_OFFSET)
				decodeOSRS(stream, opcode);
			else
				readValues(stream, opcode);
		}
	}

	public boolean aBoolean6180;

	private byte[] aByteArray1293;

	private byte[] aByteArray12930;

	private int[] anIntArray2930;

	private int idleAnimation, walkAnimation, rotate180Animation, rotate90RightAnimation, rotate90LeftAnimation;

	private void decodeOSRS(InputStream buffer, int opcode) {
		if (opcode == 1) {
			int i_54_ = buffer.readUnsignedByte();
			models = new int[i_54_];
			for (int i_55_ = 0; i_55_ < i_54_; i_55_++)
				models[i_55_] = buffer.readUnsignedShort() + Constants.OSRS_MODELS_OFFSET;
		} else if (opcode == 2)
			name = buffer.readString();
		else if (12 == opcode)
			size = buffer.readUnsignedByte();
		else if (opcode == 13)
			idleAnimation = buffer.readUnsignedShort() + Constants.OSRS_SEQ_OFFSET;
		else if (opcode == 14)
			walkAnimation = buffer.readUnsignedShort() + Constants.OSRS_SEQ_OFFSET;
		else if (opcode == 15)
			buffer.readUnsignedShort();
		else if (opcode == 16)
			buffer.readUnsignedShort();
		else if (opcode == 17) {
			walkAnimation = buffer.readUnsignedShort() + Constants.OSRS_SEQ_OFFSET;
			rotate180Animation = buffer.readUnsignedShort() + Constants.OSRS_SEQ_OFFSET;
			rotate90RightAnimation = buffer.readUnsignedShort() + Constants.OSRS_SEQ_OFFSET;
			rotate90LeftAnimation = buffer.readUnsignedShort() + Constants.OSRS_SEQ_OFFSET;
		}
		else if (opcode >= 30 && opcode < 35)
			options[opcode - 30] = buffer.readString();
		else if (opcode == 40) {
			int length = buffer.readUnsignedByte();
			recolorToFind = new short[length];
			recolorToReplace = new short[length];
			for (int index = 0; index < length; index++) {
				recolorToFind[index] = (short) buffer.readUnsignedShort();
				recolorToReplace[index] = (short) buffer.readUnsignedShort();
			}
		} else if (opcode == 41) {
			int length = buffer.readUnsignedByte();
			retextureToFind = new short[length];
			retextureToReplace = new short[length];
			for (int index = 0; index < length; index++) {
				retextureToFind[index] = (short) buffer.readUnsignedShort();
				retextureToReplace[index] = (short) buffer.readUnsignedShort();
			}
		} else if (opcode == 42) {
			int i_60_ = buffer.readUnsignedByte();
			aByteArray6135 = new byte[i_60_];
			for (int i_61_ = 0; i_61_ < i_60_; i_61_++)
				aByteArray6135[i_61_] = (byte) buffer.readByte();
		} else if (opcode == 44) {
			int i_24_ = (short) buffer.readUnsignedShort();
			int i_25_ = 0;
			for (int i_26_ = i_24_; i_26_ > 0; i_26_ >>= 1)
				i_25_++;
			aByteArray12930 = new byte[i_25_];
			byte i_27_ = 0;
			for (int i_28_ = 0; i_28_ < i_25_; i_28_++) {
				if ((i_24_ & 1 << i_28_) > 0) {
					aByteArray12930[i_28_] = i_27_;
					i_27_++;
				} else
					aByteArray12930[i_28_] = (byte) -1;
			}
		} else if (45 == opcode) {
			int i_29_ = (short) buffer.readUnsignedShort();
			int i_30_ = 0;
			for (int i_31_ = i_29_; i_31_ > 0; i_31_ >>= 1)
				i_30_++;
			aByteArray1293 = new byte[i_30_];
			byte i_32_ = 0;
			for (int i_33_ = 0; i_33_ < i_30_; i_33_++) {
				if ((i_29_ & 1 << i_33_) > 0) {
					aByteArray1293[i_33_] = i_32_;
					i_32_++;
				} else
					aByteArray1293[i_33_] = (byte) -1;
			}
		} else if (opcode == 60) {
			int i_62_ = buffer.readUnsignedByte();
			chatheads = new int[i_62_];
			for (int i_63_ = 0; i_63_ < i_62_; i_63_++)
				chatheads[i_63_] = buffer.readUnsignedShort();
		} else if (opcode == 93)
			invisible = false;
		else if (opcode == 95)
			combatLevel = buffer.readUnsignedShort();
		else if (opcode == 97)
			resizeX = buffer.readUnsignedShort();
		else if (98 == opcode)
			resizeY = buffer.readUnsignedShort();
		else if (opcode == 99)
			renderPriority = true;
		else if (opcode == 100)
			ambient = buffer.readByte();
		else if (101 == opcode)
			contrast = buffer.readByte();
		else if (opcode == 102)
			headIcon = buffer.readUnsignedShort();
		else if (103 == opcode)
			rotation = buffer.readUnsignedShort();
		else if (opcode == 106 || 118 == opcode) {
			varbit = buffer.readUnsignedShort();
			if (varbit == 65535)
				varbit = -1;
			varp = buffer.readUnsignedShort();
			if (varp == 65535)
				varp = -1;
			int i_64_ = -1;
			if (opcode == 118) {
				i_64_ = buffer.readUnsignedShort();
				if (i_64_ == 65535)
					i_64_ = -1;
			}
			int i_65_ = buffer.readUnsignedByte();
			morphisms = new int[2 + i_65_];
			for (int i_66_ = 0; i_66_ <= i_65_; i_66_++) {
				morphisms[i_66_] = buffer.readUnsignedShort();
				if (65535 == morphisms[i_66_])
					morphisms[i_66_] = -1;
			}
			morphisms[i_65_ + 1] = i_64_;
		} else if (opcode == 107)
			clickable = false;
		else if (opcode == 109)
			aBoolean6164 = false;
		else if (111 == opcode)
			aBoolean6165 = false;
		else if (opcode == 113) {
			aShort6191 = (short) buffer.readUnsignedShort();
			aShort6153 = (short) buffer.readUnsignedShort();
		} else if (114 == opcode) {
			aByte6168 = (byte) buffer.readByte();
			aByte6152 = (byte) buffer.readByte();
		} else if (119 == opcode)
			walkMask = (byte) buffer.readByte();
		else if (opcode == 121) {
			anIntArrayArray6166 = new int[models.length][];
			int i_67_ = buffer.readUnsignedByte();
			for (int i_68_ = 0; i_68_ < i_67_; i_68_++) {
				int i_69_ = buffer.readUnsignedByte();
				int[] is = (anIntArrayArray6166[i_69_] = new int[3]);
				is[0] = buffer.readByte();
				is[1] = buffer.readByte();
				is[2] = buffer.readByte();
			}
		} else if (opcode == 122)
			anInt6167 = buffer.readUnsignedShort();
		else if (opcode == 123)
			anInt6143 = buffer.readUnsignedShort();
		else if (opcode == 125)
			respawnDirection = (byte) buffer.readByte();
		else if (127 == opcode)
			renderEmote = buffer.readUnsignedShort();
		else if (128 == opcode)
			buffer.readUnsignedByte();
		else if (opcode == 134) {
			anInt6151 = buffer.readUnsignedShort();
			if (65535 == anInt6151)
				anInt6151 = -1;
			anInt6174 = buffer.readUnsignedShort();
			if (65535 == anInt6174)
				anInt6174 = -1;
			anInt6169 = buffer.readUnsignedShort();
			if (65535 == anInt6169)
				anInt6169 = -1;
			anInt6176 = buffer.readUnsignedShort();
			if (anInt6176 == 65535)
				anInt6176 = -1;
			anInt6176 = buffer.readUnsignedByte();
		} else if (opcode == 135 || 136 == opcode) {
			anInt6145 = buffer.readUnsignedByte();
			anInt6128 = buffer.readUnsignedShort();
		} else if (opcode == 137)
			anInt6147 = buffer.readUnsignedShort();
		else if (opcode == 138)
			anInt6178 = buffer.readUnsignedShort();
		else if (opcode == 140)
			anInt6179 = buffer.readUnsignedByte();
		else if (opcode == 141)
			aBoolean6173 = true;
		else if (opcode == 142)
			anInt6186 = buffer.readUnsignedShort();
		else if (143 == opcode)
			aBoolean6155 = true;
		else if (opcode >= 150 && opcode < 155) {
			options[opcode - 150] = buffer.readString();
		} else if (opcode == 155) {
			int aByte6138 = buffer.readByte();
			int aByte6139 = buffer.readByte();
			int aByte6157 = buffer.readByte();
			int aByte6141 = buffer.readByte();
		} else if (opcode == 160) {
			int i_70_ = buffer.readUnsignedByte();
			anIntArray6131 = new int[i_70_];
			for (int i_71_ = 0; i_71_ < i_70_; i_71_++)
				anIntArray6131[i_71_] = buffer.readUnsignedShort();
		} else if (opcode == 162)
			aBoolean6180 = true;
		else if (opcode == 163)
			buffer.readUnsignedByte();
		else if (164 == opcode) {
			buffer.readUnsignedShort();
			buffer.readUnsignedShort();
		} else if (165 == opcode)
			buffer.readUnsignedByte();
		else if (168 == opcode)
			buffer.readUnsignedByte();
		else if (opcode >= 170 && opcode < 176) {
			if (null == anIntArray2930) {
				anIntArray2930 = new int[6];
				Arrays.fill(anIntArray2930, -1);
			}
			int i_44_ = (short) buffer.readUnsignedShort();
			anIntArray2930[opcode - 170] = i_44_;
		} else if (249 == opcode) {
			int size = buffer.readUnsignedByte();
			if (params == null) {
				params = new HashMap<>(size);
			}
			for (int index = 0; index < size; index++) {
				boolean bool = buffer.readUnsignedByte() == 1;
				int key = buffer.read24BitInt();
				Object value;
				if (bool)
					value = buffer.readString();
				else
					value = buffer.readInt();
				params.put(key, value);
			}
		}
	}

	private void readValues(InputStream stream, int opcode) {
		if (opcode != 1) {
			if (opcode == 2) {
				name = stream.readString();
			} else if ((opcode ^ 0xffffffff) != -13) {
				if (opcode >= 30 && (opcode ^ 0xffffffff) > -36) {
					options[opcode - 30] = stream.readString();
					if (options[-30 + opcode].equalsIgnoreCase("Hidden")) {
						options[-30 + opcode] = null;
					}
				} else if ((opcode ^ 0xffffffff) != -41) {
					if (opcode == 41) {
						int i = stream.readUnsignedByte();
						recolorToFind = new short[i];
						recolorToReplace = new short[i];
						for (int i_54_ = 0; (i_54_ ^ 0xffffffff) > (i ^ 0xffffffff); i_54_++) {
							recolorToFind[i_54_] = (short) stream
									.readUnsignedShort();
							recolorToReplace[i_54_] = (short) stream
									.readUnsignedShort();
						}
					} else if (opcode == 44) {
						int i_24_ = (short) stream.readUnsignedShort();
						int i_25_ = 0;
						for (int i_26_ = i_24_; i_26_ > 0; i_26_ >>= 1) {
							i_25_++;
						}
						aByteArray12930 = new byte[i_25_];
						byte i_27_ = 0;
						for (int i_28_ = 0; i_28_ < i_25_; i_28_++) {
							if ((i_24_ & 1 << i_28_) > 0) {
								aByteArray12930[i_28_] = i_27_;
								i_27_++;
							} else {
								aByteArray12930[i_28_] = (byte) -1;
							}
						}
					} else if (45 == opcode) {
						int i_29_ = (short) stream.readUnsignedShort();
						int i_30_ = 0;
						for (int i_31_ = i_29_; i_31_ > 0; i_31_ >>= 1) {
							i_30_++;
						}
						aByteArray1293 = new byte[i_30_];
						byte i_32_ = 0;
						for (int i_33_ = 0; i_33_ < i_30_; i_33_++) {
							if ((i_29_ & 1 << i_33_) > 0) {
								aByteArray1293[i_33_] = i_32_;
								i_32_++;
							} else {
								aByteArray1293[i_33_] = (byte) -1;
							}
						}
					} else if ((opcode ^ 0xffffffff) == -43) {
						int i = stream.readUnsignedByte();
						aByteArray6135 = new byte[i];
						for (int i_55_ = 0; i > i_55_; i_55_++) {
							aByteArray6135[i_55_] = (byte) stream.readByte();
						}
					} else if ((opcode ^ 0xffffffff) != -61) {
						if (opcode == 93) {
							invisible = false;
						} else if ((opcode ^ 0xffffffff) == -96) {
							combatLevel = stream.readUnsignedShort();
						} else if (opcode != 97) {
							if ((opcode ^ 0xffffffff) == -99) {
								resizeY = stream.readUnsignedShort();
							} else if ((opcode ^ 0xffffffff) == -100) {
								renderPriority = true;
							} else if (opcode == 100) {
								ambient = stream.readByte();
							} else if ((opcode ^ 0xffffffff) == -102) {
								contrast = stream.readByte() * 5;
							} else if ((opcode ^ 0xffffffff) == -103) {
								headIcon = stream.readUnsignedShort();
							} else if (opcode != 103) {
								if (opcode == 106 || opcode == 118) {
									varbit = stream.readUnsignedShort();
									if (varbit == 65535) {
										varbit = -1;
									}
									varp = stream.readUnsignedShort();
									if (varp == 65535) {
										varp = -1;
									}
									int i = -1;
									if ((opcode ^ 0xffffffff) == -119) {
										i = stream.readUnsignedShort();
										if ((i ^ 0xffffffff) == -65536) {
											i = -1;
										}
									}
									int i_56_ = stream.readUnsignedByte();
									morphisms = new int[2 + i_56_];
									for (int i_57_ = 0; i_56_ >= i_57_; i_57_++) {
										morphisms[i_57_] = stream
												.readUnsignedShort();
										if (morphisms[i_57_] == 65535) {
											morphisms[i_57_] = -1;
										}
									}
									morphisms[i_56_ - -1] = i;
								} else if ((opcode ^ 0xffffffff) != -108) {
									if ((opcode ^ 0xffffffff) == -110) {
										aBoolean6164 = false;
									} else if ((opcode ^ 0xffffffff) != -112) {
										if (opcode != 113) {
											if (opcode == 114) {
												aByte6168 = (byte) stream.readByte();
												aByte6152 = (byte) stream.readByte();
											} else if (opcode == 115) {
												stream.readUnsignedByte();
												stream.readUnsignedByte();
											} else if ((opcode ^ 0xffffffff) != -120) {
												if (opcode != 121) {
													if ((opcode ^ 0xffffffff) != -123) {
														if (opcode == 123) {
															anInt6143 = stream
																	.readUnsignedShort();
														} else if (opcode != 125) {
															if (opcode == 127) {
																renderEmote = stream
																		.readUnsignedShort();
															} else if ((opcode ^ 0xffffffff) == -129) {
																stream.readUnsignedByte();
															} else if (opcode != 134) {
																if (opcode == 135) {
																	anInt6145 = stream
																			.readUnsignedByte();
																	anInt6128 = stream
																			.readUnsignedShort();
																} else if (opcode != 136) {
																	if (opcode != 137) {
																		if (opcode != 138) {
																			if ((opcode ^ 0xffffffff) != -140) {
																				if (opcode == 140) {
																					anInt6179 = stream
																							.readUnsignedByte();
																				} else if (opcode == 141) {
																					aBoolean6173 = true;
																				} else if ((opcode ^ 0xffffffff) != -143) {
																					if (opcode == 143) {
																						aBoolean6155 = true;
																					} else if ((opcode ^ 0xffffffff) <= -151
																							&& opcode < 155) {
																						options[opcode - 150] = stream
																								.readString();
																						if (options[opcode - 150]
																								.equalsIgnoreCase("Hidden")) {
																							options[opcode
																							        + -150] = null;
																						}
																					} else if ((opcode ^ 0xffffffff) == -161) {
																						int i = stream
																								.readUnsignedByte();
																						anIntArray6131 = new int[i];
																						for (int i_58_ = 0; i > i_58_; i_58_++) {
																							anIntArray6131[i_58_] = stream
																									.readUnsignedShort();
																						}

																						// all
																						// added
																						// after
																						// here
																					} else if (opcode == 155) {
																						int aByte821 = stream
																								.readByte();
																						int aByte824 = stream
																								.readByte();
																						int aByte843 = stream
																								.readByte();
																						int aByte855 = stream
																								.readByte();
																					} else if (opcode == 158) {
																						byte aByte833 = (byte) 1;
																					} else if (opcode == 159) {
																						byte aByte833 = (byte) 0;
																					} else if (opcode == 162) { // added
																						// opcode
																						aBoolean6180 = true;
																					} else if (opcode == 163) { // added
																						// opcode
																						int anInt864 = stream
																								.readUnsignedByte();
																					} else if (opcode == 164) {
																						int anInt848 = stream
																								.readUnsignedShort();
																						int anInt837 = stream
																								.readUnsignedShort();
																					} else if (opcode == 165) {
																						int anInt847 = stream
																								.readUnsignedByte();
																					} else if (opcode == 168) {
																						int anInt828 = stream
																								.readUnsignedByte();
																					} else if (opcode >= 170 && opcode < 176) {
																						if (null == anIntArray2930) {
																							anIntArray2930 = new int[6];
																							Arrays.fill(anIntArray2930, -1);
																						}
																						int i_44_ = (short) stream.readUnsignedShort();
																						if (i_44_ == 65535) {
																							i_44_ = -1;
																						}
																						anIntArray2930[opcode - 170] = i_44_;
																					} else if (opcode == 249) {
																						int i = stream
																								.readUnsignedByte();
																						if (params == null) {
																							params = new HashMap<Integer, Object>(
																									i);
																						}
																						for (int i_60_ = 0; i > i_60_; i_60_++) {
																							boolean stringInstance = stream
																									.readUnsignedByte() == 1;
																							int key = stream.read24BitInt();
																							Object value;
																							if (stringInstance) {
																								value = stream.readString();
																							} else {
																								value = stream.readInt();
																							}
																							params.put(key, value);
																						}
																					}
																				} else {
																					anInt6186 = stream
																							.readUnsignedShort();
																				}
																			} else {
																				stream.readBigSmart();
																			}
																		} else {
																			anInt6178 = stream
																					.readBigSmart();
																		}
																	} else {
																		anInt6147 = stream
																				.readUnsignedShort();
																	}
																} else {
																	anInt837 = stream
																			.readUnsignedByte();
																	anInt889 = stream
																			.readUnsignedShort();
																}
															} else {
																anInt6151 = stream
																		.readUnsignedShort();
																if (anInt6151 == 65535) {
																	anInt6151 = -1;
																}
																anInt6174 = stream
																		.readUnsignedShort();
																if (anInt6174 == 65535) {
																	anInt6174 = -1;
																}
																anInt6169 = stream
																		.readUnsignedShort();
																if ((anInt6169 ^ 0xffffffff) == -65536) {
																	anInt6169 = -1;
																}
																anInt6176 = stream
																		.readUnsignedShort();
																if ((anInt6176 ^ 0xffffffff) == -65536) {
																	anInt6176 = -1;
																}
																anInt6140 = stream
																		.readUnsignedByte();
															}
														} else {
															respawnDirection = (byte) stream
																	.readByte();
														}
													} else {
														anInt6167 = stream
																.readBigSmart();
													}
												} else {
													anIntArrayArray6166 = new int[models.length][];
													int i = stream
															.readUnsignedByte();
													for (int i_62_ = 0; (i_62_ ^ 0xffffffff) > (i ^ 0xffffffff); i_62_++) {
														int i_63_ = stream
																.readUnsignedByte();
														int[] is = anIntArrayArray6166[i_63_] = new int[3];
														is[0] = stream
																.readByte();
														is[1] = stream
																.readByte();
														is[2] = stream
																.readByte();
													}
												}
											} else {
												walkMask = (byte) stream.readByte();
											}
										} else {
											aShort6191 = (short) stream
													.readUnsignedShort();
											aShort6153 = (short) stream
													.readUnsignedShort();
										}
									} else {
										aBoolean6165 = false;
									}
								} else {
									clickable = false;
								}
							} else {
								rotation = stream.readUnsignedShort();
							}
						} else {
							resizeX = stream.readUnsignedShort();
						}
					} else {
						int i = stream.readUnsignedByte();
						chatheads = new int[i];
						for (int i_64_ = 0; (i_64_ ^ 0xffffffff) > (i ^ 0xffffffff); i_64_++) {
							chatheads[i_64_] = stream.readBigSmart();
						}
					}
				} else {
					int i = stream.readUnsignedByte();
					retextureToFind = new short[i];
					retextureToReplace = new short[i];
					for (int i_65_ = 0; (i ^ 0xffffffff) < (i_65_ ^ 0xffffffff); i_65_++) {
						retextureToReplace[i_65_] = (short) stream
								.readUnsignedShort();
						retextureToFind[i_65_] = (short) stream
								.readUnsignedShort();
					}
				}
			} else {
				size = stream.readUnsignedByte();
			}
		} else {
			int i = stream.readUnsignedByte();
			models = new int[i];
			for (int i_66_ = 0; i_66_ < i; i_66_++) {
				models[i_66_] = stream.readBigSmart();
				if ((models[i_66_] ^ 0xffffffff) == -65536) {
					models[i_66_] = -1;
				}
			}
		}
	}

	public static final void clearNPCDefinitions() {
		npcDefinitions.clear();
	}

	public NPCDefinitions(int id) {
		this.id = id;
		anInt6174 = -1;
		varbit = -1;
		anInt837 = -1;
		anInt6143 = -1;
		rotation = 32;
		combatLevel = -1;
		anInt6167 = -1;
		name = "null";
		ambient = 0;
		walkMask = (byte) 0;
		anInt850 = 255;
		anInt6176 = -1;
		aBoolean6164 = true;
		aShort6191 = (short) 0;
		anInt6151 = -1;
		aByte6168 = (byte) -96;
		anInt6140 = 0;
		anInt6147 = -1;
		renderEmote = -1;
		respawnDirection = (byte) 7;
		aBoolean6165 = true;
		anInt6186 = -1;
		anInt6128 = -1;
		anInt6145 = -1;
		resizeX = 128;
		headIcon = -1;
		aBoolean6155 = false;
		varp = -1;
		aByte6152 = (byte) -16;
		renderPriority = false;
		invisible = true;
		anInt889 = -1;
		anInt6169 = -1;
		clickable = true;
		anInt6179 = -1;
		resizeY = 128;
		aShort6153 = (short) 0;
		options = new String[5];
		contrast = 0;
		anInt6178 = -1;
	}

	public boolean hasMarkOption() {
		for (String option : options) {
			if (option != null && option.equalsIgnoreCase("mark")) {
				return true;
			}
		}
		return false;
	}

	public boolean hasOption(String op) {
		for (String option : options) {
			if (option != null && option.equalsIgnoreCase(op)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasAttackOption() {
		for (String option : options) {
			if (option != null && (option.equalsIgnoreCase("attack") || option.equalsIgnoreCase("disturb"))) {
				return true;
			}
		}
		return false;
	}

	public String getName() {
		return name;
	}
	public int getId() {
		return id;
	}

}