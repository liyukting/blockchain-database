package com.blockchain.dao.accesstoken;

import com.blockchain.dao.entity.EntityDaoImpl;
import com.blockchain.entity.SysTokenAccess;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class AccessTokenDaoImpl extends EntityDaoImpl<SysTokenAccess, Long> implements AccessTokenDao
{
    public AccessTokenDaoImpl()
    {
        super(SysTokenAccess.class);
    }

    @Override
    @Transactional(readOnly = true, noRollbackFor = NoResultException.class)
    public SysTokenAccess findByToken(String secu)
    {
        CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SysTokenAccess> query = builder.createQuery(this.entityClass);
        Root<SysTokenAccess> root = query.from(this.entityClass);
        query.where(builder.equal(root.get("accessToken"), secu));

        try {
        	SysTokenAccess at = this.getEntityManager().createQuery(query).getSingleResult();
//        	System.out.println("User: " + at.getUser().getId() + " - Token: " + at.getToken());
            return at;
        } catch (NoResultException e) {
            return null;
        }
    }
}
