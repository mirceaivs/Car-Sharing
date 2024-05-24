package carsharing;

import carsharing.db.dao.car.Car;
import carsharing.db.dao.car.CarDAO;
import carsharing.db.dao.company.Company;
import carsharing.db.dao.company.CompanyDAO;
import carsharing.db.dao.customer.Customer;
import carsharing.db.dao.customer.CustomerDAO;
import carsharing.db.database.Initializer;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public  class GUIController implements GUIHandler {
    private final Scanner scanner;
    private int choice=0;
    private final CompanyDAO companyDao;
    private final CarDAO carDAO;
    private final CustomerDAO customerDAO;
    private Customer customer;
    private Company company;

    public GUIController(Initializer initializer)
    {
        this.scanner = new Scanner(System.in);
        this.companyDao = initializer.getCompanyDAO();
        this.carDAO = initializer.getCarDAO();
        this.customerDAO = initializer.getCustomerDAO();
        company = null;
        customer = null;
        displayMainMenu();
        scanner.close();
    }

    public void displayMainMenu(){
        do{
            System.out.println("1. Log in as a manager\n" +
                    "2. Log in as a customer\n" +
                    "3. Create a customer\n" +
                    "0. Exit");
            try{
                choice = scanner.nextInt();
                scanner.nextLine();
                switch(choice){
                    case 1:{
                        displayManagerMenu();
                        break;
                    }
                    case 2:{
                        if(selectCustomer()){
                            displayCustomerMenu();
                        }else{
                            break;
                        }
                    }
                    case 3:{
                        addCustomer();
                        break;
                    }
                    case 0:{
                        System.out.println("Exiting program...");
                        break;
                    }
                    default:
                        System.out.println("Invalid choice. Please try again!");
                }
            }catch (InputMismatchException e){
                System.out.println("Invalid input. Please enter a valid integer choice.");
                scanner.next();
            }
        }while(choice != 0 );
    }

    public void displayManagerMenu(){
        do{
            System.out.println("1. Company list");
            System.out.println("2. Create a company");
            System.out.println("0. Back");
            try{
                choice = scanner.nextInt();
                scanner.nextLine();
                switch(choice){
                    case 1:{
                        if(selectCompany()){
                            displayCompanyMenu();
                        }else{
                            break;
                        }
                    }
                    case 2:{
                        addCompany();
                        break;
                    }
                    case 0:{
                        displayMainMenu();
                        break;
                    }
                    default:
                        System.out.println("Invalid choice. Please try again!");
                }
            }catch (InputMismatchException e){
                System.out.println("Invalid input. Please enter a valid integer choice.");
                scanner.next();
            }
        }while(choice != 0 );

    }

    public void displayCompanyMenu(){
        do{
            System.out.println("'"+company.getName()+"' company");
            System.out.println("1. Car list");
            System.out.println("2. Create a car");
            System.out.println("0. Back");
            try{
                choice = scanner.nextInt();
                scanner.nextLine();
                switch(choice){
                    case 1:{
                        listCars();
                        break;
                    }
                    case 2:{
                        addCars(company.getId());
                        break;
                    }
                    case 0:{
                        displayManagerMenu();
                        break;
                    }
                    default:
                        System.out.println("Invalid choice. Please try again!");
                }
            }catch (InputMismatchException e){
                System.out.println("Invalid input. Please enter a valid integer choice.");
                scanner.next();
            }
        }while(choice != 0 );

    }

    public void displayCustomerMenu(){
        do{
            System.out.println("1. Rent a car");
            System.out.println("2. Return a rented car");
            System.out.println("3. My rented car");
            System.out.println("0. Back");
            try{
                choice = scanner.nextInt();
                scanner.nextLine();
                switch(choice){
                    case 1:{
                        rentACar();
                        break;
                    }
                    case 2:{
                        returnACar();
                        break;
                    }
                    case 3:{
                        displayRentedCar();
                        break;
                    }
                    case 0:{
                        displayMainMenu();
                        break;
                    }
                    default:
                        System.out.println("Invalid choice. Please try again!");
                }
            }catch (InputMismatchException e){
                System.out.println("Invalid input. Please enter a valid integer choice.");
                scanner.next();
            }
        }while(choice != 0 );
    }
    private boolean selectCustomer(){
        List <Customer> customers = customerDAO.findAll();
        if(customers.isEmpty() ){
            System.out.println("The customer list is empty!\n");
            return false;
        }else{
        System.out.println("Customer list:");
        HashMap<Integer, Customer> customerMap = new HashMap<>(customers.size());
        IntStream.range(0, customers.size())
                .forEach( i ->{
                    customerMap.put(i, customers.get(i));
                    System.out.println((i+1) + ". " + customers.get(i).getName());
                });
        System.out.println("0. Back");
        setChoice();
        if(choice == 0){
            displayMainMenu();
        }
            customer = customerMap.get(choice - 1);
            return true;
        }
    }

    private boolean selectCompany(){
        List <Company> companies = companyDao.findAll();
        if(companies.isEmpty() ){
            System.out.println("The company list is empty!\n");
            return false;
        }
        else{
            System.out.println("Choose the company:");
            HashMap<Integer, Company> companiesMap = new HashMap<>(companies.size());
            IntStream.range(0, companies.size())
                    .forEach( i ->{
                        companiesMap.put(i, companies.get(i));
                        System.out.println((i+1) + ". " + companies.get(i).getName());
                    });
            System.out.println("0. Back");
            setChoice();
            if(choice == 0){
                displayManagerMenu();
            }
            company = companiesMap.get(choice - 1);
            return true;
        }
    }

    private void listCars(){
        List <Car> cars = carDAO.findAll(company.getId());
        if(cars.isEmpty() ){
            System.out.println("The car list is empty!\n");
        }
        System.out.println("\nCar list:");
        IntStream.range(0, cars.size())
                .forEach( i ->{
                    System.out.println((i+1) + ". " + cars.get(i).getName());
                });

    }

    private void addCustomer(){
        System.out.println("Enter the customer name:");
        String name = scanner.nextLine();
        Customer customer = new Customer(name);
        customerDAO.add(customer);
    }

    private void addCompany(){
        System.out.println("Enter the company name:");
        String name = scanner.nextLine();
        Company company = new Company(name);
        companyDao.add(company);
    }

    private void rentACar() {
        if (customerDAO.checkRentedCar(customer.getId())) {
            System.out.println("You've already rented a car!");
        } else {
            if (!selectCompany()) {
                return;
            }
            List<Car> cars = carDAO.findAvailableCars();
            if (cars.isEmpty()) {
                System.out.println("The car list is empty!\n");
                return;
            }
            System.out.println("Choose car:");
            HashMap<Integer, Car> carMap = new HashMap<>(cars.size());
            IntStream.range(0, cars.size())
                    .forEach(i -> {
                        carMap.put(i + 1, cars.get(i));
                        System.out.println((i + 1) + ". " + cars.get(i).getName());
                    });
            System.out.println("0. Back");
            setChoice();
            if (choice == 0) {
                return;
            }
            Car selectedCar = carMap.get(choice);
            if (selectedCar == null) {
                System.out.println("Invalid choice. Please try again.");
                return;
            }
            if (customerDAO.update(customer.getId(), "RENTED_CAR_ID", selectedCar.getId())) {
                System.out.println("You rented '" + selectedCar.getName() + "'");
                customer = customerDAO.findById(customer.getId());
            } else {
                System.out.println("Something went wrong!");
            }
        }
    }


    public void returnACar(){
        if(!customerDAO.checkRentedCar(customer.getId())){
            System.out.println("You didn't rent a car!");
        }else{
            if(customerDAO.update(customer.getId(), "RENTED_CAR_ID", null)){
                System.out.println("You've returned a rented car!");
            }else{
                System.out.println("Something went wrong!");
            }
        }
    }

    public void displayRentedCar(){
        if(!customerDAO.checkRentedCar(customer.getId()))
            System.out.println("You didn't rent a car!");
        else{
            Car rentedCar = carDAO.findById(customer.getRentedCarId());
            if(rentedCar != null){
            Company rentedCarCompany = companyDao.findById(rentedCar.getCompany_id());
            System.out.println("Your rented car:\n" + rentedCar.getName()+
                    "\nCompany:\n"+ rentedCarCompany.getName() );
            }
            System.out.println("Something went wrong!");
        }
    }

    private void setChoice() {
        while (true) {
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer choice.");
                scanner.next();
            }
        }
    }

    private void addCars(int companyId){
        System.out.println("Enter the car name:");
        String name = scanner.nextLine();
        Car car = new Car(name, companyId);
        carDAO.add(car);
    }
}
