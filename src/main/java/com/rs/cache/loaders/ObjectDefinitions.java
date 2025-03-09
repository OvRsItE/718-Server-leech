package com.rs.cache.loaders;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.rs.Constants;
import com.rs.cache.Cache;
import com.rs.game.map.ObjectAction;
import com.rs.game.map.WorldObject;
import com.rs.network.io.InputStream;

@SuppressWarnings("unused")
public class ObjectDefinitions {

	private static final ConcurrentHashMap<Integer, ObjectDefinitions> objectDefinitions = new ConcurrentHashMap<Integer, ObjectDefinitions>();


	private short[] recolorToFind;
	int[] toObjectIds;
	static int anInt3832;
	int[] anIntArray3833 = null;
	private int anInt3834;
	int anInt3835;
	static int anInt3836;
	private byte aByte3837;
	int anInt3838 = -1;
	boolean aBoolean3839;
	private int contrast;
	private int anInt3841;
	static int anInt3842;
	static int anInt3843;
	int anInt3844;
	boolean aBoolean3845;
	static int anInt3846;
	private byte aByte3847;
	private byte aByte3849;
	int anInt3850;
	int anInt3851;
	public boolean secondBool;
	public boolean aBoolean3853;
	int anInt3855;
	public boolean notCliped;
	int anInt3857;
	private byte[] aByteArray3858;
	int[] anIntArray3859;
	int anInt3860;
	public String[] options;
	public int configFileId;
	private short[] recolorToReplace;
	int anInt3865;
	boolean aBoolean3866;
	boolean nonFlatShading;
	public boolean projectileCliped;
	private int[] anIntArray3869;
	boolean aBoolean3870;
	public int sizeY;
	boolean aBoolean3872;
	boolean aBoolean3873;
	public int aByte5363;
	private int anInt3875;
	public int objectAnimation;
	private int anInt3877;
	private int ambient;
	public int clipType;
	private int anInt3881;
	private int anInt3882;
	private int offsetX;
	Object loader;
	private int offsetHeight;
	public int sizeX;
	public boolean aBoolean3891;
	int anInt3892;
	public int optionType;
	boolean aBoolean3894;
	boolean aBoolean3895;
	int anInt3896;
	int configId;
	private byte[] modelTypes;
	int anInt3900;
	public String name;
	private int anInt3902;
	int anInt3904;
	int anInt3905;
	boolean aBoolean3906;
	int[] anIntArray3908;
	private byte aByte3912;
	int anInt3913;
	private byte aByte3914;
	private int offsetY;
	public int[][] modelIds;
	private int anInt3917;
	/**
	 * Object anim shit 1
	 */
	private short[] retextureToReplace;
	/**
	 * Object anim shit 2
	 */
	private short[] retextureToFind;
	int anInt3921;
	private HashMap<Integer, Object> parameters;
	boolean aBoolean3923;
	boolean aBoolean3924;
	int anInt3925;
	public int id;

	private int[] anIntArray4534;

	private byte[] unknownArray4;

	private byte[] unknownArray3;

	public ObjectAction[] actions;

	public String getFirstOption() {
		if (options == null || options.length < 1)
			return "";
		if (name.contains("XP Well")) {
			return "Add Cash";
		}
		return options[0];
	}

	public String getSecondOption() {
		if (options == null || options.length < 2)
			return "";
		if (name.contains("XP Well")) {
			return "Add Cash";
		}
		return options[1];
	}

	public String getOption(int option) {
		if (options == null || options.length < option || option == 0)
			return "";
		return options[option - 1];
	}

	public int getOption(String name) {
		if (options != null) {
			for (int i = 0; i < options.length; i++) {
				String option = options[i];
				if (option.equalsIgnoreCase(name))
					return i + 1;
			}
		}
		return -1;
	}

	public String getThirdOption() {
		if (options == null || options.length < 3)
			return "";
		return options[2];
	}

	public boolean containsOption(int i, String option) {
		if (options == null || options[i] == null || options.length <= i)
			return false;
		return options[i].equals(option);
	}

	public boolean containsOption(String o) {
		if (options == null)
			return false;
		for (String option : options) {
			if (option == null)
				continue;
			if (option.equalsIgnoreCase(o))
				return true;
		}
		return false;
	}
	
