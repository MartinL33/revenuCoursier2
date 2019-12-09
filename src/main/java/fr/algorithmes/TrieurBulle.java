package fr.algorithmes;

import java.util.ArrayList;

public class TrieurBulle<T extends ComparableWithArg<T>> implements Trieur<T> {
    ArrayList<T> array;
    Object modeTri;
    public TrieurBulle() { }  

    @Override
    public void setArray(ArrayList<T> array) {
	this.array=array;
	
    }
    
    @Override
    public void setModeTri(Object modeTri) {
	this.modeTri=modeTri;
    }
    
    @Override
    public void tri() {
	int compteur;
	T element;

	for (int i = 1; i < array.size() ; i++){    
	    element = array.get(i);
	    compteur = i - 1;
	    while (compteur >= 0 && array.get(compteur).compareTo(element,modeTri)>0)
	    {
		array.set(compteur + 1, array.get(compteur));		          
		compteur--;
	    }
	    array.set(compteur + 1, element);
	}
	
    }

   

   
    
}
