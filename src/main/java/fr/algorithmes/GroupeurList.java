package fr.algorithmes;

import java.util.ArrayList;
import java.util.Iterator;

import fr.modele.Shift;

public class GroupeurList<T> {

	ArrayList<T> list=null;
	Groupeur<T> modeRegroupageSelected=null;


	public GroupeurList(Groupeur<T> modeRegroupageSelected){
		this.modeRegroupageSelected=modeRegroupageSelected;
		list=new ArrayList<T>();	
	}

	public void setList(ArrayList<T> list){
		this.list=list;
	}

	public ArrayList<T> grouper(){
		assert(modeRegroupageSelected!=null);
		assert(list!=null);

		ArrayList<T> result = new ArrayList<T>();	
		ArrayList<T> aRegrouper = new ArrayList<T>();		

		
		if(controleList()) {			

			removeDoublonAndTri();
			
			
			Iterator<T> iterator=list.iterator();
			T  tRef=iterator.next();
			aRegrouper.add(tRef);
			
			while(iterator.hasNext()){
				
				T t=iterator.next();
				
				if(modeRegroupageSelected.isARegrouper(t,tRef)) {
					aRegrouper.add(t);
				}

				else{ 
					T tRegroupe = modeRegroupageSelected.groupement(aRegrouper);
					result.add(tRegroupe);
					aRegrouper=new ArrayList<T>();	
					aRegrouper.add(t);
					tRef=t;					
				}   
			}
			T tRegroupe = modeRegroupageSelected.groupement(aRegrouper);
			result.add(tRegroupe);
			
		}		
		return result;
	}

	private Boolean controleList(){
		if (list.isEmpty()) return false;	
		if(list.get(0)==null) return false;
		return true;
	}


	private void removeDoublonAndTri(){

		list.sort(modeRegroupageSelected);
		for(int index=0;index<list.size()-1;index++){                
			if(list.get(index).equals(list.get(index+1))){		   
				list.remove(index);	
				index--;
			}                
		}		
	}


	void affiche() {
		System.out.println(this.toString());
	}

	public String toString() {
		StringBuilder res=new StringBuilder();
		res.append("Groupeur"+"\n");
		for(T t:list) {
			res.append(t.toString()+"\n");
		}
		return res.toString();
	}



}
