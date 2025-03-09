package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.FadingScreen;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.controller.impl.Introduction;

/**
 * The nurse's dialogue. Part of the Rome introduction.
 * Created by Arham 4 on 3/15/14.
 */
public class Nurse extends Dialogue {
    private int npcId;
    private Introduction controler;
    @Override
    public void start() {
        npcId = (int) parameters[0];
        controler = (Introduction) parameters[1];
        if (parameters.length == 3)
            stage = (byte) parameters[2];
        player.lock();
        sendNPCDialogue(npcId, 9753, "It can't be!");
    }

    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case -1:
                sendNPCDialogue(npcId, 9843, "Hello?! Roman! Wake up!");
                stage = 0;
                break;
            case 0:
                String gender = player.getAppearence().isMale() ? "He" : "She";
                sendNPCDialogue(npcId, 9814, "Guards! Help this Roman immediately! " + gender + " is in critical condition, we don't have much time!");
                stage = 1;
                break;
            case 1:
                sendDialogue("The guards come in. Medics surround you.");
                stage = 2;
                break;
            case 2:
                sendNPCDialogue(npcId, 9814, "Patch up the wounds! Quick!");
                stage = 3;
                break;
            case 3:
                sendDialogue("You suddenly become unconscious.");
                stage = 4;
                break;
            case 4:
                FadingScreen.unfade(player, new Runnable() {
                    @Override
                    public void run() {
                        player.lock();
                    }
                });
                sendDialogue("You start to regain consciousness.");
                stage = 5;
                break;
            case 5:
                player.getInterfaceManager().closeScreenInterface();
                player.getPackets().sendBlackOut(0);
                player.unlock();
                controler.setStage(1);
                player.getAppearence().setRenderEmote(1999);
                sendPlayerDialogue(9827, "What happened?");
                stage = 6;
                break;
            case 6:
                sendPlayerDialogue(9827, "Where am I?");
                stage = 7;
                break;
            case 7:
                sendPlayerDialogue(9830, "Maybe there is something here that can help me.");
                stage = 8;
                break;
            case 8:
                sendDialogue("<col=0000ff>Maybe there is something here that can help you.</col>", "Try clicking the crates and see what you can find.");
                stage = -2;
                break;
            default:
                end();
                break;
        }
    }

    @Override
    public void finish() {

    }
}
