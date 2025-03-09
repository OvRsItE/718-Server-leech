package com.rs.game.world.entity.player.content.xeric;

import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.activities.group.PlayerGroup;
import com.rs.game.world.entity.player.content.activities.group.PlayerGroupListener;
import com.rs.utility.Utils;
import lombok.RequiredArgsConstructor;

/**
 * @author ReverendDread
 * Created 4/5/2021 at 7:12 AM
 * @project 718---Server
 */
@RequiredArgsConstructor
public class ChambersGroupListener implements PlayerGroupListener {

    private final ChambersOfXeric chambers;

    @Override
    public boolean allowPlayerLeave(Player player, PlayerGroup group) {
        return true;
    }

    @Override
    public boolean allowPlayerJoin(Player player, PlayerGroup group) {
        if (group.getBoolean(ChambersOfXeric.STARTED)) {
            player.sendMessage("The raid has already begun. You can no longer enter.");
            return false;
        }
        return true;
    }

    @Override
    public void onMemberJoin(Player member, PlayerGroup group) {

    }

    @Override
    public void onMemberLeave(Player member, PlayerGroup group) {
        chambers.getDungeon().leave(member);
    }

    @Override
    public void onGroupDisband(PlayerGroup group) {
        group.notifyAll("Your group has been disbanded.");
    }

    @Override
    public void onLeaderUpdated(Player member, PlayerGroup group) {
        group.notifyAll(Utils.formatPlayerNameForDisplay(member.getDisplayName()) + " is now the group leader.");
    }

}
