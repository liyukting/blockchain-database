package com.blockchain.dao.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.sql.PooledConnection;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.blockchain.connection.MySqlConnectionPool;
import com.blockchain.entity.MyResponse;
import com.blockchain.entity.SysRole;
import com.blockchain.entity.SysTokenAccess;
import com.blockchain.entity.User;
import com.blockchain.service.UserService;
import com.blockchain.util.StringUtility;

import oracle.jdbc.OracleTypes;

public class CommonDAO {
	protected final Logger logger = Logger.getLogger(getClass());
	Connection conn;
	@Autowired
	private UserService userService;
	final SimpleDateFormat MYSQLDATEFORMATTER = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

	public User getUser(String email, String password) {
		String json = null;
		User user = new User(email, password);
		try {
			if (conn == null) {
				PooledConnection pc = MySqlConnectionPool.getConnection();
				if (pc == null) {
					logger.info("Connection pool is null!");
					return null;
				}
				conn = pc.getConnection();
				if (conn == null) {
					logger.info("Connection is null!");
					return null;
				}
			}
			String encrypted = StringUtility.applySha256(password);
			String sql = "CALL `blockchain_dev`.`spLogin`('" + email + "', '" + encrypted + "');";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			if (rs != null && rs.next())
				json = rs.getString(2);
			ps.close();
			logger.info(json);
			return null;
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			logger.error("Connection is ClassNotFoundException: " + e1.getMessage());
			conn = null;
			return null;
		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.error("Connection is SQLException: " + e1.getMessage());
			conn = null;
			return null;
		}
	}

