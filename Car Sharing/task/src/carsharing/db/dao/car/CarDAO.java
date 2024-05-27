package carsharing.db.dao.car;

import carsharing.db.database.ConnectionProvider;
import carsharing.db.database.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CarDAO extends DAO<Car> {
    public CarDAO(ConnectionProvider connection){
        super(connection);
    }

    @Override
    public List<Car> findAll(Object... params){
        String sql = "SELECT * FROM CAR WHERE COMPANY_ID=?";
        List<Car> cars = new ArrayList<>();
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)) {
                statement.setObject(1, params[0]);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) {
                    throw new SQLException("ResultSet is empty!");
                }
                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("NAME");
                    int company_id = resultSet.getInt("COMPANY_ID");
                    Car car = new Car(id, name, company_id);
                    cars.add(car);
                }
            }
            System.out.println("The query has been executed!");
            return cars;
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }
    public Car findById(int id){
        Car car = null;
        String sql = "SELECT * FROM CAR WHERE ID = ?";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)) {
            statement.setObject(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                System.out.println("The query has been executed!");
                if (resultSet.next()) {
                    String name = resultSet.getString("NAME");
                    int company_id = resultSet.getInt("COMPANY_ID");
                    car = new Car(id, name, company_id);
                } else {
                    System.out.println("ResultSet is empty!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return car;
    }

    public List<Car> findAvailableCars(){
        String sql = " SELECT car.id, car.name, car.company_id " +
                "FROM car LEFT JOIN customer " +
                "ON car.id = customer.rented_car_id " +
                "WHERE customer.name IS NULL;";
        List<Car> cars = new ArrayList<>();
        try(PreparedStatement statement = connection.getConnection().prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()){
            if(!resultSet.isBeforeFirst()){
                throw new SQLException("ResultSet is empty!");
            }
            while(resultSet.next()){
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                int company_id = resultSet.getInt("COMPANY_ID");
                Car car = new Car(id, name, company_id);
                cars.add(car);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println("The query has been executed!");
        return cars;
    }

    @Override
    public boolean add(Car car){
        String sql = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES(?, ?)";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)) {
            statement.setString(1, car.getName());
            statement.setInt(2, car.getCompany_id());
            int result = statement.executeUpdate();
            return result != 0;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean update(int car_id, String columnName, Object columnValue){
        String sql = "UPDATE CAR SET %s = ? WHERE ID = ?";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(String.format(sql, columnName))) {
            statement.setObject(1, columnValue);
            statement.setInt(2, car_id);
            int result = statement.executeUpdate();
            if (result != 0) {
                System.out.println("Car has been updated!");
                return true;
            } else {
                System.out.println("Car was not updated!");
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean deleteById(int id){
        String sql = "DELETE FROM CAR WHERE ID = ?";
        if(isCarRented(id))
        {
            return false;
        }else{
            try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)) {
                statement.setObject(1, id);
                int result = statement.executeUpdate();
                return result != 0;
            }catch (SQLException e){
                e.printStackTrace();
                return false;
            }
        }
    }

    private boolean isCarRented(int id){
        String sql = "SELECT car.id FROM car JOIN customer on car.id = customer.rented_car_id WHERE car.id = ?;";
        try(PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setInt(1, id);
            try(ResultSet resultSet = statement.executeQuery()){
                return resultSet.next();
            }
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void init(){
        String sql = "CREATE TABLE IF NOT EXISTS CAR (" +
                "ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY," +
                "NAME VARCHAR(255) UNIQUE NOT NULL," +
                "COMPANY_ID INT NOT NULL," +
                "FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID)" +
                ");";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)) {
            statement.executeUpdate();
        }catch (SQLException e ){
            e.printStackTrace();
        }
    }
}
