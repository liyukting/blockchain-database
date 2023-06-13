package com.blockchain.dao.entity;

import java.util.List;

import com.blockchain.entity.Entity;

public interface EntityDao<T extends Entity, I>
{
    List<T> findAll();

    T find(I id);

    T save(T entity);

    void delete(I id);

    void delete(T entity);
}
