package com.rs.mysql.impl;

import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.utility.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class FoxVote implements Runnable {

    public static final String HOST = "";
    public static final String USER = "";
    public static final String PASS = "";
    public static final String DATABASE = "";

    private Player player;
    private Connection conn;
    private Statement stmt;

    public FoxVote(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        try {
            if (!connect(HOST, DATABASE, USER, PASS)) {
                return;
            }

            String name = player.getUsername().replace("_", " ");
            ResultSet rs = executeQuery("SELECT * FROM votes WHERE username='"+name+"' AND claimed=0 AND voted_on != -1");

            while (rs.next()) {
                String ipAddress = rs.getString("ip_address");
                int siteId = rs.getInt("site_id");


                // -- ADD CODE HERE TO GIVE TOKENS OR WHATEVER
                player.getInventory().addItem(28750, 1);

                System.out.println("[FoxVote] Vote claimed by "+name+". (sid: "+siteId+", ip: "+ipAddress+")");

                if (Utils.random(200) == 1) {
                    player.getInventory().addItem(6199, 1);
                    World.sendWorldMessage("[<col=ff0000>Vote</col>] - <col=ff0000>" + Utils.formatPlayerNameForDisplay(player.getUsername()) +
                            "<col=ff0000> got lucky and recieved a mystery box from voting. <col=ff0000>", false);
                }
                player.voteclaimed++;

                if (player.voteclaimed == 2) {
                    player.voteclaimed = 0;
                    World.sendWorldMessage("[<col=ff0000>Vote</col>] - <col=ff0000>" + Utils.formatPlayerNameForDisplay(player.getUsername()) +
                            "<col=ff0000> has just voted on both sites and recieved there reward. <col=ff0000>", false);

                    player.votestreak++;
                    if (player.votestreak == 10) {
                        player.getInventory().addItemOrBank(6199, 1);
                        player.getPackets().sendGameMessage("[<col=ff0000>Vote] - For your 10th vote streak you recieve a mystery box");
                    }
                    if (player.votestreak == 25) {
                        player.getInventory().addItemOrBank(29484, 1);
                        player.getPackets().sendGameMessage("[<col=ff0000>Vote] - For your 25th vote streak you recieve a toxic mystery box");
                    }
                    if (player.votestreak == 50) {
                        player.getInventory().addItemOrBank(28750, 15);
                        player.getPackets().sendGameMessage("[<col=ff0000>Vote] - For your 50th vote streak you recieve 15 vote tickets");
                    }
                    if (player.votestreak == 100) {
                        player.getInventory().addItemOrBank(28825, 1);
                        player.getPackets().sendGameMessage("[<col=ff0000>Vote] - For your 100th vote streak you recieve a legendary egg");
                        player.votestreak = 0;
                    }
                }



                rs.updateInt("claimed", 1); // do not delete otherwise they can reclaim!
                rs.updateRow();
            }

            destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean connect(String host, String database, String user, String pass) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conn = DriverManager.getConnection("jdbc:mysql://"+host+":3306/"+database, user, pass);
            System.out.println("im getting here");
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void destroy() {
        try {
            conn.close();
            conn = null;
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public int executeUpdate(String query) {
        try {
            this.stmt = this.conn.createStatement(1005, 1008);
            int results = stmt.executeUpdate(query);
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public ResultSet executeQuery(String query) {
        try {
            this.stmt = this.conn.createStatement(1005, 1008);
            ResultSet results = stmt.executeQuery(query);
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
