package com.rs.game.world.entity.player.content.interfaces.teleport;

import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.Magic;

import java.io.Serializable;

/**
 * @author Paolo, Discord Shnek#6969
 * 22/01/2019
 */
public class TeleportInterface implements Serializable {


    /**
     * static data
     */
    public static final int INTERFACE_ID = 1369;
    private static final int MAIN_CONTAINER = 38;
    private static final int TEXT_BASE_ID =  42;
    private static final int BUTTON_BASE_ID =  39;
    private static final int BUTTON_AMOUNT =   21;

    /**
     * player data
     */
    private Player player;
    private TeleportLocation[] selectedPage = null;
    private TeleportLocation selectedLocation = null;

    /**
     * more correct than using a "setPlayer" method
     * @param p
     */
    public TeleportInterface(Player p){
        player = p;
    }


    /**
     * sends the interface to the player
     */
    public void sendInterface(){
         player.getInterfaceManager().sendInterface(INTERFACE_ID);
         hideButtons();
    }

    /**
     * hides every button
     */
    private void hideButtons(){
        for(int i = 1; i < BUTTON_AMOUNT; i++) {
            player.getPackets().sendHideIComponent(INTERFACE_ID, BUTTON_BASE_ID + i*4, true);
        }

    }

    /**
     * sends the buttons to the interface of the selected type
     * @param teleportType
     */
    public void sendTeleport(TeleportData teleportType){

        int buttonIndex =  BUTTON_BASE_ID;
        int textIndex = TEXT_BASE_ID;
        hideButtons();
        selectedPage = teleportType.getLocations();
        for(TeleportLocation loc : teleportType.getLocations()){
            player.getPackets().sendIComponentText(INTERFACE_ID,textIndex, loc.getName());
            player.getPackets().sendHideIComponent(INTERFACE_ID,buttonIndex , false);
            textIndex+=4;
            buttonIndex+=4;
        }
        
    }

    /**
     * button clicking
     * @param buttonId
     */
    public void handleButtons(int buttonId){
        if(buttonId == 27)
            sendTeleport(TeleportData.SKILLING);
        else if(buttonId == 29)
            sendTeleport(TeleportData.BOSSES);
        else if(buttonId == 31)
            sendTeleport(TeleportData.MONSTERS);
        else if(buttonId == 33)
            sendTeleport(TeleportData.MINIGAMES);
        else if(buttonId == 35)
            sendTeleport(TeleportData.WILDERNESS);
        else if(buttonId == 37)
            sendTeleport(TeleportData.SLAYER);
        if(buttonId >= 39 && buttonId <= 119){
                      selectedLocation = selectedPage[(buttonId - 39) / 4];
                      player.getPackets().sendIComponentText(INTERFACE_ID,126, "Teleport to "+selectedLocation.getName());
        }
        if(buttonId == 126){
            if(selectedLocation == null){
                player.sm("Please select an location first.");
                return;
            }
            teleport();
        }
    }

    /**
     * actual teleport
     */
    private void teleport() {
        Magic.sendNormalTeleportSpell(player, 1,0, selectedLocation.getTeleTile());
        if(selectedLocation.getController() != null) {
               player.getControlerManager().startControler(selectedLocation.getController());
        }
            
    }


}
