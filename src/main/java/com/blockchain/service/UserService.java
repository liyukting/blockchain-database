package com.blockchain.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.blockchain.entity.MyResponse;
import com.blockchain.entity.SysTokenAccess;
import com.blockchain.entity.User;

public interface UserService extends UserDetailsService
{
    User findUserByAccessToken(SysTokenAccess token);
	
    SysTokenAccess createTokenAccess(User user);
    
    SysTokenAccess findTokenAccess(String accessTokenString);

    SysTokenAccess login(User user);
    
    SysTokenAccess register(User user);
    
    void logout(SysTokenAccess token);
    
    User getUser(String userID, String pass);

    SysTokenAccess findMyToken(User user);
    
    MyResponse changeMyPassword(Long loggedKey, String pass, String newPass);
    
    MyResponse findAllUsers(String user);
        
    MyResponse findRoleByUserKey(Long userKey);

    MyResponse findFunctionByRoles(String listRoles);
    
    MyResponse saveUser(String user, Long loggedKey);

    MyResponse updateUserRole(Long userKey, String roleList, Long loggedKey);

    MyResponse addUser(String user, String userPass, Long loggedKey);
	
    MyResponse disableUserById(Long id, Long loggedKey);

    MyResponse enableUserById(Long id, Long loggedKey);
    
    MyResponse findMenu(Long loggedKey);
}
