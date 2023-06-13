package com.blockchain.rest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.PooledConnection;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.blockchain.Chain;
import com.blockchain.FirstChain;
import com.blockchain.entity.MyResponse;
import com.blockchain.util.StringUtility;
import com.blockchain.connection.MySqlConnectionPool;

@Component
@Path("/Greeting")
public class GreetingResource {
	protected final Logger logger = Logger.getLogger(getClass());
	Connection conn;

	@Path("/post")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public MyResponse greeting(@DefaultValue("Hello World!") @FormParam("json") String json) {
		return new MyResponse("1", json);
	}

	@Path("/reset-chains")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public MyResponse resetChains() {
		FirstChain firstChain = new FirstChain();
		firstChain.blockChain.clear();
		Chain chain = new Chain();
		chain.blockchain.clear();
		return new MyResponse("1", "[]");
	}

	@Path("/chain")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public MyResponse chain(@FormParam("data") String data) {
		FirstChain chain = new FirstChain();
		return new MyResponse(chain.isChainValid() ? "1" : "ERROR_INVALID", chain.appendBlock(data).toString());
	}

	@Path("/transaction")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public MyResponse transaction(@FormParam("difficulty") String difficulty) {
		int level = 5;
		if (difficulty.matches("-?\\d+(\\.\\d+)?"))
			level = Integer.parseInt(difficulty);
		Chain chain = new Chain();
		return new MyResponse("1", chain.transaction().toString());
	}

	@Path("/oracle-test")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public MyResponse oracleTest() throws ClassNotFoundException, SQLException {
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
		String sql = "INSERT INTO `blockchain_dev`.`helper` (`helperName`,`helperGender`,`helperPassport`,`helperNation`,`education`,`religion`,`isEmployed`,`agents`) \r\n"
				+ "VALUES (\"helperName\",\"M\",\"helperPassport\",\"helperNation\",\"education\",\"religion\",true,\"agents\")";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.execute();
		ps.close();
		return new MyResponse("1", "[]");
	}

	@Path("/register-test")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public MyResponse registerTest(@FormParam("email") String email, @FormParam("password") String password)
			throws ClassNotFoundException, SQLException {
		MyResponse response = null;
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
		String sql = "CALL `blockchain_dev`.`spRegister`('" + email + "', '" + encrypted + "', 'helper');";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.execute();
		ResultSet rs = ps.getResultSet();
		if (rs != null && rs.next())
			response = new MyResponse(rs.getString(1), rs.getString(2));
		ps.close();
		return response;
	}

	@Path("/helper-update")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public MyResponse helperUpdate(@FormParam("secu") String secu, @FormParam("dataJson") String dataJson)
			throws ClassNotFoundException, SQLException {
		/* check if security status is logged in */
//		MyResponse response = securityCheck(secu);
//		if(!securityCheck(secu).getResult().equalsIgnoreCase("1"))
//			return response;
		MyResponse response = null;
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
		String sql = "CALL `blockchain_dev`.`spHelperUpdate`('" + dataJson + "');";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.execute();
		ResultSet rs = ps.getResultSet();
		if (rs != null && rs.next())
			response = new MyResponse(rs.getString(1), rs.getString(2));
		ps.close();
		return response;
	}

}