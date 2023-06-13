package com.blockchain.connection;

import java.sql.SQLException;

import java.sql.Connection;

public class DBConnection {

	static SqlConnectionPool pool = new SqlConnectionPool();

	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		Connection connection = pool.getConnectionFromPool();
		return connection;
	}

	public static void returnConnection(Connection connection) {
		pool.returnConnectionToPool(connection);
	}
}