package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * The Firemaking tutor's dialogue.
 *
 * @author Arham Siddiqui
 */
public class MarcusEverburn extends Dialogue {
    private NPC npc;
    private int npcId;

    @Override
    public void start() {
        npc = (NPC) parameters[0];
        npcId = npc.getId();
        sendPlayerDialogue(9850, "Hello.");
    }

    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case -1:
                sendNPCDialogue(npcId, 9835, "Hello Roman... I mean kid. What do you want?");
                stage = 0;
                break;
            case 0:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Can you teach me the basics of the Firemaking skill?", "What is this place?", "Who are you calling 'kid' kid?", "I am fine, thank you.", "I'm going to kill your idiocity!");
                stage = 1;
                break;
            case 1:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "Can you teach me the basics of the Firemaking skill?");
                        stage = 2;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9827, "What is this place?");
                        stage = 9;
                        break;
                    case OPTION_3:
                        sendPlayerDialogue(9790, "Who are you calling 'kid' kid?");
                        stage = 10;
                        break;
                    case OPTION_4:
                        sendPlayerDialogue(9850, "I am fine, thank you.");
                        stage = -2;
                        break;
                    case OPTION_5:
                        sendPlayerDialogue(9790, "I am going to kill your idiocity!");
                        stage = 12;
                        break;
                }
                break;
            case 2:
                sendNPCDialogue(npcId, 9835, "How about, hmm, no?");
                stage = 3;
                break;
            case 3:
                sendNPCDialogue(npcId, 9835, "Listen up kid, all you need to do is just get a dang Tinderbox and use it on some Logs. Got that?");
                stage = 4;
                break;
            case 4:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "I got it!", "You have no idea who you're messing with, kid...", "Why are you so mean?");
                stage = 5;
                break;
            case 5:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9850, "I got it!");
                        stage = 6;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9790, "You have no idea who you're messing with, kid...");
                        stage = 7;
                        break;
                    case OPTION_3:
                        sendPlayerDialogue(9760, "Why are you so mean?");
                        stage = 8;
                        break;
                }
                break;
            case 6:
                sendNPCDialogue(npcId, 9790, "Good, now scram!");
                stage = -2;
                break;
            case 7:
                sendNPCDialogue(npcId, 9790, "Get out of my face kid! I have power over fire and am not afraid to use it!");
                stage = -2;
                break;
            case 8:
                sendNPCDialogue(npcId, 9851, "Haha! Me and mean? You better get out of here or else I'll burn you to bits kid.");
                stage = -2;
                break;
            case 9:
                sendNPCDialogue(npcId, 9835, "Listen! You are now at the center of Rome, got that? We got some trading places here and there with some noob tutors scrambled around! Got that?");
                stage = 4;
                break;
            case 10:
                sendNPCDialogue(npcId, 9835, "I'm calling you a kid. Who else?");
                stage = 11;
                break;
            case 11:
                sendPlayerDialogue(9851, "Well, I bet you I'll beat you up anytime!");
                stage = 7;
                break;
            case 12:
                sendNPCDialogue(npcId, 9851, "In your dreams!");
                stage = -2;
                break;
            default:
                player.closeInterfaces();
                break;
        }
    }

    @Override
    public void finish() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
