package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.Utils;

/**
 * The Weird Old Man's dialogue, just for fun.
 *
 * @author Arham Siddiqui
 */
public class WeirdOldMan extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (int) parameters[0];
        sendEntityDialogue(SEND_2_TEXT_CHAT, new String[]{player.getDisplayName(), "Hello."}, IS_PLAYER, npcId, 9850);
    }

    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case -1:
                stage = 0;
                switch (Utils.random(4)) {
                    case 0:
                        player.getPackets().sendVoice(11001);
                        sendEntityDialogue(SEND_2_TEXT_CHAT, new String[]{NPCDefinitions.getNPCDefinitions(npcId).name, "Nobody is exactly sure. All that was found were their boots covered in saliva and bees..."}, IS_NPC, npcId, 9775);
                        break;
                    case 1:
                        player.getPackets().sendVoice(11015);
                        sendEntityDialogue(SEND_2_TEXT_CHAT, new String[]{NPCDefinitions.getNPCDefinitions(npcId).name, "Ugh Saradomin. That thing stinks. Just looking at it makes me wanna vomit..."}, IS_NPC, npcId, 9785);
                        break;
                    case 2:
                        player.getPackets().sendVoice(11014);
                        sendEntityDialogue(SEND_2_TEXT_CHAT, new String[]{NPCDefinitions.getNPCDefinitions(npcId).name, "Always make sure you have plenty of food to heal your wounds! I hear there is good farmland, fishing, and hunting to the south."}, IS_NPC, npcId, 9850);
                        break;
                    case 4:
                    case 3:
                        player.getPackets().sendVoice(11006);
                        sendEntityDialogue(SEND_2_TEXT_CHAT, new String[]{NPCDefinitions.getNPCDefinitions(npcId).name, "Sorry friend, I am a little busy at the moment..."}, IS_NPC, npcId, 9845);
                        break;
                }
                break;
            case 0:
                stage = 1;
                sendEntityDialogue(SEND_2_TEXT_CHAT, new String[]{player.getDisplayName(), "Okay..."}, IS_PLAYER, npcId, 9780);
                break;
            default:
                end();
                break;
        }
    }

    @Override
    public void finish() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
