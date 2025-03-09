package com.rs.utility;
/*
package com.rs.utils;
     
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.rs.game.player.Player;
import com.rs.game.player.Skills;
     
    public class AdventureLog {
     
        public static Connection con = null;
        public static Statement stmt;
        public static boolean connectionMade;
          
        public static void createConnection() {
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();//opens class
                String IP="31.220.17.87";//connection ip
                String DB="mirag555_adventurelog";//database name
                String User="mirag555_HS";//username
                String Pass="m@tST,U*)h8%"; //password
                con = DriverManager.getConnection("jdbc:mysql://"+IP+"/"+DB, User, Pass);//creates connection
                stmt = con.createStatement();
            } catch (Exception e) {//catches if connection failed
                Logger.log("Hiscores", "Connection to SQL database failed!");//tells you it failed @ the run.bat
                e.printStackTrace();
            }
        }
     
        public static String Timer() {
        	DateTime jodaTime = new DateTime();
        	DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM dd HH:mm:ss a");
        	return "[" +formatter.print(jodaTime)+ "]";
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
     
        
        public static boolean DeleteHighScore(Player player) {//saves hiscores
            try {
                createConnection();//creates connection
                Skills skills = player.getSkills();//gets skills
                int[] overall = getOverall(player);//just a int
                query("DELETE FROM `hs_users` WHERE username = '"+player.getUsername()+"';");
                                destroyConnection();
            } catch (Exception e) {
                    Logger.log("Hiscores", "Error, could not save highscores for " + player.getUsername() +".");//couldnt save the player on hiscores, it tells u it didnt on the run.bat
                return false;
            }
            return true;
        }
		
		public static boolean saveHighScore(Player player) {//saves hiscores
            try {
                createConnection();//creates connection
                Skills skills = player.getSkills();//gets skills
                int[] overall = getOverall(player);//just a int
                query("INSERT INTO `hs_users` (`username`,`Time`,`Action`) VALUES ('"+player.getUsername()+"','"+Timer()+"'");
                // Logger.log("Hiscores", "Hiscores created for " + player.getUsername() + ".");
                destroyConnection();
            } catch (Exception e) {
                    Logger.log("Hiscores", "Error, could not save highscores for " + player.getUsername() +".");//couldnt save the player on hiscores, it tells u it didnt on the run.bat
                return false;
            }
            return true;
        }
		
		public static long getTotalXp(Player player) {
		long totalxp = 0;
		for (double xp : player.getSkills().getXp()) {
			totalxp += xp;
		}
		return totalxp;
	}	
     
        public static void restore(Player player) throws SQLException  {
            createConnection();//creates connection
            Statement statement = con.createStatement();
            String query = "SELECT * FROM hs_users WHERE playerName = '" + player.getUsername() + "'";//read code blablabla
            ResultSet results = statement.executeQuery(query);
            if (results.next()) {
                    for (int skill = 0; skill < player.getSkills().level.length; skill++) {
                            player.getSkills().level[skill] = (short) player.getSkills().getLevelForXp(skill);
                            player.getSkills().refresh(skill);
                    }
            }
            destroyConnection();
        }
     
        public static int[] getOverall(Player player) {
            int totalLevel = 0;
            int totalXp = 0;
            for(int i = 0; i < 24; i++) {
                totalLevel += player.getSkills().getLevelForXp(i);
            }
            for(int i = 0; i < 24; i++) {
                totalXp += player.getSkills().getXp(i);
            }
            return new int[] {totalLevel, totalXp};
        }
       
    }

*/