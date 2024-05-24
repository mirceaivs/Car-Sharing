package carsharing.db.dao.customer;

import carsharing.db.database.ConnectionProvider;
import carsharing.db.database.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO extends DAO<Customer> {
    public CustomerDAO(ConnectionProvider connection){
        super(connection);
    }
    @Override
    public List<Customer> findAll(Object... params){
        String sql = "SELECT * FROM CUSTOMER";
        List<Customer> customers = new ArrayList<>();
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) {
                    throw new SQLException("ResultSet is empty!");
                }
                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("NAME");
                    int rented_car_id = resultSet.getInt("RENTED_CAR_ID");
                    Customer customer = new Customer(id, name, rented_car_id);
                    customers.add(customer);
                }
            System.out.println("The query has been executed!");
            return customers;
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }
    @Override
    public Customer findById(int id){
        Customer customer = null;
        String sql = "SELECT * FROM CUSTOMER WHERE id = ?";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)) {
            statement.setObject(1, id);
            try(ResultSet resultSet = statement.executeQuery()){
                System.out.println("The query has been executed!");
                if (resultSet.next()) {
                    String name = resultSet.getString("NAME");
                    int rented_car_id = resultSet.getInt("RENTED_CAR_ID");
                    customer = new Customer(id, name, rented_car_id);
                }else{
                    System.out.println("ResultSet is empty!");
                    return null;
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return customer;
    }
    @Override
    public boolean add(Customer customer){
        String sql = "INSERT INTO CUSTOMER (NAME) VALUES(?)";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)) {
            statement.setString(1, customer.getName());
            int result = statement.executeUpdate();
            if (result != 0) {
                System.out.println("Customer has been added!");
                return true;
            } else {
                System.out.println("Customer was not found!");
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean update(int customer_id, String columnName, Object columnValue){
        String sql = "UPDATE CUSTOMER SET %s = ? WHERE ID = ?";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(String.format(sql, columnName))) {
            statement.setObject(1, columnValue);
            statement.setInt(2, customer_id);
            int result = statement.executeUpdate();
            if (result != 0) {
                System.out.println("Customer has been updated!");
                return true;
            } else {
                System.out.println("Customer was not updated!");
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean deleteById(int id){
        String sql = "DELETE FROM CUSTOMER WHERE ID = ?";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)) {
            statement.setObject(1, id);
            int result = statement.executeUpdate();
            if (result != 0) {
                System.out.println("Customer has been removed!");
                return true;
            } else {
                System.out.println("Customer was not removed!");
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
//    public boolean checkRentedCar(int customer_id){
//        String sql = "SELECT RENTED_CAR_ID FROM CUSTOMER WHERE ID = ?" ;
//        try(PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
//            statement.setInt(1, customer_id);
//            try(ResultSet resultSet = statement.executeQuery()){
//                if(resultSet.isBeforeFirst()){
//                    return false;
//                }else{
//                    return true;
//                }
//            }
//        }catch (SQLException e){
//            e.printStackTrace();
//            return false;
//        }
//    }
public boolean checkRentedCar(int customer_id) {
    String sql = "SELECT RENTED_CAR_ID FROM CUSTOMER WHERE ID = ?";
    try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)) {
        statement.setInt(1, customer_id);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getObject("RENTED_CAR_ID") != null; // Check for non-null RENTED_CAR_ID
            } else {
                return true; // No record found, likely no rented car
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false; // Return false on error
    }
}

    public void init(){
        String sql = "CREATE TABLE IF NOT EXISTS CUSTOMER (" +
                "ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY," +
                "NAME VARCHAR(255) UNIQUE NOT NULL," +
                "RENTED_CAR_ID INT DEFAULT NULL, " +
                "FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID)" +
                ");";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)) {
            statement.executeUpdate();
        }catch (SQLException e ){
            e.printStackTrace();
        }
    }
}
