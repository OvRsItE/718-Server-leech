package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class ClanMotto extends Dialogue {

    @Override
    public void start() {
	player.getInterfaceManager().sendChatBoxInterface(1103);
    }

    @Override
    public void run(int interfaceId, int componentId) {
	end();

    }

    @Override
    public void finish() {

    }

}
