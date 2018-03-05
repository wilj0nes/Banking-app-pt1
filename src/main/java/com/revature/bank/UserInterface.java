package com.revature.bank;

import java.io.*;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.log4j.Logger;


public class UserInterface extends Bank implements Serializable {

    Scanner scan = new Scanner(System.in);
    private String userChoice;

    private CustomerCollection customers;
    private Bank b;

    private User currentUser = null;
    private boolean go = true;

    private int userInput;

    public UserInterface(){

        File file = new File("users.dat");
        if(file.length() == 0){
            System.out.println("file is empty ");
            customers = new CustomerCollection();
        }
        else{
            System.out.println("file is not empty");

            try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.dat"))){
                customers = (CustomerCollection) ois.readObject();                         // read from file
                System.out.println("All Users:\n" + customers);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void choice(){
        while(go){
            if(currentUser == null){
                System.out.println("\nWhat would you like to do today?");
                System.out.println("--------------------------------");
                System.out.println("1. To login");
                System.out.println("2. To create a profile");
                System.out.println("3. To register for an account");
                System.out.println("4. To exit");
                System.out.print("->");
                userChoice = scan.nextLine();

                //TODO make sure this is an int
                userInput = Integer.parseInt(userChoice);
                switch (userInput){
                    case 1: logIn();
                        break;
                    case 2: createProfile();
                        break;
                    case 3:
                    default: stop();
                }
            }
            else{
                System.out.println("\nAccount Options");
                System.out.println("--------------------------------");
                System.out.println("1. Logout");
                System.out.println("2. Deposit funds");
                System.out.println("3. Withdraw funds");
                System.out.println("4. View funds");
                userChoice = scan.nextLine();

                //TODO make sure this is an int
                userInput = Integer.parseInt(userChoice);
                switch (userInput){
                    case 1: currentUser = null;
                        break;
                    default: stop();
                }
            }
        }
    }

    public void createProfile(){
        System.out.print("Please pick a username: ");
        String username = scan.nextLine();
        System.out.print("\nPlease pick a password: ");
        String password = scan.nextLine();

//        customers.addUser("will", "pass1");
//        customers.addUser("jones", "pass");
//        customers.addUser("user", "pass3");

    }

    public void logIn(){
        System.out.print("Enter your username: ");
        String u = scan.nextLine();
        System.out.print("Enter your password: ");
        String p = scan.nextLine();

        currentUser = customers.checkUserAndPass(u, p);
        if(currentUser == null){
            System.out.println("Wrong username or password");
        }
    }

    public void stop(){
        System.out.println("stop()");

        // add true to append
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.dat"))){
            oos.writeObject(customers);                                             // write to file
            System.out.println(customers);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        go = false;
    }

    public int getUserInput() {
        return userInput;
    }

    public void setUserInput(int userInput) {
        this.userInput = userInput;
    }
}
