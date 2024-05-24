package carsharing.db.database;

import java.util.List;
public abstract class DAO <T>{
    protected final ConnectionProvider connection;
    public DAO(ConnectionProvider connection){
        this.connection = connection;
    }
    public abstract List<T> findAll(Object... params);
    public abstract T findById(int id);
    public abstract boolean add(T object);
    public abstract boolean update(int id, String columnName, Object columnValue);
    public abstract boolean deleteById(int id);
    public abstract void init();
}
