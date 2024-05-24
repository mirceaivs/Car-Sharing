package carsharing.db.dao.car;

public class Car {
    private int id;
    private String name;
    private int company_id;

    public Car(String name, int company_id){
        this.name = name;
        this.company_id = company_id;
    }

    public Car(int id, String name, int company_id){
        this.name = name;
        this.id = id;
        this.company_id = company_id;
    }

    public void setId(int id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setCompany_id(int company_id){
        this.company_id = company_id;
    }

    public String getName(){
        return this.name;
    }
    public int getId(){
        return this.id;
    }
    public int getCompany_id(){
        return this.company_id;
    }
    @Override
    public String toString(){
        return "Car: [Id "+ this.id +", Name :" + this.name+", Company Id:" + this.company_id+"]";
    }
}
