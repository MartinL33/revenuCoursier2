package algorithmes;

import java.util.ArrayList;

public class GroupeurAddition<T extends GroupableAddition<T>> {

    ArrayList<T> list;
    Object modeRegroupageselected=null;

    public GroupeurAddition(Object modeRegroupageselected){
	setModeRegoupage(modeRegroupageselected);	
	list=new ArrayList<T>();	
    }

    public void setList(ArrayList<T> list){
	this.list=list;
    }

    void setModeRegoupage(Object modeRegroupageselected) {	
	this.modeRegroupageselected=modeRegroupageselected;
    }

    public ArrayList<T> grouperAddition(){
	assert(modeRegroupageselected!=null);

	ArrayList<T> res = new ArrayList<T>();	
	GroupeurAddition<T> source = this.cloneEclate();	
	if(source.controleList()) {			
	    T total=null;      

	    for(T t:source.list){
		if(total==null) total=t;

		else if(total.isARegouper(t,this.modeRegroupageselected)) {
		    total.addition(t);		    
		}

		else{  
		    total.miseEnforme(this.modeRegroupageselected);
		    res.add(total);
		    total=t; 
		}             
	    }

	    total.miseEnforme(this.modeRegroupageselected);
	    res.add(total);                  
	}		
	return res;
    }


    private GroupeurAddition<T> cloneEclate(){
	GroupeurAddition<T> res=new GroupeurAddition<T>(this.modeRegroupageselected);

	for(T t:list){
	    res.list.addAll(t.cloneEclate(modeRegroupageselected));
	}
	res.removeDoublonAndTri();	
	return res;
    }  

    private void removeDoublonAndTri(){
	if(controleList()){
	    tri();	
	    for(int index=0;index<list.size()-1;index++){                
		if(list.get(index).equals(list.get(index+1))){		   
		    list.remove(index);	
		    index--;
		}                
	    }
	}
    }

    private Boolean controleList(){
	if (list.isEmpty()) return false;
	if (list.size() < 2 ) return false;
	if(list.get(0)==null) return false;
	return true;
    }

    void tri(){      
	Trieur<T> trieur = new TrieurBulle<T>();
	trieur.setModeTri(modeRegroupageselected);
	trieur.setArray(list);	
	trieur.tri();
	
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
