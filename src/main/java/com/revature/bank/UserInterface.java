package com.revature.bank;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

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

    private String userChoice;

    private transient final Logger logger = LogManager.getLogger(AccountCollection.class);

    private CustomerCollection customers;
    private AccountCollection accounts;

    private User currentUser = null;
    private Account currentAccount = null;
    private boolean go = true;
    private CollectionHolder objs;
    //private int userInput;
    private Admin admin;
    private boolean adminLoggedIn = false;

    public UserInterface(){
        admin = new Admin();

        File file = new File("collections.dat");
        if(file.length() == 0){
            //System.out.println("collections file is empty ");
            objs = new CollectionHolder();

            objs.newCC();
            objs.newAC();

            customers = objs.getCC();
            accounts = objs.getAC();
        }
        else{
            //System.out.println("collections file is not empty");

            try(ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream("collections.dat"))){

                objs = (CollectionHolder) ois.readObject();                         // read from file
                //System.out.println("All Users:\n" + objs);
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


    }

    public void choice(boolean jump){
        while(go){
            if((currentUser == null || adminLoggedIn) && !jump){                               // LogIn menu
                System.out.println("\nWhat would you like to do today?");
                System.out.println("--------------------------------");
                System.out.println("1. Exit");
                if(!adminLoggedIn){
                    System.out.println("2. login");
                }
                System.out.println("3. create a profile");
                System.out.println("4. Erase file"); //TODO remove this
                if(adminLoggedIn){
                    System.out.println("----------Admin options----------");
                    System.out.println("5. View all Users");
                    System.out.println("6. View all Accounts");
                }

                System.out.print("-> ");
                Scanner scan = new Scanner(System.in);
                userChoice = scan.nextLine();
                int userInput = 0;

                try{
                    userInput = Integer.parseInt(userChoice);
                }
                catch(NumberFormatException e){
                    System.out.println("Invalid input");
                }
                switch (userInput){
                    case 1: stop();
                        break;
                    case 2: if(!adminLoggedIn){ logIn(); }
                        break;
                    case 3: createProfile();
                        break;
                    case 4: eraseFile();  //TODO and this
                        break;
                    case 5: viewAllUsers();
                        break;
                    case 6: viewAllAccounts();
                        break;
                    //default: stop();
                }
            }
            else if(currentUser != null || jump){                                                                   // User menu
                System.out.println("\nProfile Options");
                System.out.println("--------------------------------");
                System.out.println("1. Logout");
                System.out.println("2. Create new bank account");
                System.out.println("3. View Profile info");
                System.out.println("4. View Account options");
                if(adminLoggedIn){
                    System.out.println("----------Admin options----------");
                    System.out.println("5. Delete '" + currentUser.getUserName() + "'");
                }
                System.out.print("-> ");
                Scanner scan = new Scanner(System.in);
                userChoice = scan.nextLine();

                int userInput = 0;
                try{
                    userInput = Integer.parseInt(userChoice);
                }
                catch(NumberFormatException e){
                    System.out.println("Invalid input");
                }

                switch (userInput){
                    case 1: currentUser = null;
                        //go = false;
                        break;
                    case 2: newBankAccount();
                        break;
                    case 3: System.out.println(currentUser.toString());
                        break;
                    case 4: viewAccountInfo(currentUser);
                        break;
                    case 5:
                        deleteProfile();
                        viewAllUsers();
                        break;
                    case 9: stop();
                        break;
                    //default: stop();
                }
            }
        }
    }

    private void createProfile(){
        Scanner scan = new Scanner(System.in);
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

    private void deleteProfile(){
        customers.getUserList().remove(currentUser);
        //System.out.println("'" + currentUser.getUserName()+ "'" + " has been removed");
        logger.debug("'" + currentUser.getUserName()+ "'" + " has been removed");
        //TODO deleting profile should delete account if no other owners exist
    }

    private void newBankAccount(){
        accounts.createAccount(currentUser);
    }

    private void viewAccountInfo(User currentUser){
        String input;
        boolean loop = true;
        UUID chosenID = null;

        do{
            System.out.println("\nPlease pick an account to view");
            System.out.println("--------------------------------");
            for(int i = 0; i < currentUser.getIdList().size(); i++){
                System.out.println(i + ". " + currentUser.getIdList().get(i) + ", ");
            }
            System.out.println(currentUser.getIdList().size() + ". Back to Profile Options");
            System.out.print("-> ");
            Scanner scan = new Scanner(System.in);
            input = scan.nextLine();

            int index = -1; // Used to be 0
            try{
                index = Integer.parseInt(input);
            }
            catch(NumberFormatException e){
                System.out.println("Invalid input");
            }

            for(int i = 0; i < currentUser.getIdList().size(); i++){
                if(i == index){
                    chosenID = currentUser.getIdList().get(i);
                    loop = false;
                }
                if(index == currentUser.getIdList().size()){
                    // back to profile options
                    loop = false;
                    choice(true);
                }
            }
        } while(loop);

        currentAccount = accounts.returnAccount(chosenID);
        manageAccount();
    }

    private void manageAccount(){
        boolean manageAccountLoop = true;

        while(manageAccountLoop){                                                   // Account menu
            System.out.println(currentAccount.toString());
            System.out.println("Account Options");
            System.out.println("--------------------------------");
            System.out.println("1. Logout");
            System.out.println("2. Pick a different account");
            System.out.println("3. Back to Profile Options");
            System.out.println("4. Withdraw funds");
            System.out.println("5. Deposit funds");
            if(adminLoggedIn){
                System.out.println("----------Admin options----------");
            }
            System.out.print("-> ");
            Scanner scan = new Scanner(System.in);
            String s = scan.nextLine();

            int n = 0;
            try{
                n = Integer.parseInt(s);
            }
            catch(NumberFormatException e){
                System.out.println("Invalid input");
            }

            switch (n){
                case 1: // Logout
                    manageAccountLoop = false;
                    currentUser = null;
                    choice(false);
                    break;
                case 2: viewAccountInfo(currentUser);
                    break;
                case 3: // Back to profile options
                    manageAccountLoop = false;
                    choice(false);
                    break;
                case 4: withdraw();
                    break;
                case 5: deposit();
                    break;
                case 99: stop();
                    break;
            }
        }
    }

    private void viewAllUsers(){
        boolean loop = true;

        do{
            System.out.println("\nAll Users");
            System.out.println("--------------------------------");
            for(int i = 0; i < customers.getUserList().size(); i++){
                System.out.println(i + ". " + customers.getUserList().get(i).getUserName());
            }
            System.out.println(customers.getUserList().size() + ". Back to Profile Options");
            System.out.print("-> ");
            Scanner scan = new Scanner(System.in);
            String s = scan.nextLine();

            //TODO input validation -- not tested
            int index = -1;

            try{
                index = Integer.parseInt(s);
            }
            catch(NumberFormatException e){
                System.out.println("Invalid input");
            }

            for(int i = 0; i < customers.getUserList().size(); i++){
                if(index == i){
                    currentUser = customers.getUserList().get(i);
                    loop = false;
                    choice(true);
                }
                else if(index == customers.getUserList().size()){
                    loop = false;
                    choice(false);
                }
            }
        } while(loop);

    }

    private void viewAllAccounts(){
        boolean loop = true;
        do{
            System.out.println("\nAll Accounts");
            System.out.println("--------------------------------");
            for(int i = 0; i < accounts.getAccountList().size(); i++){
                System.out.println(i + ". " + accounts.getAccountList().get(i).getId() + "\n");
                //System.out.println("\t" + accounts.getAccountList().get(i).getUserList().toString());
            }

            System.out.println(accounts.getAccountList().size() + " Back to Profile Options");
            System.out.print("-> ");
            Scanner scan = new Scanner(System.in);
            String s = scan.nextLine();

            int index = -1;
            try{
                index = Integer.parseInt(s);
            }
            catch(NumberFormatException e){
                System.out.println("Invalid input");
            }

            for(int i = 0; i < accounts.getAccountList().size(); i++){
                if(index == i){
                    currentAccount = accounts.getAccountList().get(i);
                    loop = false;
                    choice(true);
                }
                else if (index == customers.getUserList().size()) {
                    loop = false;
                    choice(false);
                }
            }
        } while(loop);
    }

    private void withdraw(){
        boolean wLoop = true;
        System.out.println(currentAccount.toString());
        while(wLoop){
            System.out.println("Enter amount: ");
            System.out.print("-> ");
            Scanner scan = new Scanner(System.in);
            String s = scan.nextLine();

            float money = currentAccount.getBalance();

            int index = -1;
            float f = 0;
            try{
                f = Float.parseFloat(s);
            }
            catch(NumberFormatException e){
                System.out.println("Invalid input");
            }

            if((money - f) < 0){
                System.out.println("Insufficient funds");
            }
            else if(f < 0){
                System.out.println("You cannot withdraw a negative amount");
            }
            else{
                currentAccount.withdraw(f);
                if(adminLoggedIn){
                    logger.debug("admin withdrew " + f + " from account" + currentAccount.getId());
                }
                else{
                    logger.debug(currentUser.getUserName()+ " withdrew " + f + " from account" + currentAccount.getId());
                }
                wLoop = false;
            }
        }
    }

    private void deposit(){
        boolean dLoop = true;
        System.out.println(currentAccount.toString());
        while(dLoop){
            System.out.println("Enter amount: ");
            System.out.print("-> ");
            Scanner scan = new Scanner(System.in);
            String s = scan.nextLine();

            //float money = currentAccount.getBalance();
            float f = 0;
            try{
                f = Float.parseFloat(s);
            }
            catch(NumberFormatException e){
                System.out.println("Invalid input");
            }

            if(f < 0){
                System.out.println("You cannot withdraw a negative amount");
            }
            else{
                currentAccount.deposit(f);
                if(adminLoggedIn){
                    logger.debug("admin deposited " + f + " into account" + currentAccount.getId());
                }
                else{
                    logger.debug(currentUser.getUserName()+ " deposited " + f + " into account" + currentAccount.getId());
                }
                dLoop = false;
            }
        }
    }

    private void logIn(){
        boolean loop = true;
        do{
            Scanner scan = new Scanner(System.in);
            System.out.print("Enter your username: ");
            String u = scan.nextLine();
            System.out.print("Enter your password: ");
            String p = scan.nextLine();

            if(admin.getUserName().equals(u) && admin.getPassWord().equals(p)){
                adminLoggedIn = true;
            }
            else{
                currentUser = customers.checkUserAndPass(u, p);
                if(currentUser == null){
                    System.out.println("Wrong username or password");
                    loop = true;
                }
                else{
                    System.out.println("User: " + "'" + currentUser.getUserName() + "'" + " login successful");
                    logger.debug("User: " + "'" + currentUser.getUserName() + "'" + " logged in");
                    loop = false;
                }
            }
        } while(loop);
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

    private void stop(){
        objs.setcc(customers);
        objs.setAc(accounts);
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("collections.dat"))){
            oos.writeObject(objs);                                             // write to file
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        go = false;
    }
}
