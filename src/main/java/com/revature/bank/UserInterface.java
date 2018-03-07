package com.revature.bank;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.*;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

public class UserInterface extends Bank implements Serializable {

    Scanner scan = new Scanner(System.in);
    private String userChoice;

    private CustomerCollection customers;
    private AccountCollection accounts;

    private User currentUser = null;
    private Account currentAccount = null;
    private boolean go = true;
    private CollectionHolder objs;
    private int userInput;

    public UserInterface(){

        File f = new File("collections.dat");
        if(f.length() == 0){
            System.out.println("collections file is empty ");
            objs = new CollectionHolder();

            objs.newCC();
            objs.newAC();

            customers = objs.getCC();
            accounts = objs.getAC();
        }
        else{
            System.out.println("collections file is not empty");

            try(ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream("collections.dat"))){

                objs = (CollectionHolder) ois.readObject();                         // read from file
                System.out.println("All Users:\n" + objs);

                customers = objs.getCC();
                accounts = objs.getAC();
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

//        File file = new File("users.dat");
//        if(file.length() == 0){
//            System.out.println("users file is empty ");
//            customers = new CustomerCollection();
//        }
//        else{
//            System.out.println("users file is not empty");
//
//            try(ObjectInputStream ois = new ObjectInputStream(
//                    new FileInputStream("users.dat"))){
//
//                customers = (CustomerCollection) ois.readObject();                         // read from file
//                System.out.println("All Users:\n" + customers);
//                ois.close();
//            }
//            catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//            catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        }

    }

//    public void readAccounts(){
//        File file2 = new File("accounts.dat");
//        if(file2.length() == 0){
//            System.out.println("accounts file is empty");
//            accounts = new AccountCollection();
//        }
//        else{
//            System.out.println("accounts file is not empty");
//
//            try(ObjectInputStream ois = new ObjectInputStream(
//                    new FileInputStream("users.dat"))){
//
//                accounts = (AccountCollection) ois.readObject();                         // read from file
//                System.out.println("All accounts:\n" + accounts);
//            }
//            catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//            catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public void choice(){                                               // LogIn menu
        while(go){
            if(currentUser == null){
                System.out.println("\nWhat would you like to do today?");
                System.out.println("--------------------------------");
                System.out.println("1. To Exit");
                System.out.println("2. To login");
                System.out.println("3. To create a profile");
                System.out.println("4. To Erase file"); //TODO remove this
                System.out.print("-> ");
                userChoice = scan.nextLine();

                //TODO input validation
                userInput = Integer.parseInt(userChoice);
                switch (userInput){
                    case 1: stop();
                        break;
                    case 2: logIn();
                        break;
                    case 3: createProfile();
                        break;
                    case 4: eraseFile();                //TODO and this
                        break;
                    default: stop();
                }
            }
            else{
                System.out.println("\nProfile Options");
                System.out.println("--------------------------------");
                System.out.println("1. Logout");
                System.out.println("2. Create new bank account");
                System.out.println("3. View account info");
                System.out.println("4. Bank account options");
                System.out.print("-> ");
                userChoice = scan.nextLine();

                //TODO input validation
                userInput = Integer.parseInt(userChoice);
                switch (userInput){
                    case 1: currentUser = null;
                        break;
                    case 2: newBankAccount();
                        break;
                    case 3: System.out.println(currentUser.toString());
                        break;
                    case 4: viewInfo(currentUser);
                        break;
                    case 9: stop();
                        break;
                    default: stop();
                }
            }
        }
    }

    public void newBankAccount(){
        //System.out.println(accounts);
        accounts.createAccount(currentUser);
    }

