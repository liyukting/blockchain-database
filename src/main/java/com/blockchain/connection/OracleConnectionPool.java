package com.blockchain.connection;

import java.sql.SQLException;

import javax.sql.PooledConnection;

import oracle.jdbc.pool.OracleConnectionPoolDataSource;

public class OracleConnectionPool {

	public static PooledConnection getConnection() throws ClassNotFoundException, SQLException {
		try {
		    OracleConnectionPoolDataSource ocpds = new OracleConnectionPoolDataSource();
		    ocpds.setURL("jdbc:oracle:thin:@localhost:1521/blockchain_dev");
		    ocpds.setUser("root");
		    ocpds.setPassword(null);
		    PooledConnection conn = ocpds.getPooledConnection();

			return conn;

		} catch (SQLException e) {
			System.out.println("\nAn SQL exception occurred : " + e.getMessage());
			return null;
		}
	}

}