	public SysTokenAccess login(User user) {
		String result = null;
		String json = null;
		try {
			if (conn == null) {
				PooledConnection pc = MySqlConnectionPool.getConnection();
				if (pc == null) {
					logger.info("Connection pool is null!");
					return null;
				}
				conn = pc.getConnection();
				if (conn == null) {
					logger.info("Connection is null!");
					return null;
				}
			}
			String sql = "CALL `blockchain_dev`.`spLogin`('" + user.getUserEmail() + "', '" + user.getPassword()
					+ "');";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			if (rs != null && rs.next()) {
				result = rs.getString(1);
				json = rs.getString(2);
			}
			if (null != result && result.equalsIgnoreCase("1")) {
				logger.info(sql);
				JSONObject accessCode = new JSONObject(json);
				String secu = accessCode.getString("accessCode");
				SysTokenAccess token = new SysTokenAccess(secu);
				token.setId(Long.valueOf(accessCode.get("loginTimeId").toString()));
				token.setDate(MYSQLDATEFORMATTER.parse(accessCode.get("date").toString()));
				token.setDueDate(MYSQLDATEFORMATTER.parse(accessCode.get("dueDate").toString()));
				user=findUserBySecu(token);
				if (result.equalsIgnoreCase("1")) {
					token.setUser(user);
					return token;
				}
				return token;
			}
			ps.close();
			return null;
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			logger.error("Connection is ClassNotFoundException: " + e1.getMessage());
			conn = null;
			return null;
		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.error("Connection is SQLException: " + e1.getMessage());
			conn = null;
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public SysTokenAccess register(User user) {
		String result = null;
		String json = null;
		try {
			if (conn == null) {
				PooledConnection pc = MySqlConnectionPool.getConnection();
				if (pc == null) {
					logger.info("Connection pool is null!");
					return null;
				}
				conn = pc.getConnection();
				if (conn == null) {
					logger.info("Connection is null!");
					return null;
				}
			}
			String sql = "CALL `blockchain_dev`.`spRegister`('" + user.getUserEmail() + "', '" + user.getPassword()
					+ "', 'helper');";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			if (rs != null && rs.next()) {
				result = rs.getString(1);
				json = rs.getString(2);
			}
			if (null != result && result.equalsIgnoreCase("1")) {
				logger.info(sql);
				return login(user);
			}
			ps.close();
			return null;
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			logger.error("Connection is ClassNotFoundException: " + e1.getMessage());
			conn = null;
			return null;
		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.error("Connection is SQLException: " + e1.getMessage());
			conn = null;
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void logout(SysTokenAccess token) {
		try {
			if (conn == null) {
				PooledConnection pc = MySqlConnectionPool.getConnection();
				if (pc == null) {
					logger.info("Connection pool is null!");
				}
				conn = pc.getConnection();
				if (conn == null) {
					logger.info("Connection is null!");
				}
			}
			String sql = "CALL `blockchain_dev`.`spLogout`('" + token.getAccessCode() + "');";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.execute();
			ps.close();
			logger.info(sql);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			logger.error("Connection is ClassNotFoundException: " + e1.getMessage());
			conn = null;
		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.error("Connection is SQLException: " + e1.getMessage());
			conn = null;
		}
	}

	public SysTokenAccess findTokenAccess(String secu) {
		String result = null;
		String json = null;
		try {
			if (conn == null) {
				PooledConnection pc = MySqlConnectionPool.getConnection();
				if (pc == null) {
					logger.info("Connection pool is null!");
					return null;
				}
				conn = pc.getConnection();
				if (conn == null) {
					logger.info("Connection is null!");
					return null;
				}
			}
			String sql = "CALL `blockchain_dev`.`spSecurityCheck`('" + secu + "');";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			if (rs != null && rs.next()) {
				result = rs.getString(1);
				json = rs.getString(2);
			}
			ps.close();
			if (result.equalsIgnoreCase("1")) {
				logger.info(sql);
				JSONObject accessCode = new JSONObject(json);
				SysTokenAccess token = new SysTokenAccess(secu);
				token.setId(Long.valueOf(accessCode.get("loginTimeId").toString()));
				token.setDate(MYSQLDATEFORMATTER.parse(accessCode.get("date").toString()));
				token.setDueDate(MYSQLDATEFORMATTER.parse(accessCode.get("dueDate").toString()));
				return token;
			}
			return null;
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			logger.error("Connection is ClassNotFoundException: " + e1.getMessage());
			conn = null;
		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.error("Connection is SQLException: " + e1.getMessage());
			conn = null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	public String getHelperProfileBySecu(SysTokenAccess token) {
		String result=null;
		String json=null;
		try {
			if (conn == null) {
				PooledConnection pc = MySqlConnectionPool.getConnection();
				if (pc == null) {
					logger.info("Connection pool is null!");
				}
				conn = pc.getConnection();
				if (conn == null) {
					logger.info("Connection is null!");
				}
			}
			String sql = "CALL `blockchain_dev`.`spHelperProfile`('" + token.getAccessCode() + "');";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			if (rs != null && rs.next()) {
				result = rs.getString(1);
				json = rs.getString(2);
			}
			if (result.equalsIgnoreCase("1")) {
				logger.info(sql);
				return json;
			}
			return null;
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			logger.error("Connection is ClassNotFoundException: " + e1.getMessage());
			conn = null;
		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.error("Connection is SQLException: " + e1.getMessage());
			conn = null;
		}
		return null;

	}

	public User findUserBySecu(SysTokenAccess token) {
		String result=null;
		String json=null;
		User user=new User();
		try {
			if (conn == null) {
				PooledConnection pc = MySqlConnectionPool.getConnection();
				if (pc == null) {
					logger.info("Connection pool is null!");
				}
				conn = pc.getConnection();
				if (conn == null) {
					logger.info("Connection is null!");
				}
			}
			String sql = "CALL `blockchain_dev`.`spGetUserBySecu`('" + token.getAccessCode() + "');";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			if (rs != null && rs.next()) {
				result = rs.getString(1);
				json = rs.getString(2);
			}
			if (result.equalsIgnoreCase("1")) {
				logger.info(sql);
				JSONObject userInfo = new JSONObject(json);
				user.setUserEmail((String) userInfo.get("userEmail"));
				user.setPassword((String) userInfo.get("userPassword"));
				user.setId(Long.valueOf(userInfo.get("userId").toString()));
				user.addUserGroup(findRole(userInfo.get("userGroup").toString()));
				user.setCreatedAt((String) userInfo.get("createdAt"));
				token.setUser(user);
				return user;
			}
			return null;
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			logger.error("Connection is ClassNotFoundException: " + e1.getMessage());
			conn = null;
		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.error("Connection is SQLException: " + e1.getMessage());
			conn = null;
		}
		return null;

	}

	public String getHelperList() {
		String result=null;
		String json=null;
		try {
			if (conn == null) {
				PooledConnection pc = MySqlConnectionPool.getConnection();
				if (pc == null) {
					logger.info("Connection pool is null!");
				}
				conn = pc.getConnection();
				if (conn == null) {
					logger.info("Connection is null!");
				}
			}
			String sql = "CALL `blockchain_dev`.`spHelperList`();";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.execute();
			logger.info(sql);

			ResultSet rs = ps.getResultSet();
			if (rs != null && rs.next()) {
				result = rs.getString(1);
				json = rs.getString(2);
			}
			if (result.equalsIgnoreCase("1")) {
				logger.info(sql);
				return json;
			}
			return null;
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			logger.error("Connection is ClassNotFoundException: " + e1.getMessage());
			conn = null;
		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.error("Connection is SQLException: " + e1.getMessage());
			conn = null;
		}
		return null;

	}

	public JSONArray convertResultSetToJSON(ResultSet rs) throws SQLException {
		JSONArray json = new JSONArray();

		if (rs == null) {
			logger.error("Warning: ResultSet is null.");
			return json;
		}

		try {
			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {
				int numColumns = rsmd.getColumnCount();
				JSONObject obj = new JSONObject();

				for (int i = 1; i < numColumns + 1; i++) {
					String column_name = rsmd.getColumnName(i);

					if (rsmd.getColumnType(i) == OracleTypes.VARCHAR) {
						obj.put(column_name, rs.getString(column_name));
					} else if (rsmd.getColumnType(i) == OracleTypes.NUMERIC) {
						obj.put(column_name, rs.getLong(column_name));
					} else if (rsmd.getColumnType(i) == OracleTypes.NVARCHAR) {
						obj.put(column_name, rs.getNString(column_name));
					} else if (rsmd.getColumnType(i) == OracleTypes.INTEGER) {
						obj.put(column_name, rs.getInt(column_name));
					} else if (rsmd.getColumnType(i) == OracleTypes.TIMESTAMP) {
						obj.put(column_name, rs.getTimestamp(column_name));
					} else if (rsmd.getColumnType(i) == OracleTypes.DATE) {
						obj.put(column_name, rs.getDate(column_name));
					} else if (rsmd.getColumnType(i) == OracleTypes.ARRAY) {
						obj.put(column_name, rs.getArray(column_name));
					} else if (rsmd.getColumnType(i) == OracleTypes.BIGINT) {
						obj.put(column_name, rs.getInt(column_name));
					} else if (rsmd.getColumnType(i) == OracleTypes.BOOLEAN) {
						obj.put(column_name, rs.getBoolean(column_name));
					} else if (rsmd.getColumnType(i) == OracleTypes.BLOB) {
						obj.put(column_name, rs.getBlob(column_name));
					} else if (rsmd.getColumnType(i) == OracleTypes.DOUBLE) {
						obj.put(column_name, rs.getDouble(column_name));
					} else if (rsmd.getColumnType(i) == OracleTypes.FLOAT) {
						obj.put(column_name, rs.getFloat(column_name));
					} else if (rsmd.getColumnType(i) == OracleTypes.TINYINT) {
						obj.put(column_name, rs.getInt(column_name));
					} else if (rsmd.getColumnType(i) == OracleTypes.SMALLINT) {
						obj.put(column_name, rs.getInt(column_name));
					} else {
						obj.put(column_name, rs.getObject(column_name));
					}
				}

				json.put(obj);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
		}

		return json;
	}

	public SysRole findRole(String roleName) {
		if (null == roleName) {
			return SysRole.ROLE_GUEST;
		}
		switch (roleName) {
		case "helper":
			return SysRole.ROLE_HELPER;
		case "employer":
			return SysRole.ROLE_EMPLOYER;
		case "training school":
			return SysRole.ROLE_TRAININGSCHOOL;
		case "agent":
			return SysRole.ROLE_AGENT;
		case "guest":
			return SysRole.ROLE_GUEST;
		default:
			return null;
		}
	}
}
