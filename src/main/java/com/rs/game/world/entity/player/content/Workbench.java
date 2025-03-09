package com.rs.game.world.entity.player.content;

import com.google.common.collect.Lists;
import com.rs.game.item.Item;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.Misc;
import com.rs.utility.Utils;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author ReverendDread
 * Created 6/20/2021 at 2:42 AM
 * @project 718---Server
 */
@RequiredArgsConstructor
public class Workbench implements Serializable {

    private static final long serialVersionUID = -6343613853414700303L;

    private static final int[] ITEM_NAME_COMPONENTS = {
            57, 60, 64, 69, 72, 75, 78, 81, 84, 87, 90, 93, 96, 99, 102, 105, 108, 111, 114, 117, 120
    };

    public static final int INTERFACE_ID = 3014;
    private static final int REQUIRED_ITEMS_COMPONENT = 123;
    private static final int DESCRIPTION = 121;
    private static final int PREVIEW_ITEM_NAME = 40;
    private static final int PREVIEW_ITEM_COMPONENT = 66;
    private static final int REQUIRED_ITEMS_KEY = 6969;
    private static final int NEXT_PAGE = 125;
    private static final int PREVIEW_KEY = 6968;
    private static final int FUSE_ITEM_BUTTON = 43;
    private static final int PREVIEW_BUTTON = 49;

    private final Player player;
    private transient int page = 0;
    private final List<WorkbenchRecipe> recipesList = Lists.newArrayListWithCapacity(ITEM_NAME_COMPONENTS.length);
    private transient WorkbenchRecipe selectedRecipe;

    public void open() {
        player.getInterfaceManager().sendInterface(INTERFACE_ID);
        populateRecipes();
        player.getPackets().sendIComponentText(INTERFACE_ID, DESCRIPTION, "");
        player.getPackets().sendIComponentText(INTERFACE_ID, PREVIEW_ITEM_NAME, "");
        player.setCloseInterfacesEvent(() -> {
            page = 0;
        });
    }

    private int getMaxPage() {
        return 2;
    }

    private void populateRecipes() {
        recipesList.clear();
        int startOrdinal = page * ITEM_NAME_COMPONENTS.length;
        int idx = 0;
        for (int index = startOrdinal; index < ((startOrdinal == 0 ? ITEM_NAME_COMPONENTS.length : startOrdinal * 2)); index++, idx++) {
            if (index < WorkbenchRecipe.VALUES.length) {
                WorkbenchRecipe recipe = WorkbenchRecipe.VALUES[index];
                recipesList.add(recipe);
                player.getPackets().sendIComponentText(INTERFACE_ID, ITEM_NAME_COMPONENTS[idx], recipe.result.getName());
            } else
                player.getPackets().sendIComponentText(INTERFACE_ID, ITEM_NAME_COMPONENTS[idx], "");
        }
    }

    private void selectRecipe(WorkbenchRecipe recipe) {

        selectedRecipe = recipe;

        player.getPackets().sendIComponentText(INTERFACE_ID, PREVIEW_ITEM_NAME, recipe.result.getName());
        player.getPackets().sendIComponentText(INTERFACE_ID, DESCRIPTION, recipe.description);

        player.getPackets().sendItems(REQUIRED_ITEMS_KEY, recipe.required);
        player.getPackets().sendInterSetItemsOptionsScript(INTERFACE_ID, REQUIRED_ITEMS_COMPONENT, REQUIRED_ITEMS_KEY, 3, 10, "Examine");
        player.getPackets().sendUnlockIComponentOptionSlots(INTERFACE_ID, REQUIRED_ITEMS_COMPONENT, 0, 160, 0);

        player.getPackets().sendItems(PREVIEW_KEY, new Item[] { recipe.result });
        player.getPackets().sendInterSetItemsOptionsScript(INTERFACE_ID, PREVIEW_ITEM_COMPONENT, PREVIEW_KEY, 1, 1, "Examine");
        player.getPackets().sendUnlockIComponentOptionSlots(INTERFACE_ID, PREVIEW_ITEM_COMPONENT, 0, 160, 0);

        player.getPackets().sendIComponentText(INTERFACE_ID, FUSE_ITEM_BUTTON, "Fuse Item - <col=ff0000>" + recipe.chance + "%");
    }

