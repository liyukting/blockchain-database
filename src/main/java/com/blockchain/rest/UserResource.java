package com.blockchain.rest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.PooledConnection;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blockchain.connection.MySqlConnectionPool;
import com.blockchain.dao.common.CommonDAO;
import com.blockchain.entity.MyResponse;
import com.blockchain.entity.SysTokenAccess;
import com.blockchain.entity.User;
import com.blockchain.service.UserService;
import com.blockchain.util.StringUtility;

@Component
@Path("/user")
public class UserResource {
	private CommonDAO commonDAO = new CommonDAO();

	protected final Logger logger = Logger.getLogger(getClass());
	Connection conn;
	@Autowired
	private UserService userService;

	@Path("/user-register")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public MyResponse userRegister(@FormParam("email") String email, @FormParam("password") String password) {
		User user = new User(email, StringUtility.applySha256(password));
		SysTokenAccess token = userService.register(user);
		if (null != token) {
			return new MyResponse("1", "{\"secu\":\"" + token.toString() + "\"}");
		}
		return new MyResponse("-1", "");
	}

	@Path("/user-login")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public MyResponse userLogin(@FormParam("email") String email, @FormParam("password") String password) {
		User user = new User(email, StringUtility.applySha256(password));
		SysTokenAccess token = userService.login(user);
		if (null != token) {
			return new MyResponse("1", "{\"secu\":\"" + token.toString() + "\"}");
		}
		return new MyResponse("-1", "");
	}

	@Path("/user-logout")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public MyResponse userLogout(@FormParam("secu") String secu) {
		SysTokenAccess token = new SysTokenAccess(secu);
		userService.logout(token);
		return new MyResponse("1", "");
	}

	@Path("/security-check")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public MyResponse securityCheck(@FormParam("secu") String secu) {
		SysTokenAccess token = userService.findTokenAccess(secu);
		if (null != token)
			return new MyResponse("1", token.toString());
		else
			return new MyResponse("-1", "");
	}

	@Path("/user-profile")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public MyResponse userProfile(@FormParam("secu") String secu) {
		SysTokenAccess token = userService.findTokenAccess(secu);
		if (null != token) {
			return new MyResponse("1", commonDAO.getHelperProfileBySecu(token));
		}
		return new MyResponse("-1", "");
	}

	@Path("/helper-list")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public MyResponse getHelperList() {
		return new MyResponse("1", commonDAO.getHelperList());
	}

}