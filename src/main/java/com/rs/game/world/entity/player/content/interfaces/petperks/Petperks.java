package com.rs.game.world.entity.player.content.interfaces.petperks;

import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.settings.Settings;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
public class Petperks implements Serializable {
    private static final long serialVersionUID = 8784097306466321446L;
    public static final int INTERFACE_ID = 3204;
    private final Player player;



    private static final int[] COMPONENTS_TO_HIDE = {
           28, 29, 30, 31
    };
    private static final int[] TEXT_COMPONENTS = {
            36,37,38,39
    };
    private static final int[] BUTTON_COMPONENTS = {
            22, 26, 30, 34, 38, 42, 46, 50, 54, 59, 63, 67, 71, 75, 79, 83
    };

    public void open() {
        player.getInterfaceManager().sendInterface(INTERFACE_ID);
        if (player.isOwner()) {
            player.getPackets().sendItemOnIComponent(INTERFACE_ID, 28, 28607, 1);
            player.getPackets().sendItemOnIComponent(INTERFACE_ID, 29, 28607, 1);
            player.getPackets().sendItemOnIComponent(INTERFACE_ID, 30, 28607, 1);
            player.getPackets().sendItemOnIComponent(INTERFACE_ID, 31, 28607, 1);
            player.getPackets().sendIComponentText(INTERFACE_ID, 36, "15%");
            player.getPackets().sendIComponentText(INTERFACE_ID, 37, "10%");
            player.getPackets().sendIComponentText(INTERFACE_ID, 38, "10%");
            player.getPackets().sendIComponentText(INTERFACE_ID, 39, "15%");
        }


    }


}
