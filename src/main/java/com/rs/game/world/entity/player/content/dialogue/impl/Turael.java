/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.actions.skilling.slayer.Slayer;
import com.rs.game.world.entity.player.actions.skilling.slayer.SlayerMaster;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class Turael extends Dialogue {

    /**
     * Starts the dialogue
     */
    public Turael() {
    }

    @Override
    public void start() {
        npcId = ((Integer) parameters[0]).intValue();
        sendNPCDialogue(npcId, NORMAL, "Hello, brave warrior. What would you like?");
    }

    /**
     * Runs the dialogue
     */
    @Override
    public void run(int interfaceId, int componentId) {
        if (stage == -1) {
            stage = 0;
            sendOptionsDialogue("What would you like to say?","I would like a slayer task.","What is my current slayer task?", "Can I cancel my curren task?");
        } else if (stage == 0) {
            if (componentId == OPTION_1) {
                if (player.hasTask == false) {
                    Slayer.assignTask(player, SlayerMaster.TURAEL);
                    sendNPCDialogue(npcId, NORMAL, "Your slayer task is to kill " + player.slayerTask.getTaskMonstersLeft() + " " + player.slayerTask.getTask().simpleName);
                } else {
                	sendNPCDialogue(npcId, NORMAL, "You already have a slayer task!", "You need to kill " + player.slayerTask.getTaskMonstersLeft()+ " " + player.slayerTask.getTask().simpleName);
                }
            } else if (componentId == OPTION_2) {
                if (player.hasTask == true) {
                	sendNPCDialogue(npcId, NORMAL, "You have a short memory, don't you?", "You need to kill " + player.slayerTask.getTaskMonstersLeft() + " " + player.slayerTask.getTask().simpleName);
                } else {
                	sendNPCDialogue(npcId, NORMAL, "Foolish warrior. You don't have a slayer task!");                
                }
                stage = -1;           
            } else if (componentId == OPTION_3) {
                Slayer.displayPoints(player, 1);
                end();
            } else {
                end();
            }
        }
    }

    @Override
    public void finish() {
    }
    /**
     * Declares the npc ID
     */
    private int npcId;
}
