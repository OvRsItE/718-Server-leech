package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.controller.impl.FightCaves;
import com.rs.game.world.entity.updating.impl.ForceTalk;

/**
 * The gypsy's dialogue.
 * Created by Arham 4 on 3/30/14.
 */
public class Gypsy extends Dialogue {
    private NPC npc;
    private int npcId;
    private Position position;
    @Override
    public void start() {
        npc = (NPC) parameters[0];
        npcId = npc.getId();
        sendPlayerDialogue(9830, "Hello.");
    }

    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case -1:
                sendNPCDialogue(npcId, 9830, "Hello... Roman. I am the Gypsy of Rome. The one and only!");
                stage = 0;
                break;
            case 0:
                sendNPCDialogue(npcId, 9830, "I transport people around in Rome. Where may I take you today Roman?");
                stage = 1;
                break;
            case 1:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Minigame Teleports", "Bossing Teleports", "Training Teleports", "Skilling Teleports");
                stage = 2;
                break;
            case 2:
                switch (componentId) {
                    case OPTION_1:
                        sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "The Duel Arena", "The Dominion Tower", "Fight Pits", "Fight Caves");
                        stage = 3;
                        break;
                    case OPTION_2:
                        sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "The God Wars Dungeon", "The Tormented Demons", "The Corporeal Beast", "The Kalphite Queen", "The Queen Black Dragon");
                        stage = 5;
                        break;
                    case OPTION_3:
                        sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Central Training Area", "Lumbridge Catacombs");
                        stage = 6;
                        break;
                    case OPTION_4:
                        sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Mining Area", "Coming soon...");
                        stage = 7;
                        break;
                }
                break;
            case 3:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9850, "I'd like to go to the Duel Arena.");
                        position = new Position(3365, 3275, 0);
                        stage = 4;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9850, "I'd like to go to the Dominion Tower.");
                        position = new Position(3366, 3083, 0);
                        stage = 4;
                        break;
                    case OPTION_3:
                        sendPlayerDialogue(9850, "I'd like to go to the Fight Pits.");
                        position = new Position(4608, 5061, 0);
                        stage = 4;
                        break;
                    case OPTION_4:
                        sendPlayerDialogue(9850, "I'd like to go to the Fight Caves.");
                        position = FightCaves.OUTSIDE;
                        stage = 4;
                        break;
                }
                break;
            case 4:
                npc.setNextForceTalk(new ForceTalk("Senventior Disthinte Molesko!"));
                player.setNextPosition(position);
                if (position.getX() == 3365 && position.getY() == 3275)
                    player.getControlerManager().startControler("DuelControler");
                else if (position.getX() == 2881 && position.getY() == 5310)
                    player.getControlerManager().startControler("GodWars");
                else if (position.getX() == 0)
                    player.getControlerManager().startControler("QueenBlackDragonControler");
                stage = -2;
                break;
            case 5:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9850, "I'd like to go to the God Wars Dungeon.");
                        position = new Position(2881, 5310, 2);
                        stage = 4;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9850, "I'd like to go to the Tormented Demons.");
                        position = new Position(2562, 5739, 0);
                        stage = 4;
                        break;
                    case OPTION_3:
                        sendPlayerDialogue(9850, "I'd like to go to the Corporeal Beast.");
                        position = new Position(2966, 4383, 2);
                        stage = 4;
                        break;
                    case OPTION_4:
                        sendPlayerDialogue(9850, "I'd like to go to the Kalphite Queen.");
                        position = new Position(3226, 3108, 0);
                        stage = 4;
                        break;
                    case OPTION_5:
                        sendPlayerDialogue(9850, "I'd like to go to the Queen Black Dragon.");
                        position = new Position(0, 0, 0);
                        stage = 4;
                        break;
                }
                break;
            case 6:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9850, "I'd like to go to the Central Training Area.");
                        position = new Position(3183, 5470, 0);
                        stage = 4;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9850, "I'd like to go to the Lumbridge Catacombs.");
                        position = new Position(3972, 5562, 0);
                        stage = 4;
                        break;
                }
                break;
            case 7:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9850, "I'd like to go to the mining area.");
                        position = new Position(2891, 9907, 0);
                        stage = 4;
                        break;
                    case OPTION_2:
                        end();
                        break;
                }
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
