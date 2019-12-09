package importFacture;

import GUI.GUI;
import Map.Localisation;
import Map.Map;
import algorithmes.Trieur;
import algorithmes.TrieurBulle;

public class BikerImpGPS extends Biker implements Cloneable{

    Map map=null;    
    Localisation locaDomicile=null;
    
    public Map getMap() {
	return map;
    }
    
    void setLocaDomicile(Localisation locaDomicile) {
	this.locaDomicile=locaDomicile;
    }

    static BikerImpGPS createFromBiker(Biker biker) {
	BikerImpGPS res=new BikerImpGPS();
	res.setProprity(biker);
	for(Shift shift:biker.getList()) {

	    ShiftImplGPS  shiftGPS= ShiftImplGPS.createFromShift(shift);	 
	    res.addShift(shiftGPS);
	}
	return res;
    }  

    @Override
    Biker clone2() {	
	return clone();
    }


    @Override
    public BikerImpGPS clone() {

	BikerImpGPS clone=new BikerImpGPS();
	clone.setProprity(this);
	for(Shift shift:this.getList()){
	    clone.addShift(shift.clone2());
	}
	clone.map=(Map) map.clone();
	if(this.locaDomicile!=null) clone.locaDomicile=this.locaDomicile.clone();    
	return clone;
    }


    @Override
    void addShift(Shift shift) {
	assert(shift!=null);
	assert(shift.getDuree()>0);	
	shift=(ShiftImplGPS.createFromShift(shift)) ;
	(this.getList()).add(shift);	
    }

    void setMap(Map map) {

	Regroupement r=RegroupementToutLesShifts.getInstance();			

	Trieur<Shift> trieur = new TrieurBulle<Shift>();
	trieur.setModeTri(r);
	trieur.setArray(this.getList());	
	trieur.tri();
	GUI.updateUI("Intersection whith Biker " +getNameBiker());
	Map map2=map.clone();
	map2.intersectionWithBiker(this);

	this.map=map2;	
	GUI.updateUI("Nettoyage carte");

	map.fixTracks();
	GUI.updateUI("Mise à jour shifts du Biker "+getNameBiker());

	calculDistance();

    } 

    void calculDistance() {
	
	for(Shift shift:this.getList()) {
	    assert shift instanceof ShiftImplGPS;
	    ShiftImplGPS shift2=(ShiftImplGPS)shift;
	    calculDistance(shift2);	    
	}		
    }

    private void calculDistance(ShiftImplGPS shift) {
	shift.setDebutEtFinLocalisation();
	shift.calculDistance();
	//TODO detection shift collé : pas le temps de revenir au domicile
	if(locaDomicile!=null) {	    				
	    shift.calulDistanceDomicile(locaDomicile);	    
	}

	if(Value.isSupprimerShiftSansGPSfiable) {
	    if(!shift.hasGPSfiable())  shift.resetGPS();
	}
    }

}
