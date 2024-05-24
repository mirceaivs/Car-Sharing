package carsharing.db.dao.company;
public class Company{
    private int id;
    private String name;
    public Company(int id, String name){
        this.id = id;
        this.name = name;
    }
    public Company(String name){
        this.name = name;
        this.id = 0;
    }

    public void setId(int id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }

    public int getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    @Override
    public String toString(){
        return "Company: [Id "+ this.id +", Name :" + this.name+"]";
    }
}