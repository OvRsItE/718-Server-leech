package com.rs.game.world.entity.player.controller.impl;

import com.rs.Constants;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Equipment;
import com.rs.game.world.entity.player.content.PlayerLook;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.network.decoders.handlers.ButtonHandler;

/**
 * The introduction to Rome.
 *
 * @author Arham Siddiqui
 */
public class RomeIntroduction extends Controller {

    private static final int RUNESCAPE_GUIDE_NPC = 945;
    private static final int SURVIVAL_EXPERT = 943;
    private static final int FINANCIAL_ADVISOR = 947;

    @Override
    public void start() {
        player.setYellOff(true);
        refreshStage();
        player.getPackets().sendMusicEffect(13);
        player.getPackets().sendMusic(12);
    }

    public void refreshStage() {
        int stage = getStage();
        if (stage == 0) {
            player.getBank().addItems(new Item[]{new Item(995, 5000000), new Item(1323), new Item(8845), new Item(1333), new Item(8850), new Item(385, 200), new Item(554, 250), new Item(555, 250), new Item(556, 250), new Item(557, 250), new Item(558, 250), new Item(566, 250), new Item(841), new Item(882, 100)}, false);
            player.getPackets().sendGameMessage("A starter has been added to your bank.");
            player.setFlashGuide(true);
            ButtonHandler.sendRemove(player, Equipment.SLOT_CAPE);
            PlayerLook.openCharacterCustomizing(player);
        } else if (stage == 3) {
            NPC guide = findNPC(SURVIVAL_EXPERT);
            if (guide != null) {
				player.getHintIconsManager().addHintIcon(guide, 0, -1, false);
			}
        } else if (stage == 5) {
            NPC guide = findNPC(FINANCIAL_ADVISOR);
            if (guide != null) {
				player.getHintIconsManager().addHintIcon(guide, 0, -1, false);
			}
        } else {
            player.setNextPosition(Constants.HOME_PLAYER_LOCATION1);
        }
        sendInterfaces();
        if (stage == 9) {
            player.getControlerManager().startControler(null);
        }
    }

    public int getStage() {
        if (getArguments() == null)
		 {
			setArguments(new Object[]{1}); // index 0 = stage
		}
        return (Integer) getArguments()[1];
    }

    public void setStage(int stage) {
        getArguments()[0] = stage;
    }

    public NPC findNPC(int id) {
        // as it may be far away
        for (NPC npc : World.getNPCs()) {
            if (npc == null || npc.getId() != id) {
				continue;
			}
            return npc;
        }
        return null;
    }

    @Override
    public void process() {

    }

