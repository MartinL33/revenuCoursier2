package fr.algorithmes;

import java.util.ArrayList;

public interface GroupableAddition<T> extends ComparableWithArg<T>{
    
    T addition(T t);
    
    boolean isARegouper(T t,Object regroupageSelected);

    void miseEnforme(Object regroupageSelected);

    ArrayList<T> cloneEclate(Object regroupageSelected);    
    
}
