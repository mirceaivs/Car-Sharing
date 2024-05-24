package carsharing;

import carsharing.db.database.ConnectionProvider;
import carsharing.db.database.ConnectionProviderFactory;
import carsharing.db.database.Initializer;

public class Main {
    public static void main(String[] args) {
        ConnectionProvider connection = ConnectionProviderFactory.getConnectionProvider("h2");
        Initializer initializer = new Initializer(connection);
        GUIHandler guiHandler = new GUIController(initializer);



    }
}