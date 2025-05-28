package org.uniremington.shared.base;

import org.uniremington.shared.exception.NotFoundException;

import java.util.List;

public abstract class BaseService<T extends BaseEntity, ID> {

    protected final BaseRepository<T, ID> repository;

    protected BaseService(BaseRepository<T, ID> repository) { this.repository = repository; }

    public List<T> findAll() { return repository.findAll(); }

    public T findById(ID id) {
        return repository
            .findById(id)
            .orElseThrow( () ->
                new NotFoundException("Entidad no encontrada con id: " + id)
            );
    }

    public T save(T entity) { return repository.save(entity); }

    public void deleteById(ID id) {
        T entity = findById(id);
        entity.setEstado(false);
        save(entity);
    }

}