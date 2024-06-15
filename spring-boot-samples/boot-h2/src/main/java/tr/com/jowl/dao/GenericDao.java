package tr.com.jowl.dao;


import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @param <T>
 * @param <Id>
 * @author ikarayel
 */
public interface GenericDao<T, Id extends Serializable> {

    void save(T object);

    void saveAll(Collection<T> entities);

    void update(T object);

    void updateAll(Collection<T> entities);

    void delete(T object);

    void deleteAll(Collection objectList);

    void deleteById(Class<T> type, Id id);

    T find(Class<T> clazz, Id objectId) throws RuntimeException;

    List<T> findAll(final Class<T> clazz) throws RuntimeException;


}
