package carsharing.db.database;

import carsharing.db.dao.car.Car;
import carsharing.db.dao.car.CarDAO;
import carsharing.db.dao.company.Company;
import carsharing.db.dao.company.CompanyDAO;
import carsharing.db.dao.customer.Customer;
import carsharing.db.dao.customer.CustomerDAO;

public interface DAOFactory <T>{
    DAO<T> create(ConnectionProvider connection);
}

class CompanyDAOFactory implements DAOFactory<Company>{
    public DAO<Company> create(ConnectionProvider connection){
        return new CompanyDAO(connection);
    }
}

class CarDAOFactory implements DAOFactory<Car>{
    public DAO<Car> create(ConnectionProvider connection){
        return new CarDAO(connection);
    }
}

class CustomerDAOFactory implements DAOFactory<Customer>{
    public DAO<Customer> create(ConnectionProvider connection){
        return new CustomerDAO(connection);
    }
}

