package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.item.Item;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.controller.impl.Introduction;

/**
 * The dialogue when you click the ladder.
 * Created by Arham 4 on 3/15/14.
 */
public class RomanGuard extends Dialogue {
    private int npcId;
    private Introduction controler;
    @Override
    public void start() {
        npcId = (int) parameters[0];
        controler = (Introduction) parameters[1];
        sendNPCDialogue(npcId, 9753, "You're alive?");
    }

    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case -1:
                sendPlayerDialogue(9827, "What happened?");
                stage = 0;
                break;
            case 0:
                sendNPCDialogue(npcId, 9827, "We won the war! Do you not remember anything?");
                stage = 1;
                break;
            case 1:
                sendPlayerDialogue(9827, "War? What war?");
                stage = 2;
                break;
            case 2:
                sendNPCDialogue(npcId, 9830, "I take it you don't remember anything.");
                stage = 3;
                break;
            case 3:
                sendNPCDialogue(npcId, 9830, "We Romans have been at war with the barbarians for over a century now. Our final push was 2 nights ago. Many Romans didn't make it out alive. It looks like you managed to teleport here, Varrock, just before your death.");
                stage = 4;
                break;
            case 4:
                sendNPCDialogue(npcId, 9830, "Our finest medics were able to patch up your wounds before they got the best of you.");
                stage = 5;
                break;
            case 5:
                sendPlayerDialogue(9830, "That explains the weakness.");
                stage = 6;
                break;
            case 6:
                sendNPCDialogue(npcId, 9830, "Since you don't remember anything, we will need to train you from the beginning again.");
                stage = 7;
                break;
            case 7:
                sendNPCDialogue(npcId, 9830, "This is our home. There are many things to do here, such as buy and sell items in shops. Meet up and trade with some of the locals in the center of town. Maybe even talk with skill masters and learn how to become a master yourself.");
                stage = 8;
                controler.setStage(3);
                controler.doStageAction();
                break;
            case 8:
                controler.setStage(4);
                controler.doStageAction();
                controler.doStageAction(); // twice to confirm the facing.
                player.getPackets().sendBlackOut(0);
                sendNPCDialogue(npcId, 9830, "I would suggest going to your bank first. You may have some items from before you passed out.");
                stage = 9;
                break;
            case 9:
                sendPlayerDialogue(9827, "Where is the bank again?");
                stage = 10;
                break;
            case 10:
                sendNPCDialogue(npcId, 9830, "There are two banks here, one to the east and one to the west. Follow either path and you won't miss it.");
                stage = 11;
                break;
            case 11:
                sendNPCDialogue(npcId, 9830, "Also, since you seem to have forgot alot, please, take this book. Read up on what to do.");
                player.getInventory().addItem(new Item(757));
                stage = 12;
                break;
            case 12:
                player.getCutscenesManager().logout();
                sendPlayerDialogue(9850, "Okay, thanks!");
                stage = 13;
                break;
            case 13:
                sendDialogue("The world shifts and you go from one dimension to the other. You are now in the real world.");
                stage = -2;
                controler.setStage(5);
                controler.doStageAction();
                break;
            default:
                end();
                player.getControlerManager().startControler(null);
                break;
        }
    }

    @Override
    public void finish() {

    }
}
