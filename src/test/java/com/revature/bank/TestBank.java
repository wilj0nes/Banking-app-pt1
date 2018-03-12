package com.revature.bank;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestBank {

    Bank bank = new Bank();
    //CustomerCollection cc = new CustomerCollection();


    @Test
    public void TestInsert(){
        User u = new User("A", "A");
    }

    @Test
    public void TestShowAllUsers(){
        UserInterface ui = new UserInterface();
        ui.showAllUsers();
    }

    @Test
    public void testDeleteUser(){
        UserInterface ui = new UserInterface();
        ui.deleteUser();
    }

    @Test
    public void testDeposit(){
        AccountCollection ac = new AccountCollection();
        User u = new User("asdf", "asfd");
        Account a = new Account(u, ac.generateUUID());
        a.deposit(1);
    }

}