    public void viewInfo(User currentUser){
        int accountChoice = 0;
        String input;
        boolean viewInfoLoop = true;
        UUID chosenID = null;


        do{
            System.out.println("\nPlease pick an account to view");
            System.out.println("--------------------------------");
            for(int i = 0; i < currentUser.getIdList().size(); i++){
                System.out.println(i + ". " + currentUser.getIdList().get(i) + ", ");
            }
            System.out.print("-> ");
            Scanner scan = new Scanner(System.in);
            input = scan.nextLine();

            //TODO input validation
            int index = Integer.parseInt(input);

            for(int i = 0; i < currentUser.getIdList().size(); i++){
                if(i == index){
                    chosenID = currentUser.getIdList().get(i);
                    viewInfoLoop = false;
                }
            }
        } while(viewInfoLoop);

        currentAccount = accounts.returnAccount(chosenID);
        manageAccount();
    }

    public void manageAccount(){
        boolean manageAccountLoop = true;

        while(manageAccountLoop){
            System.out.println(currentAccount.toString());
            System.out.println("Account Options");
            System.out.println("--------------------------------");
            System.out.println("1. Logout");
            System.out.println("2. Back to Profile Options");
            System.out.println("3. Withdraw funds");
            System.out.println("4. Deposit funds");
            System.out.print("-> ");
            Scanner scan = new Scanner(System.in);
            String s = scan.nextLine();
            int n = Integer.parseInt(s);

            switch (n){
                case 1: // Logout
                    manageAccountLoop = false;
                    currentUser = null;
                    choice();
                    break;
                case 2: // Back to profile options
                    manageAccountLoop = false;
                    choice();
                    break;
                case 3: withdraw();
                    break;
                case 4: deposit();
                    break;
                case 9: stop();
            }
        }
    }

    public void withdraw(){
        boolean wLoop = true;
        System.out.println(currentAccount.toString());
        while(wLoop){
            System.out.println("Enter amount: ");
            System.out.print("-> ");
            Scanner scan = new Scanner(System.in);
            String s = scan.nextLine();

            float money = currentAccount.getBalance();
            float f = Float.parseFloat(s);
            if((money - f) < 0){
                System.out.println("Insufficient funds");
            }
            else if(f < 0){
                System.out.println("You cannot withdraw a negative amount");
            }
            else{
                currentAccount.withdraw(f);
                wLoop = false;
            }
        }
    }

    public void deposit(){
        boolean dLoop = true;
        System.out.println(currentAccount.toString());
        while(dLoop){
            System.out.println("Enter amount: ");
            System.out.print("-> ");
            Scanner scan = new Scanner(System.in);
            String s = scan.nextLine();

            float money = currentAccount.getBalance();
            float f = Float.parseFloat(s);
            if(f < 0){
                System.out.println("You cannot withdraw a negative amount");
            }
            else{
                currentAccount.deposit(f);
                dLoop = false;
            }
        }
    }

    public void createProfile(){
        System.out.print("Please pick a username: ");
        String username = scan.nextLine();
        System.out.print("Please pick a password: ");
        String password = scan.nextLine();
        customers.addUser(username, password);

        //TODO auto login
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
        else{
            System.out.println("User: " + "'" + currentUser.getUserName() + "'" + " login successful");
        }
    }



    private void eraseFile(){
        go = false;
        try{
            PrintWriter writer = new PrintWriter("collections.dat");
            writer.print("");
            writer.close();
        }
        catch (FileNotFoundException e){}
    }

    public void setUserInput(int userInput) {
        this.userInput = userInput;
    }

    public void stop(){
        objs.setcc(customers);
        objs.setAc(accounts);

        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("collections.dat"))){
            oos.writeObject(objs);                                             // write to file
            System.out.println(objs);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
//        if(customers.getMaxLength() != 0){
//            try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.dat"))){
//                oos.writeObject(customers);                                             // write to file
//                System.out.println(customers);
//            }
//            catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if(accounts != null){
//            if(accounts.getMaxLength() != 0){
//                try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("accounts.dat"))){
//                    oos.writeObject(accounts);                                             // write to file
//                    System.out.println(accounts);
//                }
//                catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//                catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        go = false;
    }
}
