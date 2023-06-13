package com.blockchain.dao.user;

import com.blockchain.dao.entity.EntityDao;
import com.blockchain.entity.User;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDao extends EntityDao<User, Long>
{
    User loadUserByUsername(String username) throws UsernameNotFoundException;

    User findByName(String name);
}
