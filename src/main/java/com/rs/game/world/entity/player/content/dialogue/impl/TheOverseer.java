package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ReverendDread
 * Created 3/8/2021 at 3:42 PM
 * @project 718---Server
 */
public class TheOverseer extends Dialogue {

    private static final Item BLUDGEON = Item.createOSRS(13263);
    public static final Item BLUDGEON_SPINE = Item.createOSRS(13274);
    public static final Item BLUDGEON_CLAW = Item.createOSRS(13275);
    public static final Item BLUDGEON_AXON = Item.createOSRS(13276);
    public static final Item[] BLUDGEON_PIECES = { BLUDGEON_SPINE, BLUDGEON_CLAW, BLUDGEON_AXON };

    @Override
    public void start() {
        boolean hasComponents = player.getInventory().containsAny(BLUDGEON_PIECES);
        if (!hasComponents) {
            sendNPCDialogue(NPC.asOSRS(6178), NORMAL, !player.hasTalkedToOverseer ? "Yes, human?" : "Have you harvested any components for me?");
            stage = 0;
        } else {
            sendDialogue("You hand over the component(s) to the Overseer.");
            stage = 21;
        }
    }

    @Override
    public void run(int interfaceId, int componentId) {
        if (stage == 0) {
            sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, !player.hasTalkedToOverseer ? "What can I do here?" : "What's this stuff for?", "Tell me about yourself.", "Tell me about the Abyssal Sires.", !player.hasTalkedToOverseer ? "I'll be off for now." : "No, sorry.");
            stage = 1;
        } else if (stage == 1) {
            switch (componentId) {
                case OPTION_1:
                    if (!player.hasTalkedToOverseer) {
                        sendPlayerDialogue(NORMAL, "What can I do here?");
                        stage = 2;
                    } else {
                        sendPlayerDialogue(NORMAL, "What's this stuff for?");
                        stage = 4;
                    }
                    break;
                case OPTION_2:
                    sendPlayerDialogue(NORMAL, "Tell me about yourself.");
                    stage = 5;
                    break;
                case OPTION_3:
                    sendPlayerDialogue(NORMAL, "Tell me about the Abyssal Sires.");
                    stage = 10;
                    break;
                case OPTION_4:
                    if (!player.hasTalkedToOverseer) {
                        sendPlayerDialogue(NORMAL, "I'll be off for now.");
                        stage = 127;
                    } else {
                        sendPlayerDialogue(NORMAL, "No, sorry.");
                        stage = 19;
                    }
                    break;
            }
        } else if (stage == 2) {
            sendNPCDialogue(NPC.asOSRS(6178), NORMAL, "There is something you can do here, human. In fact it may perhaps benefit us both.");
            stage = 3;
        } else if (stage == 3) {
            sendNPCDialogue(NPC.asOSRS(6178), NORMAL, "Bring me a spine, a claw and an axon from the Abyssal", "Sires, and I shall construct them into a mighty weapon", "for you. Yes, we shall both benefit from this.");
            stage = 127;
        } else if (stage == 4) {
            sendNPCDialogue(NPC.asOSRS(6178), NORMAL, "I shall use the components to construct a mighty weapon for you. Rest assured, the process will be beneficial to both of us.");
            stage = 127;
        } else if (stage == 5) {
            sendNPCDialogue(NPC.asOSRS(6178), NORMAL, "Ah... Time does not pass in the Abyss at the same rate", "that it does in your realm, but nevertheless I have been", "in this place for aeons.");
            stage = 6;
        } else if (stage == 6) {
            sendNPCDialogue(NPC.asOSRS(6178), NORMAL, "Indeed, when I was last in your realm, the armies of", "Zamorak were battling the other gods for control, and", "we sought reinforcements from any plane we could", "reach.");
            stage = 7;
        } else if (stage == 7) {
            sendNPCDialogue(NPC.asOSRS(6178), NORMAL, "That was how I came here. We broke though to the", "Abyss, seeking allies for our cause, but instead I ended", "up a prisoner in this place, unable to leave, while the", "God Wars continued without me.");
            stage = 8;
        } else if (stage == 8) {
            sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Tell me about the Abyssal Sires.", "I'll be off for now.");
            stage = 9;
        } else if (stage == 9) {
            switch (componentId) {
                case OPTION_1:
                    sendPlayerDialogue(NORMAL, "Tell me about the Abyssal Sires.");
                    stage = 10;
                    break;
                case OPTION_2:
                    sendPlayerDialogue(NORMAL, "I'll be off for now.");
                    stage = 127;
                    break;
            }
        } else if (stage == 10) {
            sendNPCDialogue(NPC.asOSRS(6178), NORMAL, "The Sires are the engineers of this living pocket", "universe known to you and I as the Abyss.");
            stage = 11;
        } else if (stage == 11) {
            sendNPCDialogue(NPC.asOSRS(6178), NORMAL, "At the hands of the 'Chaos God', Zamorak, like myself,", "they too were cast down and imprisoned in this living", "tomb.");
            stage = 12;
        } else if (stage == 12) {
            sendNPCDialogue(NPC.asOSRS(6178), NORMAL, "They worked for centuries attempting to escape the pull", "of the Abyss but as food resources began to deplete", "they knew that their only chance for survival was to", "construct this Nexus.");
            stage = 13;
        } else if (stage == 13) {
            sendNPCDialogue(NPC.asOSRS(6178), NORMAL, "They absorb flesh and bone of their demon kin, weaving", "it into the fabric of these very walls. Here they have", "remained in deep stasis, asleep.");
            stage = 14;
        } else if (stage == 14) {
            sendNPCDialogue(NPC.asOSRS(6178), NORMAL, "If you succeed in awakening them, they will no doubt", "unleash their full fury upon you. Someone of your", "calibre should be able to deal with them though, especially", "with the shadows on your side...");
            stage = 15;
        } else if (stage == 15) {
            sendNPCDialogue(NPC.asOSRS(6178), NORMAL, "If you succeed in killing a Sire, you may be fortunate", "enough to find one of its unsired offspring among its", "remains. Bring the Unsired here, to the Font of", "Consumption.");
            stage = 16;
        } else if (stage == 16) {
            sendNPCDialogue(NPC.asOSRS(6178), NORMAL, "By placing the Unsired into the Font, I will be able to", "consume its flesh and perhaps you will receive something", "worth having.");
            player.hasTalkedToOverseer = true;
            stage = 17;
        } else if (stage == 17) {
            sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Tell me about yourself.", "I'll be off for now.");
            stage = 18;
        } else if (stage == 18) {
            switch (componentId) {
                case OPTION_1:
                    sendPlayerDialogue(NORMAL, "Tell me about yourself.");
                    stage = 5;
                    break;
                case OPTION_2:
                    sendPlayerDialogue(NORMAL, "I'll be off for now.");
                    stage = 127;
                    break;
            }
        } else if (stage == 19) {
            sendNPCDialogue(NPC.asOSRS(6178), NORMAL, "I still require a spine, a claw, and an axon from an", "Abyssal Sire.");
            stage = 127;
        } else if (stage == 21) {
            List<Integer> remaining = Arrays.stream(BLUDGEON_PIECES).map(Item::getId).collect(Collectors.toList());
            for (Item item : BLUDGEON_PIECES) {
                if (player.getInventory().contains(item) && !player.getOverseerItems().contains(item.getId())) {
                    player.getInventory().deleteItem(item);
                    player.getOverseerItems().add(item.getId());
                }
                if (player.getOverseerItems().contains(item.getId())) {
                    remaining.remove((Integer) item.getId());
                }
            }
            if (remaining.isEmpty()) {
                sendNPCDialogue(NPC.asOSRS(6178), NORMAL, "I now have all the components I require, so here is the weapon I promised you.");
                player.getInventory().addItem(BLUDGEON);
                player.getOverseerItems().clear();
                stage = 22;
            } else {
                sendNPCDialogue(NPC.asOSRS(6178), NORMAL, "I still require " + getRequiredParts(remaining) + " from an Abyssal Sire.");
                stage = 127;
            }
        } else if (stage == 22) {
            sendItemDialogue(BLUDGEON.getId(), "The Overseer presents you with an Abyssal Bludgeon.");
            stage = 23;
        } else if (stage == 23) {
            sendNPCDialogue(NPC.asOSRS(6178), NORMAL, "You have served me well, it is good to see that you humans are still as gullible as ever!");
            stage = 24;
        } else if (stage == 24) {
            sendNPCDialogue(NPC.asOSRS(6178), NORMAL, "Bringing me those body parts from the Sires has now granted me enough teleportation energy", "to leave this living hell hole. Perhaps I will pay your realm a visit and there will be much to harvest!");
            stage = 25;
        } else if (stage == 25) {
            sendPlayerDialogue(NORMAL, "Yuck! This is disgusting. What use is this? It's unwieldly!");
            stage = 26;
        } else if (stage == 26) {
            sendNPCDialogue(NPC.asOSRS(6178), NORMAL, "Have you not learned yet, appearances can be deceiving. You hold in your hands a living weapon whose power is rivalled by few weapons from your world, use it wisely.");
            stage = 30;
//        } else if (stage == 27) {
//            sendNPCDialogue(1, NORMAL, "Do not think that I am ungrateful for my freedom human. I will leave you this ancient tome that will instruct you in creating more of these weapons in future.");
//            stage = 28;
//        } else if (stage == 28) {
//            sendNPCDialogue(1, NORMAL, "Just use the three components on the book, and it will do the rest. You can get spare copies here any time you need.");
//            stage = 29;
//        } else if (stage == 29) {
//            sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Thank you.", "Perhaps I shall use it to slay you, demon.");
//            stage = 30;
        } else if (stage == 30) {
//            switch (componentId) {
//                case OPTION_1:
                    sendPlayerDialogue(NORMAL, "Thank you.");
                    stage = 127;
//                    break;
//                case OPTION_2:
//                    sendPlayerDialogue(NORMAL, "Perhaps I shall use it to slay you, demon.");
//                    stage = 32;
//                    break;
//            }
//        } else if (stage == 31) {
//            sendNPCDialogue(1, NORMAL, "Farewell...");
//            FadingScreen.fade(player);
//            stage = 127;
//        } else if (stage == 32) {
//            sendNPCDialogue(1, NORMAL, "So be it... I will await you on the fields of blood!");
//            FadingScreen.fade(player);
//            stage = 127;
        } else if (stage == 127) {
            end();
        }
    }

    @Override
    public void finish() {

    }

    private String getRequiredParts(List<Integer> remaining) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < remaining.size(); i++) {
            builder.append(ItemDefinitions.getItemDefinitions(remaining.get(i)).name);
            if (i < remaining.size() - 1)
                builder.append(", ");
        }
        return builder.toString();
    }

}
