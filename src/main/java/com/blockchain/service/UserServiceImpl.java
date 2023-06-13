package com.blockchain.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.blockchain.dao.accesstoken.AccessTokenDao;
import com.blockchain.dao.common.CommonDAO;
import com.blockchain.dao.user.UserDao;
import com.blockchain.entity.MyResponse;
import com.blockchain.entity.SysTokenAccess;
import com.blockchain.entity.User;

public class UserServiceImpl implements UserService
{
    private UserDao userDao;
    private AccessTokenDao accessTokenDao;
    private CommonDAO commonDAO = new CommonDAO();
    
    protected UserServiceImpl()
    {
        /* Reflection instantiation */
    }

    public UserServiceImpl(UserDao userDao, AccessTokenDao accessTokenDao)
    {
        this.userDao = userDao;
        this.accessTokenDao = accessTokenDao;
    }

    
    @Override
	public User findUserByAccessToken(SysTokenAccess token) {
		return commonDAO.findUserBySecu(token);
	}

	@Override
    @Transactional(readOnly = true)
    public User getUser(String userID, String pass) {
    	return commonDAO.getUser(userID, pass);
    }

    @Transactional(readOnly = true)
    public SysTokenAccess login(User user) {
    	return commonDAO.login(user);
    }

    @Transactional(readOnly = true)
    public SysTokenAccess register(User user) {
    	return commonDAO.register(user);
    }

    @Transactional(readOnly = true)
    public void logout(SysTokenAccess token) {
    	commonDAO.logout(token);
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        return this.userDao.loadUserByUsername(username);
    }
    
    @Override
    @Transactional(readOnly = true)
    public SysTokenAccess findTokenAccess(String accessTokenString) {
    	return commonDAO.findTokenAccess(accessTokenString);
    }

	@Override
	public SysTokenAccess createTokenAccess(User user) {
    	return commonDAO.login(user);
	}

	@Override
	public SysTokenAccess findMyToken(User user) {

		return null;
	}

	@Override
	public MyResponse changeMyPassword(Long loggedKey, String pass, String newPass) {

		return null;
	}

	@Override
	public MyResponse findAllUsers(String user) {

		return null;
	}

	@Override
	public MyResponse findRoleByUserKey(Long userKey) {

		return null;
	}

	@Override
	public MyResponse findFunctionByRoles(String listRoles) {

		return null;
	}

	@Override
	public MyResponse saveUser(String user, Long loggedKey) {

		return null;
	}

	@Override
	public MyResponse updateUserRole(Long userKey, String roleList, Long loggedKey) {

		return null;
	}

	@Override
	public MyResponse addUser(String user, String userPass, Long loggedKey) {

		return null;
	}

	@Override
	public MyResponse disableUserById(Long id, Long loggedKey) {

		return null;
	}

	@Override
	public MyResponse enableUserById(Long id, Long loggedKey) {

		return null;
	}

	@Override
	public MyResponse findMenu(Long loggedKey) {

		return null;
	}
    
}
