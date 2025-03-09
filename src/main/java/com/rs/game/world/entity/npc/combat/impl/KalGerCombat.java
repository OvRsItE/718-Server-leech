package com.rs.game.world.entity.npc.combat.impl;
 
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.npc.others.KalGer;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.toxin.ToxinType;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.utility.Utils;
 
public class KalGerCombat extends CombatScript {
 
        @Override
        public Object[] getKeys() {
                return new Object[] { KalGer.Type.BARE_HANDED.getId(), KalGer.Type.SWORDED.getId(), KalGer.Type.STAFFED.getId(), KalGer.Type.TWO_HANDED.getId(), KalGer.Type.BOWED.getId(), KalGer.Type.MAULED.getId()};
        }
 
        @Override
        public int attack(final NPC npc, Entity target) {
                //special anim 14384
                final NPCCombatDefinitions defs = npc.getCombatDefinitions();
                //int damage = getRandomMaxHit(npc, 910,
                                //NPCCombatDefinitions.SPECIAL2, target);
                int meleedamage = getRandomMaxHit(npc, 150,
                                NPCCombatDefinitions.MELEE, target) + Utils.random(100) + Utils.random(100) + Utils.random(100) + Utils.random(100); //max 550
                int rangedamage = getRandomMaxHit(npc, 375,
                                NPCCombatDefinitions.RANGE, target) + Utils.random(50) + Utils.random(50) + Utils.random(50) + Utils.random(50);
                int rangedamage1 = getRandomMaxHit(npc, 375,
                                NPCCombatDefinitions.RANGE, target) + Utils.random(50) + Utils.random(50) + Utils.random(50) + Utils.random(50); //max 575, 2 hits, makes 1150, possible 1-hit-kill
                int magedamage = getRandomMaxHit(npc, 500,
                                NPCCombatDefinitions.MAGE, target) + Utils.random(75) + Utils.random(25) + Utils.random(75) + Utils.random(25); //max 800, low or high
                for(Player p : KalGer.playersOn) {
                        if(!npc.withinDistance(p, 12)) {
                                if(p == null || p.isDead() || target == null || target.isDead()) {
                                        World.removeNPC(npc);
                                }
                        }
                }
                if (Utils.getRandom(14) == 0) {
                        switch (Utils.getRandom(6)) {
                        case 0:
                        case 2:
                                npc.setNextForceTalk(new ForceTalk("ENOUGH!"));
                                npc.playSound(3012, 2);
                                ((Player) target).getPrayer().closeAllPrayers();
                                ((Player) target).sm("All your prayers have been closed!");
                                break;
                        case 1:
                                npc.setNextForceTalk(new ForceTalk("GRRRRRR!"));
                                npc.playSound(2986, 2);
                                break;
                        case 5:
                                npc.setNextForceTalk(new ForceTalk("MUHAAHAAHAA!"));
                                npc.playSound(3011, 2);
                                break;
                        }
                       
                }
                int attackStyle = 0;
                if(npc.getId() == KalGer.Type.BARE_HANDED.getId())
                        attackStyle = 0;
                if(npc.getId() == KalGer.Type.SWORDED.getId())
                        attackStyle = 1;
                if(npc.getId() == KalGer.Type.STAFFED.getId())
                        attackStyle = 2;
                if(npc.getId() == KalGer.Type.TWO_HANDED.getId())
                        attackStyle = 3;
                if(npc.getId() == KalGer.Type.BOWED.getId())
                        attackStyle = 4;
                if(npc.getId() == KalGer.Type.MAULED.getId())
                        attackStyle = 5;
               
                switch (attackStyle) {
                case 0:
                        npc.animate(new Animation(KalGer.Type.BARE_HANDED.getAnim()));
                        delayHit(npc, 1, target, getMeleeHit(npc, meleedamage));
                        break;
                case 1:
                        npc.animate(new Animation(KalGer.Type.SWORDED.getAnim()));
                        delayHit(npc, 1, target, getMeleeHit(npc, meleedamage));
                        if (Utils.getRandom(14) == 0)
                        	target.getToxin().applyToxin(ToxinType.POISON);
                        break;
                case 2:
                        npc.animate(new Animation(KalGer.Type.STAFFED.getAnim()));
                        World.sendProjectile(npc, target, 471, 41, 16, 41, 35, 16, 0);
                        delayHit(npc, 2, target, getMagicHit(npc, magedamage));
                        if (Utils.getRandom(14) == 0)
                        	target.getToxin().applyToxin(ToxinType.POISON);
                        break;
                case 3:
                        npc.animate(new Animation(KalGer.Type.TWO_HANDED.getAnim()));
                        //World.sendProjectile(npc, t, 471, 41, 16, 41, 35, 16, 0);
                        delayHit(npc, 1, target, getMeleeHit(npc, meleedamage));
                        if (Utils.getRandom(14) == 0)
                        	target.getToxin().applyToxin(ToxinType.POISON);
                        break;
                case 4:
                        npc.animate(new Animation(KalGer.Type.BOWED.getAnim()));
                        World.sendProjectile(npc, target, 471, 41, 16, 41, 25, 16, 0);
                        World.sendProjectile(npc, target, 471, 41, 16, 41, 52, 16, 0);
                        delayHit(npc, 1, target, getRangeHit(npc, rangedamage));
                        delayHit(npc, 2, target, getRangeHit(npc, rangedamage1));
                        if (Utils.getRandom(14) == 0)
                        	target.getToxin().applyToxin(ToxinType.POISON);
                        break;
                case 5:
                        npc.animate(new Animation(KalGer.Type.MAULED.getAnim()));
                        delayHit(npc, 1, target, getMeleeHit(npc, meleedamage));
                        if (Utils.getRandom(14) == 0)
                        	target.getToxin().applyToxin(ToxinType.POISON);
                        break;
                }
                return defs.getAttackDelay();
                }
}