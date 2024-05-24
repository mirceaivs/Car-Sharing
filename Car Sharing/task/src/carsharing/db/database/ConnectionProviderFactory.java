package carsharing.db.database;

public class ConnectionProviderFactory {
    public static ConnectionProvider getConnectionProvider(String DbType){
        switch (DbType.toLowerCase()){
            case "h2":
                return ConnectionProviderH2.getInstance();
            default:
                throw new IllegalArgumentException("Provider type not supported!");
        }
    }
}