    public boolean handleButtonClick(int interfaceId, int buttonId) {

        if (interfaceId != INTERFACE_ID)
            return false;

        if (buttonId == FUSE_ITEM_BUTTON) {
            if (selectedRecipe != null) {
                if (player.getInventory().contains(selectedRecipe.required)) {
                    if (selectedRecipe.chance < 100D) {
                        player.getDialogueManager().startDialogue(new Dialogue() {

                            @Override
                            public void start() {
                                sendOptionsDialogue("<col=ff0000>WARNING: This fusion is only a <col=ffffff>" + selectedRecipe.chance + "% <col=ff0000>chance. If this fails the items will be consumed.", "Yes I understand.", "Nevermind.");

                            }

                            @Override
                            public void run(int interfaceId, int componentId) {
                                if (stage == -1 && componentId == OPTION_1)
                                    exchangeItems((Math.random() * 100) < selectedRecipe.chance);
                                end();
                            }

                            @Override
                            public void finish() {}

                        });
                    } else
                        exchangeItems(true);
                } else
                    player.sendMessage("You don't have the required items to create that.");
            }
            return true;
        }

        if (buttonId == PREVIEW_BUTTON) {
            //TODO?
            return true;
        }

        if (buttonId == NEXT_PAGE) {
            if (page + 1 < getMaxPage()) {
                page++;
            } else page = 0;
            populateRecipes();
            return true;
        }

        for (int index = 0; index < ITEM_NAME_COMPONENTS.length; index++) {
            if (buttonId == ITEM_NAME_COMPONENTS[index]) {
                selectRecipe(recipesList.get(index));
            }
        }

       // player.sendMessage("Unhandled button: " + buttonId);

        return true;
    }

    private void exchangeItems(boolean giveResult) {
        for (Item item : selectedRecipe.required) {
            player.getInventory().deleteItem(item);
        }
        if (giveResult) {
            player.getInventory().addItem(selectedRecipe.result);
            player.sendMessage("<col=00ff00>You successfully fused your items together. You receive " + selectedRecipe.result.getAmount() + "x " + selectedRecipe.result.getName());
            World.sendWorldMessage("[<col=00ff00>Upgrade Bench</col>] - <col=00ff00>" + Utils.formatPlayerNameForDisplay(player.getUsername()) +
                    "<col=00ff00> has successfully fused a <col=00ff00>" + selectedRecipe.result.getName(), false);
        } else
            player.sendMessage("<col=ff0000>You failed to fuse your items together, as a result they were consumed.");
    }

    @RequiredArgsConstructor
    enum WorkbenchRecipe {
        //slime - 28881
        //upgrade gem - 28831

