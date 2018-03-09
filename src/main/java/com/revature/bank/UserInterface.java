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

    private transient final Logger logger = LogManager.getLogger(AccountCollection.class);

    private CustomerCollection customers;
    private AccountCollection accounts;

    private User currentUser = null;
    private Account currentAccount = null;
    private boolean go = true;
    private CollectionHolder objs;

    private Admin admin;
    private boolean adminLoggedIn = false;

    public UserInterface(){
        admin = new Admin();

        File file = new File("collections.dat");
        if(file.length() == 0){
            objs = new CollectionHolder();

            objs.newCC();
            objs.newAC();

            customers = objs.getCC();
            accounts = objs.getAC();
        }
        else{
            try(ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream("collections.dat"))){

                objs = (CollectionHolder) ois.readObject();                         // read from file
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
        String userChoice;

        while(this.go){
            if((currentUser == null || adminLoggedIn) && !jump){                               // LogIn menu
                System.out.println("\nMain Menu");
                System.out.println("--------------------------------");
                System.out.println("1. Exit");
                if(!adminLoggedIn){
                    System.out.println("2. login");
                }
                else{
                    System.out.println("2. Logout");
                }
                System.out.println("3. create a profile");
                if(adminLoggedIn){
                    System.out.println("----------Admin options----------");
                    System.out.println("4. Erase all data"); //TODO remove this
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
                    case 2:
                        if(!adminLoggedIn) {
                            logIn();
                        }
                        else{
                            adminLoggedIn = false;
                        }
                        break;
                    case 3: createProfile();
                        break;
                    case 4: //eraseFile();  //TODO and this
                        break;
                    case 5: viewAllUsers();
                        break;
                    case 6: viewAllAccounts();
                        break;
                    //default: stop();
                }
            }
            else if(currentUser != null || jump){                                                                   // User menu
//                if(currentAccount != null){
//                    System.out.println(currentUser.toString());
//                }
                System.out.println("\nProfile Options");
                System.out.println("--------------------------------");
                System.out.println("1. Logout");
                if(currentUser != null){
                    System.out.println("2. Create a new bank account");
                }
                System.out.println("3. View Profile info");
                System.out.println("4. View Account options");
                if(adminLoggedIn){
                    System.out.println("----------Admin options----------");
                    if(currentUser != null){
                        System.out.println("5. Delete '" + currentUser.getUserName() + "'");
                        System.out.println("6. Go back <--");
                    }
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
                    case 1: currentUser = null;                         // Logout
                        choice(false);
                        break;
                    case 2:                                             // new bank account
                        if(currentUser != null){
                            newBankAccount();
                        }
                        break;
                    case 3: System.out.println(currentUser.toString()); // User info
                        break;
                    case 4: viewAccountInfo(currentUser);               // account info
                        break;
                    case 5:                                             // delete profile
                        if(currentUser != null){
                            deleteProfile();
                            viewAllUsers();
                        }
                        break;
                    case 6:                                             // logout
                        if(adminLoggedIn){
                            currentUser = null;
                        }
                        break;
                    case 99: stop();                                    // Exit
                        break;
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
        currentUser = customers.addUser(username, password);
        logger.trace("User: " + currentUser.getUserName() + ", has been added to the userList");
    }

    private void deleteProfile(){
        //customers.getUserList().remove(currentUser);
        customers.deleteUser(currentUser, this.accounts);
        logger.trace("'" + currentUser.getUserName()+ "'" + " has been removed");
        //TODO deleting profile should delete account if no other owners exist
    }

    private void newBankAccount(){
        currentAccount = accounts.createAccount(currentUser);
        logger.trace(currentAccount + ", has been added to the accountList by " + currentUser.getUserName());
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
            System.out.println(currentUser.getIdList().size() + ". Back to Profile Options <--");
            System.out.print("-> ");
            Scanner scan = new Scanner(System.in);
            input = scan.nextLine();

            int index = -1;
            try{
                index = Integer.parseInt(input);
            }
            catch(NumberFormatException e){
                System.out.println("Invalid input");
            }

            if(index == currentUser.getIdList().size()){
                // back to profile options
                loop = false;
                choice(true);
            }
            for(int i = 0; i < currentUser.getIdList().size(); i++){
                if(i == index){
                    chosenID = currentUser.getIdList().get(i);
                    loop = false;
                }
            }

        } while(loop);

        currentAccount = accounts.returnAccount(chosenID);
        manageAccount();
    }


    private void manageAccount(){
        boolean loop = true;

        while(loop){                                                   // Account menu
            if(this.currentAccount != null){
                System.out.println(currentAccount.toString());
            }
            System.out.println("Account Options");
            System.out.println("--------------------------------");
            System.out.println("1. Logout");
            System.out.println("2. Pick a different account");
            System.out.println("3. Withdraw funds");
            System.out.println("4. Deposit funds");
            System.out.println("5. Back to Profile Options <--");
            if(adminLoggedIn){
                System.out.println("----------Admin options----------");
                System.out.println("6. Delete User");

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
                    this.go = true;
                    loop = false;
                    currentUser = null;
                    choice(false);
                    break;
                case 2: viewAccountInfo(currentUser);
                    break;
                case 3: withdraw();
                    break;
                case 4: deposit();
                    break;
                case 5: // Back to profile options
                    loop = false;
                    choice(false);
                case 6:
                    if(currentUser != null){
                        deleteProfile();
                        accounts.deleteUnusedAccounts(currentUser.getIdList());
                    }
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
            System.out.println(customers.getUserList().size() + ". Go back <--");
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

                if(accounts.getAccountList().get(i).getApproval()) { // approved
                    System.out.println(i + ". " + accounts.getAccountList().get(i).getId());
                }
                else{ // pending approval
                    System.out.println(i + ". " + accounts.getAccountList().get(i).getId() + " -Pending Approval");
                }
                System.out.print("\tOwners: ");
                for(int j = 0; j < accounts.getAccountList().get(i).getUserList().size(); j++){
                    System.out.println( accounts.getAccountList().get(i).getUserList().get(j).getUserName() + ", ");
                }
            }

            System.out.println(accounts.getAccountList().size() + ". Go back <--");
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
                    //choice(true);
                    adminEditAccount(currentAccount);
                }
            }
            if (index == accounts.getAccountList().size()){
                loop = false;
                choice(false); // back to profile options
            }
        } while(loop);
    }


    private void withdraw(){
        boolean loop = true;
        System.out.println(currentAccount.toString());
        while(loop){
            System.out.println("Enter amount: ");
            System.out.print("-> ");
            Scanner scan = new Scanner(System.in);
            String s = scan.nextLine();

            float money = currentAccount.getBalance();

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
                    logger.trace("admin withdrew " + f + " from account " + currentAccount.getId());
                }
                else{
                    logger.trace(currentUser.getUserName()+ " withdrew " + f + " from account " + currentAccount.getId());
                }
                loop = false;
            }
        }
    }


    private void deposit(){
        boolean loop = true;
        System.out.println(currentAccount.toString());

        while(loop){
            System.out.println("Enter amount: ");
            System.out.print("-> ");
            Scanner scan = new Scanner(System.in);
            String s = scan.nextLine();

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
                    logger.trace("admin deposited " + f + " into account " + currentAccount.getId());
                }
                else{
                    logger.trace(currentUser.getUserName()+ " deposited " + f + " into account " + currentAccount.getId());
                }
                loop = false;
            }
        }
    }

    private void adminEditAccount(Account account){
        boolean loop = true;
        do{                                                         // Admin account options
            System.out.println(account.toString());
            System.out.println("Admin Account Options");
            System.out.println("--------------------------------");
            System.out.println("1. Pick a different account <--");
            System.out.println("2. Withdraw funds");
            System.out.println("3. Deposit funds");
            System.out.println("4. Approve account");
            System.out.println("5. Deny account");
            System.out.println("6. Delete account");
            System.out.println("7. Back <--");
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
                case 1:
                    loop = false;
                    viewAllAccounts();
                    break;
                case 2: withdraw();
                    break;
                case 3: deposit();
                    break;
                case 4: account.setApproved(true);
                    break;
                case 5: account.setApproved(false);
                    break;
                case 6: accounts.deleteAccount(account);
                    break;
                case 7: choice(true); // to profile options
                    loop = false;
                    break;
            }
        } while (loop);
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
                loop = false;
            }
            else{
                currentUser = customers.checkUserAndPass(u, p);
                if(currentUser == null){
                    System.out.println("Wrong username or password");
                    loop = true;
                }
                else{
                    System.out.println("User: " + "'" + currentUser.getUserName() + "'" + " login successful");
                    logger.trace("User: " + "'" + currentUser.getUserName() + "'" + " logged in");
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
