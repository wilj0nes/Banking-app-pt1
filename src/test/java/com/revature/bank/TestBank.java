package com.revature.bank;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestBank {

    Bank bank = new Bank();
    //CustomerCollection cc = new CustomerCollection();


    @Test
    public void TestInsert(){
        User u = new User();
        u.insertUser("d", "d");
    }

    @Test
    public void TestShowAllUsers(){
        UserInterface ui = new UserInterface();
        ui.showAllUsers();
    }

    @Test
    public void displaySQL(){
        UserInterface ui = new UserInterface();
        ui.displaySQL();
    }

    @Test
    public void testDeleteUser(){
        UserInterface ui = new UserInterface();
        ui.deleteUser(0);
    }



}
