package tr.com.jowl.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tr.com.jowl.dao.GenericDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @param <T>
 * @param <Id>
 * @author ikarayel
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@Repository("genericDao")
@Transactional
public class GenericDaoImpl<T, Id extends Serializable> implements GenericDao<T, Id>, Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(T entity) {
        entityManager.persist(entity);
    }

    @Override
    public void saveAll(Collection<T> entities) {
        entities.forEach(entity -> entityManager.persist(entity));
    }

    @Override
    public void update(T entity) {
        entityManager.merge(entity);
    }

    @Override
    public void updateAll(Collection<T> entities) {
        entities.forEach(entity -> entityManager.merge(entity));
    }

    @Override
    public void delete(T object) {
        entityManager.remove(object);
    }

    @Override
    public void deleteAll(Collection objectList) {
        objectList.forEach(o -> entityManager.remove(o));

    }

    @Override
    public void deleteById(Class<T> type, Id objectId) {
        T entity = find(type, objectId);
        delete(entity);
    }

    @Override
    public T find(Class<T> clazz, Id objectId) throws RuntimeException {
        return entityManager.find(clazz, objectId);
    }

    @Override
    public List<T> findAll(Class<T> clazz) throws RuntimeException {
        return entityManager.createQuery("from " + clazz.getName()).getResultList();
    }

}
