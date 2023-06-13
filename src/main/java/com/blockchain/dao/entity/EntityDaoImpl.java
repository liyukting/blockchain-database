package com.blockchain.dao.entity;

import org.springframework.transaction.annotation.Transactional;

import com.blockchain.entity.Entity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class EntityDaoImpl<T extends Entity, I> implements EntityDao<T, I>
{
    private EntityManager entityManager;

    protected Class<T> entityClass;

    public EntityDaoImpl(Class<T> entityClass)
    {
        this.entityClass = entityClass;
    }

    public EntityManager getEntityManager()
    {
        return this.entityManager;
    }

    @PersistenceContext
    public void setEntityManager(final EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll()
    {
        final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<T> criteriaQuery = builder.createQuery(this.entityClass);

        criteriaQuery.from(this.entityClass);

        TypedQuery<T> typedQuery = this.getEntityManager().createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public T find(I id)
    {
        return this.getEntityManager().find(this.entityClass, id);
    }

    @Override
    @Transactional
    public T save(T entity)
    {
        return this.getEntityManager().merge(entity);
    }

    @Override
    @Transactional
    public void delete(I id)
    {
        if (id == null) {
            return;
        }

        T entity = this.find(id);
        if (entity == null) {
            return;
        }

        this.getEntityManager().remove(entity);
    }

    @Override
    @Transactional
    public void delete(T entity)
    {
        this.getEntityManager().remove(entity);
    }
}
