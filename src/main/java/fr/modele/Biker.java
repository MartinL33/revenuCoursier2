/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.modele;

import static fr.modele.Value.regroupageSelected;

import java.util.ArrayList;

import fr.algorithmes.GroupeurAddition;

/**
 *
 * @author Martin Lepers
 */



public abstract class Biker  {

    protected String nameBiker="";	
    protected int nbFactures=0;	
    private ArrayList<Shift> arrayListShift=new ArrayList<>();

    public String getNameBiker() {
	return nameBiker;
    }	

    void setNameBiker(String nameBiker) {
	this.nameBiker=nameBiker;
    }

    int getNbFactures() {
	return nbFactures;
    }

    void addFacture(Facture facture) {
	this.addList(facture.getShifts());
	this.nbFactures++;
    }

    void addList(ArrayList<Shift> l){	   
	for(Shift s:l) {
	    addShift(s);
	}	
    }

    abstract void addShift(Shift shift);

    public ArrayList<Shift> getList(){
	return arrayListShift;
    }

    void setList(ArrayList<Shift> listShift) {
	arrayListShift=listShift;
    }

    public Boolean isEmpty(){
	return arrayListShift.isEmpty();
    }

    public int getSize(){
	return arrayListShift.size();
    }
    
    abstract Biker clone2();

    void setProprity(Biker biker) {
	this.nameBiker=biker.nameBiker;	
	this.nbFactures=biker.nbFactures;
    }

    public Shift getFirstShift() {
	return arrayListShift.get(0);
    }     

    
    
    Shift getShift(Shift shift){

	for(Shift shift1:arrayListShift){
	    if(shift.isARegouper(shift1,Value.regroupageSelected)){
		return shift1;
	    }
	}
	return null;	
    }

    @Override
    public String toString(){
	String res="";
	for(Shift shift:arrayListShift){
	    res=res+ shift.toString()+";\n";
	}
	return res;
    }

    void setNameFile(String nameFile){
	if(!nameFile.isEmpty()&&!nameFile.equals("")){
	    for(Shift shift:arrayListShift){
		shift.setNameFile(nameFile);	
	    }
	}	
    }

    protected BikerImp[] getTabBikerParAnnee() {

	ArrayList<BikerImp> res =new ArrayList<>();

	for(Shift s:this.getList()) {

	    if(res.isEmpty()) {
		BikerImp b=new BikerImp();
		b.setNameBiker(String.valueOf(s.getAnnee()));				
		b.addShift(s.clone2());
		res.add(b);
	    }
	    else {
		boolean newBiker=true;
		
		for (Biker b:res) {					
		    if(b.getNameBiker().equals(String.valueOf(s.getAnnee()))) {
			newBiker=false;
			b.addShift(s.clone2());
			break;
		    }
		}

		if(newBiker) {				
		    BikerImp b=new BikerImp();
		    b.setNameBiker(String.valueOf(s.getAnnee()));					
		    b.addShift(s.clone2());
		    res.add(b);
		}
	    }
	}

	return (BikerImp[]) res.toArray(new BikerImp[res.size()]);

    }  

    Biker bikerCloneWithShiftRegroupe() {
	Biker res= (Biker) this.clone2();
	GroupeurAddition<Shift> groupeurAddition=new GroupeurAddition<Shift>(Value.regroupageSelected);
		
	groupeurAddition.setList(res.arrayListShift);
	res.arrayListShift=groupeurAddition.grouperAddition();
	
	return res;
    }    

    String getLineToWrite(Shift shiftTotal) {

	String res;
	Shift shiftBiker=getShift(shiftTotal);
	if(shiftBiker==null) {
	    res=regroupageSelected.separateurEnteteEnd+Value.separateurCSV;
	}
	else {
	    regroupageSelected.setShift(shiftBiker);  
	    res=regroupageSelected.lineBikerCA();
	}
	return res;
    }

}

