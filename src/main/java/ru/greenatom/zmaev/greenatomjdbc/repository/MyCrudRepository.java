package ru.greenatom.zmaev.greenatomjdbc.repository;

import java.util.List;

public interface MyCrudRepository<E> {
    int save(E entity);
    int update(E entity);

    E findById(Long id);
    int deleteById(Long id);
    List<E> findAll();
}
