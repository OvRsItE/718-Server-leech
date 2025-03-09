package com.rs.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import lombok.Getter;
import lombok.Setter;

public class Database {

	@Getter private Connection connection;
	@Getter private Statement statement;

	private String host;
	private String user;
	private String pass;
	private String database;
	
	@Getter @Setter private int timeout;

	public Database(String host, String user, String pass, String database) {
		this.host = host;
		this.user = user;
		this.pass = pass;
		this.database = database;
		this.timeout = 2500;
	}

	public boolean init() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			this.connection = DriverManager.getConnection(
					"jdbc:mysql://" + host + ":3306/" + database + "?connectTimeout=" + timeout, user, pass);
			return true;
		} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			System.err.println("[Database] Failed to connect! Reason: " + e.getMessage().split("\n")[0] + "");
			return false;
		}
	}

	public boolean initBatch() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + database
					+ "?connectTimeout=" + timeout + "&rewriteBatchedStatements=true", user, pass);
			return true;
		} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			System.err.println("[Database] Failed to connect! Reason: " + e.getMessage().split("\n")[0] + "");
			return false;
		}
	}

	public int executeUpdate(String query) {
		try {
			this.statement = this.connection.createStatement(1005, 1008);
			int results = statement.executeUpdate(query);
			return results;
		} catch (SQLException ex) {
			System.err.println("[Database] Update failed! Reason: " + ex.getMessage().split("\n")[0] + "");
		}
		return -1;
	}

	public ResultSet executeQuery(String query) {
		try {
			this.statement = this.connection.createStatement(1005, 1008);
			ResultSet results = statement.executeQuery(query);
			return results;
		} catch (SQLException ex) {
			System.err.println("[Database] Query failed! Reason: " + ex.getMessage().split("\n")[0] + "");
		}
		return null;
	}

	public PreparedStatement prepare(String query) throws SQLException {
		return connection.prepareStatement(query);
	}

	public void destroyAll() {
		try {
			connection.close();
			connection = null;
			if (statement != null) {
				statement.close();
				statement = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