	void decodeOSRS(InputStream buffer, int opcode) {
		try {
			int length;
			if (opcode == 1) {
				length = buffer.readUnsignedByte();
				modelTypes = new byte[length];
				modelIds = new int[length][];
				for (int index = 0; index < length; index++) {
					modelIds[index] = new int[1];
					modelIds[index][0] = (buffer.readUnsignedShort() + Constants.OSRS_MODELS_OFFSET);
					modelTypes[index] = (byte) buffer.readUnsignedByte();
				}

			} else {
				int index;
				if (opcode == 5) {
					length = buffer.readUnsignedByte();
					int[] models = new int[length];
					for (index = 0; index < length; index++) {
						models[index] = (buffer.readUnsignedShort() + Constants.OSRS_MODELS_OFFSET);
					}
					modelTypes = new byte[23];
					modelIds = new int[23][];
					for (index = 0; index <= 22; index++) {
						modelTypes[index] = ((byte) index);
						modelIds[index] = models;
					}
				} else if (opcode == 2) {
					name = buffer.readString();
				} else if (opcode == 14) {
					sizeX = (buffer.readUnsignedByte());
				} else if (opcode == 15) {
					sizeY = (buffer.readUnsignedByte());
				} else if (opcode == 17) {
					clipType = 0;
					projectileCliped = false;
				} else if (opcode == 18) {
					projectileCliped = false;
				} else if (opcode == 19) {
					optionType = buffer.readUnsignedByte();
				} else if (opcode == 21) {
					aByte5363 = (byte) 1;
				} else if (opcode == 22) {
					nonFlatShading = true;
				} else if (opcode == 23) {
					int anInt5376 = 3;
				} else if (opcode == 24) {
					int count = buffer.readUnsignedShort();
					if (count != -1) {
						count += Constants.OSRS_SEQ_OFFSET;
						objectAnimation = count;
					}
				} else if (opcode == 27) {
					clipType = 1137740721;
				} else if (opcode == 28) {
					anInt3892 = ((buffer.readUnsignedByte() << 2));
				} else if (opcode == 29) {
					ambient = buffer.readByte();
				} else if (opcode == 39) {
					contrast = buffer.readByte() * 5;
				} else if ((opcode >= 30) && (opcode <= 35)) {
					options[opcode - 30] = buffer.readString();
					if (options[opcode - 30].equalsIgnoreCase("hidden")) {
						options[opcode - 30] = null;
					}
				} else {
					int m;
					if (opcode == 40) {
						length = buffer.readUnsignedByte();
						recolorToFind = new short[length];
						recolorToReplace = new short[length];
						for (int i_7_ = 0; i_7_ < length; i_7_++) {
							recolorToFind[i_7_] = (short) buffer.readUnsignedShort();
							recolorToReplace[i_7_] = (short) buffer.readUnsignedShort();
						}
					} else if (opcode == 41) {
						length = buffer.readUnsignedByte();
						retextureToFind = new short[length];
						retextureToReplace = new short[length];
						for (int i_9_ = 0; i_9_ < length; i_9_++) {
							retextureToFind[i_9_] = (short) buffer.readUnsignedShort();
							retextureToReplace[i_9_] = (short) buffer.readUnsignedShort();
						}
					} else if (opcode == 62) {
						aBoolean3839 = true;
					} else if (opcode == 64) {
						aBoolean3872 = false;
					} else if (opcode == 65) {
						anInt3902 = buffer.readUnsignedShort();
					} else if (opcode == 66) {
						anInt3841 = buffer.readUnsignedShort();
					} else if (opcode == 67) {
						anInt3917 = buffer.readUnsignedShort();
					} else if (opcode == 68) {
						length = buffer.readUnsignedShort();
//						mapSceneID = length;
//						if (length == 88) {
//							mapSceneID = 0;
//						}
					} else if (opcode == 69) {
						buffer.readUnsignedByte();
					} else if (opcode == 70) {
						offsetX = (buffer.readShort() << 2);
					} else if (opcode == 71) {
						offsetHeight = ((buffer.readShort() << 2));
					} else if (opcode == 72) {
						offsetY = ((buffer.readShort() << 2));
					} else if (opcode == 73) {
						secondBool = true;
					} else if (opcode == 74) {
						notCliped = true;
					} else if (opcode == 75) {
						anInt3855 = buffer.readUnsignedByte();
					} else if (opcode == 77 || opcode == 92) {
						configFileId = buffer.readUnsignedShort();
						configFileId = -1;
						configId = buffer.readUnsignedShort();
						configId = -1;
						length = -1;
						if (92 == opcode) {
							length = buffer.readUnsignedShort();
							if (length == 65565) {
								length = -1;
							} else {
								length += Constants.OSRS_OBJECTS_OFFSET;
							}
						}
						m = buffer.readUnsignedByte();
						toObjectIds = new int[2 + m];
						for (int model = 0; model <= m; model++) {
							toObjectIds[model] = buffer.readUnsignedShort();
							if (toObjectIds[model] == 65565) {
								toObjectIds[model] = -1;
							} else {
								toObjectIds[model] += Constants.OSRS_OBJECTS_OFFSET;
							}
						}
						toObjectIds[m + 1] = length;
					} else if (opcode == 78) {
						anInt3860 = buffer.readUnsignedShort();
						anInt3904 = buffer.readUnsignedByte();
					} else if (opcode == 79) {
						anInt3900 = buffer.readUnsignedShort();
						anInt3905 = buffer.readUnsignedShort();
						anInt3904 = buffer.readUnsignedByte();
						length = buffer.readUnsignedByte();
						anIntArray3859 = new int[length];
						for (int i_65_ = 0; i_65_ < length; i_65_++)
							anIntArray3859[i_65_] = buffer.readUnsignedShort();
					} else if (opcode == 81) {
						aByte5363 = (byte) 2;
						anInt3882 = buffer.readUnsignedByte();
					} else if (opcode == 82) {
						int mapIconId = buffer.readUnsignedShort();
					} else if (opcode == 249) {
						length = buffer.readUnsignedByte();
						if (parameters == null)
							parameters = new HashMap<Integer, Object>(length);
						for (index = 0; index < length; index++) {
							boolean isString = buffer.readUnsignedByte() == 1;
							int key = buffer.read24BitInt();
							if (!isString)
								parameters.put(key, buffer.readInt());
							else
								parameters.put(key, buffer.readString());
						}
					} else {
						System.out.println("Missing OSRS Object opcode: " + opcode);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readValues(InputStream stream, int opcode) {
		if (opcode != 1 && opcode != 5) {
			if (opcode != 2) {
				if (opcode != 14) {
					if (opcode != 15) {
						if (opcode == 17) { // nocliped
							projectileCliped = false;
							clipType = 0;
						} else if (opcode != 18) {
							if (opcode == 19)
								optionType = stream.readUnsignedByte();
							else if (opcode == 21)
								aByte3912 = (byte) 1;
							else if (opcode != 22) {
								if (opcode != 23) {
									if (opcode != 24) {
										if (opcode == 27) // cliped, no idea
											// diff between 2
											// and 1
											clipType = 1;
										else if (opcode == 28)
											anInt3892 = (stream.readUnsignedByte() << 2);
										else if (opcode != 29) {
											if (opcode != 39) {
												if (opcode < 30 || opcode >= 35) {
													if (opcode == 40) {
														int i_53_ = (stream.readUnsignedByte());
														recolorToFind = new short[i_53_];
														recolorToReplace = new short[i_53_];
														for (int i_54_ = 0; i_53_ > i_54_; i_54_++) {
															recolorToFind[i_54_] = (short) (stream
																	.readUnsignedShort());
															recolorToReplace[i_54_] = (short) (stream
																	.readUnsignedShort());
														}
													} else if (44 == opcode) {
														int i_86_ = (short) stream.readUnsignedShort();
														int i_87_ = 0;
														for (int i_88_ = i_86_; i_88_ > 0; i_88_ >>= 1)
															i_87_++;
														unknownArray3 = new byte[i_87_];
														byte i_89_ = 0;
														for (int i_90_ = 0; i_90_ < i_87_; i_90_++) {
															if ((i_86_ & 1 << i_90_) > 0) {
																unknownArray3[i_90_] = i_89_;
																i_89_++;
															} else
																unknownArray3[i_90_] = (byte) -1;
														}
													} else if (opcode == 45) {
														int i_91_ = (short) stream.readUnsignedShort();
														int i_92_ = 0;
														for (int i_93_ = i_91_; i_93_ > 0; i_93_ >>= 1)
															i_92_++;
														unknownArray4 = new byte[i_92_];
														byte i_94_ = 0;
														for (int i_95_ = 0; i_95_ < i_92_; i_95_++) {
															if ((i_91_ & 1 << i_95_) > 0) {
																unknownArray4[i_95_] = i_94_;
																i_94_++;
															} else
																unknownArray4[i_95_] = (byte) -1;
														}
													} else if (opcode != 41) { // object
														// anim
														if (opcode != 42) {
															if (opcode != 62) {
																if (opcode != 64) {
																	if (opcode == 65)
																		anInt3902 = stream.readUnsignedShort();
																	else if (opcode != 66) {
																		if (opcode != 67) {
																			if (opcode == 69)
																				anInt3925 = stream.readUnsignedByte();
																			else if (opcode != 70) {
																				if (opcode == 71)
																					offsetHeight = stream.readShort() << 2;
																				else if (opcode != 72) {
																					if (opcode == 73)
																						secondBool = true;
																					else if (opcode == 74)
																						notCliped = true;
																					else if (opcode != 75) {
																						if (opcode != 77
																								&& opcode != 92) {
																							if (opcode == 78) {
																								anInt3860 = stream
																										.readUnsignedShort();
																								anInt3904 = stream
																										.readUnsignedByte();
																							} else if (opcode != 79) {
																								if (opcode == 81) {
																									aByte3912 = (byte) 2;
																									anInt3882 = 256
																											* stream.readUnsignedByte();
																								} else if (opcode != 82) {
																									if (opcode == 88)
																										aBoolean3853 = false;
																									else if (opcode != 89) {
																										if (opcode == 90)
																											aBoolean3870 = true;
																										else if (opcode != 91) {
																											if (opcode != 93) {
																												if (opcode == 94)
																													aByte3912 = (byte) 4;
																												else if (opcode != 95) {
																													if (opcode != 96) {
																														if (opcode == 97)
																															aBoolean3866 = true;
																														else if (opcode == 98)
																															aBoolean3923 = true;
																														else if (opcode == 99) {
																															anInt3857 = stream
																																	.readUnsignedByte();
																															anInt3835 = stream
																																	.readUnsignedShort();
																														} else if (opcode == 100) {
																															anInt3844 = stream
																																	.readUnsignedByte();
																															anInt3913 = stream
																																	.readUnsignedShort();
																														} else if (opcode != 101) {
																															if (opcode == 102)
																																anInt3838 = stream
																																		.readUnsignedShort();
																															else if (opcode == 103)
																																aByte5363 = 0;
																															else if (opcode != 104) {
																																if (opcode == 105)
																																	aBoolean3906 = true;
																																else if (opcode == 106) {
																																	int i_55_ = stream
																																			.readUnsignedByte();
																																	anIntArray3869 = new int[i_55_];
																																	anIntArray3833 = new int[i_55_];
																																	for (int i_56_ = 0; i_56_ < i_55_; i_56_++) {
																																		anIntArray3833[i_56_] = id >= 100000
																																				? stream.readUnsignedShort()
																																				: stream.readBigSmart();
																																		int i_57_ = stream
																																				.readUnsignedByte();
																																		anIntArray3869[i_56_] = i_57_;
																																		anInt3881 += i_57_;
																																	}
																																} else if (opcode == 107)
																																	anInt3851 = stream
																																			.readUnsignedShort();
																																else if (opcode >= 150
																																		&& opcode < 155) {
																																	options[opcode
																																			+ -150] = stream
																																					.readString();
																																} else if (opcode != 160) {
																																	if (opcode == 162) {
																																		aByte3912 = (byte) 3;
																																		anInt3882 = stream
																																				.readInt();
																																	} else if (opcode == 163) {
																																		aByte3847 = (byte) stream
																																				.readByte();
																																		aByte3849 = (byte) stream
																																				.readByte();
																																		aByte3837 = (byte) stream
																																				.readByte();
																																		aByte3914 = (byte) stream
																																				.readByte();
																																	} else if (opcode != 164) {
																																		if (opcode != 165) {
																																			if (opcode != 166) {
																																				if (opcode == 167)
																																					anInt3921 = stream
																																							.readUnsignedShort();
																																				else if (opcode != 168) {
																																					if (opcode == 169) {
																																						aBoolean3845 = true;
																																						// added
																																						// opcode
																																					} else if (opcode == 170) {
																																						int anInt3383 = stream
																																								.readUnsignedSmart();
																																						// added
																																						// opcode
																																					} else if (opcode == 171) {
																																						int anInt3362 = stream
																																								.readUnsignedSmart();
																																						// added
																																						// opcode
																																					} else if (opcode == 173) {
																																						int anInt3302 = stream
																																								.readUnsignedShort();
																																						int anInt3336 = stream
																																								.readUnsignedShort();
																																						// added
																																						// opcode
																																					} else if (opcode == 177) {
																																						boolean ub = true;
																																						// added
																																						// opcode
																																					} else if (opcode == 178) {
																																						int db = stream
																																								.readUnsignedByte();
																																					} else if (opcode == 189) {
																																						boolean bloom = true;
																																					} else if (opcode >= 190
																																							&& opcode < 196) {
																																						if (anIntArray4534 == null) {
																																							anIntArray4534 = new int[6];
																																							Arrays.fill(
																																									anIntArray4534,
																																									-1);
																																						}
																																						anIntArray4534[opcode
																																								- 190] = stream
																																										.readUnsignedShort();
																																					} else if (opcode == 249) {
																																						int length = stream
																																								.readUnsignedByte();
																																						if (parameters == null)
																																							parameters = new HashMap<Integer, Object>(
																																									length);
																																						for (int i_60_ = 0; i_60_ < length; i_60_++) {
																																							boolean bool = stream
																																									.readUnsignedByte() == 1;
																																							int i_61_ = stream
																																									.read24BitInt();
																																							if (!bool)
																																								parameters
																																										.put(i_61_,
																																												stream.readInt());
																																							else
																																								parameters
																																										.put(i_61_,
																																												stream.readString());

																																						}
																																					}
																																				} else
																																					aBoolean3894 = true;
																																			} else
																																				anInt3877 = stream
																																						.readShort();
																																		} else
																																			anInt3875 = stream
																																					.readShort();
																																	} else
																																		anInt3834 = stream
																																				.readShort();
																																} else {
																																	int i_62_ = stream
																																			.readUnsignedByte();
																																	anIntArray3908 = new int[i_62_];
																																	for (int i_63_ = 0; i_62_ > i_63_; i_63_++)
																																		anIntArray3908[i_63_] = stream
																																				.readUnsignedShort();
																																}
																															} else
																																anInt3865 = stream
																																		.readUnsignedByte();
																														} else
																															anInt3850 = stream
																																	.readUnsignedByte();
																													} else
																														aBoolean3924 = true;
																												} else {
																													aByte3912 = (byte) 5;
																													anInt3882 = stream
																															.readShort();
																												}
																											} else {
																												aByte3912 = (byte) 3;
																												anInt3882 = stream
																														.readUnsignedShort();
																											}
																										} else
																											aBoolean3873 = true;
																									} else
																										aBoolean3895 = false;
																								} else
																									aBoolean3891 = true;
																							} else {
																								anInt3900 = stream
																										.readUnsignedShort();
																								anInt3905 = stream
																										.readUnsignedShort();
																								anInt3904 = stream
																										.readUnsignedByte();
																								int i_64_ = stream
																										.readUnsignedByte();
																								anIntArray3859 = new int[i_64_];
																								for (int i_65_ = 0; i_65_ < i_64_; i_65_++)
																									anIntArray3859[i_65_] = stream
																											.readUnsignedShort();
																							}
																						} else {
																							configFileId = stream
																									.readUnsignedShort();
																							if (configFileId == 65535)
																								configFileId = -1;
																							configId = stream
																									.readUnsignedShort();
																							if (configId == 65535)
																								configId = -1;
																							int i_66_ = -1;
																							if (opcode == 92) {
																								i_66_ = id >= 100000
																										? stream.readUnsignedShort()
																										: stream.readBigSmart();
																							}
																							int i_67_ = stream
																									.readUnsignedByte();
																							toObjectIds = new int[i_67_
																									- -2];
																							for (int i_68_ = 0; i_67_ >= i_68_; i_68_++) {
																								toObjectIds[i_68_] = id >= 100000
																										? stream.readUnsignedShort()
																										: stream.readBigSmart();
																							}
																							toObjectIds[i_67_
																									+ 1] = i_66_;
																						}
																					} else
																						anInt3855 = stream
																								.readUnsignedByte();
																				} else
																					offsetY = stream.readShort() << 2;
																			} else
																				offsetX = stream.readShort() << 2;
																		} else
																			anInt3917 = stream.readUnsignedShort();
																	} else
																		anInt3841 = stream.readUnsignedShort();
																} else
																	// 64
																	aBoolean3872 = false;
															} else
																aBoolean3839 = true;
														} else {
															int i_69_ = (stream.readUnsignedByte());
															aByteArray3858 = (new byte[i_69_]);
															for (int i_70_ = 0; i_70_ < i_69_; i_70_++)
																aByteArray3858[i_70_] = (byte) (stream.readByte());
														}
													} else { // object anim?
														int i_71_ = (stream.readUnsignedByte());
														retextureToFind = new short[i_71_];
														retextureToReplace = new short[i_71_];
														for (int i_72_ = 0; i_71_ > i_72_; i_72_++) {
															retextureToFind[i_72_] = (short) (stream
																	.readUnsignedShort());
															retextureToReplace[i_72_] = (short) (stream
																	.readUnsignedShort());
														}
													}
												} else {
													options[-30 + opcode] = (stream.readString());
												}
											} else
												// 39
												contrast = (stream.readByte() * 5);
										} else {// 29
											ambient = stream.readByte();
										}
									} else {
										objectAnimation = id >= 100000 ? stream.readUnsignedShort()
												: stream.readBigSmart();
									}
								} else
									aByte5363 = 1;
							} else
								nonFlatShading = true;
						} else
							projectileCliped = false;
					} else
						// 15
						sizeY = stream.readUnsignedByte();
				} else
					// 14
					sizeX = stream.readUnsignedByte();
			} else {
				name = stream.readString();
			}
		} else {
			int length = stream.readUnsignedByte();
			modelIds = new int[length][];
			modelTypes = new byte[length];
			for (int index = 0; index < length; index++) {
				modelTypes[index] = (byte) stream.readByte();
				int i_75_ = stream.readUnsignedByte();
				modelIds[index] = new int[i_75_];
				for (int i_76_ = 0; i_75_ > i_76_; i_76_++)
					modelIds[index][i_76_] = id >= 100000 ? stream.readUnsignedShort() : stream.readBigSmart();
			}
			if (opcode == 5)
			 skipReadModelIds(stream);
		}
	}
	private void skipReadModelIds(InputStream stream) {
		int length = stream.readUnsignedByte();
		for (int index = 0; index < length; index++) {
			stream.skip(1);
			int length2 = stream.readUnsignedByte();
			for (int i = 0; i < length2; i++)
				stream.readBigSmart();
		}
	}
	
	private void readValueLoop(InputStream stream) {
		for (;;) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				break;
			if (id >= Constants.OSRS_OBJECTS_OFFSET)
				decodeOSRS(stream, opcode);
			else 
				readValues(stream, opcode);
		}
	}

	private ObjectDefinitions() {
		anInt3835 = -1;
		anInt3860 = -1;
		configFileId = -1;
		aBoolean3866 = false;
		anInt3851 = -1;
		anInt3865 = 255;
		aBoolean3845 = false;
		nonFlatShading = false;
		anInt3850 = 0;
		anInt3844 = -1;
		anInt3881 = 0;
		anInt3857 = -1;
		aBoolean3872 = true;
		anInt3882 = -1;
		anInt3834 = 0;
		options = new String[5];
		anInt3875 = 0;
		aBoolean3839 = false;
		anIntArray3869 = null;
		sizeY = 1;
		aByte5363 = -1;
		offsetX = 0;
		aBoolean3895 = true;
		contrast = 0;
		aBoolean3870 = false;
		offsetHeight = 0;
		aBoolean3853 = true;
		secondBool = false;
		clipType = 2;
		projectileCliped = true;
		notCliped = false;
		anInt3855 = -1;
		ambient = 0;
		anInt3904 = 0;
		sizeX = 1;
		objectAnimation = -1;
		aBoolean3891 = false;
		anInt3905 = 0;
		name = "null";
		anInt3913 = -1;
		aBoolean3906 = false;
		aBoolean3873 = false;
		aByte3914 = (byte) 0;
		offsetY = 0;
		anInt3900 = 0;
		optionType = -1;
		aBoolean3894 = false;
		aByte3912 = (byte) 0;
		anInt3921 = 0;
		anInt3902 = 128;
		configId = -1;
		anInt3877 = 0;
		anInt3925 = 0;
		anInt3892 = 64;
		aBoolean3923 = false;
		aBoolean3924 = false;
		anInt3841 = 128;
		anInt3917 = 128;
	}

	final void method3287() {
		if (optionType == -1) {
			optionType = 0;
			if (modelTypes != null && modelTypes.length == 1 && modelTypes[0] == 10)
				optionType = 1;
			for (int i_13_ = 0; i_13_ < 5; i_13_++) {
				if (options[i_13_] != null) {
					optionType = 1;
					break;
				}
			}
		}
		if (anInt3855 == -1)
			anInt3855 = clipType != 0 ? 1 : 0;
	}

	private static int getArchiveId(int i_0_) {
		return i_0_ >>> -1135990488;
	}

	private static final ObjectDefinitions HotDog = getObjectDefinitions(70197);

	private static final int[] underwater = { 12500,
			12479,
			12513,
			12502,
			12510,
			12483,
			12491,
			12493,
			12490,
			12482,
			12520,
			12492,
			12501,
			12521,
			12516,
			12503,
			12522,
			12489,
			12509,
			12480,
			12524,
			12523,
			12511,
			12481,
			12514,
			12512,
			12505,
			12486,
			12526,
			12499,
			12527,
			12494,
			12506 };

	public static ObjectDefinitions getObjectDefinitions(int id) {
		ObjectDefinitions def = objectDefinitions.get(id);
		if (def == null) {
			def = new ObjectDefinitions();
			def.id = id;
			byte[] data = Cache.STORE.getIndexes()[16].getFile(getArchiveId(id), id & 0xff);
			if (data == null) {
				// System.out.println("Failed loading Object " + id + ".");
			} else
				def.readValueLoop(new InputStream(data));
			def.method3287();
			if (underwater == null)
				return def;
			for (int pie : underwater) {
				if (pie == id) {
					def.modelIds = new int[][] {{665735}};
					def.clipType = 0;
					def.name = "water";
				}
			}
			if (id == 14233 || id == 14235) {
				def.modelIds = new int[][] {{665735}};
				def.clipType = 0;
				def.options = null;
				def.name = "gate";
			}
			if (def.name.equalsIgnoreCase("bank booth") || def.name.equalsIgnoreCase("counter")) {
				def.notCliped = false;
				def.projectileCliped = true;
				if (def.clipType == 0)
					def.clipType = 1;
			}
			if (def.notCliped) {
				def.projectileCliped = false;
				def.clipType = 0;
			}
			objectDefinitions.put(id, def);
		}
		return def;
	}

	public int getClipType() {
		return clipType;
	}

	public boolean isProjectileCliped() {
		return projectileCliped;
	}

	public int getSizeX() {
		return sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

	public static void clearObjectDefinitions() {
		objectDefinitions.clear();
	}

	/**
	 * Prints all fields in this class.
	 */
	public void printFields() {
		for (Field field : getClass().getDeclaredFields()) {
			if ((field.getModifiers() & 8) != 0) {
				continue;
			}
			try {
				System.out.println(field.getName() + ": " + getValue(field));
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		System.out.println("-- end of " + getClass().getSimpleName() + " fields --");
	}

	private Object getValue(Field field) throws Throwable {
		field.setAccessible(true);
		Class<?> type = field.getType();
		if (type == int[][].class) {
			return Arrays.toString((int[][]) field.get(this));
		} else if (type == int[].class) {
			return Arrays.toString((int[]) field.get(this));
		} else if (type == byte[].class) {
			return Arrays.toString((byte[]) field.get(this));
		} else if (type == short[].class) {
			return Arrays.toString((short[]) field.get(this));
		} else if (type == double[].class) {
			return Arrays.toString((double[]) field.get(this));
		} else if (type == float[].class) {
			return Arrays.toString((float[]) field.get(this));
		} else if (type == Object[].class) {
			return Arrays.toString((Object[]) field.get(this));
		}
		return field.get(this);
	}

	public int getAccessBlockFlag() {
		return anInt3925;
	}

	public String getName() {
		return name;
	}

}