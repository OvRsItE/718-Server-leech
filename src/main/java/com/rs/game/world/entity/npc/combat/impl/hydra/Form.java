package com.rs.game.world.entity.npc.combat.impl.hydra;


public enum Form {

    GREEN(8615, 8616, 8237, 3, -1, 8234, 8235, 8236, 34568),
    BLUE(8619, 8617, 8244, 2, 8238, 8241, 8242, 8243, 34569),
    RED(8620, 8618, 8251, 3, 8245, 8248, 8249, 8250, 34570),
    GREY(8621, -1, -1, 0, 8252, -1, 8256, 8255, -1),
    ;

    Form(int npcId, int loseHeadNPCId, int loseHeadAnim, int loseHeadDuration, int fadeInAnim, int middleHeadAttackAnim, int rightHeadsAttackAnim, int leftHeadsAttackAnim, int weaknessVent) {
        this.npcId = npcId;
        this.loseHeadNPCId = loseHeadNPCId;
        this.loseHeadAnim = loseHeadAnim;
        this.loseHeadDuration = loseHeadDuration;
        this.fadeInAnim = fadeInAnim;
        this.middleHeadAttackAnim = middleHeadAttackAnim;
        this.rightHeadsAttackAnim = rightHeadsAttackAnim;
        this.leftHeadsAttackAnim = leftHeadsAttackAnim;
        this.weaknessVent = weaknessVent;
    }

    protected int npcId; // main npc id (the one that fights)
    protected int loseHeadNPCId; // npc id this phase transforms into to do the losing head animation
    protected int loseHeadAnim; // the losing head animation
    protected int loseHeadDuration; // how many ticks to wait for lose head anim
    protected int fadeInAnim; // the animation this form performs when switching into it (comes right after the previous form's lose head animation)
    protected int middleHeadAttackAnim, rightHeadsAttackAnim, leftHeadsAttackAnim; // attack anims
    protected int weaknessVent;

}