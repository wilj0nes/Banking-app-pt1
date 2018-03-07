package com.revature.bank;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestBank {

    Bank bank = new Bank();
    CustomerCollection cc = new CustomerCollection();

    @Test
    public void choiceText(){
        UserInterface ui = new UserInterface();
        ui.setUserInput(1);
        ui.choice(false);
    }
    @BeforeClass

    @Test
    public void testAddUser(){
//        cc.addUser("will", "pass");
//        cc.addUser("jones", "pass");
//        cc.toString();
    }

//    @Test
//    public void test_checkUserAndPass(){
//        User u = new User("will", "pass1");
//        User uu = cc.checkUserAndPass("will", "pass1");
//        assertEquals(u, uu);
//    }

    @Test
    public void test_toString(){
        System.out.println(cc.toString());
    }

}
