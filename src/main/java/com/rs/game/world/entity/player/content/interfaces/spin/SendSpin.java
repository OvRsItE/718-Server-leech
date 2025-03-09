package com.rs.game.world.entity.player.content.interfaces.spin;


import com.rs.game.world.entity.player.Player;

import java.util.TimerTask;

public class SendSpin extends TimerTask {
    int x;
    int y;
    int ticks = 0;
    Player player;
    int finalIndex;
    int[] spinItemComponents;

    public SendSpin(int x, int y, Player player, int finalIndex, int[] spinItemComponents) {
        this.x = x;
        this.y = y;
        this.player = player;
        this.finalIndex = finalIndex;
        this.spinItemComponents = spinItemComponents;
    }

    @Override
    public void run() {
        if(player.getTemporaryAttributtes().get("mbox_stop") == Boolean.TRUE) {
            cancel();
            return;
        }
        int subtract = 15;
        subtract = subtract - (1 * ((int) ticks / 10));
        if(subtract <= 1)
            subtract = 1;
        x -= subtract;
        //itx -= subtract;
        if(x <= -(38 * 20)) {
            x = 380;
        }
        /*if(itx <= -(38 * 20)) {
            itx = 380;
        }*/
        player.getPackets().sendMoveIComponent(1161, spinItemComponents[finalIndex], x, y);

        //player.getPackets().sendMoveIComponent(1161, 71 + finalIndex, itx, ity);
        ticks++;
        if(ticks == (375)) { //375
            player.getTemporaryAttributtes().put("mbox_stop", Boolean.TRUE);
        }
        // ticks++;
    }
}
