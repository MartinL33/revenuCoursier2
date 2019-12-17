package fr.algorithmes;

import java.util.ArrayList;
import java.util.Comparator;

public interface Groupeur<T> extends Comparator<T>{  
    
    T groupement (ArrayList<T> list);
    
    boolean isARegrouper(T t1,T t2);
  
}
