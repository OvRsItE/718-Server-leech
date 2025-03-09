package com.rs.game.world.entity.listeners;

import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.updating.impl.Hit;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Hit Defend Types:
 * pre - Called BEFORE hit damage modifications. (Such as dragonfire reductions, defensive misses, etc)
 * post - Called AFTER hit damage modifications. ( ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ )
 * final - Called AFTER post defend. (Damage will cap at target's max hp and will not change again)
 */
public class HitListener {

    /**
     * Hits happening to an entity
     */
    public Consumer<Hit> preDamage;
    public Consumer<Hit> postDamage;

    public HitListener preDamage(Consumer<Hit> preDamage) {
        this.preDamage = preDamage;
        return this;
    }

    public HitListener postDamage(Consumer<Hit> postDamage) {
        this.postDamage = postDamage;
        return this;
    }

    /**
     * Hits happening to a target of an entity.
     */

    public BiConsumer<Hit, Entity> preTargetDefend;
    public BiConsumer<Hit, Entity> postTargetDefend;

    public BiConsumer<Hit, Entity> preTargetDamage;
    public BiConsumer<Hit, Entity> postTargetDamage;

    public HitListener preTargetDefend(BiConsumer<Hit, Entity> preTargetDefend) {
        this.preTargetDefend = preTargetDefend;
        return this;
    }

    public HitListener postTargetDefend(BiConsumer<Hit, Entity> postTargetDefend) {
        this.postTargetDefend = postTargetDefend;
        return this;
    }

    public HitListener preTargetDamage(BiConsumer<Hit, Entity> preTargetDamage) {
        this.preTargetDamage = preTargetDamage;
        return this;
    }

    public HitListener postTargetDamage(BiConsumer<Hit, Entity> postTargetDamage) {
        this.postTargetDamage = postTargetDamage;
        return this;
    }

}
