package fr.algorithmes;

import java.util.ArrayList;

public interface Trieur<T extends Comparable<T>> {   
    void setArray(ArrayList<T> array);
    void setModeTri(Object modeTri);
    void tri();
}
