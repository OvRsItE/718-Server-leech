package com.rs.mysql.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.chest.ReaperChest;

/**
 * Using this class:
 * To call this class, it's best to make a new thread. You can do it below like so:
 * new Thread(new Donation(player)).start();
 */
public class Donation implements Runnable {

	public static final String HOST = ""; // website ip address
	public static final String USER = "";
	public static final String PASS = "";
	public static final String DATABASE = "";

	private Player player;
	private Connection conn;
	private Statement stmt;

	/**
	 * The constructor
	 * @param player
	 */
	public Donation(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		try {
			if (!connect(HOST, DATABASE, USER, PASS)) {
				return;
			}

			String name = player.getUsername().replace("_", " ");
			ResultSet rs = executeQuery("SELECT * FROM payments WHERE player_name='"+name+"' AND status='charge:confirmed' AND claimed=0");

			while (rs.next()) {
				int item_number = rs.getInt("item_number");
				double paid = rs.getDouble("paid");
				int quantity = rs.getInt("quantity");

				System.out.println(item_number);

				switch (item_number) {// add products according to their ID in the ACP

				case 1023: // example
					player.getInventory().addItem(28756, quantity);
						player.promoreward+=5;

					break;
					case 1024: // example
						player.getInventory().addItem(28755, quantity);
							player.promoreward+= 10;
						break;
					case 1025: // example
						player.getInventory().addItem(28754, quantity);

							player.promoreward+= 20;
						break;
					case 1026: // example
						player.getInventory().addItem(28753, quantity);
							player.promoreward+= 30;
						break;
					case 1027: // example
						player.getInventory().addItem(28752, quantity);
							player.promoreward+= 50;
						break;
					case 1028: // example
						player.getInventory().addItem(28751, quantity);
							player.promoreward+= 100;
						break;



				}
				if (player.promoreward >= 500) {
					player.getInventory().addItem(6199, 1);
					player.getInventory().addItem(6199, 1);
					player.getInventory().addItem(6199, 1);
						player.promoreward-= 500;
				}

				rs.updateInt("claimed", 1); // do not delete otherwise they can reclaim!
				rs.updateRow();
			}

			destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @param host the host ip address or url
	 * @param database the name of the database
	 * @param user the user attached to the database
	 * @param pass the users password
	 * @return true if connected
	 */

	public boolean connect(String host, String database, String user, String pass) {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.conn = DriverManager.getConnection("jdbc:mysql://"+host+":3306/"+database, user, pass);
			return true;
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Disconnects from the MySQL server and destroy the connection
	 * and statement instances
	 */
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

	/**
	 * Executes an update query on the database
	 * @param query
	 * @see {@link Statement#executeUpdate}
	 */
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

	/**
	 * Executres a query on the database
	 * @param query
	 * @see {@link Statement#executeQuery(String)}
	 * @return the results, never null
	 */
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
