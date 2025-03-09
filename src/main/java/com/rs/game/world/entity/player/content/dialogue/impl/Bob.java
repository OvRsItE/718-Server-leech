package com.rs.game.world.entity.player.content.dialogue.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.rs.game.item.Item;
import com.rs.game.world.entity.player.content.ItemConstants;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.ShopsHandler;


/**
 * @Author Chaz - Jul 14, 2013
 * <p/>
 * Bob the axe trader/item repairer's dialogue.
 */
public class Bob extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "I'd like to trade.",
                "Can you repair my items for me?");
    }

    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case -1:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "I'd like to trade.");
                        stage = 0;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9827, "Can you repair my items for me?");
                        stage = 1;
                        break;
                }
                break;
            case 0:
                sendNPCDialogue(npcId, 9827,
                        "Great! I buy and sell pickaxes and hatchets. "
                                + "There are plenty to choose from.");
                stage = 3;
                break;
            case 1:
                sendNPCDialogue(npcId, 9827,
                        "Of course I can, though materials may cost you. Just "
                                + "hand me your items and I'll take a look.");
                stage = 2;
                break;
            case 2:
            	HashMap<Item, Item> items = calculateRepairCost();
            	if (items.isEmpty()) {
                    sendNPCDialogue(npcId, 9827, "You don't have any items i can repair.");
                    stage = 3;
            	} else {
	            	for (Item item : items.keySet()) {
	            		System.out.println("Item - " + item.getName() + ", Cost: " + items.get(item).getId() + " x" + items.get(item).getAmount());
	            		if (player.getInventory().containsItem(item.getId(), item.getAmount()) && 
	            				player.getInventory().containsItem(items.get(item).getId(), items.get(item).getAmount())
	            				&& ItemConstants.getRepairedItem(item.getId()) != -1) {
	            			player.getInventory().deleteItem(items.get(item).getId(), items.get(item).getAmount());
	            			player.getInventory().deleteItem(item); 
	            			player.getInventory().addItem(ItemConstants.getRepairedItem(item.getId()), 1);
	            			player.getCharges().getCharges().put(item.getId(), ItemConstants.getItemDefaultCharges(item.getId()));
	            		} else {
	            			player.sendMessage("You don't have the required materials to repair your " + item.getName() + ".");
	            		}
	            	}
	            	sendNPCDialogue(npcId, 9827, "There you are, have a nice day.");
	            	end();
            	}
                break;
            default:
            	end();
                	
        }
    }


    @Override
    public void finish() {}

    /**
     * Calculates the repair cost for each item that needs repaired.
     * @return
     */
    private HashMap<Item, Item> calculateRepairCost() {
    	HashMap<Item, Item> repairCosts = new HashMap<Item, Item>();
    	for (int slot = 0; slot < 28; slot++) {
    		Item item = player.getInventory().getItem(slot);
    		if (item == null)
    			continue;
    		int max_charges = ItemConstants.getItemDefaultCharges(item.getId());
    		Integer charges = player.getCharges().getCharges().get(item.getId());
    		if (charges != null && charges < max_charges) {
    			int needed = max_charges - charges.intValue();
    			int per_charge = ItemConstants.getRepairCostPerCharge(item.getId());
    			if (per_charge <= 0)
    				continue;
    			int cost = ItemConstants.getRepairCostPerCharge(item.getId()) * needed;
    			int repairItem = ItemConstants.getRepairItem(item.getId());
    			if (repairItem != -1) {
    				repairCosts.put(item, new Item(repairItem, cost));
    			}
    		}
    	}
    	return repairCosts;
    }

}