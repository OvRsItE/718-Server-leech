package com.rs.game.world.entity.player.content.interfaces.settings;

import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.settings.Settings;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class SettingsInterface implements Serializable {
    private static final long serialVersionUID = 8784097306466321446L;
    public static final int INTERFACE_ID = 3203;
    private final Player player;


    private static final int[] COMPONENTS_TO_HIDE = {
            19,23,27,31,35,39,43,47,51,55,59,63,67,71,75
    };
    private static final int[] TEXT_COMPONENTS = {
            21, 25, 29, 33, 37, 41, 45, 49, 53, 58, 62, 66, 70, 74, 78
    };
    private static final int[] BUTTON_COMPONENTS = {
            22, 26, 30, 34, 38, 42, 46, 50, 54, 59, 63, 67, 71, 75, 79, 83
    };

    public void open() {
        player.getInterfaceManager().sendInterface(INTERFACE_ID);
        for (int index = 5; index < COMPONENTS_TO_HIDE.length; index++) {
            player.getPackets().sendHideIComponent(INTERFACE_ID, COMPONENTS_TO_HIDE[index], true);
        }
        player.getPackets().sendIComponentText(INTERFACE_ID, 21, "Ground Items");
        player.getPackets().sendIComponentText(INTERFACE_ID, 25, "Npc Name Hover");
        player.getPackets().sendIComponentText(INTERFACE_ID, 29, "10x Hits");
        player.getPackets().sendIComponentText(INTERFACE_ID, 33, "Player Name Hover");
        player.getPackets().sendIComponentText(INTERFACE_ID, 37, "Draw Distance Increase");


        if (player.getSettingsManager().getSettings().get(Settings.GROUND_ITEMS) != null && player.getSettingsManager().getSettings().get(Settings.GROUND_ITEMS)) {
            player.getPackets().sendIComponentSprite(INTERFACE_ID, 22, 3746);
        } else {
            player.getSettingsManager().update(Settings.GROUND_ITEMS, false);
        }

        if (player.getSettingsManager().getSettings().get(Settings.NPC_NAMES) != null && player.getSettingsManager().getSettings().get(Settings.NPC_NAMES)) {
            player.getPackets().sendIComponentSprite(INTERFACE_ID, 26, 3746);
        }  else {
            player.getSettingsManager().update(Settings.NPC_NAMES, false);
        }
        if (player.getSettingsManager().getSettings().get(Settings.OLD_HITS) != null && player.getSettingsManager().getSettings().get(Settings.OLD_HITS)) {
            player.getPackets().sendIComponentSprite(INTERFACE_ID, 30, 3746);
        } else {
            player.getSettingsManager().update(Settings.OLD_HITS, false);
        }

        if (player.getSettingsManager().getSettings().get(Settings.PLAYER_NAMES) != null && player.getSettingsManager().getSettings().get(Settings.PLAYER_NAMES)) {
            player.getPackets().sendIComponentSprite(INTERFACE_ID, 34, 3746);
        }  else {
            player.getSettingsManager().update(Settings.PLAYER_NAMES, false);
        }

        if (player.getSettingsManager().getSettings().get(Settings.DRAW_DISTANCE) != null && player.getSettingsManager().getSettings().get(Settings.DRAW_DISTANCE)) {
            player.getPackets().sendIComponentSprite(INTERFACE_ID, 38, 3746);
        } else {
            player.getSettingsManager().update(Settings.DRAW_DISTANCE, false);
        }

    }

    public boolean handleButtonClick(int interfaceId, int buttonId) {

        if (interfaceId != INTERFACE_ID)
            return false;


        if (buttonId == 22) {
            if (player.getSettingsManager().getSettings().get(Settings.GROUND_ITEMS) != null) {
                if (!player.getSettingsManager().getSettings().get(Settings.GROUND_ITEMS)) {
                    player.getSettingsManager().update(Settings.GROUND_ITEMS, true);
                    player.getPackets().sendIComponentSprite(INTERFACE_ID, 22, 3746);
                } else if (player.getSettingsManager().getSettings().get(Settings.GROUND_ITEMS)) {
                    player.getSettingsManager().update(Settings.GROUND_ITEMS, false);
                    player.getPackets().sendIComponentSprite(INTERFACE_ID, 22, 3745);
                }

            }

        }
        if (buttonId == 26) {
            if (player.getSettingsManager().getSettings().get(Settings.NPC_NAMES) != null) {
                if (!player.getSettingsManager().getSettings().get(Settings.NPC_NAMES)) {
                    player.getSettingsManager().update(Settings.NPC_NAMES, true);
                    player.getPackets().sendIComponentSprite(INTERFACE_ID, 26, 3746);
                } else if (player.getSettingsManager().getSettings().get(Settings.NPC_NAMES)) {
                    player.getSettingsManager().update(Settings.NPC_NAMES, false);
                    player.getPackets().sendIComponentSprite(INTERFACE_ID, 26, 3745);
                }
            }
        }


        if (buttonId == 30) {
            if (player.getSettingsManager().getSettings().get(Settings.OLD_HITS) != null) {
                if (!player.getSettingsManager().getSettings().get(Settings.OLD_HITS)) {
                    player.getSettingsManager().update(Settings.OLD_HITS, true);
                    player.getPackets().sendIComponentSprite(INTERFACE_ID, 30, 3746);
                } else if (player.getSettingsManager().getSettings().get(Settings.OLD_HITS)) {
                    player.getSettingsManager().update(Settings.OLD_HITS, false);
                    player.getPackets().sendIComponentSprite(INTERFACE_ID, 30, 3745);
                }
            }
        }
        if (buttonId == 34) {
            if (player.getSettingsManager().getSettings().get(Settings.PLAYER_NAMES) != null) {
                if (!player.getSettingsManager().getSettings().get(Settings.PLAYER_NAMES)) {
                    player.getSettingsManager().update(Settings.PLAYER_NAMES, true);
                    player.getPackets().sendIComponentSprite(INTERFACE_ID, 34, 3746);
                } else if (player.getSettingsManager().getSettings().get(Settings.PLAYER_NAMES)) {
                    player.getSettingsManager().update(Settings.PLAYER_NAMES, false);
                    player.getPackets().sendIComponentSprite(INTERFACE_ID, 34, 3745);
                }
            }
        }
        if (buttonId == 38) {
            if (player.getSettingsManager().getSettings().get(Settings.DRAW_DISTANCE) != null) {
                if (!player.getSettingsManager().getSettings().get(Settings.DRAW_DISTANCE)) {
                    player.getSettingsManager().update(Settings.DRAW_DISTANCE, true);
                    player.getPackets().sendIComponentSprite(INTERFACE_ID, 38, 3746);
                } else if (player.getSettingsManager().getSettings().get(Settings.DRAW_DISTANCE)) {
                    player.getSettingsManager().update(Settings.DRAW_DISTANCE, false);
                    player.getPackets().sendIComponentSprite(INTERFACE_ID, 38, 3745);
                }
            }
        }


        return true;
    }

}