    @Override
    public void sendInterfaces() {
        int stage = getStage();
        player.getInterfaceManager().replaceRealChatBoxInterface(372);
        if (stage == 0) {
            player.getPackets().sendIComponentText(372, 0, "Getting Started");
            player.getPackets()
                    .sendIComponentText(372, 1,
                            "To start the tutorial use your left mouse button to click on the");
            player.getPackets().sendIComponentText(372, 2,
                    "RuneScape Guide in this room. He is indicated by a flashing");
            player.getPackets()
                    .sendIComponentText(372, 3,
                            "yellow arrow above his head. If you can't see him use your");
            player.getPackets().sendIComponentText(372, 4,
                    "keyboard arrow keys to rotate the view.");
            player.getPackets().sendIComponentText(372, 5, "");
            player.getPackets().sendIComponentText(372, 6, "");
        } else if (stage == 3) {
            player.getPackets().sendIComponentText(372, 0, "Walk Your Way");
            player.getPackets()
                    .sendIComponentText(372, 1,
                            "...perhaps even run. It doesn't matter! The important thing is");
            player.getPackets().sendIComponentText(372, 2,
                    "getting there atleast! There is a flashing yellow arrow above");
            player.getPackets()
                    .sendIComponentText(372, 3,
                            "her head. If you can't see her, walk more south or use your");
            player.getPackets().sendIComponentText(372, 4,
                    "keyboard arrow keys to rotate the view.");
            player.getPackets().sendIComponentText(372, 5, "");
            player.getPackets().sendIComponentText(372, 6, "");
        } else if (stage == 4 || stage == 1000) {
            player.getPackets().sendIComponentText(372, 0, "Your Training Area");
            player.getPackets()
                    .sendIComponentText(372, 1,
                            "Talk to the " + NPCDefinitions.getNPCDefinitions(SURVIVAL_EXPERT).name + " for information about the training");
            player.getPackets().sendIComponentText(372, 2,
                    "area! This is where you train and master your combat skills! Go");
            player.getPackets()
                    .sendIComponentText(372, 3,
                            "and talk to her!");
            player.getPackets().sendIComponentText(372, 4,
                    "");
            player.getPackets().sendIComponentText(372, 5, "");
            player.getPackets().sendIComponentText(372, 6, "");
        } else if (stage == 5) {
            player.getPackets().sendIComponentText(372, 0, "Getting Started with Business");
            player.getPackets()
                    .sendIComponentText(372, 1,
                            "We are now talking money right here. Talk to the " + NPCDefinitions.getNPCDefinitions(FINANCIAL_ADVISOR).name);
            player.getPackets().sendIComponentText(372, 2,
                    "for information about acquiring money in Rome! He is indicated");
            player.getPackets()
                    .sendIComponentText(372, 3,
                            "with a flashing yellow arrow above his head.");
            player.getPackets().sendIComponentText(372, 4,
                    "");
            player.getPackets().sendIComponentText(372, 5, "");
            player.getPackets().sendIComponentText(372, 6, "");
        } else if (stage == 6 || stage == 7 || stage == 8) {
            player.getPackets().sendIComponentText(372, 0, "Getting Started with Business");
            player.getPackets()
                    .sendIComponentText(372, 1,
                            "We are now talking money right here. Talk to the " + NPCDefinitions.getNPCDefinitions(FINANCIAL_ADVISOR).name);
            player.getPackets().sendIComponentText(372, 2,
                    "for information about acquiring money in Rome! He is a scrawny,");
            player.getPackets()
                    .sendIComponentText(372, 3,
                            "little man with a briefcase.");
            player.getPackets().sendIComponentText(372, 4,
                    "");
            player.getPackets().sendIComponentText(372, 5, "");
            player.getPackets().sendIComponentText(372, 6, "");
        } else {
            player.getInterfaceManager().closeReplacedRealChatBoxInterface();
        }
    }

    @Override
    public boolean login() {
        start();
        return false;
    }

    @Override
    public boolean processNPCClick1(NPC npc) {
        if (npc.getId() == RUNESCAPE_GUIDE_NPC) {
            player.getDialogueManager().startDialogue("RuneScapeGuide",
                    RUNESCAPE_GUIDE_NPC, this);
            npc.faceEntity(player);
            npc.resetWalkSteps();
            return false;
        } else if (npc.getId() == SURVIVAL_EXPERT) {
            player.getDialogueManager().startDialogue("SurvivalExpert",
                    SURVIVAL_EXPERT, this);
            npc.faceEntity(player);
            npc.resetWalkSteps();
            return false;
        } else if (npc.getId() == FINANCIAL_ADVISOR) {
            player.getDialogueManager().startDialogue("FinancialAdvisor",
                    FINANCIAL_ADVISOR, this);
            npc.faceEntity(player);
            npc.resetWalkSteps();
            return false;
        }
        return true;
    }

    /*
 * return remove controler
 */
    @Override
    public boolean logout() {
        return false;
    }

    @Override
    public boolean processMagicTeleport(Position toTile) {
        return false;
    }

    @Override
    public boolean keepCombating(Entity target) {
        return false;
    }

    @Override
    public boolean canAttack(Entity target) {
        return false;
    }

    @Override
    public boolean canHit(Entity target) {
        return false;
    }

    @Override
    public boolean processItemTeleport(Position toTile) {
        return false;
    }

    @Override
    public boolean processObjectTeleport(Position toTile) {
        return false;
    }

    @Override
    public void forceClose() {

    }
}
