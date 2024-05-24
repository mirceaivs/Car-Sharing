package carsharing.db.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public interface ConnectionProvider {
    Connection getConnection() throws SQLException;
}

//Singleton CLass
class ConnectionProviderH2 implements ConnectionProvider{
    private static final String DBURL = "jdbc:h2:./src/carsharing/db/carsharing";
    private static final String JDBCDriver = "org.h2.Driver";
    private static ConnectionProviderH2 instance;
    private ConnectionProviderH2(){
        try{
            Class.forName(JDBCDriver);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    public static synchronized ConnectionProviderH2 getInstance(){
        if(instance == null){
            instance = new ConnectionProviderH2();
        }
        return instance;
    }
@Override
    public Connection getConnection() throws SQLException{
        Connection conn = DriverManager.getConnection(DBURL);
        conn.setAutoCommit(true);
        return conn;
    }


}