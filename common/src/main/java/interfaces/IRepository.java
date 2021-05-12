package interfaces;

import domain.Entity;

import java.util.List;
import java.util.Optional;

public interface IRepository<ID, E extends Entity<ID>> {

    Optional<E> findOne(ID id);
    List<E> findAll();
    void add(E entity);
    void delete(ID id);
    void update(E entity);
}


