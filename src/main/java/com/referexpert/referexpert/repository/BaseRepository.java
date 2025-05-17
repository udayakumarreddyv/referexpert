package com.referexpert.referexpert.repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic base repository interface defining common CRUD operations
 * @param <T> Entity type
 * @param <ID> Entity ID type
 */
public interface BaseRepository<T, ID> {
    Optional<T> findById(ID id);
    List<T> findAll();
    T save(T entity);
    void delete(T entity);
    void deleteById(ID id);
    boolean existsById(ID id);
}
