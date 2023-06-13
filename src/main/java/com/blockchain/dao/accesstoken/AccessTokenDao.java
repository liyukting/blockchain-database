package com.blockchain.dao.accesstoken;

import com.blockchain.dao.entity.EntityDao;
import com.blockchain.entity.SysTokenAccess;

public interface AccessTokenDao extends EntityDao<SysTokenAccess, Long> {
	SysTokenAccess findByToken(String accessTokenString);
}
