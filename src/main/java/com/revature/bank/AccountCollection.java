package com.revature.bank;

import java.io.Serializable;


public class AccountCollection implements Serializable {

    private static final long serialVersionUID = -5726690022975290484L;
    private int maxLength = 10;
    private int currentIndex = 0;
    boolean stop = false;
    private Account[] accounts;

    public AccountCollection() {

    }


}
