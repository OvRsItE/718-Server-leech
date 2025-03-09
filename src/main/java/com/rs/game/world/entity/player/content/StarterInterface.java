package com.rs.game.world.entity.player.content;

import com.rs.Constants;
import com.rs.game.item.Item;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.BuffManager;
import com.rs.game.world.entity.player.GameMode;
import com.rs.game.world.entity.player.Player;
import com.rs.utility.Utils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StarterInterface {

    public static final int INTERFACE_ID = 3005;
    private static final int CONTAINER_ID = 89;

    private final Player player;
    private GameMode mode;

    public void openInterface() {
        player.getInterfaceManager().sendInterface(INTERFACE_ID);
        player.getPackets().sendIComponentText(INTERFACE_ID, 50, "Welcome to Venomite.");
        player.getPackets().sendIComponentText(INTERFACE_ID, 54, "Select your game mode to continue.");

        for (int i = 0; i < GameMode.MODES.length; i++) {
            player.getPackets().sendIComponentText(INTERFACE_ID, i + 27, GameMode.MODES[i].getName());
        }
        showModeInfo(player.getGameMode());
    }

    public void handleClick(int component) {
        if (component == 47) //the item container click
            return;
        if (component != 45) {
            GameMode selected = GameMode.MODES[component - 33];
            mode = selected;
            showModeInfo(selected);
            player.getPackets().sendHideIComponent(INTERFACE_ID, component, true);
            player.getPackets().sendIComponentText(INTERFACE_ID, 54, "You've selected: " + selected.getName() + "<br>This cannot be changed after you click confirm!");
        } else {
            //confirm & start customization
            if (mode != null) {
                player.setGameMode(mode);
                player.getInterfaceManager().closeScreenInterface();
                if (!player.finishedStarter) {
                    for (Item item : player.getGameMode().getItems()) {
                        player.getInventory().addItem(item);
                    }
                }
                player.getGameMode().getAction().accept(player);
                player.finishedStarter = true;
                PlayerLook.openMageMakeOver(player);
                player.getBuffManager().applyBuff(new BuffManager.Buff(BuffManager.BuffType.XP_BOOST, 3000, true));
                World.sendWorldMessage("<col=FF0000>Welcome " +Utils.formatPlayerNameForDisplay(player.getUsername())+ " has just joined " + Constants.SERVER_NAME + " for the first time.</col>", false);
            } else {
                player.sendMessage("You can't progress until you've picked a game mode!");
            }
        }
    }

    private void showModeInfo(GameMode mode) {
        for (int i = 33; i < 39; i++) {
            player.getPackets().sendHideIComponent(INTERFACE_ID, i, false);
        }
        player.getPackets().sendHideIComponent(INTERFACE_ID, 38, true);
        player.getPackets().sendHideIComponent(INTERFACE_ID, 51, true);
        player.getPackets().sendHideIComponent(INTERFACE_ID, 24, true);
        player.getPackets().sendHideIComponent(INTERFACE_ID, 32, true);
        player.getPackets().sendIComponentText(INTERFACE_ID, 42, mode.getDescription());
        player.getPackets().sendItems(CONTAINER_ID, mode.getItems());
        player.getPackets().sendInterSetItemsOptionsScript(INTERFACE_ID, 47, CONTAINER_ID, 7, 7, "Examine");
        player.getPackets().sendUnlockIComponentOptionSlots(INTERFACE_ID, 47, 0, 160, 0);
    }

}
