package com.rs.game.world.entity.player.content.dialogue.impl;


import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * @Author Chaz <Skype: ChazKohatsu>
 * <p/>
 * Doom Sayer dialogue.
 */
public class DoomSayer extends Dialogue {


    private int npcId;


    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendEntityDialogue(SEND_1_TEXT_CHAT,
                new String[]{NPCDefinitions.getNPCDefinitions(npcId).name,
                        "Dooooom!"}, IS_NPC, npcId, 9827);
    }


    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case -1:
                stage = 1;
                sendPlayerDialogue(9827, "Where?");
                break;
            case 1:
                stage = 2;
                sendNPCDialogue(
                        npcId,
                        9827,
                        "All around us! I can feel it in the air, hear it on the wind, smell it... also in the air!");
                break;
            case 2:
                stage = 3;
                sendPlayerDialogue(9827,
                        "Is there anything we can do about this doom?");
                break;
            case 3:
                stage = 4;
                sendNPCDialogue(
                        npcId,
                        9827,
                        "There is nothing you need to do my friend! I am the Doomsayer, although my real title could be something like the Danger Tutor.");
                break;
            case 4:
                stage = 5;
                sendPlayerDialogue(9827, "Danger Tutor?");
                break;
            case 5:
                stage = 6;
                sendNPCDialogue(npcId, 9827,
                        "Yes! I roam the world sensing danger.");
                break;
            case 6:
                stage = 7;
                sendNPCDialogue(
                        npcId,
                        9827,
                        "If I find a dangerous area, then I put up warning signs that will tell you what is so dangerous about that area.");
                break;
            case 7:
                stage = 8;
                sendNPCDialogue(
                        npcId,
                        9827,
                        "If you see the signs often enough, then you can turn them off; by the time you likely know what the area has in store for you.");
                break;
            case 8:
                stage = 9;
                sendPlayerDialogue(9827,
                        "But what if I want to see the warnings again?");
                break;
            case 9:
                stage = 10;
                sendNPCDialogue(npcId, 9827, "That's why I'm waiting here!");
                break;
            case 10:
                stage = 11;
                sendNPCDialogue(
                        npcId,
                        9827,
                        "If you want to see the warning messages again, I can turn them back on for you.");
                break;
            case 11:
                stage = 12;
                sendNPCDialogue(npcId, 9827,
                        "Do you need to turn on any warnings right now?");
                break;
            case 12:
                stage = 13;
                sendOptionsDialogue("Select an Option", "Yes I do.",
                        "Not right now.");
                break;
            case 13:
                switch (componentId) {
                    case OPTION_1:
                        player.getInterfaceManager().sendInterface(583);
                        end();
                        break;
                    case OPTION_2:
                        end();
                        break;
                }
                break;
            case 14:
            default:
                end();
                break;
        }
    }


    @Override
    public void finish() {

    }


}