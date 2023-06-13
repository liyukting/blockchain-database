package com.blockchain.connection;

import java.sql.SQLException;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;


public class MySqlConnectionPool {

    public static ConnectionPoolDataSource getMysqlDataSource() {
    	MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();

        // Set dataSource Properties
        dataSource.setServerName("localhost");
        dataSource.setPortNumber(3306);
        dataSource.setDatabaseName("blockchain_dev");
        dataSource.setUser("root");
        dataSource.setPassword("949466");
        return dataSource;
      }

	public static PooledConnection getConnection() throws ClassNotFoundException, SQLException {
		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			Class.forName("com.mysql.cj.jdbc.Driver");
			PooledConnection  mysqlPooledConnection = getMysqlDataSource().getPooledConnection();

			return mysqlPooledConnection;

		} catch (SQLException e) {
			System.out.println("\nAn SQL exception occurred : " + e.getMessage());
			return null;
		}
	}

}