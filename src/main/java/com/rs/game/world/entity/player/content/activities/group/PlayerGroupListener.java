package com.rs.game.world.entity.player.content.activities.group;

import com.rs.game.world.entity.player.Player;

/**
 * @author ReverendDread
 * Created 4/5/2021 at 4:14 AM
 * @project 718---Server
 */
public interface PlayerGroupListener {

    boolean allowPlayerLeave(Player player, PlayerGroup group);

    boolean allowPlayerJoin(Player player, PlayerGroup group);

    void onMemberJoin(Player member, PlayerGroup group);

    void onMemberLeave(Player member, PlayerGroup group);

    void onGroupDisband(PlayerGroup group);

    void onLeaderUpdated(Player member, PlayerGroup group);

}
