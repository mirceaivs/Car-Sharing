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
    private final CompanyDAO companyDAO;
    private final CarDAO carDAO;
    private final CustomerDAO customerDAO;
    private Customer customer;
    private Company company;


    public GUIController(Initializer initializer)
    {
        this.scanner = new Scanner(System.in);
        this.companyDAO = initializer.getCompanyDAO();
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
                        return;
                    }
                    case 2:{
                        if(selectCustomer()){
                            displayCustomerMenu();
                            return;
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
                        return;
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
            System.out.println("3. Delete a company");
            System.out.println("0. Back");
            try{
                choice = scanner.nextInt();
                scanner.nextLine();
                switch(choice){
                    case 1:{
                        if(selectCompany()){
                            displayCompanyMenu();
                            return;
                        }else{
                            displayManagerMenu();
                            return;
                        }
                    }
                    case 2:{
                        addCompany();
                        break;
                    }
                    case 3:{
                        if (selectCompany()) {
                            deleteCompany(company.getId());
                            company = null;
                            return;
                        }
                        else{
                            displayManagerMenu();
                            return;
                        }
                    }
                    case 0:{
                        displayMainMenu();
                        return;
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
            System.out.println("3. Delete a car");
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
                        addCar(company.getId());
                        break;
                    }
                    case 3:{
                        Car car = selectCar();
                        if(car != null){
                            deleteCar(car.getId());
                            break;
                        }else if(choice == 0){
                            displayCompanyMenu();
                            return;
                        }
                        break;
                    }
                    case 0:{
                        displayManagerMenu();
                        return;
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
            System.out.println("4. Delete customer");
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
                    case 4:{
                        deleteCustomer(customer.getId());
                        displayMainMenu();
                        return;
                    }
                    case 0:{
                        displayMainMenu();
                        return;
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
            return false;
        }
            customer = customerMap.get(choice - 1);
            return true;
        }
    }

    private boolean selectCompany(){
        List <Company> companies = companyDAO.findAll();
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
                return false;
            }
            company = companiesMap.get(choice - 1);
            return true;
        }
    }

    private void listCars(){
        List <Car> cars = carDAO.findAll(company.getId());
        if(cars.isEmpty() ){
            System.out.println("The car list is empty!\n");
            return;
        }
        System.out.println("\nCar list:");
        IntStream.range(0, cars.size())
                .forEach( i ->{
                    System.out.println((i+1) + ". " + cars.get(i).getName());
                });
    }

    private Car selectCar(){
        List <Car> cars = carDAO.findAll(company.getId());
        if(cars.isEmpty() ){
            System.out.println("The car list is empty!\n");
            return null;
        }
        else{
            System.out.println("Choose the company:");
            HashMap<Integer, Car> carsMap = new HashMap<>(cars.size());
            IntStream.range(0, cars.size())
                    .forEach( i ->{
                        carsMap.put(i, cars.get(i));
                        System.out.println((i+1) + ". " + cars.get(i).getName());
                    });
            System.out.println("0. Back");
            setChoice();
            if(choice == 0){
                return null;
            }
            return carsMap.get(choice - 1);
        }
    }

    private void rentACar() {
        if (customerDAO.checkRentedCar(customer.getId())) {
            System.out.println("You've already rented a car!");
        } else {
            if (!selectCompany()) {
                displayCustomerMenu();
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
                rentACar();
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
            Company rentedCarCompany = companyDAO.findById(rentedCar.getCompany_id());
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

    private void addCustomer(){
        System.out.println("Enter the customer name:");
        String name = scanner.nextLine();
        Customer customer = new Customer(name);
        if (customerDAO.add(customer)){
            System.out.println("Customer has been added!");
        }else{
            System.out.println("Customer was not added!");
        }
    }

    private void addCompany(){
        System.out.println("Enter the company name:");
        String name = scanner.nextLine();
        Company company = new Company(name);
        if (companyDAO.add(company)){
            System.out.println("Company has been added!");
        }else {
            System.out.println("Company was not added!");
        }
    }

    private void addCar(int companyId){
        System.out.println("Enter the car name:");
        String name = scanner.nextLine();
        Car car = new Car(name, companyId);
        if (carDAO.add(car)) {
            System.out.println("Car has been added!");
        } else{
            System.out.println("Car was not added!");
        }

    }

    private void deleteCompany(int companyId){
        if (companyDAO.deleteById(companyId)){
            System.out.println("The company has been removed!");
        }
        else{
            System.out.println("The company owns cars!");
        }
    }
    private void deleteCar(int carId){
        if(carDAO.deleteById(carId)){
            System.out.println("The car has been removed!");
        }else{
            System.out.println("The car is rented!");
        }
    }
    private void deleteCustomer(int customerId){
        if (customerDAO.deleteById(customerId)){
            System.out.println("The customer has been removed!");
        }else{
            System.out.println("The customer has a rented car!");
        }

    }
}
