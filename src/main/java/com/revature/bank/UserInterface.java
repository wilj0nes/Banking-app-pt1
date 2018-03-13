package com.revature.bank;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Scanner;
import java.util.UUID;

import java.sql.*;

public class UserInterface extends Bank implements Serializable {

    //private transient final Logger logger = LogManager.getLogger(AccountCollection.class);

    public User currentUser = null;
    private Account currentAccount = null;
    private boolean go = true;

    private Admin admin;
    private boolean adminLoggedIn = false;
    Scanner scan;

    public UserInterface(){

        admin = new Admin();
//        showAllUsers();
//        showAllAccounts();
    }

    public void displaySQL(){
        //System.out.println("displaySQL()");

        try {
            Connection conn = ConnectionFactory.getInstance().getConnection();
            String sql = "SELECT * FROM EMPLOYEE";
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                System.out.println(rs.getRow());
                System.out.println(rs.getString(2));
            }

            stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public void showAllUsers(){
        try {
            Connection conn = ConnectionFactory.getInstance().getConnection();
            String sql = "SELECT * FROM USERS";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                System.out.print(rs.getInt("USER_ID") + ". ");
                System.out.println(rs.getString("USERNAME"));
                //System.out.println(rs.getString("TYPE"));
            }

            //stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public void showAllAccounts(){
        try {
            Connection conn = ConnectionFactory.getInstance().getConnection();
            String sql = "SELECT * FROM ACCOUNTS ORDER BY ACCOUNT_ID";

            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                System.out.print(rs.getInt("ACCOUNT_ID") + ". ");
                System.out.print(rs.getFloat("BALANCE") + ", ");
                System.out.print(rs.getString("STATUS") + ", ");
                System.out.print(rs.getString("TYPE") + ", \n");
            }

            stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
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
                    System.out.println("2. Login");
                }
                else{
                    System.out.println("2. Logout");
                }
                System.out.println("3. Create a profile");
                if(adminLoggedIn){
                    System.out.println("----------Admin options----------");
                    System.out.println("4. Erase all data");
                    System.out.println("5. View all Users");
                    System.out.println("6. View all Accounts");
                }
                System.out.print("-> ");
                scan = new Scanner(System.in);
                userChoice = scan.nextLine();
                int userInput = 0;

                try{
                    userInput = Integer.parseInt(userChoice);
                }
                catch(NumberFormatException e){
                    System.out.println("Invalid input");
                }
                switch (userInput){
                    case 1: exit();
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
                    case 4: //eraseFile();
                        break;
                    case 5: viewAllUsers();
                        break;
                    case 6: viewAllAccounts();
                        break;
                    //default: exit();
                }
            }
            else if(currentUser != null || jump){                                                                   // User menu
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
                 scan = new Scanner(System.in);
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
                            deleteUser(this.currentUser.getId());
                            //viewAllUsers();
                        }
                        break;
                    case 6:                                             // logout
                        if(adminLoggedIn){
                            currentUser = null;
                            choice(false);
                        }
                        break;
                }
            }
        }
    }

    public void createProfile(){
        User u = new User();
        scan = new Scanner(System.in);
        System.out.print("Please pick a username: ");
        String username = scan.nextLine();
        System.out.print("Please pick a password: ");
        String password = scan.nextLine();
        u.insertUser(username, password);
        this.currentUser = searchUser(username, password);

    }

    @SuppressWarnings("Duplicates")
    public void deleteUser(int id){
        try {
            Connection conn = ConnectionFactory.getInstance().getConnection();
            String sql = "DELETE FROM USERS " +
                    "WHERE USER_ID = " + id;
            //TODO make sure every thing commits
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

//            while(rs.next()){
//                System.out.println(rs);
//                System.out.println(rs);
//            }
            sql = "COMMIT";
            rs = stmt.executeQuery(sql);

            stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        showAllUsers();
    }

    @SuppressWarnings("Duplicates")
    public void deleteAccount(int id){
        try {
            Connection conn = ConnectionFactory.getInstance().getConnection();
            String sql = "DELETE FROM ACCOUNTS " +
                    "WHERE ACCOUNT_ID = " + id;
            //TODO make sure every thing commits
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

//            while(rs.next()){
//                System.out.println(rs);
//                System.out.println(rs);
//            }
            sql = "COMMIT";
            rs = stmt.executeQuery(sql);

            stmt.close();
        }
        catch (SQLException e) {
            //e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private void newBankAccount(){
        Account a = new Account();
        a.insertAccount(currentUser.getId());


        //System.out.println("----->" + );

        currentAccount = searchAccount(a.getId());
        System.out.println(currentAccount.toString());
    }

    private void viewAccountInfo(User currentUser){
        String input;
        boolean loop = true;

        System.out.println("\nPlease pick an account to view");
        System.out.println("--------------------------------");

        showAllAccounts();
        while(loop){

            System.out.print("-> ");
            scan = new Scanner(System.in);
            input = scan.nextLine();
            if(input == "back"){
                currentAccount = searchAccount(2);
                loop = false;
                choice(true);
            }

            int index = -1;
            try{
                index = Integer.parseInt(input);
            }
            catch(NumberFormatException e){
                System.out.println("Invalid input");
            }

            currentAccount = searchAccount(index);

            //System.out.println(currentAccount.toString());


            //currentAccount = accounts.returnAccount(chosenID);
            if(adminLoggedIn){
                adminEditAccount(currentAccount);
            }
            else{
                manageAccount();
            }
        }
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
            scan = new Scanner(System.in);
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
                        deleteUser(currentUser.getId());
                        //accounts.deleteUnusedAccounts(currentUser.getIdList());
                    }
                    break;
            }
        }
    }

    private void viewAllUsers(){
        boolean loop = true;
        showAllUsers();

        do{
            System.out.println("\nAll Users");
            System.out.println("--------------------------------");
//            for(int i = 0; i < customers.getUserList().size(); i++){
//                System.out.println(i + ". " + customers.getUserList().get(i).getUserName());
//            }
            //System.out.println(customers.getUserList().size() + ". Go back <--");
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

//            for(int i = 0; i < customers.getUserList().size(); i++){
//                if(index == i){
//                    currentUser = customers.getUserList().get(i);
//                    loop = false;
//                    choice(true);
//                }
//                else if(index == customers.getUserList().size()){
//                    loop = false;
//                    choice(false);
//                }
//            }
        } while(loop);
    }

    private void viewAllAccounts(){
        boolean loop = true;

        do{
            System.out.println("\nAll Accounts");
            System.out.println("--------------------------------");
            showAllAccounts();
            System.out.print("-> ");
            Scanner scan = new Scanner(System.in);
            String s = scan.nextLine();
            showAllAccounts();

            int index = -1;
            try{
                index = Integer.parseInt(s);
            }
            catch(NumberFormatException e){
                System.out.println("Invalid input");
            }

//            for(int i = 0; i < accounts.getAccountList().size(); i++){
//                if(index == i){
//                    currentAccount = accounts.getAccountList().get(i);
//                    loop = false;
//                    //choice(true);
//                    adminEditAccount(currentAccount);
//                }
//            }
//            if (index == accounts.getAccountList().size()){
//                loop = false;
//                choice(false); // back to profile options
//            }
        } while(loop);
    }


    private void withdraw(){
        boolean loop = true;
        System.out.println(currentAccount.toString());
        while(loop){
            System.out.println("Enter amount: ");
            System.out.print("-> ");
             scan = new Scanner(System.in);
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
                withdrawFunds(f, currentAccount.getId());
                if(adminLoggedIn){
//                    logger.trace("admin withdrew " + f + " from account " + currentAccount.getId());
                }
                else{
//                    logger.trace(currentUser.getUserName()+ " withdrew " + f + " from account " + currentAccount.getId());
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
             scan = new Scanner(System.in);
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
                //currentAccount.deposit(f, currentUser.getId());
                depositFunds(f, currentAccount.getId());
                if(adminLoggedIn){
//                    logger.trace("admin deposited " + f + " into account " + currentAccount.getId());
                }
                else{
//                    logger.trace(currentUser.getUserName()+ " deposited " + f + " into account " + currentAccount.getId());
                }
                loop = false;
            }
        }
    }

    private void adminEditAccount(Account account){
        boolean loop = true;
        do{                                            // Admin account options
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

             scan = new Scanner(System.in);
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
                case 4: setApprovedOrDeny(true);
                    break;
                case 5: setApprovedOrDeny(false);
                    break;
                case 6: deleteAccount(this.currentUser.getId());
                    break;
                case 7:
                    loop = false;
                    choice(true); // to profile options
                    break;
            }
        } while (loop);
    }

    private void logIn(){
        User user = new User();
        //user = null;
        boolean loop = true;
        do{
            scan = new Scanner(System.in);
            System.out.print("Enter your username: ");
            String u = scan.nextLine();
            System.out.print("Enter your password: ");
            String p = scan.nextLine();



            currentUser = searchUser(u, p);

            //System.out.println(currentUser.toString());

            if(u.equals("admin") && p.equals("a")){
                System.out.println("Admin login sucessful");
                adminLoggedIn = true;
                loop = false;
                choice(true);

            }
            else if(currentUser != null){
                //System.out.println("Welcome Back!");
                try{
                    BufferedReader br = new BufferedReader(
                            new FileReader("Success.txt"));

                    String line = null;
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                    }
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                catch (IOException e){}
                System.out.println("\n");
                this.currentUser = user;
                loop = false;
                choice(true);
            }
            else{
                System.out.println("Wrong username or password");
                loop = true;
            }
        } while(loop);
    }

    @SuppressWarnings("Duplicates")
    public Account searchAccount(int id){
        Account a = new Account();
        try {
            Connection conn = ConnectionFactory.getInstance().getConnection();

            String sql = "SELECT * FROM ACCOUNTS WHERE ACCOUNT_ID = " + id;

            //TODO make sure every thing commits
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                System.out.println(rs.getInt("ACCOUNT_ID") + "--------------");
                a.setId(rs.getInt("ACCOUNT_ID"));
                a.setStatus(rs.getString("STATUS"));
                a.setBalance(rs.getFloat("BALANCE"));
                a.setType(rs.getString("TYPE"));
            }

//            System.out.println(rs.getInt("ACCOUNT_ID") + "--------------");
//            a.setId(rs.getInt("ACCOUNT_ID"));
//            a.setStatus(rs.getString("STATUS"));
//            a.setBalance(rs.getFloat("BALANCE"));
//            a.setType(rs.getString("TYPE"));
            stmt.close();
            return a;
            //TODO insert into joint table using the user id;
        }
        catch (SQLException e) {
            //e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        return null;

    }

    public User searchUser(String user, String pass){
        User u = new User();
        try {
            Connection conn = ConnectionFactory.getInstance().getConnection();
            String sql = "SELECT * FROM" + " USERS WHERE USERNAME = '" + user + "'";
            //TODO make sure every thing commits
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
//                u.setId(rs.getInt("USER_ID"));
//                u.setUserName(rs.getString("USERNAME"));
//                u.setPassWord(rs.getString("PASSWORD"));
                u.setId(rs.getInt("USER_ID"));
                u.setUserName(rs.getString("USERNAME"));
                u.setPassWord(rs.getString("PASSWORD"));
            }
            System.out.println(u.getId());
            stmt.close();
            return u;
        }
        catch (SQLException e) {
            //e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        return null;
    }

    public Account returnAccount(int n){
        Account a = new Account();
        try {
            Connection conn = ConnectionFactory.getInstance().getConnection();
            String sql = "SELECT * FROM ACCOUNTS" +
                    "WHERE ACCOUNT_ID = " + n;
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                a.setId(rs.getInt("ACCOUNT_ID"));
                a.setStatus(rs.getString("STATUS"));
                a.setBalance(rs.getFloat("BALANCE"));
                a.setType(rs.getString("TYPE"));
            }

            stmt.close();
            return a;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        return a;
    }

    private void eraseFile(){
        go = false;
//        try{
//            PrintWriter writer = new PrintWriter("collections.dat");
//            writer.print("");
//            writer.close();
//        }
//        catch (FileNotFoundException e){
//            logger.debug("collections.dat not found");
//        }
    }

    private void exit(){
        go = false;
        try{
            BufferedReader br = new BufferedReader(
                    new FileReader("goodbye.txt"));

            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e){}
        System.out.println("\n");
//        objs.setcc(customers);
//        objs.setAc(accounts);
//        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("collections.dat"))){
//            oos.writeObject(objs);                                             // write to file
//        }
//        catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    @SuppressWarnings("Duplicates")
    public void setApprovedOrDeny(boolean b){

        if(b){
            currentAccount.setStatus("Approved");
        }
        else{
            currentAccount.setStatus("Denied");
        }
        try {
            Connection conn = ConnectionFactory.getInstance().getConnection();
            String sql = "UPDATE ACCOUNTS SET STATUS = " + currentAccount.getStatus() + " " +
                            "WHERE ACCOUNT_ID = " + currentAccount.getId();
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);
            stmt.close();
        }
        catch (SQLException e) {
            //e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        commit();
    }

    @SuppressWarnings("Duplicates")
    public void depositFunds(float f, int id){
        currentAccount.setBalance(currentAccount.getBalance() + f);

        try {
            Connection conn = ConnectionFactory.getInstance().getConnection();
            String sql = "UPDATE ACCOUNTS SET BALANCE = " + currentAccount.getBalance() + " WHERE ACCOUNT_ID = " + id;
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);
            stmt.close();
        }
        catch (SQLException e) {
            //e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        try{
            BufferedReader br = new BufferedReader(
                    new FileReader("money.txt"));

            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e){}
        System.out.println("\n");
        commit();
    }

    @SuppressWarnings("Duplicates")
    public void withdrawFunds(float f, int id){
        currentAccount.setBalance(currentAccount.getBalance() - f);

        try {
            Connection conn = ConnectionFactory.getInstance().getConnection();
            String sql = "UPDATE ACCOUNTS SET BALANCE = " + currentAccount.getBalance() + " WHERE ACCOUNT_ID = " + id;
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);
            stmt.close();
        }
        catch (SQLException e) {
            //e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

        try{
            BufferedReader br = new BufferedReader(
                    new FileReader("money.txt"));

            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e){}
        commit();
    }



    public void commit(){
        User u = new User();
        try {
            Connection conn = ConnectionFactory.getInstance().getConnection();
            String sql = "COMMIT";
            //TODO make sure every thing commits
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println(u.getId());
            stmt.close();
        }
        catch (SQLException e) {
            //e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }
}
