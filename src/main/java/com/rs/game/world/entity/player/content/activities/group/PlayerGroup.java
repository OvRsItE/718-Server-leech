package com.rs.game.world.entity.player.content.activities.group;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;
import java.security.acl.Group;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Represents a group of players. Used for things like chambers of xeric.
 *
 * @author ReverendDread
 * Created 4/5/2021 at 4:16 AM
 * @project 718---Server
 */
public abstract class PlayerGroup {

    /**
     * The event listener for this group.
     */
    @Setter private PlayerGroupListener listener;

    /**
     * The group leader.
     */
    @Getter private Player leader;

    /**
     * Players in this group, inclues leader.
     */
    @Getter private final LinkedList<Player> members = Lists.newLinkedList();

    /**
     * Attributes attached to this group of players.
     */
    private final Map<String, String> attributes = Maps.newConcurrentMap();

    /**
     * Constructs a new player group, with the desired player as its leader.
     * @param leader
     *          the leader.
     */
    public PlayerGroup(Player leader) {
        this(leader, null);
    }

    /**
     * Constructs a new player group, with the desired player as its leader.
     * @param leader
     *          the leader.
     * @param listener
     *          the group event listener.
     */
    public PlayerGroup(Player leader, PlayerGroupListener listener) {
        this.listener = listener;
        this.members.add(leader);
        this.leader = leader;
        notifyMember(leader, "You've been made leader of the group.");
    }

    /**
     * Attempts to add the desired player to the group.
     * @param player
     *          the player to add.
     * @return
     *          true if the player was added successfully, false otherwise.
     */
    public final boolean join(Player player) {
        if (!members.contains(player) && listener != null && listener.allowPlayerJoin(player, this)) {
            members.add(player);
            listener.onMemberJoin(player, this);
            return true;
        }
        return false;
    }

    /**
     * Attempts to remove the desired player to the group.
     * @param player
     *          the player to remove.
     * @return
     *          true if the player was removed successfully, false otherwise.
     */
    public final boolean leave(Player player) {
        if (members.contains(player) && listener != null && listener.allowPlayerLeave(player, this)) {
            members.remove(player);
            listener.onMemberLeave(player, this);
            return true;
        }
        return false;
    }

    /**
     * Sets the groups leader to the desired player.
     * @param leader
     *          the new leader.
     */
    public final void setLeader(Player leader) {
        this.leader = leader;
        if (listener != null)
            listener.onLeaderUpdated(leader, this);
    }

    /**
     * Checks if the desired player is the group leader.
     * @param player
     * @return
     */
    public boolean isLeader(Player player) {
        return getLeader().getUsername().equals(player.getUsername());
    }

    /**
     * Disbands the group, discarding all players.
     */
    public void disband() {
        if (listener != null)
            listener.onGroupDisband(this);
        members.clear();
    }

    /**
     * Notify's a group member.
     * @param member
     * @param message
     */
    public final void notifyMember(Player member, String message) {
       //notify the user formatted with the group prefix
        member.sendMessage(notifyPrefix() + message);
    }

    /**
     * Notifys all members who meet the predicate.
     * @param message
     *          the message to send.
     * @param predicate
     *          the predicate filter.
     */
    public final void notifyAll(String message, Predicate<Player> predicate) {
        members.stream().filter(predicate).forEach(member -> notifyMember(member, message));
    }

    /**
     * Notify's all players in the group.
     * @param message
     *          the message to send.
     */
    public final void notifyAll(String message) {
        members.forEach(member -> notifyMember(member, message));
    }

    /**
     * Gets the desired attribute as an int.
     * @param attribute
     *          the attribute.
     * @return
     */
    public final int getInt(String attribute) {
        return Integer.parseInt(attributes.get(attribute));
    }

    /**
     * Gets the desired attribute as a long.
     * @param attribute
     *          the attribute.
     * @return
     */
    public final long getLong(String attribute) {
        return Long.parseLong(attributes.get(attribute));
    }

    /**
     * Gets the desired attribute as a boolean.
     * @param attribute
     *          the attribute.
     * @return
     */
    public final boolean getBoolean(String attribute) {
        return Boolean.parseBoolean(attributes.get(attribute));
    }

    /**
     * Gets the desired attribute as a byte.
     * @param attribute
     *          the attribute.
     * @return
     */
    public final byte getByte(String attribute) {
        return Byte.parseByte(attributes.get(attribute));
    }

    /**
     * Gets the desired attribute as a string.
     * @param attribute
     *          the attribute.
     * @return
     */
    public final String getString(String attribute) {
        return attributes.get(attribute);
    }

    /**
     * Sets the desired string.
     * @param key
     *          the key.
     * @param value
     *          the value.
     * @return
     *          the previous value assosiated with this key, otherwise null.
     */
    public final String setAttribute(String key, Object value) {
        return attributes.put(key, String.valueOf(value));
    }

    /**
     * Gets the amount of members in this group.
     * @return
     */
    public final int getSize() {
        return members.size();
    }

    /**
     * The name of the group, i.e Chambers of Xeric, Theatre of Blood.
     * @return
     *         the name of the group.
     */
    public abstract String name();

    /**
     * The prefix for notify messages.
     * @return
     *      the prefix.
     */
    public abstract String notifyPrefix();

}
