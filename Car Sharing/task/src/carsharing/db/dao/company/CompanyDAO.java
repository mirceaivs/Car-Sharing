package carsharing.db.dao.company;

import carsharing.db.database.ConnectionProvider;
import carsharing.db.database.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAO extends DAO<Company> {
    public CompanyDAO(ConnectionProvider connection) {
        super(connection);
    }

    public List<Company> findAll(Object... params) {
        String sql = "SELECT * FROM COMPANY";
        List<Company> companies = new ArrayList<>();
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) {
                    throw new SQLException("ResultSet is empty!");
                }
                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("NAME");
                    Company company = new Company(id, name);
                    companies.add(company);
                }
            System.out.println("The query has been executed!");
            return companies;
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public Company findById(int id) {
        Company company = null;
        String sql = "SELECT * FROM COMPANY WHERE id = ?";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)) {
            statement.setObject(1, id);
            try(ResultSet resultSet = statement.executeQuery()){
              System.out.println("The query has been executed!");
                if (resultSet.next()) {
                    String name = resultSet.getString("NAME");
                    company = new Company(id, name);
                }else{
                    System.out.println("ResultSet is empty!");
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return company;
    }


    public boolean add(Company company) {
        String sql = "INSERT INTO COMPANY (NAME) VALUES(?)";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)) {
            statement.setString(1, company.getName());
            int result = statement.executeUpdate();
            return result != 0;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(int company_id, String columnName, Object columnValue){
        String sql = "UPDATE COMPANY SET %s = ? WHERE ID = ?";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(String.format(sql, columnName))) {
            statement.setInt(1, company_id);
            statement.setObject(2, columnValue);
            int result = statement.executeUpdate();
            if (result != 0) {
                System.out.println("Company has been updated!");
                return true;
            } else {
                System.out.println("Company was not updated!");
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteById(int id){
        if (hasCars(id))
            return false;
        String sql = "DELETE FROM COMPANY WHERE ID = ?";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)) {
            statement.setObject(1, id);
            int result = statement.executeUpdate();
            return result != 0;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean hasCars(int id){
        String sql = "SELECT company.id FROM company JOIN car ON company.id = car.company_id where company.id = ?";
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

    public void init(){
        String sql = "CREATE TABLE IF NOT EXISTS COMPANY (" +
                "ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY," +
                "NAME VARCHAR(255) UNIQUE NOT NULL);";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)) {
            statement.executeUpdate();
        }catch (SQLException e ){
            e.printStackTrace();
        }
    }

}
