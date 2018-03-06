package com.revature.bank;

import java.io.Serializable;

public class CollectionHolder implements Serializable {
    private transient static final long serialVersionUID = 2679870783007889695L;

    private Object[] collectionArray;

    public CollectionHolder(){
        collectionArray = new Object[2];
    }

    public void saveCollection(Object[] objArr, int index, int length){
        collectionArray[index] = objArr;
    }

}
