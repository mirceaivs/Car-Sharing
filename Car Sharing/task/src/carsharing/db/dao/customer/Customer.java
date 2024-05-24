package carsharing.db.dao.customer;

public class Customer {
    private int id;
    private String name;
    private int rented_car_id;

    public Customer(int id, String name, int rented_car_id){
        this.id = id;
        this.name = name;
        this.rented_car_id = rented_car_id;
    }

    public Customer(String name){
        this.name = name;
    }
    public Customer(int id, String name){
        this.id = id;
        this.name = name;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setRentedCarId(int id){
        this.rented_car_id = id;
    }

    public String getName(){
        return this.name;
    }

    public int getId(){
        return this.id;
    }

    public int getRentedCarId(){
        return this.rented_car_id;
    }
}
