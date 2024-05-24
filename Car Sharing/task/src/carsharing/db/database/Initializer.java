package carsharing.db.database;

import carsharing.db.dao.car.CarDAO;
import carsharing.db.dao.company.CompanyDAO;
import carsharing.db.dao.customer.CustomerDAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Initializer {
    private final List<DAO<?>> DAOList = new ArrayList<>();

    public Initializer(ConnectionProvider connectionProvider){
        List<DAOFactory<?>> DAOFactories = Arrays.asList(
                new CompanyDAOFactory(),
                new CarDAOFactory(),
                new CustomerDAOFactory()
        );
        for(DAOFactory<?> factory : DAOFactories){
            DAO<?> dao =  factory.create(connectionProvider);
            dao.init();
            DAOList.add(dao);
            }
    }

    public CompanyDAO getCompanyDAO() {
        return (CompanyDAO) DAOList.stream()
                .filter(dao -> dao instanceof CompanyDAO)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("CompanyDAO not found"));
    }

    public CarDAO getCarDAO() {
        return (CarDAO) DAOList.stream()
                .filter(dao -> dao instanceof CarDAO)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("CarDAO not found"));
    }

    public CustomerDAO getCustomerDAO(){
        return (CustomerDAO) DAOList.stream()
                .filter(dao -> dao instanceof CustomerDAO)
                .findFirst()
                .orElseThrow( ()-> new IllegalArgumentException("CustomerDAO not found"));
    }
}
