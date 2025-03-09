package com.rs.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.rs.utility.Misc;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;

public class DonationManager {

	public static Connection con = null;
	public static Statement stmt;
	public static boolean connectionMade;

	
	public static void createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		String IP="";
        String DB="";
        String User="";
        String Pass=""; 
			con = DriverManager.getConnection("jdbc:mysql://"+IP+"/"+DB, User, Pass);
			stmt = con.createStatement();
		System.out.println("Connection to Donation database successful!"); //You can take these system prints out. Get annoying sometimes.
		} catch (Exception e) {
        System.out.println("Connection to Donation database failed");
        e.printStackTrace();
		}
	}
	
	public static void startProcess(final Player player) {  //Choose which payment options give which things.

	}
	
	public static ResultSet query(String s) throws SQLException {
		try {
			if (s.toLowerCase().startsWith("select")) {
				ResultSet rs = stmt.executeQuery(s);
				return rs;
			} else {
				stmt.executeUpdate(s);
			}
			return null;
		} catch (Exception e) {
			destroyConnection();
		}
		return null;
	}

	public static void destroyConnection() {
		try {
			stmt.close();
			con.close();
		} catch (Exception e) {
		}
	}

	public static boolean checkDonation(String playerName) {
   
        try {
			String name2 = playerName.replaceAll("_", " ");
            Statement statement = con.createStatement();
            String query = "SELECT * FROM donation WHERE username = '" + name2 + "'";
            ResultSet results = statement.executeQuery(query);
				while(results.next()) {
                    int tickets = results.getInt("tickets"); //tickets are "Recieved" technically. 0 for claimed, 1 for not claimed.
                        if(tickets == 1) {                     
                         return true;
                         }
                                
                        }
                } catch(SQLException e) {
                        e.printStackTrace();
                }
				
                return false;
				
        }
	
	public static int checkDonationItem(String playerName) {
        
        try {
			String name2 = playerName.replaceAll("_", " ");
			Statement statement = con.createStatement();
            String query = "SELECT * FROM donation WHERE username = '" + name2 + "'";
            ResultSet results = statement.executeQuery(query);
                while(results.next()) {
                    int productid = results.getInt("productid");
                        if(productid >= 1) {				                          
							return productid;
                            }
								                               
                        }
                } catch(SQLException e) {
                        e.printStackTrace();
                }
				
                return 0;
				
        }		
	
	public static boolean donationGiven(String playerName) {       
              
			  try
                {
				String name2 = playerName.replaceAll("_", " ");
				query("DELETE FROM `donation` WHERE username = '"+name2+"';");
                       // query("UPDATE donations SET tickets = 0 WHERE username = '" + playerName + "'");
						//query("UPDATE donations SET productid = 0 WHERE username = '" + playerName + "'");
						
                } catch (Exception e) {
                        e.printStackTrace();
						
                        return false;
                }
                return true;
        }	
	
}