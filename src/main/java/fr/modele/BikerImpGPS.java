package fr.modele;

import java.util.ArrayList;

import fr.algorithmes.Trieur;
import fr.algorithmes.TrieurBulle;
import fr.gui.GUI;
import fr.map.Localisation;
import fr.map.Map;
import fr.map.Track;

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
			res.addCloneShift(shiftGPS);
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
			clone.addCloneShift(shift);
		}
		clone.map=(Map) map.clone();
		if(this.locaDomicile!=null) clone.locaDomicile=this.locaDomicile.clone();    		
		
		return clone;
	}


	@Override
	void addCloneShift(Shift shift) {
		assert(shift!=null);
		assert(shift.getDuree()>0);	
		ShiftImplGPS shiftArajouter=(ShiftImplGPS.createFromShift(shift.clone2())) ;	
		(this.getList()).add(shiftArajouter);	
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

		this.map.fixTracks();
		GUI.updateUI("Mise à jour shifts du Biker "+getNameBiker());

		calculDistance();

	} 

	private void calculDistance() {
		ShiftImplGPS shiftPrecedent=null;
		ArrayList<Track> tracksShift=new ArrayList<Track>();
		for(Track t:map.getTracks()) {
		
			if(shiftPrecedent==null){
				shiftPrecedent=t.getShiftImplGPS();				
			}
			else if(!shiftPrecedent.equals(t.getShiftImplGPS())) {
			
				shiftPrecedent.calculDistance(locaDomicile,tracksShift);			
				shiftPrecedent=t.getShiftImplGPS();
				tracksShift=new ArrayList<Track>();
				 
			}
			tracksShift.add(t);
		}		
		shiftPrecedent.calculDistance(locaDomicile,tracksShift);		
	}
	

		
	

}