        BLOOD_TWISTED_BOW(new Item(28816), new Item[] { new Item(28881, 500), new Item(28831), new Item(29943) , new Item(28848) }, "Upgrades the twisted bow giving it a chance to recover some health from damage dealt.", 100),
        BLOOD_SCYTHE_OF_VITUR(new Item(28814), new Item[] { new Item(28881, 500), new Item(28831), new Item(52325) , new Item(28848) }, "Upgrades the scythe of vitur giving it a chance to recover some health from damage dealt.", 100),
        VIGGORA_CHAINMACE_BLOOD(new Item(28852), new Item[] { new Item(28881, 500), new Item(52545), new Item(28848) }, "Upgrades the viggora's chainmace to do extra damage and not require ether.", 50),
        CRAWS_BOW_BLOOD(new Item(28854), new Item[] { new Item(28881, 500), new Item(52550), new Item(28848) }, "Upgrades the craw's bow to do extra damage and not require ether.", 50),
        THAMMARONS_SCEPTRE(new Item(28850), new Item[] { new Item(28881, 500), new Item(52555), new Item(28848) }, "Upgrades the thammaron's sceptre to do extra damage and not require ether", 50),
        DARK_FULL_HELM(new Item(29216), new Item[] { new Item(28881, 1000), new Item(28831), new Item(28959), new Item(20135)  }, "Creates the dark helmet tier 85 stab armor + 5% damage in pvm.", 70),
        DARK_PLATEBODY(new Item(29214), new Item[] { new Item(28881, 1000), new Item(28831), new Item(28960), new Item(20139)  }, "Creates the dark platebody tier 85 stab armor + 5% damage in pvm.", 70),
        DARK_PLATELEGS(new Item(29218), new Item[] { new Item(28881, 1000), new Item(28831), new Item(28958), new Item(20143)  }, "Creates the dark platelegs tier 85 stab armor + 5% damage in pvm.", 70),
        LIGHTNING_FULL_HELM(new Item(29223), new Item[] { new Item(28881, 1000), new Item(28831), new Item(28956), new Item(20135)  }, "Creates the lightning helmet tier 85 slash armor + 5% damage in pvm.", 70),
        LIGHTNING_PLATEBODY(new Item(29221), new Item[] { new Item(28881, 1000), new Item(28831), new Item(28957), new Item(20139)  }, "Creates the lightning platebody tier 85 slash armor + 5% damage in pvm.", 70),
        LIGHTNINGPLATELEGS(new Item(29225), new Item[] { new Item(28881, 1000), new Item(28831), new Item(28955), new Item(20143)  }, "Creates the lightning platelegs tier 85 slash armor + 5% damage in pvm.", 70),
        ETHER_COIF(new Item(28900), new Item[] { new Item(28881, 1000), new Item(28831), new Item(28834), new Item(28843)  }, "Creates the ether coif tier 85 range armor + 5% damage in pvm.", 70),
        ETHER_BODY(new Item(28902), new Item[] { new Item(28881, 1000), new Item(28831), new Item(28834), new Item(28847)  }, "Creates the ether body tier 85 range armor + 5% damage in pvm.", 70),
        ETHER_LEGS(new Item(28904), new Item[] { new Item(28881, 1000), new Item(28831), new Item(28834), new Item(28845)  }, "Creates the ether legs tier 85 range armor + 5% damage in pvm.", 70),
        CURSED_RING(new Item(28830), new Item[] { new Item(28881, 500), new Item(28803), new Item(29463), new Item(29513),  new Item(29511),  new Item(29509)  }, "Combines the hazelmere's signet ring (i) and ring of wealth (i) making it untradable and auto kept on death.", 50),
        CURSED_WARD(new Item(29521), new Item[] { new Item(28881, 500), new Item(41924), new Item(41926), new Item(29513),  new Item(29511),  new Item(29509)  }, "Creates the cursed ward superior stats to the odium and malediction wards with the effect of recoiling 10% of damage taken.", 50),
        OVERLOAD_HEART(new Item(28882), new Item[] { new Item(28881, 500), new Item(29698), new Item(29472),  }, "Turns the imbued heart into a overloaded heart giving the effects of overload.", 25),
        ICICLE_DAGGER(new Item(28929), new Item[] { new Item(28881, 500), new Item(43265) , new Item(28925, 4) }, "Upgrades the abyssal dagger giving it a powerful special attack and superior stats.", 100),
        HAZELMERE_SIGNET_RING_I(new Item(28803), new Item[] { new Item(28881, 2500), new Item(29107, 1) }, "Imbues the hazelmere's signet ring increasing its stats and droprate percent", 25),
        CURSED_RING_I(new Item(28804), new Item[] { new Item(28881, 2500), new Item(28830, 1) }, "Imbues the cured ring increasing its stats and recoil effect", 25),
        LEGENDARY_EGG(new Item(28825), new Item[] { new Item(28881, 2500), new Item(28831, 1) }, "Open for a chance at different legendary pets", 50),
        ELITE_VOID_Top(new Item(19785), new Item[] { new Item(28640, 1), new Item(8839, 1), new Item(28881, 500)  }, "Upgrades void into a elite version.", 100),
        ELITE_VOID_Robe(new Item(19786), new Item[] { new Item(28640, 1), new Item(8840, 1), new Item(28881, 500) }, "Upgrades void into a elite version.", 100);


        private final Item result;
        private final Item[] required;
        private final String description;
        private final double chance;

        public static final WorkbenchRecipe[] VALUES = values();

    }

}
