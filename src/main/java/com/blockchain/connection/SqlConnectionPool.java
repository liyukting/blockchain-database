package com.blockchain.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


public class SqlConnectionPool {

	List<Connection> availableConnections = new ArrayList<Connection>();
	protected final Logger logger = Logger.getLogger(getClass());

	public SqlConnectionPool() {
		initializeConnectionPool();
	}

	private void initializeConnectionPool() {
		while (!checkIfConnectionPoolIsFull()) {
			availableConnections.add(createNewSQLConnectionForPool());//For SQL Server
		}
	}

	private synchronized boolean checkIfConnectionPoolIsFull() {
		if (availableConnections.size() < 10) {
			return false;
		}
		return true;
	}

	// Creating a connection
	private Connection createNewSQLConnectionForPool() {
		try {
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			Connection connection = (Connection) DriverManager.getConnection("jdbc:oracle:thin:@localhost:3306/blockchain_dev", "root",
					null);
			return connection;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return null;
	}
	
	public Connection createNewConnectionForPool(){
		try {
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			Connection connection = (Connection) DriverManager.getConnection("jdbc:oracle:thin:@localhost:3306/blockchain_dev", "root",
					null);
			return connection;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return null;
	}	

	public synchronized Connection getConnectionFromPool() {
		Connection connection = null;
		if(availableConnections.size() == 0) {
			availableConnections = new ArrayList<Connection>();
			initializeConnectionPool();
			logger.info("Connection ==> Renew: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
		}
		
		if (availableConnections.size() > 0) {
			connection = (Connection) availableConnections.get(0);
			availableConnections.remove(0);
		}else {
			logger.error("Error: Cannot create database connection!");
		}
		return connection;
	}

	public synchronized void returnConnectionToPool(Connection connection) {
		availableConnections.add(connection);
	}
}